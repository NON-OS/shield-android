#!/usr/bin/env bash
# Usage: nice -n 19 scripts/release-build.sh <tag> [out]. On the build server, from a clean checkout
# of the tag with the core built: two builds from clean, which must be the same bytes, then the
# unsigned package, the bundle, their hashes and both build hashes in <out>, outside the checkout.
set -euo pipefail

tag="${1:?usage: $0 <tag> [out]}"
root="$(cd "$(dirname "$0")/.." && pwd)"
out="${2:-$root/../release-$tag}"
name="NONOS-$tag"
cd "$root"

if [ "$(git describe --exact-match --tags HEAD 2>/dev/null)" != "$tag" ]; then
    echo "the checkout is not at $tag" >&2
    exit 1
fi
if [ -n "$(git status --porcelain)" ]; then
    echo "the checkout has changes, so it is not $tag" >&2
    exit 1
fi

export GITHUB_REF_NAME="$tag"
work=$(mktemp -d)
trap 'rm -rf "$work"' EXIT

scripts/build-release.sh "$work/one"
scripts/check-manifest.sh
scripts/check-package.sh
scripts/check-no-logging.sh
scripts/build-release.sh "$work/two"
scripts/same-bytes.sh "$work/one.apk" "$work/two.apk"
scripts/same-bytes.sh "$work/one.aab" "$work/two.aab"

mkdir -p "$out"
cp "$work/one.apk" "$out/$name-unsigned.apk"
cp "$work/one.aab" "$out/$name-unsigned.aab"
(
    cd "$out"
    sha256sum "$name-unsigned.apk" >"$name-unsigned.apk.sha256"
    sha256sum "$name-unsigned.aab" >"$name-unsigned.aab.sha256"
    {
        echo "first  $(sha256sum "$work/one.apk" | cut -d' ' -f1)"
        echo "second $(sha256sum "$work/two.apk" | cut -d' ' -f1)"
    } >"$name-builds.txt"
)
echo "ready to sign: $out"
