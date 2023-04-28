package pt.ipc_app.service.models.register

import java.util.*

data class RegisterOutput(
    val id : UUID,
    val token : String
)