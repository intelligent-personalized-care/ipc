package pt.ipc_app.service.utils

import okhttp3.MediaType.Companion.toMediaType

enum class ContentType(val type: String) {
    JSON("application/json"),
    PROBLEM_JSON("application/problem+json"),
    IMAGE("image/jpeg"),
    VIDEO("video/mp4"),
    PDF("application/pdf"),
    MULTIPART("multipart/form-data");

    val mediaType = type.toMediaType()
}
