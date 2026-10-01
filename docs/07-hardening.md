# Hardening

Each protection of the Android app, where it lives and what checks it. What the core checks and what
it does not is in
[its security status](https://github.com/NON-OS/shield-core/blob/main/docs/20-security-status.md).
Paths under `core/` and `ui/` are relative to `app/src/main/kotlin/systems/nonos/shield`.

| Protection | Where | Checked by |
|---|---|---|
| The seed sealed under an AES-256 key in StrongBox, or in the TEE where there is no chip. A key the keystore keeps in software is deleted and refused | `core/StrongBoxGuard.kt`, `core/KeySecurity.kt` | the key reads back its own security level, shown in Settings |
| The key needs an unlocked phone and the owner, confirmed by a strong biometric or the screen lock, for 30 seconds per use | `core/VaultKeySpec.kt`, `core/AuthPrompt.kt` | the keystore, which refuses the key without that confirmation |
| A key gone for good, invalidated or made after the vault, is told apart from a refusal, and the locked screen then offers to destroy the wallet before a restore | `core/KeyProbe.kt`, `core/DeviceGuard.kt`, `ui/screens/KeyGoneSection.kt` | the GCM tag of the vault, which a newer key cannot pass |
| `FLAG_SECURE` before the first frame, lifted only when the owner allows recording and never over the words or a key | `MainActivity.kt`, `ui/state/Recording.kt` | the capture build is the only build with it off, under its own application id |
| A copied value marked sensitive, cleared after 60 seconds, never read back | `core/Clip.kt`, `core/Copying.kt` | `CopyingTest` |
| No backup and no device transfer of any app data | `AndroidManifest.xml`, `res/xml/data_extraction_rules.xml` | `scripts/check-manifest.sh` on the merged manifest |
| No cleartext, and only the system certificates, for anything on the platform network stack | `res/xml/network_security_config.xml` | `scripts/check-manifest.sh`. Every connection today is made by Tor inside the core |
| Nothing written to the log | the source, and `app/proguard-rules.pro` | `scripts/check-no-logging.sh` on the source, and R8, which strips every call into `android.util.Log` and `java.util.logging` |
| Three install time permissions, and no advertiser, analytics or crash SDK | `AndroidManifest.xml` | `scripts/check-manifest.sh`, `scripts/check-package.sh` |
| Every native library on 16 KB pages | `scripts/build-core.sh` | `scripts/align-release.sh`, run on every release build |

## The R8 rules, reviewed

| Rule | Why it stays |
|---|---|
| keep `systems.nonos.shield.core.**` | the generated bindings live there, and JNA finds their methods and fields by name at runtime. The hand written classes beside them are kept too. Their names are in the public source, so renaming them would hide nothing |
| keep `com.sun.jna.**` and its implementers | the same lookup, from the other side |
| `-dontwarn java.awt.**` | a desktop helper inside JNA names AWT, which no phone has and nothing on a phone calls |
| strip `android.util.Log` and `java.util.logging` | a log is readable by anyone with a cable and a debugger, and JNA writes to `java.util.logging` |

## The memory of the first proof

The core reads the peak resident memory of the app process (`VmHWM`) when a proof finishes, and the
measurement card carries it beside the RAM of the phone. Timed as the first proof after the app
starts, it is the peak a 4 GB phone has to hold. The number is not published until it has been
measured on such a phone by the steps in [08-measure.md](08-measure.md).

## Accessibility

- A screen reader hears each statement once, never the copy laid out under it or a half typed one,
  and hears a refusal as it appears.
- Section names are headings. The network pill, the account pill, the real money band and the coin
  picker say what iOS says, and the pills and rows that act as buttons are announced as buttons.
- Buttons grow with the font size, and every screen scrolls.

To check on a phone before a release, with TalkBack on and the font at its largest: walk from the
welcome screen through creating a wallet, a deposit, a private send and Settings. Every control has
to say what it is and what it does, and no text may be cut off. A field is named by the label above
it, which TalkBack reads as the item before the field. Use headphones while the words are on screen.
