#!/usr/bin/env bash
# The app asks for three permissions, and the merged manifest is what proves it.
#
# The source manifest is not the check that matters: a dependency can add a
# permission, a service or a provider during the merge, and the merged file is
# the only place that shows up. This reads the file the build produced.
set -euo pipefail

cd "$(dirname "$0")/.."

merged=$(find app/build/intermediates -name AndroidManifest.xml -path '*merged*' | head -1)
if [ -z "$merged" ]; then
    echo "no merged manifest found, build the app first" >&2
    exit 1
fi

permissions=$(grep -oE 'android:name="android\.permission\.[A-Z_]+"' "$merged" \
    | sed -E 's/.*permission\.([A-Z_]+)"/\1/' | sort -u)

# INTERNET for the loopback proxy, and the two the system authentication
# prompt needs. All three are install time and none of them reads an
# identifier. Anything else appearing here came in with a dependency.
expected="INTERNET
USE_BIOMETRIC
USE_FINGERPRINT"
if [ "$permissions" != "$expected" ]; then
    echo "the merged manifest asks for permissions this app does not:" >&2
    echo "$permissions" >&2
    echo "expected exactly:" >&2
    echo "$expected" >&2
    exit 1
fi

# The fingerprint permission is only used by the prompt on API 28, so it must
# not be declared without a ceiling.
flat=$(tr -s '[:space:]' ' ' <"$merged")
if ! grep -qF 'android:name="android.permission.USE_FINGERPRINT" android:maxSdkVersion="28"' <<<"$flat"; then
    echo "the fingerprint permission is not bounded to API 28" >&2
    exit 1
fi

for forbidden in 'com.google.android.gms' 'com.google.firebase' 'AD_ID' 'BILLING'; do
    if grep -q "$forbidden" "$merged"; then
        echo "the merged manifest names $forbidden" >&2
        exit 1
    fi
done

if ! grep -q 'android:allowBackup="false"' "$merged"; then
    echo "the merged manifest does not refuse backup" >&2
    exit 1
fi

if ! grep -q 'android:networkSecurityConfig="@xml/network_security_config"' "$merged"; then
    echo "the merged manifest does not name the network security config" >&2
    exit 1
fi

echo "merged manifest: three install time permissions, no services, backup refused"
