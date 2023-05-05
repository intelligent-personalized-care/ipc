package pt.ipc.http.pipeline.authentication

@Target(AnnotationTarget.FUNCTION)
annotation class Authentication(val bothRoles : Boolean = false)
