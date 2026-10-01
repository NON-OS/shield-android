# Using NØNOS Wallet on Android

How to make or restore a wallet and use each screen. Installing and checking the APK is in
[05-install.md](05-install.md), and what each refusal means is in [10-messages.md](10-messages.md).
What the core checks and what it does not is in
[its security status](https://github.com/NON-OS/shield-core/blob/main/docs/20-security-status.md).

## First run

The phone needs a screen lock first, because the keystore will not seal a seed without one.

1. **Create a wallet**, then write the 24 words on paper, not in a photo and not in a manager that
   syncs. They are the backup of the wallet, and nothing else restores it. Leaving the words screen
   locks the new wallet, and the words can be shown again from Settings.
2. Confirm yourself when the phone asks. The seed is sealed under a key held in the StrongBox chip,
   or in the TEE where the phone has no chip, and that key opens only after the phone confirms you.
3. **Restore from a phrase** takes words of any standard length, 12 to 24, or an account private key.
4. If the phone loses the key, after its screen lock is removed, the locked screen says so and
   offers **Destroy this wallet**, behind the same confirmation as Settings. Then restore.

There is no proxy app and no server address: the wallet carries its own Tor client, and the first
read of the chain starts it, which is slower on a cold start.

## Home

- The **network pill** chooses Sepolia, a test network and the default, or Ethereum, where money
  is real. A screen that acts on mainnet says real money above everything else.
- The **account pill** switches between the accounts of one phrase, adds one, or adds a view-only
  account from a view key.
- **Send**, **Receive** and **Swap** act on the public `0x` account. Every send and swap is
  reviewed by the core first, then confirmed with a strong biometric or the device credential.
  Swaps run on Ethereum only.
- **Shield** opens the private balances on Sepolia.

## The shield

- **Deposit** a standard amount, 1, 2 or 5 followed by zeros, from 1,000 NOX or 0.01 ETH. From
  this account it is an approval, when NOX needs one, then the deposit, each confirmed. From
  another wallet the screen shows what to send there after the pool has been checked.
- **Receive** shows the `nox1` address and its QR code, about 2,000 characters, because it carries
  a post-quantum key. Only this wallet can open a note sealed to it.
- **Send** privately or **Withdraw**: the form shows the fee before anything is proved. A transfer
  pays from 2,400 NOX or 0.003 ETH, a withdrawal 0.50% of its amount, each with a network part that
  follows gas. The pool sets the fee, nobody types it, and the core checks it with the pool before
  proving. A withdrawal is public, so its address starts as a fresh one of this wallet.
- **Prove and seal** proves on the phone, with a clock and Cancel. Then **Send over Tor** hands the
  proof to the lander, and the screen follows it: handed over, settling, settled. **Settle it
  yourself** lands it from the public account instead, which links that account to the spend in
  public. The screen puts it first when nobody has landed the spend after half an hour.
- **The wait**: a note spends privately after 20 more notes have joined and about 6 hours have
  passed. The privacy meter counts both down, and typing EARLY sends sooner, linked by timing.
- **Take back what a relayer never settled** returns every note the chain shows unspent.

## Settings

Lock when idle, lock now, allow screen recording with the words and keys always hidden, recovery
words and keys after the phone confirms you, time a proof on this phone, licences, and destroy this
wallet. TalkBack reads the recovery words aloud, so use headphones when they are on screen.
