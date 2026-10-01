#!/usr/bin/env bash
# Usage: scripts/same-bytes.sh <first> <second>. Fails unless both files hash the same.
set -euo pipefail

one=$(sha256sum "${1:?usage: $0 <first> <second>}" | cut -d' ' -f1)
two=$(sha256sum "${2:?usage: $0 <first> <second>}" | cut -d' ' -f1)
echo "first  $one"
echo "second $two"
if [ "$one" != "$two" ]; then
    echo "two builds of one commit produced different files" >&2
    cmp -l "$1" "$2" | head -20 >&2 || true
    exit 1
fi
echo "identical: $one"
