package pt.ipc.services.users

import pt.ipc.services.users.dtos.RegisterMonitorInput
import pt.ipc.services.users.dtos.RegisterOutput

interface MonitorService {

    fun registerMonitor(registerMonitorInput: RegisterMonitorInput) : RegisterOutput


}