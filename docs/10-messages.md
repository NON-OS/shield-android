# What each message means

Every refusal is one sentence, the same on Android and iOS, and nothing is sent after any of them
unless it says so. The sentences live in `ui/state/Failures.kt`, `ProveText.kt` and `NetText.kt`,
and the pool refusals come from the core.

## Amounts, fees and the wait

| What the screen says | What it means |
|---|---|
| Amounts come in standard sizes, 1, 2 or 5 followed by zeros, from 1,000 NOX or 0.01 ETH. | the pool takes standard amounts only |
| That is more than the balance covers. | the amount and the fee are more than the notes hold |
| Two notes are needed to send. Receive again first. | the notes held cannot make the pair a spend needs |
| The pool would not take this fee. Try again in a minute, or update the app. | the pool refused the fee the core asked it to check before proving |
| These notes are still new. Wait, or go early from the send screen. | the wait after a deposit has not passed |
| That review has expired. Review the send again. | a review holds for 90 seconds |

## The pool

| What the screen says | What it means |
|---|---|
| That EVM address is not on the beta list. | only listed addresses may deposit while the pool is in beta |
| That EVM address has reached its beta limit. | the cap per address for that coin is used up |
| The pool has reached its beta limit. | the cap of the whole pool is used up |
| The beta is paused. | deposits are stopped for now |
| This pool is wound down. Money sent to it could not be spent. | it will never settle again |
| This account needs ETH to pay the network fee. | the public account holds no ETH on this network |

## Proving

| What the screen says | What it means |
|---|---|
| The notes could not be proved against the pool. Refresh, then try again. | the notes and the pool as read do not fit together, often because the read is old |
| The pool moved while the proof was made. Try again. | the root the proof used is not published yet |
| The proof did not check out on this phone. Nothing was sent. Try again. | the phone verifies every proof before it leaves, and this one failed |
| Proving was stopped. Nothing was sent. | Cancel was pressed |
| This phone gave no randomness for the proof. Try again. | the system random source failed |

## The network

| What the screen says | What it means |
|---|---|
| Tor is still starting. Try again in a moment. Nothing was sent. | the Tor client inside the app has no circuit yet |
| Tor could not open a route there yet. Try again in a moment. Nothing was sent. | Tor reached no exit or onion service for that host |
| The connection over Tor dropped before an answer came. Try again. | the circuit closed mid reply |
| A server answered for the wrong network. Nothing was signed. | an RPC reported another chain id |
| The relayer answered as someone else. Nothing was sent. | the lander answered with an identity other than the pinned one |
| Two servers disagree, about the pools or about the NOX fees | two RPC answers did not match, so nothing was sent. Try again |

## The keys

| What the screen says | What it means |
|---|---|
| The device keystore refused. | the keystore would not use the key that seals the wallet |
| This device would not confirm it was you. | the biometric or screen lock prompt failed |
| This device no longer has the key that sealed this wallet. Restore from the recovery words. | removing the screen lock destroys the keystore key, and a reinstall that kept the app data has a new one. The locked screen offers Destroy this wallet, then the words restore it |
| That recovery phrase did not check out. | a word or the checksum is wrong |
| A recovery phrase is 12, 15, 18, 21 or 24 words. | the phrase has another length |
