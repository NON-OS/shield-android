#!/usr/bin/env bash
# Build the Rust core for Android and generate its Kotlin bindings.
#
# The core is a separate checkout, since the iOS shell builds the same crate.
# Point NOX_SHIELD_CORE at it, or keep it beside this repository.
set -euo pipefail

CORE="${NOX_SHIELD_CORE:-$(cd "$(dirname "$0")/../../shield-core" && pwd)}"
OUT="$(cd "$(dirname "$0")/.." && pwd)/app/src/main"
# 32-bit ARM is deliberately absent: a Goldilocks multiply folds a 128-bit
# product, which armv7 does in software, and that is the hot path of the
# whole proof. See the abiFilters comment in app/build.gradle.kts.
TARGETS=(aarch64-linux-android x86_64-linux-android)
ABIS=(arm64-v8a x86_64)

if [ ! -d "$CORE/core" ]; then
    echo "core checkout not found at $CORE" >&2
    echo "set NOX_SHIELD_CORE to the directory holding core/" >&2
    exit 1
fi

# The core pins its own toolchain in rust-toolchain.toml, and cargo ndk spawns
# cargo again from the path. A system rust ahead of rustup's shims would win
# that lookup and then fail for want of an android standard library, so the
# shims go first here.
if [ -d "$HOME/.cargo/bin" ]; then
    PATH="$HOME/.cargo/bin:$PATH"
fi

# cargo ndk finds the toolchain through one of these. A nix shell sets them;
# outside one, the command line tools' own layout is the fallback.
if [ -z "${ANDROID_NDK_HOME:-}" ] && [ -n "${ANDROID_HOME:-}" ]; then
    ANDROID_NDK_HOME="$(ls -d "$ANDROID_HOME"/ndk/* 2>/dev/null | tail -1)"
    export ANDROID_NDK_HOME
fi
if [ -z "${ANDROID_NDK_HOME:-}" ]; then
    echo "set ANDROID_NDK_HOME, or ANDROID_HOME with an ndk under it" >&2
    exit 1
fi

# The core commit this shell is pinned to. A build from any other commit is a
# build nobody reviewed together with these screens, so it is refused; set
# NOX_CORE_UNPINNED=1 to build against a working core on purpose.
lock="$(dirname "$0")/../core.lock"
if [ -f "$lock" ] && [ -z "${NOX_CORE_UNPINNED:-}" ]; then
    want=$(tr -d '[:space:]' <"$lock")
    have=$(cd "$CORE" && git rev-parse HEAD)
    if [ "$want" != "$have" ]; then
        echo "the core is at $have, core.lock pins $want" >&2
        exit 1
    fi
fi

# No builder's path in the binary. Panic locations and C sources carry the path
# they were compiled from, which put this machine's home directory in every
# build. Rust and C paths are remapped: the home directory first, then the
# cargo home and the core checkout, the last match winning.
CARGO_HOME_DIR="${CARGO_HOME:-$HOME/.cargo}"
export RUSTFLAGS="${RUSTFLAGS:-} --remap-path-prefix=$HOME=/home --remap-path-prefix=$CARGO_HOME_DIR=/cargo --remap-path-prefix=$CORE=/nox-shield-core"
export CFLAGS="${CFLAGS:-} -ffile-prefix-map=$HOME=/home -ffile-prefix-map=$CARGO_HOME_DIR=/cargo -ffile-prefix-map=$CORE=/nox-shield-core"

# Every LOAD segment on a 16 KB boundary, which Android 15 devices with 16 KB
# pages need and Play requires. Only the phone builds get it: the host linker
# that builds the binding generator may not know the flag.
PAGES="-C link-arg=-Wl,-z,max-page-size=16384"

# Refuse a library that still names the builder's home, rather than ship it.
no_local_paths() {
    local found
    found=$(strings -a "$1" | grep -cF "$HOME" || true)
    if [ "$found" -ne 0 ]; then
        echo "$1 still carries $found local paths" >&2
        strings -a "$1" | grep -F "$HOME" | head -5 >&2
        exit 1
    fi
}

