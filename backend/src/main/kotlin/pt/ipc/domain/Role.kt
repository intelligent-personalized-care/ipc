package pt.ipc.domain

enum class Role {
    CLIENT,
    MONITOR,
    ADMIN;

    fun isMonitor(): Boolean =
        when (this) {
            MONITOR -> true
            CLIENT, ADMIN -> false
        }

    fun isClient(): Boolean =
        when (this) {
            CLIENT -> true
            MONITOR, ADMIN -> false
        }

    fun isAdmin(): Boolean =
        when (this) {
            ADMIN -> true
            MONITOR, CLIENT -> false
        }

    fun notAdmin(): Boolean =
        when (this) {
            CLIENT, MONITOR -> true
            ADMIN -> false
        }

    fun notClient(): Boolean =
        when (this) {
            ADMIN, MONITOR -> true
            CLIENT -> false
        }

    fun notMonitor(): Boolean =
        when (this) {
            CLIENT, ADMIN -> true
            MONITOR -> false
        }
}

fun Any?.toRole(): Role =
    when (this) {
        "CLIENT" -> Role.CLIENT
        "MONITOR" -> Role.MONITOR
        else -> Role.ADMIN
    }
