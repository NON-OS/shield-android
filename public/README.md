# NØNOS Wallet for Android

The releases of NØNOS Wallet for Android, a phone wallet for NOX Shield: private transfers on
Ethereum, proved on the phone and sent over a Tor client built into the app.

> [!WARNING]
> The shield runs on Sepolia, a test network, and its tokens have no value. The public account
> moves real money on Ethereum mainnet. Nothing is audited: not the app, not the core, not the pool.

The app runs on Android 9 or later, on a 64-bit ARM phone, and needs a screen lock. It needs no
Google account and no Play Services.

## Which file

| File on a release | Install it? |
|---|---|
| `NONOS-<tag>.apk` | yes. Signed with the release key, whose certificate digest is in `release-certificate.sha256` here |
| `NONOS-<tag>-unsigned.apk` and `.aab` | no. The build the release key signed, and the bundle for a store |
| `NONOS-<tag>-builds.txt` | no. The hashes of the two builds from clean that made the unsigned package, which are equal |

A test build signed with another key does not install over a release build, or the reverse. Moving
between them means uninstalling, which deletes the wallet from the phone, so write the recovery
words down first.

## Check it, then install it

1. From [Releases](https://github.com/NON-OS/shield-android/releases), download `NONOS-<tag>.apk`
   and `NONOS-<tag>.apk.sha256` into one folder.
2. Check the hash. Linux: `sha256sum -c NONOS-v1.2.3.apk.sha256`. macOS:
   `shasum -a 256 -c NONOS-v1.2.3.apk.sha256`. Both print `OK`. Windows, in PowerShell:
   `Get-FileHash NONOS-v1.2.3.apk -Algorithm SHA256`, compared by eye with the `.sha256` file.
3. Check the signer. With the Android build tools, `apksigner verify --print-certs NONOS-v1.2.3.apk`
   prints a certificate SHA-256 digest that must equal `release-certificate.sha256`. On the phone,
   AppVerifier, an open-source app, checks the package `systems.nonos.shield` against that digest.
4. To check that the signed APK is the build plus a signature and nothing else, download the
   unsigned APK too and run `scripts/check-release.sh NONOS-v1.2.3.apk NONOS-v1.2.3-unsigned.apk`
   from a clone of this repository, with the build tools installed.
5. Copy the APK to the phone and open it. Allow Install unknown apps for the app that opened it,
   then Install. Play Protect may offer to scan an app it has not seen. Scanning sends the APK to
   Google, and declining does not stop the install.

A copy from the NØNOS site is checked the same way. Its hash must equal the `.sha256` of the same
tag here, and its signer must equal `release-certificate.sha256`. A file that matches neither is
not this app, wherever it came from.

## With Obtainium

1. In Obtainium, Add app, with the source URL `https://github.com/NON-OS/shield-android`.
2. Set the filter on APK names to the regular expression `^NONOS-v[0-9.]+\.apk$`, so it takes the
   release key build and never the unsigned file.
3. Check the first install by hand as above. Obtainium does not read the `.sha256` files, but
   Android refuses an update signed by any other key, so every later update keeps that signer.

## Updating and removing

A newer release installs over the older and keeps the wallet, as long as the two have one signer.
Uninstalling deletes the keystore key that seals the wallet. If Android offers to keep the app
data, decline: a kept vault cannot be opened again. The recovery words restore the wallet on any
phone.

Report a flaw to ek@nonos.systems or team@nonos.systems. Licence: AGPL-3.0-or-later.
