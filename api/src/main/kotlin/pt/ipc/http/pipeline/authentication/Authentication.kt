package pt.ipc.http.pipeline.authentication

import pt.ipc.domain.Role

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class Authentication(val role : Role)
