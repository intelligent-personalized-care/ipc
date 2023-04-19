package pt.ipc.database_storage.artificialTransaction

import org.springframework.stereotype.Component
import pt.ipc.database_storage.cloudStorageUtils.CloudStorageUtils
import pt.ipc.database_storage.repositories.ClientsRepository
import pt.ipc.database_storage.repositories.MonitorRepository

@Component
interface Transaction {

    val clientsRepository : ClientsRepository

    val monitorRepository : MonitorRepository

    val cloudStorage : CloudStorageUtils
}