package pt.ipc.http.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import pt.ipc.domain.RequestInformation
import pt.ipc.domain.Unauthorized
import pt.ipc.domain.User
import pt.ipc.http.controllers.ClientController.Companion.addAuthenticationCookies
import pt.ipc.http.pipeline.authentication.Authentication
import pt.ipc.http.pipeline.exceptionHandler.Problem.Companion.PROBLEM_MEDIA_TYPE
import pt.ipc.http.utils.Uris
import pt.ipc.services.users.MonitorService
import pt.ipc.services.users.dtos.RegisterMonitorInput
import pt.ipc.services.users.dtos.RegisterOutput
import java.util.*
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping(produces = ["application/json", PROBLEM_MEDIA_TYPE])
class MonitorsController(private val monitorService: MonitorService) {

    @PostMapping(Uris.REGISTER_MONITOR, consumes = ["multipart/form-data"])
    fun registerMonitor(
        @RequestParam credential: MultipartFile,
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
    @PostMapping(Uris.MONITOR_PHOTO)
    fun addProfilePicture(@PathVariable monitor_id: UUID, @RequestParam photo: MultipartFile, user: User): ResponseEntity<String> {
        if (user.id != monitor_id) throw Unauthorized()

        monitorService.updateProfilePicture(monitorID = monitor_id, photo.bytes)

        return ResponseEntity.status(HttpStatus.CREATED).body("Profile Picture Updated")
    }

    @Authentication
    @PostMapping(Uris.REQUEST_CLIENT)
    fun makeRequestforClient(@PathVariable client_id: UUID, user: User): ResponseEntity<UUID> {
        val requestID = monitorService.requestClient(monitorID = user.id, clientID = client_id)

        return ResponseEntity.status(HttpStatus.CREATED).body(requestID)
    }

    @Authentication
    @GetMapping(Uris.MONITOR_REQUESTS)
    fun getMonitorRequests(@PathVariable monitor_id: UUID, user: User): ResponseEntity<List<RequestInformation>> {
        if (monitor_id != user.id) throw Unauthorized()

        val requests: List<RequestInformation> = monitorService.monitorRequests(monitorID = monitor_id)

        return ResponseEntity.ok(requests)
    }
}
