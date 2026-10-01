# Reproducing this build

What the core checks and what it does not is in
[its security status](https://github.com/NON-OS/shield-core/blob/main/docs/20-security-status.md).

A privacy wallet nobody can rebuild is a promise, not a property. Two builds of one commit have to
produce the same APK, and anybody has to be able to check that.

## The pinned set

| Component | Version | Where it is pinned |
|---|---|---|
| The core | the commit in `core.lock` | `scripts/build-core.sh` refuses a core checkout at any other commit |
| Rust | 1.91.1 | `rust-toolchain.toml` in the core, reported into `assets/core.txt` |
| Android NDK | the version named in `assets/core.txt` of the published package | the newest NDK under `ANDROID_HOME` |
| cargo-ndk | 4.1.2 | `.github/actions/core` |
| Android SDK platform | 36 | `app/build.gradle.kts` |
| Build tools | 36.0.0 or later, for `zipalign -P 16` | `scripts/align-release.sh` |
| JDK | Temurin 17 | `.github/workflows/*.yml`, and `flake.nix` |
| Android Gradle plugin, Kotlin, Compose BOM | 8.13.2, 2.0.21, 2024.12.01 | `gradle/libs.versions.toml` |
| The version | the release tag, `v1.2.3` giving name 1.2.3 and code 10203 | `GITHUB_REF_NAME` in `app/build.gradle.kts`. Any other build is `0.0.0-dev`, code 1 |

`assets/core.txt` carries the core commit, whether that checkout was clean, the Rust version and
the NDK, written by `scripts/build-core.sh` and shown in Settings. Read it from a published package
with `unzip -p NONOS-v1.2.3-unsigned.apk assets/core.txt`.

`scripts/build-core.sh` remaps every path under the home directory and the core checkout in the
compiled libraries (`--remap-path-prefix`, `-ffile-prefix-map`), so builders in different
directories produce the same bytes and no builder leaves a path behind. It links every library
with 16 KB pages (`max-page-size=16384`). Every library in the build is an exact version.

## Steps

```sh
git clone https://github.com/NON-OS/shield-android android-app
git clone https://github.com/NON-OS/shield-core shield-core
git -C android-app checkout v1.2.3
git -C shield-core checkout "$(cat android-app/core.lock)"
cd android-app
nix develop
export GITHUB_REF_NAME=v1.2.3
scripts/build-core.sh
./gradlew --no-daemon :app:assembleRelease
scripts/align-release.sh app/build/outputs/apk/release/app-release-unsigned.apk \
    NONOS-v1.2.3-unsigned.apk
sha256sum NONOS-v1.2.3-unsigned.apk
```

The last line has to print the hash in `NONOS-v1.2.3-unsigned.apk.sha256` on the release of the tag.
`GITHUB_REF_NAME` is what stamps the version into the package, so a build without it differs.

## What the build server checks on every tag

`scripts/release-build.sh` builds the tag twice from `clean` and fails if the two packages or the
two bundles differ by a single byte. Both hashes ship with the release in `NONOS-<tag>-builds.txt`,
and the signing script refuses a package they do not both name ([04-release.md](04-release.md)).
Both builds use one build of the core, so the core itself is rebuilt by the steps above, or by
`docs/07-reproduce.md` in the core.

## What is left out

The dependency metadata block (`dependenciesInfo` in `app/build.gradle.kts`): it is encrypted, and
a build carrying one cannot be checked byte for byte. Native debug symbols (`debugSymbolLevel =
"none"`): they vary with the temporary paths of the toolchain and carry nothing a user needs.

## What a signature does to this

Signing adds a block to the package, so a signed APK does not hash to the unsigned one.
`scripts/check-release.sh` shows a signed release is the unsigned build plus that block and
nothing else, as [04-release.md](04-release.md) describes.
