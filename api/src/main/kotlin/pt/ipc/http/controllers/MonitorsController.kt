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
import pt.ipc.domain.PLanID
import pt.ipc.domain.Plan
import pt.ipc.domain.PlanOutput
import pt.ipc.domain.Unauthorized
import pt.ipc.domain.User
import pt.ipc.http.controllers.ClientsController.Companion.addAuthenticationCookies
import pt.ipc.http.models.AllMonitorsAvailableOutput
import pt.ipc.http.models.RequestIdOutput
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

    @GetMapping(Uris.MONITOR_GET)
    fun getMonitor(@PathVariable monitorId: UUID): ResponseEntity<MonitorDetails> {
        val res = monitorService.getMonitor(monitorId)

        return ResponseEntity.status(HttpStatus.OK).body(res)
    }

    @GetMapping(Uris.MONITOR_SEARCH_ALL_AVAILABLE)
    fun searchMonitorsAvailable(
        @RequestParam(required = false) name: String?,
        @RequestParam(required = false) skip: Int?,
        @RequestParam(required = false) limit: Int?,
        ): ResponseEntity<AllMonitorsAvailableOutput> {

        val res = monitorService.searchMonitorsAvailable(name, skip ?: DEFAULT_SKIP, limit ?: DEFAULT_LIMIT)

        return ResponseEntity.status(HttpStatus.OK).body(AllMonitorsAvailableOutput(res))
    }

    @Authentication
    @PostMapping(Uris.MONITOR_PHOTO)
    fun addProfilePicture(@PathVariable monitorId: UUID, @RequestParam photo: MultipartFile, user: User): ResponseEntity<String> {
        if (user.id != monitorId) throw Unauthorized

        monitorService.updateProfilePicture(monitorID = monitorId, photo.bytes)

        return ResponseEntity.status(HttpStatus.CREATED).body("Profile Picture Updated")
    }

    @Authentication
    @PostMapping(Uris.MONITOR_REQUESTS)
    fun makeRequestForClient(@PathVariable monitorId: UUID, @RequestBody clientId: UUID, user: User): ResponseEntity<RequestIdOutput> {
        if (monitorId != user.id) throw Unauthorized

        val requestID = monitorService.requestClient(monitorID = user.id, clientID = clientId)

        return ResponseEntity.status(HttpStatus.CREATED).body(RequestIdOutput(requestID = requestID))
    }

    @Authentication
    @GetMapping(Uris.MONITOR_REQUESTS)
    fun getMonitorRequests(@PathVariable monitorId: UUID, user: User): ResponseEntity<List<RequestInformation>> {
        if (monitorId != user.id) throw Unauthorized

        val requests: List<RequestInformation> = monitorService.monitorRequests(monitorID = monitorId)

        return ResponseEntity.ok(requests)
    }

    @Authentication
    @PostMapping(Uris.PLANS)
    fun createPlanForClient(@PathVariable clientId: UUID, @PathVariable monitorId: UUID, user: User, @RequestBody plan: Plan): ResponseEntity<PLanID> {
        if (user.id != monitorId) throw Unauthorized

        val planID = monitorService.createPlan(monitorID = monitorId, clientID = clientId, plan = plan)

        return ResponseEntity.status(HttpStatus.CREATED).body(PLanID(id = planID))
    }

    @Authentication
    @GetMapping(Uris.PLAN_BY_ID)
    fun getPlanOfMonitorByID(@PathVariable monitorId: UUID, @PathVariable planId: Int, user: User): ResponseEntity<PlanOutput> {
        if (user.id != monitorId) throw Unauthorized
        val planOutput: PlanOutput = monitorService.getPlan(monitorID = monitorId, planID = planId)
        return ResponseEntity.ok(planOutput)
    }

    companion object {
        private const val DEFAULT_SKIP = 0
        private const val DEFAULT_LIMIT = 10
    }
}
