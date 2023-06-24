package pt.ipc.storage.transaction

import org.springframework.stereotype.Component
import pt.ipc.storage.cloudStorageUtils.CloudStorageUtils
import pt.ipc.storage.repositories.*

@Component
interface Transaction {

    val clientsRepository: ClientsRepository

    val monitorRepository: MonitorRepository

    val plansRepository: PlansRepository

    val exerciseRepository: ExerciseRepository

    val cloudStorage: CloudStorageUtils

    val adminRepository : AdminRepository
}
