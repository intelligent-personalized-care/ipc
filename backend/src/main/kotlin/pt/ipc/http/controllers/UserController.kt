package pt.ipc.http.controllers

import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pt.ipc.http.models.LoginInput
import pt.ipc.http.pipeline.authentication.Authentication
import pt.ipc.http.pipeline.exceptionHandler.Problem
import pt.ipc.http.utils.Uris
import pt.ipc.services.UserService
import pt.ipc.services.dtos.CredentialsOutput
import java.util.UUID


@RestController
@RequestMapping(produces = ["application/json", "image/png", Problem.PROBLEM_MEDIA_TYPE])
class UserController(private val userService : UserService) {

    @PostMapping(Uris.USERS_LOGIN)
    fun login(@RequestBody loginInput: LoginInput): ResponseEntity<CredentialsOutput> {
        val credentialsOutput = userService.login(email = loginInput.email, password = loginInput.password)
        return ResponseEntity.ok(credentialsOutput)
    }

    @Authentication
    @GetMapping(Uris.USER_PHOTO)
    fun getPhotoOfUser(@PathVariable userID: UUID) : ResponseEntity<ByteArray>{

        val profilePicture = userService.getUserPhoto(userID = userID)

        val headers = HttpHeaders()
        headers.contentType = MediaType.parseMediaType("image/png")
        headers.contentLength = profilePicture.size.toLong()

        return ResponseEntity.ok().headers(headers).body(profilePicture)

    }

}