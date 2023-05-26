package pt.ipc.storage.transaction

import org.springframework.stereotype.Component
import pt.ipc.storage.cloudStorageUtils.CloudStorageUtils
import pt.ipc.storage.repositories.ClientsRepository
import pt.ipc.storage.repositories.ExerciseRepository
import pt.ipc.storage.repositories.MonitorRepository
import pt.ipc.storage.repositories.PlansRepository

@Component
interface Transaction {

    val clientsRepository: ClientsRepository

    val monitorRepository: MonitorRepository

    val plansRepository: PlansRepository

    val exerciseRepository: ExerciseRepository

    val cloudStorage: CloudStorageUtils
}
