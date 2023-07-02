package pt.ipc.storage.repositories

import pt.ipc.domain.User
import pt.ipc.services.dtos.MonitorInfo
import java.util.UUID

interface AdminRepository {

    fun getUserByID(id: UUID): User?

    fun createAdmin(id: UUID, email: String, name: String, passwordHash: String, tokenHash: String)

    fun getUnverifiedMonitors(): List<MonitorInfo>

    fun decideMonitorVerification(monitorID: UUID, decision: Boolean)
}
