# The tutorial video

A script, a shot list and the steps for a video that installs the APK and tests it end to end on
Sepolia. A person performs every take by hand. Nothing in a recorded take is driven by a script,
`adb input`, a macro or an automation app, and no result is staged or spliced in.

## Before recording

- Two phones with a screen lock, Do Not Disturb on, and a clean home screen. Phone A films the
  install and the send, phone B receives. A computer with a terminal for the checks.
- Sepolia ETH for phone A, from a faucet, sent ahead of time so the take does not wait on it.
- A camera on a stand over phone A. The app blocks the screen recorder until Settings allows
  recording, so the first run is filmed with the camera, and the recovery words are blurred in
  the edit or covered by hand. Use a throwaway wallet made for the take.
- The system screen recorder, started by hand from Quick Settings, for every shot after that.

## Shot list

| # | Shot | Captured with | Keep out of frame |
|---|---|---|---|
| 1 | The releases page, `NONOS-<tag>.apk` and its `.sha256` downloaded | computer screen | other tabs and accounts |
| 2 | `shasum -a 256 -c` printing `OK`, then `apksigner verify --print-certs` | computer terminal | the home folder path |
| 3 | The APK opened on phone A, Install unknown apps allowed, Install | screen recorder | notifications |
| 4 | Create a wallet, the words, I have written them down, confirm | camera | the words, blurred |
| 5 | Settings, Screen recording allowed, then the recorder started | camera, then recorder | |
| 6 | Home: the network pill on Sepolia, the account pill, Receive at the 0x address | recorder | |
| 7 | Shield, Deposit 0.02 ETH, then 0.01 ETH, each reviewed and confirmed with a fingerprint | recorder | |
| 8 | The privacy meter counting down. The send screen then asks for EARLY, typed by hand | recorder | |
| 9 | Phone B, Receive, the `nox1` address copied or scanned as a QR code | recorder on B | |
| 10 | Send privately 0.01 ETH to B, the fee line, Prove and seal, the clock running | recorder | |
| 11 | Send over Tor: handed over, settling, settled | recorder | |
| 12 | Phone B, Shield, the note arrived | recorder on B | |
| 13 | Settings, Time a proof on this phone, the card | recorder | |

## Script

1. "NØNOS Wallet for Android. Private transfers on Ethereum, proved on the phone, sent over Tor.
   This is Sepolia, a test network, and nothing here is audited."
2. "Every release is on GitHub with its SHA-256. Check it before installing: it has to print OK.
   The signer has to match the certificate published in the repository."
3. "Allow the install from the app that opened the file. The app needs no Google account."
4. "Create a wallet. Write the words on paper. They are the only backup."
5. "Screenshots and recording are blocked by default. I allow recording here, and the words and
   keys stay hidden anyway."
6. "Deposit standard amounts into the shield, twice, because a send joins two notes. Each one is
   reviewed, then confirmed with a fingerprint."
7. "A new note waits for twenty more notes and about six hours. For this video I go early, which
   an observer can link by timing."
8. "The fee is set by the pool and checked before proving. The proof is made on this phone, in
   minutes. Watch the clock." Cut only where the clock stays visible, and say "sped up" if it is.
9. "The lander takes it over Tor and settles it on the pool. On the second phone the note arrives,
   and the chain shows neither the sender, the receiver nor the amount."
10. "Time a proof on your own phone and share the card. The docs are in the repository."

After the edit, play the video once against this list, checking each number on screen against
what the app showed in the take.
