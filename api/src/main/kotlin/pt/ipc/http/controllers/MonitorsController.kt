package pt.ipc.http.controllers


import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import pt.ipc.http.pipeline.exceptionHandler.Problem.Companion.PROBLEM_MEDIA_TYPE
import pt.ipc.http.utils.Uris
import pt.ipc.services.users.MonitorService
import pt.ipc.services.users.dtos.RegisterMonitorInput
import pt.ipc.services.users.dtos.RegisterOutput

@RestController
@RequestMapping(produces = ["application/json", PROBLEM_MEDIA_TYPE])
class MonitorsController(private val monitorService: MonitorService) {

    @PostMapping(Uris.REGISTER_MONITOR, consumes = ["multipart/form-data"])
    fun registerMonitor(
        @RequestParam("credential") credential: MultipartFile,
        @RequestParam("email") email: String,
        @RequestParam("name") name: String,
        @RequestParam("password") password: String
    ): ResponseEntity<RegisterOutput> {

        val registerMonitorInput = RegisterMonitorInput(
            email = email,
            name = name,
            password = password,
            credential = credential.bytes
        )

        val registerOutput = monitorService.registerMonitor(registerMonitorInput)
        
        return ResponseEntity.status(HttpStatus.CREATED).body(registerOutput)

    }

}