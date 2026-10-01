#!/usr/bin/env bash
# Usage: scripts/same-as-published.sh <tag> <package>. Waits up to an hour for
# the release job to publish the unsigned hash of the tag on the public
# repository, then compares.
set -euo pipefail

tag="${1:?usage: $0 <tag> <package>}"
package="${2:?usage: $0 <tag> <package>}"
asset="NONOS-$tag-unsigned.apk.sha256"
public="${NONOS_PUBLIC_REPO:-NON-OS/shield-android}"
work=$(mktemp -d)
trap 'rm -rf "$work"' EXIT

for _ in $(seq 1 60); do
    if gh release download "$tag" -R "$public" -p "$asset" -D "$work" 2>/dev/null; then
        break
    fi
    sleep 60
done
if [ ! -f "$work/$asset" ]; then
    echo "the release of $tag has no $asset after an hour" >&2
    exit 1
fi

published=$(cut -d' ' -f1 <"$work/$asset")
built=$(sha256sum "$package" | cut -d' ' -f1)
echo "published $published"
echo "built     $built"
if [ "$published" != "$built" ]; then
    echo "the published package of $tag is not the package this job built" >&2
    exit 1
fi
echo "the published package of $tag is this build"
