package pt.ipc.storage.transaction

import org.jdbi.v3.core.Handle
import pt.ipc.storage.cloudStorageUtils.CloudStorageConfiguration
import pt.ipc.storage.cloudStorageUtils.CloudStorageUtils
import pt.ipc.storage.cloudStorageUtils.CloudStorageUtilsImpl
import pt.ipc.storage.repositories.ClientsRepository
import pt.ipc.storage.repositories.MonitorRepository
import pt.ipc.storage.repositories.jdbi.JdbiClientsRepository
import pt.ipc.storage.repositories.jdbi.JdbiMonitorRepository

class TransactionImpl(
    private val handle: Handle,
    private val cloudStorageConfiguration: CloudStorageConfiguration
) : Transaction {

    override val clientsRepository: ClientsRepository by lazy { JdbiClientsRepository(handle) }

    override val monitorRepository: MonitorRepository by lazy { JdbiMonitorRepository(handle) }

    override val cloudStorage: CloudStorageUtils by lazy { CloudStorageUtilsImpl(cloudStorageConfiguration) }
}
