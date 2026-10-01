#!/usr/bin/env bash
# Usage: scripts/sdk-setup.sh <dir>. Installs Temurin 17 and the Android SDK, build tools and NDK
# this build pins into <dir>, as an ordinary user on x86_64 Linux, and prints what to export.
set -euo pipefail

# unzip where the machine has it, the stand-in in this folder where it does not.
unzip() {
    if type -P unzip >/dev/null; then
        command unzip "$@"
    else
        python3 "$(dirname "$0")/zipread.py" "$@"
    fi
}

dir="${1:?usage: $0 <dir>}"
ndk="${NONOS_NDK:-27.3.13750724}"
jdk_url="https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.20.1%2B1/OpenJDK17U-jdk_x64_linux_hotspot_17.0.20.1_1.tar.gz"
jdk_sha="3808d1d15e3ec6bd5b84057fb5d84c33d8a1536a258146bcea2e603fc726e08e"
tools_url="https://dl.google.com/android/repository/commandlinetools-linux-13114758_latest.zip"
tools_sha="7ec965280a073311c339e571cd5de778b9975026cfcbe79f2b1cdcb1e15317ee"

if [ "$(uname -s) $(uname -m)" != "Linux x86_64" ]; then
    echo "this installs the x86_64 Linux tools only" >&2
    exit 1
fi
mkdir -p "$dir/download"
dir="$(cd "$dir" && pwd)"

fetch() {
    curl -fsSL -o "$dir/download/$3" "$1"
    echo "$2  $dir/download/$3" | sha256sum -c --quiet
}

if [ ! -x "$dir/jdk/bin/java" ]; then
    fetch "$jdk_url" "$jdk_sha" jdk.tar.gz
    mkdir -p "$dir/jdk"
    tar -xzf "$dir/download/jdk.tar.gz" -C "$dir/jdk" --strip-components=1
fi
if [ ! -x "$dir/sdk/cmdline-tools/latest/bin/sdkmanager" ]; then
    fetch "$tools_url" "$tools_sha" tools.zip
    mkdir -p "$dir/sdk/cmdline-tools"
    unzip -q -o "$dir/download/tools.zip" -d "$dir/sdk/cmdline-tools"
    mv "$dir/sdk/cmdline-tools/cmdline-tools" "$dir/sdk/cmdline-tools/latest"
fi

export JAVA_HOME="$dir/jdk"
{ yes || true; } | "$dir/sdk/cmdline-tools/latest/bin/sdkmanager" --sdk_root="$dir/sdk" --licenses >/dev/null
"$dir/sdk/cmdline-tools/latest/bin/sdkmanager" --sdk_root="$dir/sdk" \
    "platforms;android-36" "build-tools;36.0.0" "ndk;$ndk" >/dev/null
rm -rf "$dir/download"

echo "export JAVA_HOME=$dir/jdk"
echo "export ANDROID_HOME=$dir/sdk"
echo "export ANDROID_NDK_HOME=$dir/sdk/ndk/$ndk"
echo "export PATH=$dir/jdk/bin:\$PATH"
