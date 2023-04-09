package pt.ipc.database_storage.repositories

import pt.ipc.domain.User
import java.time.LocalDate

interface MonitorRepository {
    fun registerMonitor(user : User, date : LocalDate, encryptedToken : String)
}