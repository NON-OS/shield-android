package systems.nonos.shield.core

/**
 * Asking the device to confirm the user is present. The prompt is raised above the core on the main
 * thread, before the call that needs the key, and the keystore counts the confirmation itself. It
 * never reports a success it did not get.
 */
internal interface Authenticator {
    /** Suspend until the device confirms the user, or throw. */
    suspend fun confirm(reason: String)
}

/** What a prompt can end as, other than success. */
internal sealed class AuthFailure : Exception() {
    /** The user dismissed the prompt. Nothing is wrong and nothing is shown. */
    internal class Cancelled : AuthFailure()

    /** The device would not confirm the user, or has no way to. */
    internal class Refused : AuthFailure()
}
