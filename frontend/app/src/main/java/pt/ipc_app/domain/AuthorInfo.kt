package pt.ipc_app.domain

import android.net.Uri

/**
 * Information about an author.
 */
data class AuthorInfo(
    val name: String,
    val githubLink: Uri,
    val email: String,
)
