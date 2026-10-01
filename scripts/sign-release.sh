#!/usr/bin/env bash
# Usage: scripts/sign-release.sh <dir> <keystore> <alias>, on the machine that holds the release key.
# <dir> is what scripts/release-build.sh left on the build server. The keystore asks for its
# password on the terminal, and nothing keeps it.
set -euo pipefail

dir="${1:?usage: $0 <dir> <keystore> <alias>}"
keystore="${2:?usage: $0 <dir> <keystore> <alias>}"
alias="${3:?usage: $0 <dir> <keystore> <alias>}"
here="$(cd "$(dirname "$0")" && pwd)"
tools=$("$here/build-tools.sh")
cd "$dir"

sha() {
    if command -v sha256sum >/dev/null; then sha256sum "$@"; else shasum -a 256 "$@"; fi
}

unsigned=$(find . -maxdepth 1 -name 'NONOS-*-unsigned.apk' | head -1)
if [ -z "$unsigned" ]; then
    echo "$dir holds no NONOS-<tag>-unsigned.apk" >&2
    exit 1
fi
name="$(basename "$unsigned" -unsigned.apk)"

sha -c "$name-unsigned.apk.sha256"
built=$(cut -d' ' -f1 <"$name-unsigned.apk.sha256")
for run in first second; do
    if ! grep -qx "$run *$built" "$name-builds.txt"; then
        echo "the $run build on the server is not this package, so it was not reproduced" >&2
        exit 1
    fi
done
"$tools/zipalign" -c -P 16 4 "$name-unsigned.apk"

"$tools/apksigner" sign --ks "$keystore" --ks-key-alias "$alias" \
    --alignment-preserved \
    --v1-signing-enabled false --v2-signing-enabled true \
    --v3-signing-enabled true --v4-signing-enabled false \
    --out "$name.apk" "$name-unsigned.apk"

ANDROID_BUILD_TOOLS="$tools" "$here/check-release.sh" "$name.apk" "$name-unsigned.apk"
sha "$name.apk" >"$name.apk.sha256"
cat "$name.apk.sha256"
echo "publish with: scripts/publish-release.sh $dir"
