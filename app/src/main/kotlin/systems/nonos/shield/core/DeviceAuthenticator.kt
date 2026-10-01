package systems.nonos.shield.core

import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext

/**
 * The system prompt as a suspending call, with the activity in the foreground. It resumes at most
 * once, so a failure and a success can never both reach the core.
 */
internal class DeviceAuthenticator(private val activity: FragmentActivity) : Authenticator {
    override suspend fun confirm(reason: String) = withContext(Dispatchers.Main) {
        suspendCancellableCoroutine { waiting ->
            val prompt = BiometricPrompt(
                activity,
                ContextCompat.getMainExecutor(activity),
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(
                        result: BiometricPrompt.AuthenticationResult,
                    ) {
                        if (waiting.isActive) waiting.resume(Unit)
                    }

                    override fun onAuthenticationError(code: Int, message: CharSequence) {
                        if (waiting.isActive) waiting.resumeWithException(outcome(code))
                    }
                },
            )
            prompt.authenticate(AuthPrompt.info(reason))
        }
    }

    // Dismissing is a change of mind and returns to idle quietly. Any other error is shown. A
    // finger that does not match keeps the prompt up and never lands here.
    private fun outcome(code: Int): AuthFailure = when (code) {
        BiometricPrompt.ERROR_USER_CANCELED,
        BiometricPrompt.ERROR_NEGATIVE_BUTTON,
        BiometricPrompt.ERROR_CANCELED,
        -> AuthFailure.Cancelled()

        else -> AuthFailure.Refused()
    }
}
