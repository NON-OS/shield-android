#!/usr/bin/env bash
# Align the unsigned release package for 16 KB pages and prove it.
#
# Usage: scripts/align-release.sh <unsigned.apk> <out.apk>
# The release and reproduce workflows both make their file here, so a hash
# from one is comparable with a hash from the other.
set -euo pipefail

# unzip where the machine has it, the stand-in in this folder where it does not.
unzip() {
    if type -P unzip >/dev/null; then
        command unzip "$@"
    else
        python3 "$(dirname "$0")/zipread.py" "$@"
    fi
}

if [ "$#" -ne 2 ]; then
    echo "usage: $0 <unsigned.apk> <out.apk>" >&2
    exit 2
fi
in="$1"
out="$2"

tools=$("$(dirname "$0")/build-tools.sh")
zipalign="$tools/zipalign"

"$zipalign" -P 16 -f 4 "$in" "$out"
if ! "$zipalign" -c -P 16 4 "$out"; then
    echo "$out is not aligned for 16 KB pages" >&2
    exit 1
fi

ndk_readelf=$(find "${ANDROID_NDK_HOME:-/missing}" -path '*/bin/llvm-readelf' 2>/dev/null | head -1 || true)
readelf=$(command -v readelf || command -v llvm-readelf || echo "$ndk_readelf")
if [ ! -x "$readelf" ]; then
    echo "readelf is needed to read the library segments: install binutils or set ANDROID_NDK_HOME" >&2
    exit 1
fi

work=$(mktemp -d)
trap 'rm -rf "$work"' EXIT
libs=$(unzip -Z1 "$out" | grep -E '^lib/[^/]+/[^/]+\.so$' || true)
if [ -z "$libs" ]; then
    echo "$out carries no native library" >&2
    exit 1
fi

for lib in $libs; do
    unzip -p "$out" "$lib" >"$work/lib.so"
    for align in $("$readelf" -lW "$work/lib.so" | awk '$1 == "LOAD" { print $NF }'); do
        if (( align < 16384 )); then
            echo "$lib has a LOAD segment aligned at $align, below 16 KB" >&2
            exit 1
        fi
    done
done

echo "$out: every library aligned for 16 KB pages"
