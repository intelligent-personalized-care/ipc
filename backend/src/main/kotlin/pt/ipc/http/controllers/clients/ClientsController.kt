package pt.ipc.http.controllers.clients

import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import pt.ipc.domain.User
import pt.ipc.domain.client.ClientOutput
import pt.ipc.domain.exceptions.ForbiddenRequest
import pt.ipc.domain.exercises.Exercise
import pt.ipc.domain.monitor.RatingInput
import pt.ipc.http.controllers.clients.models.AllMonitorsAvailableOutput
import pt.ipc.http.controllers.clients.models.ConnectionRequest
import pt.ipc.http.controllers.clients.models.ListOfExercisesOfClient
import pt.ipc.http.controllers.clients.models.RegisterClientInput
import pt.ipc.http.controllers.clients.models.RequestIdOutput
import pt.ipc.http.models.emitter.PostedVideo
import pt.ipc.http.models.emitter.RateMonitor
import pt.ipc.http.pipeline.authentication.Authentication
import pt.ipc.http.pipeline.exceptionHandler.Problem.Companion.PROBLEM_MEDIA_TYPE
import pt.ipc.http.utils.SseEmitterRepository
import pt.ipc.http.utils.Uris
import pt.ipc.services.clientService.ClientsService
import pt.ipc.services.dtos.CredentialsOutput
import pt.ipc.services.dtos.MonitorOutput
import java.time.LocalDate
import java.util.UUID

@RestController
@RequestMapping(produces = ["application/json", PROBLEM_MEDIA_TYPE])
class ClientsController(private val clientsService: ClientsService, private val sseEmitterRepository: SseEmitterRepository) {

    @PostMapping(Uris.CLIENT_REGISTER)
    fun registerClient(@RequestBody registerClientInput: RegisterClientInput): ResponseEntity<CredentialsOutput> {
        val credentialsOutput: CredentialsOutput = clientsService.registerClient(registerClientInput)

        return ResponseEntity.status(HttpStatus.CREATED).body(credentialsOutput)
    }

    @Authentication
    @PostMapping(Uris.CLIENT_PHOTO)
    fun addProfilePicture(
        @PathVariable clientID: UUID,
        @RequestParam photo: MultipartFile,
        user: User
    ): ResponseEntity<Unit> {
        if (user.id != clientID) throw ForbiddenRequest

        clientsService.addProfilePicture(clientID = clientID, profilePicture = photo.bytes)

        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @Authentication
    @GetMapping(Uris.CLIENT_PROFILE)
    fun clientProfile(@PathVariable clientID: UUID, user: User): ResponseEntity<ClientOutput> {
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
        user: User
    ): ResponseEntity<AllMonitorsAvailableOutput> {
        val monitors = clientsService.searchMonitorsAvailable(
            clientID = user.id,
            name = name,
            skip = skip,
            limit = limit
        )

        return ResponseEntity.status(HttpStatus.OK).body(AllMonitorsAvailableOutput(monitors))
    }

    @Authentication
    @PostMapping(Uris.MONITOR_BY_ID)
    fun makeRequestForMonitor(@PathVariable monitorID: UUID, @RequestBody connRequest: ConnectionRequest, user: User): ResponseEntity<RequestIdOutput> {
        if (user.id != connRequest.clientID) throw ForbiddenRequest

        val requestMonitor = clientsService.requestMonitor(monitorID = monitorID, clientID = connRequest.clientID, requestText = connRequest.text)

        sseEmitterRepository.send(userID = monitorID, obj = requestMonitor)

        return ResponseEntity.status(HttpStatus.CREATED).body(RequestIdOutput(requestID = requestMonitor.requestID))
    }

    @Authentication
    @GetMapping(Uris.CLIENT_MONITOR)
    fun getMonitorOfClient(@PathVariable clientID: UUID, user: User): ResponseEntity<MonitorOutput> {
        if (user.id != clientID) throw ForbiddenRequest

        val monitorOutput = clientsService.getMonitorOfClient(clientID = clientID)

        return ResponseEntity.status(HttpStatus.OK).body(monitorOutput)
    }

    @Authentication
    @DeleteMapping(Uris.CLIENT_MONITOR)
    fun endMonitorConnection(@PathVariable clientID: UUID): ResponseEntity<Unit> {
        clientsService.deleteConnection(clientID = clientID)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
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

        sseEmitterRepository.send(userID = monitorID, obj = RateMonitor(rate = ratingInput.rating))

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @Authentication
    @PostMapping(Uris.VIDEO_OF_EXERCISE)
    fun postVideoOfExercise(
        @RequestParam video: MultipartFile,
        @RequestParam set: Int,
        @RequestParam(required = false) feedBack: String?,
        @PathVariable clientID: UUID,
        @PathVariable planID: Int,
        @PathVariable dailyListID: Int,
        @PathVariable exerciseID: Int,
        user: User
    ): ResponseEntity<Unit> {
        if (user.id != clientID) throw ForbiddenRequest

        val (monitorID, postedVideo) = clientsService.uploadVideoOfClient(
            video = video.bytes,
            clientID = clientID,
            planID = planID,
            dailyListID = dailyListID,
            exerciseID = exerciseID,
            set = set,
            feedback = feedBack
        )

        postedVideo?.let {
            sseEmitterRepository.send(userID = monitorID, obj = PostedVideo(clientID = it.clientID, name = it.name, exerciseID = it.exerciseID))
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    companion object {
        const val DEFAULT_SKIP = "0"
        const val DEFAULT_LIMIT = "10"
    }
}
