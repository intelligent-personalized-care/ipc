package pt.ipc_app.service.models.refresh

data class RefreshTokenOutput(
    val accessToken: String,
    val refreshToken: String
)
