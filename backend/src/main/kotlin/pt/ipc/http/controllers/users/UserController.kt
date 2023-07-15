package pt.ipc.http.controllers.users

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
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
import pt.ipc.domain.jwt.PairOfTokens
import pt.ipc.http.controllers.users.models.LoginInput
import pt.ipc.http.controllers.users.models.RefreshTokenInput
import pt.ipc.http.pipeline.authentication.Authentication
import pt.ipc.http.pipeline.exceptionHandler.Problem
import pt.ipc.http.utils.SseEmitterRepository
import pt.ipc.http.utils.Uris
import pt.ipc.services.dtos.LoginOutput
import pt.ipc.services.userService.UserService
import java.util.UUID

@RestController
@RequestMapping(produces = ["application/json", "image/png", Problem.PROBLEM_MEDIA_TYPE])
class UserController(private val userService: UserService, private val sseEmitterRepository: SseEmitterRepository) {

    @PostMapping(Uris.USERS_LOGIN)
    fun login(@RequestBody loginInput: LoginInput): ResponseEntity<LoginOutput> {
        val loginOutput = userService.login(email = loginInput.email, password = loginInput.password)
        return ResponseEntity.ok(loginOutput)
    }

    @PostMapping(Uris.REFRESH_TOKEN)
    fun refreshToken(@RequestBody refreshToken: RefreshTokenInput): ResponseEntity<PairOfTokens> {
        val pairOfTokens = userService.refreshToken(refreshToken = refreshToken.refreshToken)

        return ResponseEntity.status(HttpStatus.CREATED).body(pairOfTokens)
    }

    @Authentication
    @GetMapping(Uris.USERS_SUBSCRIBE)
    fun subscribeToServerSendEvents(user: User): SseEmitter {
        return sseEmitterRepository.createConnection(userID = user.id)
    }

    @Authentication
    @PostMapping(Uris.USERS_UNSUBSCRIBE)
    fun unsubscribeToServerSendEvents(user: User): ResponseEntity<Unit> {
        sseEmitterRepository.endConnection(userID = user.id)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @Authentication
    @GetMapping(Uris.USER_PHOTO)
    fun getPhotoOfUser(@PathVariable userID: UUID): ResponseEntity<ByteArray> {
        val profilePicture = userService.getUserPhoto(userID = userID)

        val headers = HttpHeaders()
        headers.contentType = MediaType.parseMediaType("image/png")
        headers.contentLength = profilePicture.size.toLong()

        return ResponseEntity.ok().headers(headers).body(profilePicture)
    }
}
