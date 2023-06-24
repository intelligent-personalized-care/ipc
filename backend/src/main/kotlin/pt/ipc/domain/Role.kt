package pt.ipc.domain

enum class Role {
    CLIENT,
    MONITOR,
    ADMIN;
}

fun String.toRole(): Role =
    when(this){
      "CLIENT" -> Role.CLIENT
      "MONITOR" -> Role.MONITOR
      else -> Role.ADMIN
    }
