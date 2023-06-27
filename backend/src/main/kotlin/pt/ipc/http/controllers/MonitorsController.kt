package pt.ipc.http.controllers

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import pt.ipc.domain.MonitorDetails
import pt.ipc.domain.PlanID
import pt.ipc.domain.PlanInput
import pt.ipc.domain.PlanOutput
import pt.ipc.domain.User
import pt.ipc.domain.exceptions.ForbiddenRequest
import pt.ipc.http.models.AllMonitorsAvailableOutput
import pt.ipc.http.models.Decision
import pt.ipc.http.models.FeedbackInput
import pt.ipc.http.models.ListOfClients
import pt.ipc.http.models.ListOfPlans
import pt.ipc.http.models.PlanToClient
import pt.ipc.http.models.RequestsOfMonitor
import pt.ipc.http.pipeline.authentication.Authentication
import pt.ipc.http.pipeline.exceptionHandler.Problem.Companion.PROBLEM_MEDIA_TYPE
import pt.ipc.http.utils.Uris
import pt.ipc.services.MonitorService
import pt.ipc.services.dtos.RegisterInput
import pt.ipc.services.dtos.RegisterOutput
import java.util.*

@RestController
@RequestMapping(produces = ["application/json", "image/png", PROBLEM_MEDIA_TYPE])
class MonitorsController(private val monitorService: MonitorService) {

    @PostMapping(Uris.MONITOR_REGISTER)
    fun registerMonitor(@RequestBody registerInput: RegisterInput): ResponseEntity<RegisterOutput> {
        val registerOutput = monitorService.registerMonitor(registerInput = registerInput)

        return ResponseEntity.status(HttpStatus.CREATED).body(registerOutput)
    }

