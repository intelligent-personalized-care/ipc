package pt.ipc.http.controllers

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
import pt.ipc.domain.MonitorDetails
import pt.ipc.domain.Plan
import pt.ipc.domain.PlanID
import pt.ipc.domain.PlanOutput
import pt.ipc.domain.User
import pt.ipc.domain.exceptions.Unauthorized
import pt.ipc.http.controllers.ClientsController.Companion.addAuthenticationCookies
import pt.ipc.http.models.AllMonitorsAvailableOutput
import pt.ipc.http.models.ConnectionRequestDecisionInput
import pt.ipc.http.models.RequestInformation
import pt.ipc.http.pipeline.authentication.Authentication
import pt.ipc.http.pipeline.exceptionHandler.Problem.Companion.PROBLEM_MEDIA_TYPE
import pt.ipc.http.utils.Uris
import pt.ipc.services.MonitorService
import pt.ipc.services.dtos.RegisterMonitorInput
import pt.ipc.services.dtos.RegisterOutput
import java.util.*
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping(produces = ["application/json", PROBLEM_MEDIA_TYPE])
class MonitorsController(private val monitorService: MonitorService) {

    @PostMapping(Uris.MONITOR_REGISTER, consumes = ["multipart/form-data"])
    fun registerMonitor(
        @RequestParam("credential") credential: MultipartFile,
        @RequestParam email: String,
        @RequestParam name: String,
        @RequestParam password: String,
        response: HttpServletResponse
    ): ResponseEntity<RegisterOutput> {
        val registerMonitorInput = RegisterMonitorInput(
            email = email,
            name = name,
            password = password,
            credential = credential.bytes
        )

        val registerOutput = monitorService.registerMonitor(registerMonitorInput)

        addAuthenticationCookies(response = response, token = registerOutput.token)

        return ResponseEntity.status(HttpStatus.CREATED).body(registerOutput)
    }

    @Authentication
    @GetMapping(Uris.MONITOR)
    fun getMonitor(@PathVariable monitorID: UUID): ResponseEntity<MonitorDetails> {
        val res = monitorService.getMonitor(monitorID)

        return ResponseEntity.status(HttpStatus.OK).body(res)
    }

    @GetMapping(Uris.MONITOR_SEARCH_ALL_AVAILABLE)
    fun searchMonitorsAvailable(
        @RequestParam(required = false) name: String?,
        @RequestParam(required = false) skip: Int?,
        @RequestParam(required = false) limit: Int?
    ): ResponseEntity<AllMonitorsAvailableOutput> {
        val res = monitorService.searchMonitorsAvailable(name, skip ?: DEFAULT_SKIP, limit ?: DEFAULT_LIMIT)

        return ResponseEntity.status(HttpStatus.OK).body(AllMonitorsAvailableOutput(res))
    }

    @Authentication
    @PostMapping(Uris.MONITOR_DECIDE_REQUEST)
    fun decideRequest(
        @PathVariable monitorID: UUID,
        @PathVariable requestID: UUID,
        @RequestBody decision: ConnectionRequestDecisionInput,
        user: User
    ): ResponseEntity<String> {
        if (user.id != monitorID) throw Unauthorized

        monitorService.decideRequest(
            requestID = requestID,
            monitorID = monitorID,
            accept = decision.accept
        )

        return ResponseEntity.status(HttpStatus.OK).build()
    }

    @Authentication
    @PostMapping(Uris.MONITOR_PHOTO)
    fun addProfilePicture(@PathVariable monitorID: UUID, @RequestParam photo: MultipartFile, user: User): ResponseEntity<String> {
        if (user.id != monitorID) throw Unauthorized

        monitorService.updateProfilePicture(monitorID = monitorID, photo = photo.bytes)

        return ResponseEntity.status(HttpStatus.CREATED).body("Profile Picture Updated")
    }

    @Authentication
    @GetMapping(Uris.MONITOR_REQUESTS)
    fun getMonitorRequests(@PathVariable monitorID: UUID, user: User): ResponseEntity<List<RequestInformation>> {
        if (monitorID != user.id) throw Unauthorized

        val requests: List<RequestInformation> = monitorService.monitorRequests(monitorID = monitorID)

        return ResponseEntity.ok(requests)
    }

    @Authentication
    @PostMapping(Uris.PLANS)
    fun createPlanForClient(@PathVariable clientID: UUID, @PathVariable monitorID: UUID, user: User, @RequestBody plan: Plan): ResponseEntity<PlanID> {
        if (user.id != monitorID) throw Unauthorized

        val planID = monitorService.createPlan(monitorID = monitorID, clientID = clientID, plan = plan)

        return ResponseEntity.status(HttpStatus.CREATED).body(PlanID(id = planID))
    }

    @Authentication
    @GetMapping(Uris.PLAN_BY_ID)
    fun getPlanOfMonitorByID(@PathVariable monitorID: UUID, @PathVariable planID: Int, user: User): ResponseEntity<PlanOutput> {
        if (user.id != monitorID) throw Unauthorized
        val planOutput: PlanOutput = monitorService.getPlan(monitorID = monitorID, planID = planID)
        return ResponseEntity.ok(planOutput)
    }

    companion object {
        private const val DEFAULT_SKIP = 0
        private const val DEFAULT_LIMIT = 10
    }
}
