#!/usr/bin/env bash
# Usage: scripts/build-release.sh <prefix>. Builds the release package and
# bundle from clean and writes <prefix>.apk, aligned as a release is, and
# <prefix>.aab. Clean each time, so reuse is not mistaken for determinism.
set -euo pipefail

prefix="${1:?usage: $0 <prefix>}"
cd "$(dirname "$0")/.."

./gradlew --no-daemon clean
./gradlew --no-daemon --max-workers="${NONOS_WORKERS:-2}" :app:assembleRelease :app:bundleRelease
scripts/align-release.sh app/build/outputs/apk/release/app-release-unsigned.apk "$prefix.apk"
cp app/build/outputs/bundle/release/app-release.aab "$prefix.aab"
