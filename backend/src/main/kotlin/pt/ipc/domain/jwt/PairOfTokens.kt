package pt.ipc.domain.jwt

data class PairOfTokens(val accessToken: String, val refreshToken: String)
