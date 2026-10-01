#!/usr/bin/env bash
# Usage: scripts/check-release.sh <signed.apk> <unsigned.apk> [certificate SHA-256]
# Passes when the signed package is the unsigned build plus a v2 and v3
# signature by the release certificate, and nothing else.
set -euo pipefail

signed="${1:?usage: $0 <signed.apk> <unsigned.apk> [certificate SHA-256]}"
unsigned="${2:?usage: $0 <signed.apk> <unsigned.apk> [certificate SHA-256]}"
here="$(cd "$(dirname "$0")" && pwd)"
want="${3:-}"
if [ -z "$want" ] && [ -f "$here/../release-certificate.sha256" ]; then
    want=$(tr -d '[:space:]' <"$here/../release-certificate.sha256")
fi

tools=$("$here/build-tools.sh")
if [ ! -x "$tools/apksigner" ] || [ ! -x "$tools/zipalign" ]; then
    echo "apksigner and zipalign are needed: set ANDROID_HOME or ANDROID_BUILD_TOOLS" >&2
    exit 2
fi

python3 "$here/apk_same_bytes.py" "$signed" "$unsigned"
"$tools/zipalign" -c -P 16 4 "$signed"
echo "aligned for 16 KB pages"

report=$("$tools/apksigner" verify --min-sdk-version 24 --verbose --print-certs "$signed" \
    2>/dev/null)
for line in \
    "Verified using v1 scheme (JAR signing): false" \
    "Verified using v2 scheme (APK Signature Scheme v2): true" \
    "Verified using v3 scheme (APK Signature Scheme v3): true" \
    "Number of signers: 1"
do
    if ! grep -qxF "$line" <<<"$report"; then
        echo "the signature does not say: $line" >&2
        exit 1
    fi
done
echo "signed with v2 and v3, not v1, by one signer"

cert=$(sed -n 's/^Signer #1 certificate SHA-256 digest: //p' <<<"$report")
want=$(tr -d '[:space:]:' <<<"$want" | tr '[:upper:]' '[:lower:]')
want="${want#sha256}"
if [ -z "$want" ]; then
    echo "certificate SHA-256 $cert, compared with nothing: pass the published digest" >&2
    exit 2
fi
if [ "$cert" != "$want" ]; then
    echo "signed by $cert, not by the release certificate $want" >&2
    exit 1
fi
echo "signed by the release certificate $cert"
