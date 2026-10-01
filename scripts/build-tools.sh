#!/usr/bin/env bash
# Prints the Android build tools directory: ANDROID_BUILD_TOOLS, the newest
# under ANDROID_HOME, or the one holding apksigner on the path.
set -euo pipefail

if [ -n "${ANDROID_BUILD_TOOLS:-}" ]; then
    echo "$ANDROID_BUILD_TOOLS"
elif [ -n "${ANDROID_HOME:-}" ]; then
    find "$ANDROID_HOME/build-tools" -mindepth 1 -maxdepth 1 | sort -V | tail -1
else
    dirname "$(command -v apksigner || echo /missing/apksigner)"
fi
