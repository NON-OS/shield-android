# Testing NØNOS Wallet on Android

This is the test phase. The shield runs on Sepolia, a test network, and every token you use here has
no value. Nothing has been audited yet. Please try to break it, and tell us what happened.

This guide takes you from nothing to one private transfer and one withdrawal, in about 45 minutes,
most of it waiting for the network.

## What you need

- An Android phone on Android 9 or later, 64-bit ARM, with a screen lock set. The wallet will not
  create a wallet on a phone without one. No Google account and no Play Services are needed.
- A second wallet to send to. A second Android phone, an iPhone with NØNOS Wallet, or a friend.

## 1. Install

1. On [Releases](https://github.com/NON-OS/shield-android/releases), download
   `NONOS-v0.9.0-test.apk` and `NONOS-v0.9.0-test.apk.sha256` into one folder.
2. Check the file on a computer: `sha256sum -c NONOS-v0.9.0-test.apk.sha256` on Linux, or
   `shasum -a 256 -c NONOS-v0.9.0-test.apk.sha256` on macOS, must print `OK`. If it prints anything
   else, do not install it.
3. Copy the APK to the phone and open it. Allow Install unknown apps for the app that opened it,
   then Install. Play Protect may offer to scan it, which sends the APK to Google. Declining does
   not stop the install.

This test build is signed with a test key. The first build signed with the release key will not
install over it: uninstalling removes the wallet from the phone, so keep your recovery words, and
restore from them after installing the release build.

## 2. Create a wallet

1. Open the app and tap Create a wallet.
2. Write the 24 recovery words on paper, in order. They are the only way back to this wallet. The
   app hides them from screenshots and screen recordings.
3. Keep the network on Sepolia, at the top of the home screen.

Use a fresh wallet for testing, never one that has held real money.

## 3. Get test funds

1. Tap Receive, then "Receive at the 0x address", and copy your 0x address.
2. Sepolia ETH: paste the address into any Sepolia faucet. You need about 0.1 ETH for the whole
   test, gas included.
3. Test NOX: ask in the NØNOS Telegram group with your 0x address, and we send some.

## 4. Shield

1. Open Shield, then Deposit. Choose ETH and type `0.05`.
   Amounts come in standard sizes only: 1, 2 or 5 followed by zeros. For ETH that is 0.01 up to
   about 18.44, and for NOX 1,000 up to 5,000,000.
2. The review shows the 0.50% deposit fee, what the note will hold (0.04975 ETH), and the most
   the network can charge. Confirm with your fingerprint, face or screen lock.
3. The note appears once the deposit is in a block, and it can be spent after the pool commits its
   next root, which happens every 5 minutes.

## 5. Send privately

1. On the second wallet, open Receive and copy its `nox1…` address. It is long on purpose: it
   carries a post-quantum key.
2. On your wallet, open Shield, tap Send, paste the address, and type `0.02`.
3. The screen shows the fee: 0.0005 ETH for the transfer and 0.0025 ETH for whoever lands it.
4. A new note waits until 20 more notes and 6 hours have passed, so that the time of your deposit
   cannot point at your spend. The pool is small during the test, so type `EARLY` when the app asks.
   It tells you what that costs in privacy.
5. Tap Prove and seal. The proof is made on your phone, which takes a minute or two. Keep the app
   open.
6. Tap Send over Tor. The proof goes through the Tor client inside the app to the first of five
   landers that answers. The screen follows it until it shows Settled, with the transaction hash.

## 6. Receive

Open the app on the second phone. It reads the pool when it opens and every few minutes while it is
open. The note is found and opened on that phone, and the shielded balance goes up by 0.02 ETH.

## 7. Withdraw

1. On the second wallet, open Shield and tap Withdraw. The address is filled in with an unused
   address of the same wallet, so the withdrawal does not point back to an address you used before.
2. Type `0.01`. Withdrawals come in standard sizes too. The fee is 0.50% of the amount plus the
   network part, about 0.00255 ETH in all. It comes out of the shield, not out of the amount, and
   the rest stays shielded as change.
3. Prove, send, and wait for Settled. The ETH arrives at that address, in public.

## 8. Settle it yourself

If a spend has not landed after 30 minutes, the screen offers Settle it yourself. It sends the
settlement from your own public account. It works without anyone else, and it shows in public that
your account settled this spend. The fee comes back to you.

## 9. Tell us

For every step, note what happened and how long it took. Send:

- the phone model and Android version;
- the transaction hashes from Settled;
- the proving time the app shows;
- a screenshot of anything that looked wrong, never of your recovery words.

Bugs go to [Issues](https://github.com/NON-OS/shield-android/issues). A security problem goes
through the Security tab of this repository, or to ek@nonos.systems or team@nonos.systems, and
not into a public issue.

## What to expect

- The pool is new, so the crowd your transfer hides in is small. That is honest for day one, and it
  grows with every user.
- The app reads the production pool only. Notes from the earlier test pools are not shown.
- If proving stops with a sentence on screen, that sentence names the cause. Send it to us as it is.
