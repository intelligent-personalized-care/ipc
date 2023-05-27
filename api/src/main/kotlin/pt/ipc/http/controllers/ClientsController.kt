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
import pt.ipc.domain.PlanOutput
import pt.ipc.domain.Rating
import pt.ipc.domain.User
import pt.ipc.domain.exceptions.Unauthorized
import pt.ipc.http.models.ConnectionRequestInput
import pt.ipc.http.models.RequestIdOutput
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
        @PathVariable clientID: UUID,
        @RequestParam profilePicture: MultipartFile,
        user: User
    ): ResponseEntity<String> {
        if (user.id != clientID) throw Unauthorized

        clientsService.addProfilePicture(clientID = clientID, profilePicture = profilePicture.bytes)

        return ResponseEntity.status(HttpStatus.CREATED).body("Profile Picture Created")
    }

    @Authentication
    @PostMapping(Uris.CLIENT_REQUESTS)
    fun makeRequestForMonitor(@PathVariable clientID: UUID, @RequestBody connRequest: ConnectionRequestInput, user: User): ResponseEntity<RequestIdOutput> {
        if (user.id != clientID) throw Unauthorized

        val requestID = clientsService.requestMonitor(monitorID = connRequest.monitorId, clientID = clientID, requestText = connRequest.text)

        return ResponseEntity.status(HttpStatus.CREATED).body(RequestIdOutput(requestID = requestID))
    }

    @Authentication
    @GetMapping(Uris.PLAN_CURRENT)
    fun getPlanOfClientContainingDate(
        @PathVariable clientID: UUID,
        @RequestParam(required = false)
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        date: LocalDate?
    ): ResponseEntity<PlanOutput> {
        val res = clientsService.getPlanOfClientContainingDate(clientID = clientID, date = date ?: LocalDate.now())

        return ResponseEntity.ok(res)
    }

    @Authentication
    @GetMapping(Uris.EXERCISES_OF_CLIENT)
    fun getExercisesOfClient(
        @PathVariable clientID: UUID,
        @RequestParam(required = false)
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        date: LocalDate?
    ): ResponseEntity<List<Exercise>> {
        val exercises = clientsService.getExercisesOfClient(clientID = clientID, date = date)
        return ResponseEntity.ok(exercises)
    }

    @Authentication
    @PostMapping(Uris.MONITOR_RATE)
    fun rateMonitor(@PathVariable monitorID: UUID, @RequestBody rating: Rating, user: User): ResponseEntity<Unit> {
        if (rating.user != user.id) throw Unauthorized
        clientsService.rateMonitor(monitorID = monitorID, clientID = user.id, rating = rating.rating)
        return ResponseEntity.ok().build()
    }

    @Authentication
    @PostMapping(Uris.VIDEO_OF_EXERCISE)
    fun postVideoOfExercise(
        @RequestBody video: MultipartFile,
        @PathVariable clientID: UUID,
        @PathVariable dailyListID: Int,
        @PathVariable exerciseID: Int,
        @PathVariable planID: Int,
        user: User
    ): ResponseEntity<Unit> {
        if (user.id != clientID) throw Unauthorized

        clientsService.uploadVideoOfClient(
            video = video.bytes,
            clientID = user.id,
            planID = planID,
            dailyListID = dailyListID,
            exerciseID = exerciseID
        )

        return ResponseEntity.ok().build()
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
