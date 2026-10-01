#!/usr/bin/env bash
# Usage: scripts/release-notes.sh <tag>. Prints the notes of a release.
set -euo pipefail

tag="${1:?usage: $0 <tag>}"
core=$(tr -d '[:space:]' <"$(dirname "$0")/../core.lock")

cat <<EOF
NØNOS Wallet for Android $tag, built from this tag with core $core.

| File | What it is |
|---|---|
| \`NONOS-$tag.apk\` | the package signed with the release key. Install this one |
| \`NONOS-$tag-unsigned.apk\` | the same package before signing, aligned for 16 KB pages. The release key signed this file, and nothing else |
| \`NONOS-$tag-unsigned.aab\` | the bundle for a store, unsigned |
| \`NONOS-$tag-builds.txt\` | the hashes of the two builds from clean that made the unsigned package, which have to be equal |

Check the hash before installing, and the signer as the README of this repository describes:

\`\`\`sh
sha256sum -c NONOS-$tag.apk.sha256
\`\`\`

The shield runs on Sepolia, and its tokens have no value. The public account moves real money on
Ethereum mainnet. Nothing here is audited.
EOF
