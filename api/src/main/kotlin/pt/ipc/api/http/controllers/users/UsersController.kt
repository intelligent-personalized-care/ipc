package pt.ipc.api.http.controllers.users

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import pt.ipc.api.http.utils.Uris
import pt.ipc.api.service.users.UsersService

@RestController
class UsersController(private val usersService: UsersService) {

    @GetMapping(Uris.USER_HOME)
    fun getUserHome(): ResponseEntity<*> = ResponseEntity.accepted().body("Hello User")
}