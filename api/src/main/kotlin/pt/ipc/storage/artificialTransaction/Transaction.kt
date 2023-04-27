package pt.ipc.storage.artificialTransaction

import org.springframework.stereotype.Component
import pt.ipc.storage.cloudStorageUtils.CloudStorageUtils
import pt.ipc.storage.repositories.ClientsRepository
import pt.ipc.storage.repositories.MonitorRepository

@Component
interface Transaction {

    val clientsRepository: ClientsRepository

    val monitorRepository: MonitorRepository

    val cloudStorage: CloudStorageUtils
}
