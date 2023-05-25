package pt.ipc.http.controllers

import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import pt.ipc.domain.Exercise
import pt.ipc.domain.Unauthorized
import pt.ipc.domain.User
import pt.ipc.http.models.ConnectionRequestDecisionInput
import pt.ipc.http.models.RequestInformation
import pt.ipc.http.pipeline.authentication.Authentication
import pt.ipc.http.pipeline.exceptionHandler.Problem.Companion.PROBLEM_MEDIA_TYPE
import pt.ipc.http.utils.Uris
import pt.ipc.services.ClientsService
import pt.ipc.services.dtos.RegisterClientInput
import pt.ipc.services.dtos.RegisterOutput
import java.time.LocalDate
import java.util.*
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping(produces = ["application/json", PROBLEM_MEDIA_TYPE])
class ClientsController(private val clientsService: ClientsService) {

    @GetMapping(Uris.USER_HOME)
    fun getUserHome(): ResponseEntity<String> = ResponseEntity.accepted().body("Hello User")

    @PostMapping(Uris.CLIENT_REGISTER)
    fun registerClient(@RequestBody registerClientInput: RegisterClientInput, response: HttpServletResponse): ResponseEntity<RegisterOutput> {
        val registerOutput: RegisterOutput = clientsService.registerClient(registerClientInput)

        addAuthenticationCookies(response, registerOutput.token)

        return ResponseEntity.status(HttpStatus.CREATED).body(registerOutput)
    }

    @Authentication
    @PostMapping(Uris.CLIENT_PHOTO)
    fun addProfilePicture(
        @PathVariable clientId: UUID,
        user: User,
        @RequestParam profilePicture: MultipartFile
    ): ResponseEntity<String> {
        if (user.id != clientId) throw Unauthorized

        clientsService.addProfilePicture(clientID = clientId, profilePicture = profilePicture.bytes)

        return ResponseEntity.status(HttpStatus.CREATED).body("Profile Picture Created")
    }

    @Authentication
    @PostMapping(Uris.CLIENT_DECIDE_REQUEST)
    fun decideRequest(
        @PathVariable clientId: UUID,
        @PathVariable requestId: UUID,
        user: User,
        @RequestBody decision: ConnectionRequestDecisionInput
    ): ResponseEntity<String> {
        if (user.id != clientId) throw Unauthorized

        clientsService.decideRequest(requestID = requestId, clientID = user.id, accept = decision.accept)

        return ResponseEntity.status(HttpStatus.OK).body("Request Decision Made")
    }

    @Authentication
    @GetMapping(Uris.CLIENT_REQUESTS)
    fun getRequestsOfClient(@PathVariable clientId: UUID, user: User): ResponseEntity<List<RequestInformation>> {
        if (clientId != user.id) throw Unauthorized

        val requests: List<RequestInformation> = clientsService.getRequestsOfClient(clientID = clientId)

        return ResponseEntity.ok(requests)
    }

    @Authentication
    @GetMapping(Uris.EXERCISES_OF_CLIENT)
    fun getExercisesOfClient(
        @PathVariable clientId: UUID,
        @RequestParam(required = false)
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        date: LocalDate?
    ): ResponseEntity<List<Exercise>> {
        val exercises = clientsService.getExercisesOfClient(clientID = clientId, date = date)
        return ResponseEntity.ok(exercises)
    }

    companion object {

        fun addAuthenticationCookies(response: HttpServletResponse, token: String) {
            val responseCookie = ResponseCookie.from("token", token)
                .httpOnly(true)
                .path("/")
                .sameSite("Strict")
                .build()

            response.addCookie(responseCookie)
        }

        private fun HttpServletResponse.addCookie(cookie: ResponseCookie) {
            this.addHeader(HttpHeaders.SET_COOKIE, cookie.toString())
        }
    }
}
