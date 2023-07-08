package pt.ipc.http.pipeline.exceptionHandler

import org.springframework.http.ResponseEntity

data class Problem(
    val title: String,
    val status: Int
) {

    fun toResponseEntity() = ResponseEntity
        .status(status)
        .header("Content-Type", PROBLEM_MEDIA_TYPE)
        .body<Any>(this)

    companion object {

        private const val APPLICATION_TYPE = "application"
        private const val PROBLEM_SUBTYPE = "problem+json"
        const val PROBLEM_MEDIA_TYPE = "$APPLICATION_TYPE/$PROBLEM_SUBTYPE"
    }
}
