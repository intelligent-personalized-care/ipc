package pt.ipc.domain

enum class Role {
    CLIENT,
    MONITOR,
    ADMIN;

    fun isMonitor(): Boolean = this == MONITOR


    fun notAdmin(): Boolean = this != ADMIN


    fun notClient(): Boolean = this != CLIENT


    fun notMonitor(): Boolean = this != MONITOR

}

fun Any?.toRole(): Role =
    when (this) {
        "CLIENT" -> Role.CLIENT
        "MONITOR" -> Role.MONITOR
        else -> Role.ADMIN
    }
