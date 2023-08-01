package pt.ipc.http.controllers.admin

import org.springframework.cache.annotation.CacheEvict
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import pt.ipc.domain.exercises.ExerciseType
import pt.ipc.http.controllers.admin.models.ListOfUnverifiedMonitors
import pt.ipc.http.models.Decision
import pt.ipc.http.models.emitter.CredentialAcceptance
import pt.ipc.http.pipeline.authentication.Authentication
import pt.ipc.http.utils.SseEmitterRepository
import pt.ipc.http.utils.Uris
import pt.ipc.services.adminService.AdminService
import pt.ipc.services.dtos.CredentialsOutput
import pt.ipc.services.dtos.RegisterInput
import java.util.UUID

@RestController
class AdminController(private val adminService: AdminService, private val sseEmitterRepository: SseEmitterRepository) {

    @Authentication
    @PostMapping(Uris.ADMIN_CREATION)
    fun createAdminAccount(@RequestBody registerInput: RegisterInput): ResponseEntity<CredentialsOutput> {
        val registerOutput = adminService.createAdminAccount(registerInput = registerInput)

        return ResponseEntity.status(HttpStatus.CREATED).body(registerOutput)
    }

    @Authentication
    @GetMapping(Uris.UNVERIFIED_MONITORS)
    fun getUnverifiedMonitors(): ResponseEntity<ListOfUnverifiedMonitors> {
        val monitors = adminService.getUnverifiedMonitors()

        return ResponseEntity.ok(ListOfUnverifiedMonitors(monitors = monitors))
    }

    @Authentication
    @GetMapping(Uris.UNVERIFIED_MONITOR)
    fun credentialOfMonitor(@PathVariable monitorID: UUID): ResponseEntity<ByteArray> {
        val credential = adminService.getCredentialOfMonitor(monitorID = monitorID)

        val headers = HttpHeaders()
        headers.contentType = MediaType.parseMediaType("application/pdf")
        headers.contentLength = credential.size.toLong()

        return ResponseEntity.ok().headers(headers).body(credential)
    }

    @Authentication
    @PostMapping(Uris.UNVERIFIED_MONITOR)
    fun decideCredentialOfMonitor(@PathVariable monitorID: UUID, @RequestBody decision: Decision): ResponseEntity<Unit> {
        adminService.decideMonitorCredential(monitorID = monitorID, accept = decision.accept)

        sseEmitterRepository.send(userID = monitorID, CredentialAcceptance(acceptance = decision.accept))

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @Authentication
    @PostMapping(Uris.ADD_VIDEO_PREVIEW)
    @CacheEvict("cacheExercises")
    fun addVideoPreview(
        @RequestParam video: MultipartFile,
        @RequestParam title: String,
        @RequestParam description: String,
        @RequestParam type: ExerciseType

    ): ResponseEntity<Unit> {
        adminService.addExerciseInfoPreview(
            title = title,
            description = description,
            type = type,
            video = video.bytes
        )

        return ResponseEntity.status(HttpStatus.CREATED).build()
    }
}
