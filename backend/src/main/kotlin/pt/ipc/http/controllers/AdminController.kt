package pt.ipc.http.controllers

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import pt.ipc.domain.ExerciseType
import pt.ipc.http.models.Decision
import pt.ipc.http.models.ListOfUnverifiedMonitors
import pt.ipc.http.pipeline.authentication.Authentication
import pt.ipc.http.utils.Uris
import pt.ipc.services.AdminService
import pt.ipc.services.dtos.RegisterInput
import pt.ipc.services.dtos.RegisterOutput
import java.util.UUID

@RestController
class AdminController(private val adminService: AdminService) {


    @Authentication
    @PostMapping(Uris.ADMIN_CREATION)
    fun createAdminAccount(@RequestBody registerInput: RegisterInput) : ResponseEntity<RegisterOutput>{

        val registerOutput = adminService.createAdminAccount(registerInput = registerInput)

        return ResponseEntity.status(HttpStatus.CREATED).body(registerOutput)

    }

    @Authentication
    @GetMapping(Uris.UNVERIFIED_MONITORS)
    fun getUnverifiedMonitors() : ResponseEntity<ListOfUnverifiedMonitors>{
        val monitors = adminService.getUnverifiedMonitors()

        return ResponseEntity.ok(ListOfUnverifiedMonitors(monitors = monitors))
    }


    @Authentication
    @GetMapping(Uris.UNVERIFIED_MONITOR)
    fun credentialOfMonitor(@PathVariable monitorID: UUID) : ResponseEntity<ByteArray> {
        val credential = adminService.getCredentialOfMonitor(monitorID = monitorID)

        val headers = HttpHeaders()
        headers.contentType = MediaType.parseMediaType("image/png")
        headers.contentLength = credential.size.toLong()

        return ResponseEntity.ok().headers(headers).body(credential)

    }

    @Authentication
    @PostMapping(Uris.UNVERIFIED_MONITOR)
    fun decideCredentialOfMonitor(@PathVariable monitorID : UUID, @RequestBody decision : Decision) : ResponseEntity<Unit>{

        adminService.decideMonitorCredential(monitorID = monitorID, accept = decision.accept)

        return ResponseEntity.ok().build()

    }

    @Authentication
    @PostMapping(Uris.ADD_VIDEO_PREVIEW)
    fun addVideoPreview(
        @RequestParam video: MultipartFile,
        @RequestParam title: String,
        @RequestParam description: String,
        @RequestParam type: ExerciseType,

    ) : ResponseEntity<Unit>{
        adminService.addExerciseInfoPreview(
            title = title,
            description = description,
            type = type,
            video = video.bytes
        )

        return ResponseEntity.status(HttpStatus.CREATED).build()
    }


}