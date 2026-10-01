# The Android app

What the Android app owns, what it trusts, what it hands the core and the owner, and what an
attacker who controls a neighbour can do. What the core checks and what it does not is in
[its security status](https://github.com/NON-OS/shield-core/blob/main/docs/20-security-status.md).

## What this owns

The window, the keystore key, the screens, the confirmation of the owner before a send, and the
clipboard. It owns no decision about money.

Specifically: `FLAG_SECURE` on the window (`MainActivity.kt`), the StrongBox or TEE key that wraps
the file key of the vault (`core/VaultKeySpec.kt`, `core/StrongBoxGuard.kt`), the private directory
it hands the core, and one immutable state that the screens render (`ui/state/UiState.kt`). Every
protection, with what checks it, is listed in [07-hardening.md](07-hardening.md).

## What it trusts

The core, for every decision. An amount is text until the core parses it, an address is text until
the core checks it, a balance is a figure the core computed and formatted, and a send or a swap is a
review the core worked out. This app contains no arithmetic on money, no address parsing, no
cryptography and no network code: every connection is made by the Tor client inside the core.

The Android keystore, for a key it cannot export. `StrongBoxGuard` asks for StrongBox and falls back
to the TEE when the device has no security chip, and Settings says which one holds the key, because
the answer changes what a seized phone is worth. The key requires user authentication, and each use
of it, and each public send or swap, is confirmed through the system prompt with a strong biometric
or the device credential (`core/AuthPrompt.kt`, `ui/state/Guarded.kt`).

The private storage of the app, for a directory another app cannot read. Backup and device transfer
exclude the whole data directory (`res/xml/data_extraction_rules.xml`, and `allowBackup` is false).

## What it hands its neighbours

To the core: a directory path and a `HardwareGuard`. Then what the owner asks for: create, restore,
unlock, lock, read the account, send, receive, swap, and on the shield deposit, send privately,
withdraw, take back and time a proof.

To the owner: what the core returned, in the NØNOS screens, and nothing the app derived itself.
Home is a network pill, one tile of coins with ETH, NOX and USDC, and four actions: send, receive,
swap and shield.

## What an attacker who controls a neighbour can do

**The core, turned hostile.** It would already hold the seed, so the question does not arise in
that direction: the app is the smaller trusted component. What the app does not do is give the core
more than it needs. It never passes the words back in after creation, never stores a derived key,
and never holds an amount it did not get from the core.

**A malicious app on the same device.** Without root it cannot read the private storage of this
app or its memory. It cannot read the screen, because `FLAG_SECURE` also blocks the recents
thumbnail and screen recorders. Since Android 10 it cannot read the clipboard from the background,
and a copied address or hash is marked sensitive and cleared after 60 s. It can see that the app is
installed and running, which is not hidden.

**A malicious accessibility service.** This is the strongest attacker below root on Android: it can
read the view tree, which means the balances and the addresses on screen. `FLAG_SECURE` does not
stop it, nothing in an app can, and the answer is the warning the platform shows when such a
service is enabled.

**An attacker with the APK.** They get the code, which is AGPL and published, and the reproducible
build lets them confirm that an APK matches its source. The addresses it names are listed with their
evidence in the README of the core.

**Google Play Services.** Absent from every path. The app installs and runs without a Google
account.
