package pt.ipc.http.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pt.ipc.http.utils.Uris
import pt.ipc.services.users.UsersService
import pt.ipc.services.users.dtos.RegisterInput
import pt.ipc.services.users.dtos.RegisterOutput

@RestController
@RequestMapping(produces = ["application/json"])
class UsersController(private val usersService: UsersService) {

    @GetMapping(Uris.USER_HOME)
    fun getUserHome(): ResponseEntity<String> = ResponseEntity.accepted().body("Hello User")

    @PostMapping(Uris.USERS_REGISTER)
    fun registerUser(@RequestBody registerInput: RegisterInput): ResponseEntity<RegisterOutput>{

        val registerOutput : RegisterOutput = usersService.register(registerInput)

        return ResponseEntity.status(HttpStatus.CREATED).body(registerOutput)

    }
}