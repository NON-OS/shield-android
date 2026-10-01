package systems.nonos.shield.ui.screens

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import java.io.File

/**
 * The share sheet over a measured proof and its public words, for sending to a
 * Mac to check against the live verifier. The two files are shared by a
 * content address granted to the receiving app for this share only.
 */
internal fun shareProof(context: Context, proofPath: String) {
    val proof = File(proofPath)
    val publics = File(proof.parentFile, "launch-proof.publics.json")
    shareFiles(context, listOf(proof, publics), "Export the proof")
}

/** The share sheet over files the core wrote to its export folder. */
internal fun shareFiles(context: Context, all: List<File>, title: String) {
    val files = all.filter { it.isFile }
    val authority = "${context.packageName}.proofs"
    val uris = ArrayList(files.map { FileProvider.getUriForFile(context, authority, it) })
    val send = Intent(Intent.ACTION_SEND_MULTIPLE).apply {
        type = "application/octet-stream"
        putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(Intent.createChooser(send, title))
}