    @Authentication
    @PostMapping(Uris.MONITOR_CREDENTIAL)
    fun postCredentialOfMonitor(
        @PathVariable monitorID: UUID,
        @RequestBody credential: MultipartFile,
        user: User
    ): ResponseEntity<Unit> {
        if (monitorID != user.id) throw ForbiddenRequest

        monitorService.insertCredential(monitorID = monitorID, credential = credential.bytes)

        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @GetMapping(Uris.MONITOR_SEARCH_ALL_AVAILABLE)
    fun searchMonitorsAvailable(
        @RequestParam(required = false) name: String?,
        @RequestParam(required = false, defaultValue = DEFAULT_SKIP) skip: Int,
        @RequestParam(required = false, defaultValue = DEFAULT_LIMIT) limit: Int
    ): ResponseEntity<AllMonitorsAvailableOutput> {
        val res: List<MonitorDetails> = monitorService.searchMonitorsAvailable(name = name, skip = skip, limit = limit)

        return ResponseEntity.status(HttpStatus.OK).body(AllMonitorsAvailableOutput(res))
    }

    @Authentication
    @GetMapping(Uris.CLIENTS_OF_MONITOR)
    fun getClientsOfMonitor(@PathVariable monitorID: UUID, user: User): ResponseEntity<ListOfClients> {
        if (user.id != monitorID) throw ForbiddenRequest

        val clients = monitorService.getClientsOfMonitor(monitorID = user.id)

        return ResponseEntity.ok(ListOfClients(clients = clients))
    }

    @Authentication
    @GetMapping(Uris.MONITOR)
    fun getMonitor(@PathVariable monitorID: UUID): ResponseEntity<MonitorDetails> {
        val monitorDetails = monitorService.getMonitor(monitorID = monitorID)

        return ResponseEntity.status(HttpStatus.OK).body(monitorDetails)
    }

    @Authentication
    @PostMapping(Uris.MONITOR_DECIDE_REQUEST)
    fun decideRequest(
        @PathVariable monitorID: UUID,
        @PathVariable requestID: UUID,
        @RequestBody decision: Decision,
        user: User
    ): ResponseEntity<String> {
        if (user.id != monitorID) throw ForbiddenRequest

        monitorService.decideRequest(
            requestID = requestID,
            monitorID = monitorID,
            accept = decision.accept
        )

        return ResponseEntity.ok().build()
    }

    @Authentication
    @PostMapping(Uris.MONITOR_PHOTO)
    fun addProfilePicture(@PathVariable monitorID: UUID, @RequestParam photo: MultipartFile, user: User): ResponseEntity<String> {
        if (user.id != monitorID) throw ForbiddenRequest

        monitorService.updateProfilePicture(monitorID = monitorID, photo = photo.bytes)

        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @Authentication
    @GetMapping(Uris.MONITOR_PHOTO)
    fun getProfilePicture(@PathVariable monitorID: UUID, user: User): ResponseEntity<ByteArray> {
        if(monitorID != user.id) throw ForbiddenRequest

        val profilePicture = monitorService.getProfilePicture(monitorID = monitorID)

        val headers = HttpHeaders()
        headers.contentType = MediaType.parseMediaType("image/png")
        headers.contentLength = profilePicture.size.toLong()

        return ResponseEntity.ok().headers(headers).body(profilePicture)
    }

    @Authentication
    @GetMapping(Uris.MONITOR_REQUESTS)
    fun getMonitorRequests(@PathVariable monitorID: UUID, user: User): ResponseEntity<RequestsOfMonitor> {
        if (monitorID != user.id) throw ForbiddenRequest

        val requests = monitorService.monitorRequests(monitorID = monitorID)

        return ResponseEntity.ok(RequestsOfMonitor(requests))
    }

    @Authentication
    @PostMapping(Uris.PLANS_OF_MONITOR)
    fun createPlanOfMonitor(@PathVariable monitorID: UUID, @RequestBody planInput: PlanInput, user: User): ResponseEntity<PlanID> {
        if (user.id != monitorID) throw ForbiddenRequest

        val planID = monitorService.createPlan(monitorID = monitorID, planInput = planInput)

        return ResponseEntity.status(HttpStatus.CREATED).body(PlanID(planID))
    }

    @Authentication
    @GetMapping(Uris.PLANS_OF_MONITOR)
    fun getPlansOfMonitor(@PathVariable monitorID: UUID, user: User): ResponseEntity<ListOfPlans> {
        if (monitorID != user.id) throw ForbiddenRequest

        val plans = monitorService.getPlans(monitorID = monitorID)

        return ResponseEntity.ok(ListOfPlans(plans = plans))
    }

    @Authentication
    @PostMapping(Uris.PLANS_OF_CLIENT)
    fun associatePlanForClient(
        @PathVariable clientID: UUID,
        @PathVariable monitorID: UUID,
        @RequestBody planInfo: PlanToClient,
        user: User
    ): ResponseEntity<Unit> {
        if (user.id != monitorID) throw ForbiddenRequest

        monitorService.associatePlanToClient(monitorID = monitorID, clientID = clientID, startDate = planInfo.startDate, planID = planInfo.planID)

        return ResponseEntity.ok().build()
    }

    @Authentication
    @GetMapping(Uris.PLAN_BY_ID)
    fun getPlanOfMonitorByID(@PathVariable monitorID: UUID, @PathVariable planID: Int, user: User): ResponseEntity<PlanOutput> {
        if (user.id != monitorID) throw ForbiddenRequest
        val planOutput: PlanOutput = monitorService.getPlan(monitorID = monitorID, planID = planID)
        return ResponseEntity.ok(planOutput)
    }

    @Authentication
    @PostMapping(Uris.EXERCISE_FEEDBACK)
    fun postFeedback(
        @RequestBody feedbackInput: FeedbackInput,
        @PathVariable clientID: UUID,
        @PathVariable planID: Int,
        @PathVariable dailyListID: Int,
        @PathVariable exerciseID: Int,
        user: User
    ): ResponseEntity<Unit> {
        monitorService.giveFeedbackOfExercise(
            monitorID = user.id,
            planID = planID,
            dailyListID = dailyListID,
            dailyExerciseID = exerciseID,
            set = feedbackInput.set ,
            feedback = feedbackInput.feedback,
            clientID = clientID
        )
        return ResponseEntity.ok().build()
    }

    companion object {
        private const val DEFAULT_SKIP = "0"
        private const val DEFAULT_LIMIT = "10"
    }
}