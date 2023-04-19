package pt.ipc.database_storage.artificialTransaction

import org.jdbi.v3.core.Handle
import pt.ipc.database_storage.cloudStorageUtils.CloudStorageConfiguration
import pt.ipc.database_storage.cloudStorageUtils.CloudStorageUtils
import pt.ipc.database_storage.cloudStorageUtils.CloudStorageUtilsImpl
import pt.ipc.database_storage.repositories.ClientsRepository
import pt.ipc.database_storage.repositories.MonitorRepository
import pt.ipc.database_storage.repositories.jdbi.JdbiClientsRepository
import pt.ipc.database_storage.repositories.jdbi.JdbiMonitorRepository


class TransactionImpl(
    private val handle : Handle,
    private val cloudStorageConfiguration: CloudStorageConfiguration,
 ) : Transaction {

    override val clientsRepository: ClientsRepository by lazy {JdbiClientsRepository(handle)}

    override val monitorRepository: MonitorRepository by lazy { JdbiMonitorRepository(handle) }

    override val cloudStorage: CloudStorageUtils by lazy{ CloudStorageUtilsImpl(cloudStorageConfiguration) }

}