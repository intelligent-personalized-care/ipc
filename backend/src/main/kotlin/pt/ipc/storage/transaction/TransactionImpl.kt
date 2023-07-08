package pt.ipc.storage.transaction

import org.jdbi.v3.core.Handle
import pt.ipc.storage.cloudStorageUtils.CloudStorageConfiguration
import pt.ipc.storage.cloudStorageUtils.CloudStorageUtils
import pt.ipc.storage.cloudStorageUtils.CloudStorageUtilsImpl
import pt.ipc.storage.repositories.AdminRepository
import pt.ipc.storage.repositories.ClientsRepository
import pt.ipc.storage.repositories.ExerciseRepository
import pt.ipc.storage.repositories.MonitorRepository
import pt.ipc.storage.repositories.PlansRepository
import pt.ipc.storage.repositories.UsersRepository
import pt.ipc.storage.repositories.jdbi.JdbiAdminRepository
import pt.ipc.storage.repositories.jdbi.JdbiClientsRepository
import pt.ipc.storage.repositories.jdbi.JdbiExercisesRepository
import pt.ipc.storage.repositories.jdbi.JdbiMonitorsRepository
import pt.ipc.storage.repositories.jdbi.JdbiPlansRepository
import pt.ipc.storage.repositories.jdbi.JdbiUsersRepository

class TransactionImpl(
    private val handle: Handle,
    private val cloudStorageConfiguration: CloudStorageConfiguration
) : Transaction {

    override val clientsRepository: ClientsRepository by lazy { JdbiClientsRepository(handle) }

    override val monitorRepository: MonitorRepository by lazy { JdbiMonitorsRepository(handle) }

    override val plansRepository: PlansRepository by lazy { JdbiPlansRepository(handle) }

    override val exerciseRepository: ExerciseRepository by lazy { JdbiExercisesRepository(handle) }

    override val cloudStorage: CloudStorageUtils by lazy { CloudStorageUtilsImpl(cloudStorageConfiguration) }

    override val adminRepository: AdminRepository by lazy { JdbiAdminRepository(handle) }

    override val usersRepository: UsersRepository by lazy { JdbiUsersRepository(handle) }
}
