#!/usr/bin/env bash
# Usage: scripts/public-tree.sh <dir>. Writes what the public repository holds into <dir>, a clone
# of it: the install guide, the check scripts and, once it exists, the release certificate digest.
set -euo pipefail

dir="${1:?usage: $0 <dir>}"
root="$(cd "$(dirname "$0")/.." && pwd)"

mkdir -p "$dir/scripts"
cp "$root/public/README.md" "$dir/README.md"
cp "$root/scripts/check-release.sh" "$root/scripts/apk_same_bytes.py" \
    "$root/scripts/build-tools.sh" "$dir/scripts/"
if [ -f "$root/release-certificate.sha256" ]; then
    cp "$root/release-certificate.sha256" "$dir/"
fi
echo "public tree written to $dir"
