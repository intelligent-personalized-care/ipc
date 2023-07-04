package pt.ipc_app.service.utils

data class MultipartEntry(
    val name: String,
    val value: Any,
    val contentType: ContentType? = null
)
