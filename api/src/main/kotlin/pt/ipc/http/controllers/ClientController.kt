package pt.ipc.http.controllers

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import pt.ipc.domain.Role
import pt.ipc.domain.Unauthorized
import pt.ipc.domain.User
import pt.ipc.domain.jwt.JWToken
import pt.ipc.http.pipeline.authentication.Authentication
import pt.ipc.http.pipeline.exceptionHandler.Problem.Companion.PROBLEM_MEDIA_TYPE
import pt.ipc.http.utils.Uris
import pt.ipc.services.users.ClientsService
import pt.ipc.services.users.dtos.RegisterClientInput
import pt.ipc.services.users.dtos.RegisterOutput
import java.util.*
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping(produces = ["application/json", PROBLEM_MEDIA_TYPE])
class ClientController(private val clientsService: ClientsService) {

    @GetMapping(Uris.USER_HOME)
    fun getUserHome(): ResponseEntity<String> = ResponseEntity.accepted().body("Hello User")

    @PostMapping(Uris.REGISTER_CLIENT)
    fun registerClient(@RequestBody registerClientInput: RegisterClientInput,response : HttpServletResponse): ResponseEntity<RegisterOutput>{

        val registerOutput : RegisterOutput = clientsService.registerClient(registerClientInput)

        addAuthenticationCookies(response,registerOutput.token)

        return ResponseEntity.status(HttpStatus.CREATED).body(registerOutput)

    }

    @Authentication(Role.CLIENT)
    @PostMapping(Uris.CLIENT_PHOTO)
    fun addProfilePicture(
        user : User,
        @PathVariable client_id : UUID,
        @RequestParam profilePicture : MultipartFile
    ) : ResponseEntity<String>{

        if(user.id != client_id) throw Unauthorized()

        clientsService.addProfilePicture(clientID = client_id, profilePicture = profilePicture.bytes)

        return ResponseEntity.status(HttpStatus.CREATED).body("Profile Picture Created")


    }

    companion object{

        private fun addAuthenticationCookies(response: HttpServletResponse,token: String){
            val responseCookie = ResponseCookie.from("token", token)
                .httpOnly(true)
                .path("/")
                .sameSite("Strict")
                .build()

            response.addCookie(responseCookie)

        }

        private fun clearAuthenticationCookies(response: HttpServletResponse){
            val tokenCookie = ResponseCookie.from("token", "")
                .httpOnly(true)
                .maxAge(0)
                .sameSite("Strict")
                .build()
        }

        private fun HttpServletResponse.addCookie(cookie: ResponseCookie) {
            this.addHeader(HttpHeaders.SET_COOKIE, cookie.toString())
        }
    }


}