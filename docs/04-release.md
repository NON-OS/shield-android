# Making a release

How a tag becomes a signed APK on the public repository
[NON-OS/shield-android](https://github.com/NON-OS/shield-android). Nothing is built on GitHub, the
release key stays on one machine, and no token goes onto the build server or into this repository.
What the core checks and what it does not is in
[its security status](https://github.com/NON-OS/shield-core/blob/main/docs/20-security-status.md).

## Making the release key, once

On the machine that will hold it, outside every checkout:

```sh
keytool -genkeypair -v -storetype PKCS12 -keystore ~/keys/nonos-release.p12 \
    -alias nonos -keyalg RSA -keysize 4096 -validity 10000 -dname "CN=NONOS, O=NONOS"
keytool -list -v -storetype PKCS12 -keystore ~/keys/nonos-release.p12 -alias nonos | grep SHA256:
```

Write the digest after `SHA256:` into `release-certificate.sha256` at the root of this repository,
with or without its colons, and commit it. Every check compares against it, and each release copies
it to the public repository. Keep two copies of the keystore on encrypted media, apart from each
other. A lost key means no later release installs over an earlier one.

## 1. On the build server: two builds, one result

From a clean checkout of the tag, with the core built for it as [11-build.md](11-build.md) shows:

```sh
nice -n 19 scripts/release-build.sh v1.2.3
```

It refuses a checkout that is not the tag or has changes, builds the package and the bundle twice
from clean with the version of the tag, runs the manifest, package and logging checks, and fails
unless the two packages and the two bundles are the same bytes. It leaves `release-v1.2.3` beside
the checkout: the unsigned package aligned for 16 KB pages, the bundle, a `.sha256` for each, and
`NONOS-v1.2.3-builds.txt` with the hash of each build. Copy that folder to the key holder.

## 2. On the machine with the key: sign

```sh
scripts/sign-release.sh release-v1.2.3 ~/keys/nonos-release.p12 nonos
```

It checks the hash of the unsigned package, refuses unless both server builds made that package,
checks its alignment, and signs it with schemes v2 and v3, v1 and v4 off, keeping the alignment.
The keystore password is typed at the prompt and kept nowhere. Then it prints four lines, each
required: `same bytes`, the signed file is the build plus a signing block and nothing else.
`aligned for 16 KB pages`. `signed with v2 and v3, not v1, by one signer`. `signed by the release
certificate`. It needs build tools 36.0.0, Python 3 and `ANDROID_HOME`.

## 3. On a machine where gh is signed in: publish

```sh
scripts/publish-release.sh release-v1.2.3
```

It runs the check again, refreshes the README and the check scripts of the public repository
(`scripts/public-tree.sh`), then creates the release there with the signed package, the unsigned
package, the bundle, their hashes and the build hashes.

## Checking a release

Anybody with the build tools and a clone of the public repository can run
`scripts/check-release.sh NONOS-v1.2.3.apk NONOS-v1.2.3-unsigned.apk`, and anybody with the source
can rebuild the unsigned package to its hash by [03-reproduce.md](03-reproduce.md).

`keystore.properties` still makes Gradle sign its own build, scheme v3 only, for a phone of the
builder and never for a release. Every `*.jks`, `*.keystore`, `*.p12` and `keystore.properties` is
ignored by git, and a key that lands in a commit is burned and replaced. The store listing is in
[06-play.md](06-play.md).
