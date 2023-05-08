package pt.ipc.http.pipeline.exceptionHandler

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.multipart.MultipartException
import pt.ipc.domain.BadRequest
import pt.ipc.domain.Conflit
import pt.ipc.domain.Forbidden
import pt.ipc.domain.NotFound
import pt.ipc.domain.UnauthorizedRequest
import java.net.URI
import javax.servlet.http.HttpServletRequest

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(value = [BadRequest::class])
    fun handleBadRequest(
        request: HttpServletRequest,
        ex: Exception
    ): ResponseEntity<Any> =
        Problem(
            type = URI.create(PROBLEMS_DOCS_URI + ex.toProblemType()),
            title = ex.message ?: "Bad Request",
            status = HttpStatus.BAD_REQUEST.value()
        ).toResponseEntity()

    @ExceptionHandler(value = [Conflit::class])
    fun handleConflit(
        request: HttpServletRequest,
        ex: Exception
    ): ResponseEntity<Any> =
        Problem(
            type = URI.create(PROBLEMS_DOCS_URI + ex.toProblemType()),
            title = ex.message ?: "Conflit",
            status = HttpStatus.BAD_REQUEST.value()
        ).toResponseEntity()

    @ExceptionHandler(value = [NotFound::class])
    fun handleNotFound(
        request: HttpServletRequest,
        ex: Exception
    ): ResponseEntity<Any> =
        Problem(
            type = URI.create(PROBLEMS_DOCS_URI + ex.toProblemType()),
            title = ex.message ?: "Not Found",
            status = HttpStatus.NOT_FOUND.value()
        ).toResponseEntity()

    @ExceptionHandler(value = [MethodArgumentTypeMismatchException::class])
    fun handleArgumentMismatch(
        request: HttpServletRequest,
        ex: Exception
    ): ResponseEntity<Any> =
        Problem(
            type = URI.create(PROBLEMS_DOCS_URI + ex.toProblemType()),
            title = "This Argument does not exists",
            status = HttpStatus.BAD_REQUEST.value()
        ).toResponseEntity()

    @ExceptionHandler(value = [UnableToExecuteStatementException::class])
    fun hanldePostgreSQLError(
        request: HttpServletRequest,
        ex: UnableToExecuteStatementException
    ): ResponseEntity<Any> {
        val map = hashMapOf(
            "users_email_key" to "This email already exists",
            "name_length" to "Name must be bigger than 3",
            "email_is_valid" to "This email is not valid",
            "age_is_valid" to "User Age must be bigger than 7",
            "weight_is_valid" to "Weight must be between 30 and 300 Kg",
            "height_is_valid" to "Height must be between 100 and 250 cm",
            "physical_condition_length" to "The description must be bigger than 5 characters",
            "client_diff_monitor" to "You cannot be your own monitor",
            "request_yourself" to "You cannot be your own monitor",
            "rate_yourself" to "You cannot rate yourself",
            "stars_are_valid" to "Number of stars must be between 1 and 5",
            "state_check" to "You cannot input this state",
            "title_length" to "The title must be bigger than 5",
            "duration_is_valid" to "The plan must be bigger than a day",
            "date_greater_than_current" to "The start date must be after today",
            "index_is_valid" to "The day index must be bigger or equal than 1",
            "title_length" to "The exercise title must be bigger than 5",
            "duration_is_valid" to "The plan must be bigger than a day",
            "description_length" to "The description must be bigger than or the same as 10",
            "type_length" to "The type of exercise must be bigger than 5",
            "sets_is_valid" to "The number of sets must be between 1 and 10",
            "reps_is_valid" to "The number of reps must be between 1 and 50",
            "users_pkey" to "This ID is already in use",
            "client_to_monitor_client_id_key" to "Can't have more than a monitor"
        )

        val key = ex.shortMessage.substringAfter("constraint \"").substringBefore("\"")

        return Problem(
            type = URI.create(PROBLEMS_DOCS_URI + ex.toProblemType()),
            title = map[key] ?: "Error in database",
            status = HttpStatus.BAD_REQUEST.value()
        ).toResponseEntity()
    }

    @ExceptionHandler(value = [MultipartException::class])
    fun handleMultipartException(
        request: HttpServletRequest,
        ex: Exception
    ): ResponseEntity<Any> =
        Problem(
            type = URI.create(PROBLEMS_DOCS_URI + "file-too-big"),
            title = "Input file too big. Max size 10MB",
            status = HttpStatus.BAD_REQUEST.value()
        ).toResponseEntity()

    @ExceptionHandler(value = [UnauthorizedRequest::class])
    fun handleUnauthorizedRequest(
        request: HttpServletRequest,
        ex: Exception
    ): ResponseEntity<Any> =
        Problem(
            type = URI.create(PROBLEMS_DOCS_URI + ex.toProblemType()),
            title = ex.message ?: "Unauthorized",
            status = HttpStatus.UNAUTHORIZED.value()
        ).toResponseEntity()

    @ExceptionHandler(value = [Forbidden::class])
    fun handleForbidden(
        request: HttpServletRequest,
        ex: Exception
    ): ResponseEntity<Any> =
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

    @ExceptionHandler(value = [HttpRequestMethodNotSupportedException::class])
    fun handleRequestMethodNotSupportedException(
        request: HttpServletRequest,
        ex: HttpRequestMethodNotSupportedException
    ): ResponseEntity<Any> =
        Problem(
            type = URI.create("${PROBLEMS_DOCS_URI}method-not-allowed"),
            title = "${ex.method} Not allowed",
            status = HttpStatus.METHOD_NOT_ALLOWED.value()
        ).toResponseEntity()

    @ExceptionHandler(value = [HttpMessageNotReadableException::class])
    fun handleHttpMessageNotReadableExceptions(
        request: HttpServletRequest,
        ex: HttpMessageNotReadableException
    ): ResponseEntity<Any> =
        Problem(
            type = URI.create("${PROBLEMS_DOCS_URI}invalid-request-body"),
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
        const val PROBLEMS_DOCS_URI = "https://github.com/intelligent-personalized-care/ipc/tree/main/api/docs/problems/"

        fun Exception.toProblemType(): String =
            (this::class.simpleName ?: "Unknown")
                .replace(Regex("([a-z])([A-Z])")) { "${it.groupValues[1]}-${it.groupValues[2]}" }
                .lowercase()
    }
}
