package pt.ipc.http.pipeline.exceptionHandler

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import io.jsonwebtoken.JwtException
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.multipart.MultipartException
import org.springframework.web.multipart.support.MissingServletRequestPartException
import pt.ipc.domain.exceptions.BadRequest
import pt.ipc.domain.exceptions.Conflict
import pt.ipc.domain.exceptions.Forbidden
import pt.ipc.domain.exceptions.NotFound
import pt.ipc.domain.exceptions.UnauthorizedRequest
import pt.ipc.domain.jwt.message
import pt.ipc.storage.repositories.exceptions.ExceptionsDB
import javax.servlet.http.HttpServletRequest

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(value = [NullPointerException::class])
    fun handleNullPointerException(
        request: HttpServletRequest,
        ex: NullPointerException
    ): ResponseEntity<Any> =
        Problem(
            title = ex.message ?: "Null Pointer Exception",
            status = HttpStatus.BAD_REQUEST.value()
        ).toResponseEntity()

    @ExceptionHandler(value = [BadRequest::class])
    fun handleBadRequest(
        request: HttpServletRequest,
        ex: Exception
    ): ResponseEntity<Any> =
        Problem(
            title = ex.message ?: "Bad Request",
            status = HttpStatus.BAD_REQUEST.value()
        ).toResponseEntity()

    @ExceptionHandler(value = [Conflict::class])
    fun handleConflict(
        request: HttpServletRequest,
        ex: Exception
    ): ResponseEntity<Any> =
        Problem(
            title = ex.message ?: "Conflict",
            status = HttpStatus.CONFLICT.value()
        ).toResponseEntity()

    @ExceptionHandler(value = [NotFound::class])
    fun handleNotFound(
        request: HttpServletRequest,
        ex: Exception
    ): ResponseEntity<Any> =
        Problem(
            title = ex.message ?: "Not Found",
            status = HttpStatus.NOT_FOUND.value()
        ).toResponseEntity()

    @ExceptionHandler(value = [MethodArgumentTypeMismatchException::class])
    fun handleArgumentMismatch(
        request: HttpServletRequest,
        ex: Exception
    ): ResponseEntity<Any> =
        Problem(
            title = "${ex.message}",
            status = HttpStatus.BAD_REQUEST.value()
        ).toResponseEntity()

    @ExceptionHandler(value = [UnableToExecuteStatementException::class])
    fun handlePostgresError(
        request: HttpServletRequest,
        ex: UnableToExecuteStatementException
    ): ResponseEntity<Any> {
        println(ex)

        val key = ex.shortMessage.substringAfter("constraint \"").substringBefore("\"")

        return Problem(
            title = ExceptionsDB.map[key] ?: "Error in database",
            status = HttpStatus.BAD_REQUEST.value()
        ).toResponseEntity()
    }

    @ExceptionHandler(value = [MissingServletRequestParameterException::class])
    fun handleMissingMethodParameter(
        request: HttpServletRequest,
        ex: MissingServletRequestParameterException
    ): ResponseEntity<Any> =
        Problem(
            title = ex.message,
            status = HttpStatus.BAD_REQUEST.value()
        ).toResponseEntity()

    @ExceptionHandler(value = [MissingServletRequestPartException::class])
    fun handleMissingMultipartParameter(
        request: HttpServletRequest,
        ex: MissingServletRequestPartException
    ): ResponseEntity<Any> =
        Problem(
            title = ex.message ?: "Required request part is not present",
            status = HttpStatus.BAD_REQUEST.value()
        ).toResponseEntity()

    @ExceptionHandler(value = [MultipartException::class])
    fun handleMultipartException(
        request: HttpServletRequest,
        ex: Exception
    ): ResponseEntity<Any> {
        return Problem(
            title = ex.message ?: "Multipart File Problem without message",
            status = HttpStatus.BAD_REQUEST.value()
        ).toResponseEntity()
    }

    @ExceptionHandler(value = [JwtException::class])
    fun handleJWTException(
        request: HttpServletRequest,
        ex: JwtException
    ): ResponseEntity<Any> {
        return Problem(
            title = ex.message(),
            status = HttpStatus.BAD_REQUEST.value()
        ).toResponseEntity()
    }

    @ExceptionHandler(value = [UnauthorizedRequest::class])
    fun handleUnauthorizedRequest(
        request: HttpServletRequest,
        ex: Exception
    ): ResponseEntity<Any> =
        Problem(
            title = ex.message ?: "Unauthorized",
            status = HttpStatus.UNAUTHORIZED.value()
        ).toResponseEntity()

    @ExceptionHandler(value = [Forbidden::class])
    fun handleForbidden(
        request: HttpServletRequest,
        ex: Exception
    ): ResponseEntity<Any> =
        Problem(
            title = ex.message ?: "Forbidden",
            status = HttpStatus.FORBIDDEN.value()
        ).toResponseEntity()

    @ExceptionHandler(value = [MethodArgumentNotValidException::class])
    fun handleValidationExceptions(
        request: HttpServletRequest,
        ex: MethodArgumentNotValidException
    ): ResponseEntity<Any> =
        Problem(
            title = ex.bindingResult.fieldErrors.firstOrNull()?.defaultMessage ?: "Validation Error",
            status = HttpStatus.BAD_REQUEST.value()
        ).toResponseEntity()

    @ExceptionHandler(value = [HttpRequestMethodNotSupportedException::class])
    fun handleRequestMethodNotSupportedException(
        request: HttpServletRequest,
        ex: HttpRequestMethodNotSupportedException
    ): ResponseEntity<Any> =
        Problem(
            title = "${ex.method} Not allowed",
            status = HttpStatus.METHOD_NOT_ALLOWED.value()
        ).toResponseEntity()

    @ExceptionHandler(value = [HttpMessageNotReadableException::class])
    fun handleHttpMessageNotReadableExceptions(
        request: HttpServletRequest,
        ex: HttpMessageNotReadableException
    ): ResponseEntity<Any> =
        Problem(
            title = "Invalid request body${
            ex.rootCause.let {
                ": " +
                    when (it) {
                        is UnrecognizedPropertyException -> "Unknown property '${it.propertyName}'"
                        is JsonParseException -> it.originalMessage
                        is MissingKotlinParameterException -> "Missing property '${it.parameter.name}'"
                        else -> null
                    }
            }
            }",
            status = HttpStatus.BAD_REQUEST.value()
        ).toResponseEntity()

    @ExceptionHandler(value = [Exception::class])
    fun handleUncaughtExceptions(
        request: HttpServletRequest,
        ex: Exception
    ): ResponseEntity<Any> =
        Problem(
            title = ex.message ?: "Internal server Error",
            status = HttpStatus.INTERNAL_SERVER_ERROR.value()
        ).toResponseEntity()
            .also { ex.printStackTrace() }

}
