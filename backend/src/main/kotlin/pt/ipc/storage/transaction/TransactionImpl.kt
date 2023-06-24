package pt.ipc.storage.transaction

import org.jdbi.v3.core.Handle
import pt.ipc.storage.cloudStorageUtils.CloudStorageConfiguration
import pt.ipc.storage.cloudStorageUtils.CloudStorageUtils
import pt.ipc.storage.cloudStorageUtils.CloudStorageUtilsImpl
import pt.ipc.storage.repositories.*
import pt.ipc.storage.repositories.jdbi.*

class TransactionImpl(
    private val handle: Handle,
    private val cloudStorageConfiguration: CloudStorageConfiguration
) : Transaction {

    override val clientsRepository: ClientsRepository by lazy { JdbiClientsRepository(handle) }

    override val monitorRepository: MonitorRepository by lazy { JdbiMonitorsRepository(handle) }

    override val plansRepository: PlansRepository by lazy { JdbiPlansRepository(handle) }

    override val exerciseRepository: ExerciseRepository by lazy { JdbiExercisesRepository(handle) }

    override val cloudStorage: CloudStorageUtils by lazy { CloudStorageUtilsImpl(cloudStorageConfiguration) }

    override val adminRepository : AdminRepository by lazy { JdbiAdminRepository(handle) }
}