for i in "${!TARGETS[@]}"; do
    target="${TARGETS[$i]}"
    abi="${ABIS[$i]}"
    echo "building ${target}"
    ( cd "$CORE" && RUSTFLAGS="$RUSTFLAGS $PAGES" cargo ndk --target "$target" --platform 28 -- \
        build --release -p nox_shield_core --lib )
    mkdir -p "$OUT/jniLibs/$abi"
    cp "$CORE/target/$target/release/libnox_shield_core.so" "$OUT/jniLibs/$abi/"
    no_local_paths "$OUT/jniLibs/$abi/libnox_shield_core.so"
done

# The Kotlin generator needs a shared library rather than an archive, because
# it writes the library name the bindings load at runtime. It reads the
# interface out of the static symbol table, which the shipped profile strips,
# so it is given the same code built once more with that table kept. What ships
# is still the release library copied above.
echo "building the library the generator reads"
( cd "$CORE" && RUSTFLAGS="$RUSTFLAGS $PAGES" cargo ndk --target aarch64-linux-android --platform 28 -- \
    build --profile bindings -p nox_shield_core --lib )

echo "generating bindings"
mkdir -p "$OUT/generated"
( cd "$CORE" && cargo run --release --features bindgen --bin uniffi-bindgen -- \
    generate --library "target/aarch64-linux-android/bindings/libnox_shield_core.so" \
    --language kotlin --out-dir "$OUT/generated" )

# The generator exits zero when it finds no interface at all, which is how a
# stripped library produced an empty run for a day without anybody noticing.
bindings=$(find "$OUT/generated" -name '*.kt' | wc -l | tr -d ' ')
if [ "$bindings" -eq 0 ]; then
    echo "the generator wrote no bindings" >&2
    exit 1
fi

# Which core this is, written by the build rather than typed by somebody.
#
# A version in a constant is a version that goes stale, and the app shows this
# string beside its own so a person reporting a problem can read back exactly
# what they are running. The state is part of it: a library built from a dirty
# checkout is not the commit it claims, and saying so is the difference between
# provenance and decoration.
mkdir -p "$OUT/assets"
core_commit=$( cd "$CORE" && git rev-parse --short HEAD 2>/dev/null || echo unknown )
core_state=$( cd "$CORE" && [ -z "$(git status --porcelain 2>/dev/null)" ] \
    && echo clean || echo modified )
# Asked inside the core, because the core pins its toolchain in
# rust-toolchain.toml and that is the compiler that built the library. Asked
# here it reported whatever this directory defaults to, which was a nightly
# the library was never compiled with: provenance naming the wrong compiler is
# worse than none.
core_rustc=$( cd "$CORE" && rustc --version 2>/dev/null | cut -d' ' -f2 )
[ -n "$core_rustc" ] || core_rustc=unknown
# The NDK is the other half of what a reproducer has to match: the same source
# and the same rustc through a different NDK is a different library.
core_ndk=$( basename "${ANDROID_NDK_HOME:-unknown}" )
printf 'commit %s\nstate %s\nrustc %s\nndk %s\n' \
    "$core_commit" "$core_state" "$core_rustc" "$core_ndk" >"$OUT/assets/core.txt"
echo "core: $core_commit $core_state, rustc $core_rustc, ndk $core_ndk"

# Every crate the core links on the three ABIs, with the notice files they
# carry. The Gradle licence task folds these into the package's own list, and
# refuses to build without them.
python3 "$(dirname "$0")/licences.py" "$CORE" "$OUT/generated/licences" \
    "$(dirname "$0")/../licenses" aarch64-linux-android,x86_64-linux-android

echo "done: $(find "$OUT/jniLibs" -name '*.so' | wc -l | tr -d ' ') libraries, $bindings bindings"
