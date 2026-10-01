# Building on a shared server

How to build and check this app on a server that serves something else, such as the live lander:
everything in one folder of your own, no root, and the lowest priority, so the server keeps its
time for the work it is there for. [03-reproduce.md](03-reproduce.md) has the pinned versions.

## Once

```sh
mkdir -p ~/android-build && cd ~/android-build
git clone <this repository> android-app
git clone <the core repository> shield-core
nice -n 19 android-app/scripts/sdk-setup.sh ~/android-build/tools > tools.env
```

`scripts/sdk-setup.sh` installs Temurin 17, the Android platform, build tools 36.0.0 and the NDK
into `tools`, each checked against a pinned SHA-256 or by the SDK manager, and writes the variables
to `tools.env`. Set `NONOS_NDK` first to match the NDK in `assets/core.txt` of a release, when the
build has to reproduce that release. Rust comes from rustup in the home folder of the user, and the
core installs its own toolchain from `rust-toolchain.toml` on first use. cargo-ndk is
`cargo install cargo-ndk --version 4.1.2 --locked`. Keep about 20 GB free for the tools and the
two builds of the core.

## Each build

```sh
cd ~/android-build
. ./tools.env
git -C android-app pull
git -C shield-core fetch && git -C shield-core checkout "$(cat android-app/core.lock)"
cd android-app
export CARGO_BUILD_JOBS=2 NOX_SHIELD_CORE=~/android-build/shield-core
nice -n 19 scripts/build-core.sh
nice -n 19 ./gradlew --no-daemon --max-workers=2 \
    ktlintCheck detekt :app:testDebugUnitTest :app:assembleRelease :app:bundleRelease
nice -n 19 scripts/check-manifest.sh
nice -n 19 scripts/check-package.sh
nice -n 19 scripts/check-no-logging.sh
nice -n 19 scripts/align-release.sh app/build/outputs/apk/release/app-release-unsigned.apk \
    aligned.apk
```

Two jobs for Rust and two workers for Gradle keep the rest of the cores free, and `--no-daemon`
leaves no Gradle process behind. For a build that has to match a release byte for byte, also set
`GITHUB_REF_NAME` to the tag, as [03-reproduce.md](03-reproduce.md) explains.
