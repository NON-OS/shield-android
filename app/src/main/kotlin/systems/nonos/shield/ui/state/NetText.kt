package systems.nonos.shield.ui.state

import systems.nonos.shield.core.NetException

/**
 * What went wrong on the network, in one sentence per cause, the same as on iOS, so a failed send
 * says whether Tor had no route yet, the connection dropped, or the other side refused.
 */
internal fun network(source: NetException): String = when (source) {
    is NetException.WrongChain -> "A server answered for the wrong network. Nothing was signed."
    is NetException.WrongRelayer -> "The relayer answered as someone else. Nothing was sent."
    is NetException.Rejected ->
        "The network refused that (code ${source.code}). Nothing changed."
    is NetException.ProxyUnreachable ->
        "Tor is still starting. Try again in a moment. Nothing was sent."
    is NetException.ProxyRefused ->
        "Tor could not open a route there yet. Try again in a moment. Nothing was sent."
    is NetException.Transport ->
        "The connection over Tor dropped before an answer came. Try again."
    is NetException.ReplyShape, is NetException.ReplyTooLarge ->
        "A server answered in a form the wallet does not read. Nothing changed."
    else -> "The network could not be reached over Tor. Nothing was sent."
}
