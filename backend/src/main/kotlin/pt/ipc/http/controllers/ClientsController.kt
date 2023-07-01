package pt.ipc.http.controllers

import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import pt.ipc.domain.*
import pt.ipc.domain.ClientOutput
import pt.ipc.domain.RatingInput
import pt.ipc.domain.exceptions.ForbiddenRequest
import pt.ipc.http.utils.SseEmitterUtils
import pt.ipc.http.models.*
import pt.ipc.http.pipeline.authentication.Authentication
import pt.ipc.http.pipeline.exceptionHandler.Problem.Companion.PROBLEM_MEDIA_TYPE
import pt.ipc.http.utils.Uris
import pt.ipc.services.ClientsService
import pt.ipc.services.dtos.RegisterClientInput
import pt.ipc.services.dtos.CredentialsOutput
import java.time.LocalDate
import java.util.*
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping(produces = ["application/json", PROBLEM_MEDIA_TYPE])
class ClientsController(private val clientsService: ClientsService, private val sseEmitterUtils: SseEmitterUtils) {


    @PostMapping(Uris.CLIENT_REGISTER)
    fun registerClient(@RequestBody registerClientInput: RegisterClientInput, response: HttpServletResponse): ResponseEntity<CredentialsOutput> {
        val credentialsOutput: CredentialsOutput = clientsService.registerClient(registerClientInput)

        return ResponseEntity.status(HttpStatus.CREATED).body(credentialsOutput)
    }

    @Authentication
    @PostMapping(Uris.CLIENT_PHOTO)
    fun addProfilePicture(
        @PathVariable clientID: UUID,
        @RequestParam photo: MultipartFile,
        user: User
    ): ResponseEntity<String> {
        if (user.id != clientID) throw ForbiddenRequest

        clientsService.addProfilePicture(clientID = clientID, profilePicture = photo.bytes)

        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @Authentication
    @GetMapping(Uris.CLIENT_PROFILE)
    fun clientProfile(@PathVariable clientID: UUID, user : User) : ResponseEntity<ClientOutput>{

        if (user.id != clientID) throw ForbiddenRequest

        val client = clientsService.getClientProfile(clientID = clientID)

        return ResponseEntity.ok(client)
    }

    @Authentication
    @GetMapping(Uris.MONITORS)
    fun searchMonitorsAvailable(
        @RequestParam(required = false) name: String?,
        @RequestParam(required = false, defaultValue = DEFAULT_SKIP) skip: Int,
        @RequestParam(required = false, defaultValue = DEFAULT_LIMIT) limit: Int,
        user : User
    ): ResponseEntity<AllMonitorsAvailableOutput> {

        val monitors : List<MonitorAvailable> = clientsService.searchMonitorsAvailable(
            clientID = user.id,
            name = name,
            skip = skip,
            limit = limit
        )

        return ResponseEntity.status(HttpStatus.OK).body(AllMonitorsAvailableOutput(monitors))

    }


    @Authentication
    @PostMapping(Uris.CLIENT_REQUESTS)
    fun makeRequestForMonitor(@PathVariable clientID: UUID, @RequestBody connRequest: ConnectionRequest, user: User): ResponseEntity<RequestIdOutput> {
        if (user.id != clientID) throw ForbiddenRequest

        val requestID = clientsService.requestMonitor(monitorID = connRequest.monitorId, clientID = clientID, requestText = connRequest.text)

        return ResponseEntity.status(HttpStatus.CREATED).body(RequestIdOutput(requestID = requestID))
    }

    @Authentication
    @GetMapping(Uris.CLIENT_MONITOR)
    fun getMonitorOfClient(@PathVariable clientID: UUID, user: User): ResponseEntity<MonitorOutput> {
        if (user.id != clientID) throw ForbiddenRequest

        val monitorOutput = clientsService.getMonitorOfClient(clientID = clientID)

        return ResponseEntity.status(HttpStatus.OK).body(monitorOutput)
    }

    @Authentication
    @GetMapping(Uris.PLAN_CURRENT)
    fun getPlanOfClientContainingDate(
        @PathVariable clientID: UUID,
        @RequestParam(required = false)
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        date: LocalDate?
    ): ResponseEntity<PlanOutput> {
        val planOutput: PlanOutput = clientsService.getPlanOfClientContainingDate(clientID = clientID, date = date ?: LocalDate.now())

        return ResponseEntity.ok(planOutput)
    }

    @Authentication
    @GetMapping(Uris.EXERCISES_OF_CLIENT)
    fun getExercisesOfClient(
        @PathVariable clientID: UUID,
        @RequestParam(required = false)
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        date: LocalDate?,
        @RequestParam(required = false, defaultValue = DEFAULT_SKIP) skip: Int,
        @RequestParam(required = false, defaultValue = DEFAULT_LIMIT) limit: Int
    ): ResponseEntity<ListOfExercisesOfClient> {
        val exercises: List<Exercise> = clientsService.getExercisesOfClient(clientID = clientID, date = date, skip = skip, limit = limit)
        return ResponseEntity.ok(ListOfExercisesOfClient(exercises = exercises))
    }

    @Authentication
    @PostMapping(Uris.MONITOR_RATE)
    fun rateMonitor(@PathVariable monitorID: UUID, @RequestBody ratingInput: RatingInput, user: User): ResponseEntity<Unit> {
        if (ratingInput.user != user.id) throw ForbiddenRequest
        clientsService.rateMonitor(monitorID = monitorID, clientID = user.id, rating = ratingInput.rating)
        return ResponseEntity.ok().build()
    }

    @Authentication
    @PostMapping(Uris.VIDEO_OF_EXERCISE)
    fun postVideoOfExercise(
        @RequestParam video: MultipartFile,
        @RequestParam set : Int,
        @RequestParam(required = false) feedBack : String?,
        @PathVariable clientID: UUID,
        @PathVariable planID: Int,
        @PathVariable dailyListID: Int,
        @PathVariable exerciseID: Int,
        user: User
    ): ResponseEntity<Unit> {

        if(user.id != clientID) throw ForbiddenRequest

        clientsService.uploadVideoOfClient(
            video = video.bytes,
            clientID = clientID,
            planID = planID,
            dailyListID = dailyListID,
            exerciseID = exerciseID,
            set = set,
            feedback = feedBack
        )

        return ResponseEntity.ok().build()

    }

    companion object {
        const val DEFAULT_SKIP = "0"
        const val DEFAULT_LIMIT = "10"
    }
}
