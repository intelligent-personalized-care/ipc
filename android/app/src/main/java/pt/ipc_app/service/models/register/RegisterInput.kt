package pt.ipc_app.service.models.register


data class RegisterClientInput(
    val name: String,
    val email: String,
    val password: String,
    val weight : Int?,
    val height : Int?,
    val birthDate : String?,
    val physicalCondition : String?
)

data class RegisterMonitorInput(
    val name: String,
    val email: String,
    val password: String,
    val credential: ByteArray
)
