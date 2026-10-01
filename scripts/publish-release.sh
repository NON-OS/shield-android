#!/usr/bin/env bash
# Usage: scripts/publish-release.sh <dir>, on a machine where gh is signed in. Checks the signed
# package in <dir> again, refreshes the public repository, then publishes the release there.
set -euo pipefail

dir="$(cd "${1:?usage: $0 <dir>}" && pwd)"
public="${NONOS_PUBLIC_REPO:-NON-OS/shield-android}"
root="$(cd "$(dirname "$0")/.." && pwd)"
signed=$(find "$dir" -maxdepth 1 -name 'NONOS-v*.apk' ! -name '*-unsigned.apk' | head -1)
if [ -z "$signed" ]; then
    echo "$dir holds no signed package: run scripts/sign-release.sh first" >&2
    exit 1
fi
name="$(basename "$signed" .apk)"
tag="${name#NONOS-}"

"$root/scripts/check-release.sh" "$signed" "$dir/$name-unsigned.apk"

work=$(mktemp -d)
trap 'rm -rf "$work"' EXIT
gh repo clone "$public" "$work/public" -- -q
"$root/scripts/public-tree.sh" "$work/public"
git -C "$work/public" add -A
if ! git -C "$work/public" diff --cached --quiet; then
    git -C "$work/public" -c user.name=eKisNonos -c user.email=ekisanon@proton.me \
        commit -q -m "release $tag"
    git -C "$work/public" push -q origin HEAD:main
fi

cd "$dir"
gh release create "$tag" -R "$public" --target main \
    "$name.apk" "$name.apk.sha256" \
    "$name-unsigned.apk" "$name-unsigned.apk.sha256" \
    "$name-unsigned.aab" "$name-unsigned.aab.sha256" "$name-builds.txt" \
    --title "NØNOS Wallet for Android $tag" \
    --notes-file <("$root/scripts/release-notes.sh" "$tag")
