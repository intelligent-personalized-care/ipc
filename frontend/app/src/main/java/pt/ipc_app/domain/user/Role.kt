package pt.ipc_app.domain.user

enum class Role {
    CLIENT,
    MONITOR;

    companion object {
        fun isClient(role: String) = role == CLIENT.name
    }
}

fun Role.isClient(): Boolean = this == Role.CLIENT

fun String.toRole() = Role.values().find { this == it.name }
