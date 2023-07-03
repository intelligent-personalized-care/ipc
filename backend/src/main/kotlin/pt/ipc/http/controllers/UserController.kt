package pt.ipc.http.controllers

import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import pt.ipc.domain.User
import pt.ipc.http.models.LoginInput
import pt.ipc.http.pipeline.authentication.Authentication
import pt.ipc.http.pipeline.exceptionHandler.Problem
import pt.ipc.http.utils.SseEmitterUtils
import pt.ipc.http.utils.Uris
import pt.ipc.services.UserService
import pt.ipc.services.dtos.CredentialsOutput
import java.util.UUID

@RestController
@RequestMapping(produces = ["application/json", "image/png", Problem.PROBLEM_MEDIA_TYPE])
class UserController(private val userService: UserService, private val sseEmitterUtils: SseEmitterUtils) {

    @PostMapping(Uris.USERS_LOGIN)
    fun login(@RequestBody loginInput: LoginInput): ResponseEntity<CredentialsOutput> {
        val credentialsOutput = userService.login(email = loginInput.email, password = loginInput.password)
        return ResponseEntity.ok(credentialsOutput)
    }

    @Authentication
    @PostMapping(Uris.USERS_SUBSCRIBE)
    fun subscribeToServerSendEvents(user: User): SseEmitter {
        return sseEmitterUtils.createConnection(userID = user.id)
    }

    @Authentication
    @PostMapping(Uris.USERS_UNSUBSCRIBE)
    fun unsubscribeToServerSendEvents(user: User): ResponseEntity<Unit> {
        sseEmitterUtils.endConnection(userID = user.id)
        return ResponseEntity.ok().build()
    }

    @GetMapping(Uris.USER_PHOTO)
    fun getPhotoOfUser(@PathVariable userID: UUID): ResponseEntity<ByteArray> {
        val profilePicture = userService.getUserPhoto(userID = userID)

        val headers = HttpHeaders()
        headers.contentType = MediaType.parseMediaType("image/png")
        headers.contentLength = profilePicture.size.toLong()

        return ResponseEntity.ok().headers(headers).body(profilePicture)
    }
}
