#!/usr/bin/env bash
# Usage: scripts/sign-test.sh <name>. CI signs <name>-unsigned.apk with the
# throwaway test key from its secrets into <name>-test.apk, then deletes the key.
set -euo pipefail

name="${1:?usage: $0 <name>}"
: "${TEST_KEYSTORE_B64:?the test keystore is not set}"
: "${TEST_KEYSTORE_PASS:?the test keystore password is not set}"

tools=$("$(dirname "$0")/build-tools.sh")
keystore="${RUNNER_TEMP:-$(mktemp -d)}/test.p12"
trap 'rm -f "$keystore"' EXIT
echo "$TEST_KEYSTORE_B64" | base64 -d >"$keystore"

"$tools/apksigner" sign \
    --ks "$keystore" --ks-type PKCS12 --ks-pass env:TEST_KEYSTORE_PASS \
    --alignment-preserved \
    --v1-signing-enabled false --v2-signing-enabled true \
    --v3-signing-enabled true --v4-signing-enabled false \
    --out "$name-test.apk" "$name-unsigned.apk"

"$tools/apksigner" verify --min-sdk-version 24 --verbose "$name-test.apk" \
    | grep -E "v2 scheme|v3 scheme"
sha256sum "$name-test.apk" | tee "$name-test.apk.sha256"
