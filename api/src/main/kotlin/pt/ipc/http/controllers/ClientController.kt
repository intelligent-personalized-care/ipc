package pt.ipc.http.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pt.ipc.domain.User
import pt.ipc.http.pipeline.authentication.Authentication
import pt.ipc.http.pipeline.exceptionHandler.Problem.Companion.PROBLEM_MEDIA_TYPE
import pt.ipc.http.utils.Uris
import pt.ipc.services.users.ClientsService
import pt.ipc.services.users.dtos.RegisterClientInput
import pt.ipc.services.users.dtos.RegisterOutput
import java.util.*

@RestController
@RequestMapping(produces = ["application/json", PROBLEM_MEDIA_TYPE])
class ClientController(private val clientsService: ClientsService) {

    @GetMapping(Uris.USER_HOME)
    fun getUserHome(): ResponseEntity<String> = ResponseEntity.accepted().body("Hello User")

    @PostMapping(Uris.REGISTER_CLIENT)
    fun registerClient(@RequestBody registerClientInput: RegisterClientInput): ResponseEntity<RegisterOutput>{

        val registerOutput : RegisterOutput = clientsService.registerClient(registerClientInput)

        return ResponseEntity.status(HttpStatus.CREATED).body(registerOutput)

    }

    @Authentication
    @PostMapping(Uris.CLIENT_REQUEST)
    fun addClient(user : User, @PathVariable monitor_id: UUID) : ResponseEntity<UUID> {
        TODO()


    }



}