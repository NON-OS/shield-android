package systems.nonos.shield

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import systems.nonos.shield.core.Authenticator
import systems.nonos.shield.core.DeviceAuthenticator
import systems.nonos.shield.ui.ShieldRoot
import systems.nonos.shield.ui.state.LocalAuthenticator
import systems.nonos.shield.ui.state.WalletViewModel
import systems.nonos.shield.ui.state.captureAllowed
import systems.nonos.shield.ui.state.leftForeground

/**
 * The only activity. It sets the secure flag before the first frame and lifts it only while the
 * owner allows recording and no words or key are on screen. It hosts the authentication prompt.
 */
class MainActivity : FragmentActivity() {
    private val model: WalletViewModel by lazy {
        ViewModelProvider(
            this,
            WalletViewModel.factory(applicationContext),
        )[WalletViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // Before the first frame. Only the undistributed capture build sets the resource false.
        val blocking = resources.getBoolean(R.bool.block_screen_capture)
        secureWindow(blocking)
        super.onCreate(savedInstanceState)
        setContent {
            val state by model.state.collectAsStateWithLifecycle()
            val secure = blocking && !state.captureAllowed
            LaunchedEffect(secure) { secureWindow(secure) }
            val auth: Authenticator = remember { DeviceAuthenticator(this@MainActivity) }
            CompositionLocalProvider(LocalAuthenticator provides auth) {
                ShieldRoot(model)
            }
        }
    }

    /** Secure, nothing reaches a screenshot, the recents thumbnail or a recorder. */
    private fun secureWindow(secure: Boolean) {
        if (secure) {
            window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
    }

    // onStop, not onPause: older releases confirm the credential in a separate activity, and
    // locking on pause would drop the session that prompt is opening.
    override fun onStop() {
        super.onStop()
        model.leftForeground()
    }
}
