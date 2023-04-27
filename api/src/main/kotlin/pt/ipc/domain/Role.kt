package pt.ipc.domain

enum class Role {
    CLIENT,
    MONITOR;
}

fun String.toRole(): Role =
    if (this == "CLIENT") Role.CLIENT else Role.MONITOR
