package pt.ipc.database_storage.repositories

import pt.ipc.domain.User
import java.time.LocalDate
import java.util.*

interface MonitorRepository {

    fun registerMonitor(user : User, date : LocalDate, encryptedToken : String)

    fun requestClient(requestID : UUID, monitorID : UUID, clientID : UUID)

}