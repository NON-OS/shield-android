package systems.nonos.shield

import android.app.Application

/**
 * The application object. It holds nothing and starts nothing: no analytics
 * initialiser, no crash reporter, no push registration, no Play Services check.
 * The wallet is built when a screen asks for it and dropped when the session
 * locks.
 */
class ShieldApp : Application()
