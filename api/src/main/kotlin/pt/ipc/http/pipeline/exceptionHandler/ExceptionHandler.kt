package pt.ipc.http.pipeline.exceptionHandler


import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import javax.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.multipart.MultipartException
import pt.ipc.domain.BadRequest
import pt.ipc.domain.Forbidden
import pt.ipc.domain.NotFound
import pt.ipc.domain.UnauthorizedRequest
import java.net.URI

@ControllerAdvice
class ExceptionHandler{

    @ExceptionHandler(value = [BadRequest::class])
    fun handleBadRequest(
        request: HttpServletRequest,
        ex: Exception
    ): ResponseEntity<Any> =
        Problem(
            type = URI.create(PROBLEMS_DOCS_URI + ex.toProblemType()),
            title = ex.message ?: "Bad Request",
            status = HttpStatus.BAD_REQUEST.value(),
        ).toResponseEntity()


    @ExceptionHandler(value = [NotFound::class])
    fun handleNotFound(
        request: HttpServletRequest,
        ex: Exception
    ) : ResponseEntity<Any> =
        Problem(
            type = URI.create(PROBLEMS_DOCS_URI + ex.toProblemType()),
            title = ex.message ?: "Not Found",
            status = HttpStatus.NOT_FOUND.value()
        ).toResponseEntity()


    @ExceptionHandler(value = [MultipartException::class])
    fun handleMultipartException(
        request: HttpServletRequest,
        ex: Exception
    ) : ResponseEntity<Any> =
        Problem(
            type = URI.create(PROBLEMS_DOCS_URI + "file-too-big"),
            title = "Input file too big. Max size 10MB",
            status = HttpStatus.BAD_REQUEST.value()
        ).toResponseEntity()



    @ExceptionHandler(value = [UnauthorizedRequest::class])
    fun handleUnauthorizedRequest(
        request: HttpServletRequest,
        ex: Exception
    ) : ResponseEntity<Any> =
        Problem(
            type = URI.create(PROBLEMS_DOCS_URI + ex.toProblemType()),
            title = ex.message ?: "Unauthorized",
            status = HttpStatus.UNAUTHORIZED.value()
        ).toResponseEntity()


    @ExceptionHandler(value = [Forbidden::class])
    fun handleForbidden(
        request: HttpServletRequest,
        ex: Exception
    ) : ResponseEntity<Any> =
        Problem(
            type = URI.create(PROBLEMS_DOCS_URI + ex.toProblemType()),
            title = ex.message ?: "Forbidden",
            status = HttpStatus.FORBIDDEN.value()
        ).toResponseEntity()


    @ExceptionHandler(value = [MethodArgumentNotValidException::class])
    fun handleValidationExceptions(
        request: HttpServletRequest,
        ex: MethodArgumentNotValidException
    ): ResponseEntity<Any> =
        Problem(
            type = URI.create(PROBLEMS_DOCS_URI + ex.toProblemType()),
            title = ex.bindingResult.fieldErrors.firstOrNull()?.defaultMessage ?: "Validation Error",
            status = HttpStatus.BAD_REQUEST.value()
        ).toResponseEntity()


    @ExceptionHandler(value = [HttpMessageNotReadableException::class])
    fun handleHttpMessageNotReadableExceptions(
        request: HttpServletRequest,
        ex: HttpMessageNotReadableException
    ): ResponseEntity<Any> =
        Problem(
            type = URI.create("$PROBLEMS_DOCS_URI + invalid-request-body"),
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
            type = URI.create(PROBLEMS_DOCS_URI + "internal-server-error"),
            title = "Internal Server Error",
            status = HttpStatus.INTERNAL_SERVER_ERROR.value()
        ).toResponseEntity()
            .also { ex.printStackTrace() }



    companion object {
        const val PROBLEMS_DOCS_URI = "https://github.com/RodrigoNevesWork/Splitify/tree/main/docs/problems/"

        fun Exception.toProblemType(): String =
            (this::class.simpleName ?: "Unknown")
                .replace(Regex("([a-z])([A-Z])")) { "${it.groupValues[1]}-${it.groupValues[2]}" }
                .lowercase()
    }

}