#!/usr/bin/env bash
# Nothing in the package belongs to an advertiser, an analytics vendor or an
# attribution service.
#
# This reads the built apk rather than the source tree, because what ships is
# the package and a dependency can bring a class in without a line of source
# changing.
set -euo pipefail

# unzip where the machine has it, the stand-in in this folder where it does not.
unzip() {
    if type -P unzip >/dev/null; then
        command unzip "$@"
    else
        python3 "scripts/zipread.py" "$@"
    fi
}

cd "$(dirname "$0")/.."

apk=$(find app/build/outputs/apk -name '*.apk' | head -1)
if [ -z "$apk" ]; then
    echo "no apk found, build the app first" >&2
    exit 1
fi

strings_in_package=$(unzip -p "$apk" 'classes*.dex' | strings)

for forbidden in \
    'com/google/android/gms' \
    'com/google/firebase' \
    'AdvertisingIdClient' \
    'com/facebook' \
    'io/sentry' \
    'com/crashlytics' \
    'com/amplitude' \
    'com/mixpanel' \
    'com/appsflyer' \
    'com/adjust/sdk' \
    'getAdvertisingId'
do
    if grep -qF "$forbidden" <<<"$strings_in_package"; then
        echo "the package contains $forbidden" >&2
        exit 1
    fi
done

libs=$(unzip -l "$apk" | grep -oE 'lib/[a-zA-Z0-9_-]+/[a-zA-Z0-9_.-]+\.so' | sort -u)

# An architecture folder without the core is worse than no folder at all: the
# package installs on that device and the wallet fails at its first call. The
# pattern above carried no underscore, which hid every x86_64 entry from this
# check and from whoever read its output.
for abi in $(sed -E 's|lib/([^/]+)/.*|\1|' <<<"$libs" | sort -u); do
    if ! grep -qF "lib/$abi/libnox_shield_core.so" <<<"$libs"; then
        echo "the package ships lib/$abi without the core" >&2
        exit 1
    fi
done

# Every core function the Kotlin bindings call must be exported by the core
# library for every architecture. A library from an older core beside newer
# bindings installs, then fails its checksum at the first call and the app dies
# at launch, which is what one build shipped.
bindings=app/src/main/generated/systems/nonos/shield/core/nox_shield_core.kt
wanted=$(grep -oE 'uniffi_nox_shield_core_[a-z0-9_]+' "$bindings" | sort -u)
work=$(mktemp -d)
trap 'rm -rf "$work"' EXIT
for abi in $(sed -E 's|lib/([^/]+)/.*|\1|' <<<"$libs" | sort -u); do
    unzip -p "$apk" "lib/$abi/libnox_shield_core.so" >"$work/core.so"
    exported=$(nm -D --defined-only "$work/core.so" 2>/dev/null | awk '{print $NF}' | sort -u)
    missing=$(comm -23 <(echo "$wanted") <(echo "$exported"))
    if [ -n "$missing" ]; then
        echo "lib/$abi/libnox_shield_core.so lacks what the bindings call:" >&2
        echo "$missing" | head -10 >&2
        exit 1
    fi
done

echo "package clean of vendor sdks. native libraries:"
echo "$libs" | sed 's/^/  /'

# The two files the build generates about itself must actually be in the
# package. Both were written to disk and absent from the apk for one build,
# which is the kind of miss that shows up as an empty screen in front of a
# user rather than as a failure here.
listing=$(unzip -l "$apk")
for generated in assets/core.txt assets/licences.txt; do
    if ! grep -qF "$generated" <<<"$listing"; then
        echo "the package does not carry $generated" >&2
        exit 1
    fi
done

echo "package carries its own provenance and licences"
