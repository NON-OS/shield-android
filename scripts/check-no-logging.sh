#!/usr/bin/env bash
# Nothing in the Kotlin of this app, the generated bindings included, writes to
# the log, prints, or prints a stack trace. R8 strips the log calls of the
# libraries, as app/proguard-rules.pro says.
set -euo pipefail

cd "$(dirname "$0")/.."

found=$(grep -rnE \
    'android\.util\.Log|\bLog\.[a-z]+\(|\bprintln\(|\bprint\(|printStackTrace|System\.(out|err)|Timber' \
    --include='*.kt' --include='*.java' app/src || true)
if [ -n "$found" ]; then
    echo "the app writes to the log or the console:" >&2
    echo "$found" >&2
    exit 1
fi
echo "no log, print or stack trace in the app source"
