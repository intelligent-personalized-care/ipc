package pt.ipc_app.ui.components

import android.net.Uri

/**
 * Information about an author.
 */
data class AuthorInfo(
    val number: String,
    val name: String,
    val githubLink: Uri,
    val email: String,
)
