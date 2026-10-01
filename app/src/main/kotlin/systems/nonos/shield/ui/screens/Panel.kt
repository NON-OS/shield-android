package systems.nonos.shield.ui.screens

/**
 * Which panel of the unlocked wallet is showing. It is local to the balance screen, since switching
 * panels changes nothing about the wallet.
 */
internal enum class Panel {
    Overview,
    Send,
    Withdraw,
    Receive,
    Deposit,
    Settings,

    /** The public account: a signed send, and its `0x` address to receive at. */
    AccountSend,
    AccountReceive,

    /** Swapping between ETH, NOX and USDC, on mainnet. */
    Swap,

    /** The shield: private balances and what can be done with them. */
    Shield,

    /** The accounts of this wallet and the view-only ones, and the words and keys of one. */
    Accounts,
    Keys,
}
