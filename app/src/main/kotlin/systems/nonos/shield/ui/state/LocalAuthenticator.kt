package systems.nonos.shield.ui.state

import androidx.compose.runtime.staticCompositionLocalOf
import systems.nonos.shield.core.Authenticator

/**
 * The system prompt, reachable from the three screens that need it. The activity provides it, since
 * the view model outlives the activity and must not hold its window. There is no default, so
 * missing wiring fails at the first screen.
 */
internal val LocalAuthenticator = staticCompositionLocalOf<Authenticator> {
    error("no authenticator was provided by the activity")
}
