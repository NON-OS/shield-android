package systems.nonos.shield.ui.state

/** Which screen the wallet is on, decided by the core's state and not a back stack. */
enum class Stage {
    Opening,
    NeedsWallet,
    ShowingPhrase,
    Restoring,
    Locked,

    /** Locked, and the key that sealed the vault is gone from this device. */
    KeyGone,
    Ready,
    Measuring,

    ShowingLicences,
}

/** What long call is in flight. Proving takes minutes, so it can be cancelled. */
enum class Busy {
    Idle,
    Unlocking,
    Syncing,

    Depositing,
    Proving,
    Cancelling,

    Reading,
    Reviewing,
    Sending,

    Handing,
    Measuring,

    ShowingLicences,

    /** After a restore, looking for the other accounts the words were used with. */
    Finding,
}
