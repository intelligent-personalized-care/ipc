package pt.ipc.services

import org.springframework.stereotype.Service
import pt.ipc.domain.ExerciseInfo
import pt.ipc.domain.ExerciseType
import pt.ipc.domain.exceptions.ExerciseNotExists
import pt.ipc.storage.transaction.TransactionManager
import java.util.*

@Service
class ExercisesServiceImpl(
    private val transactionManager: TransactionManager
) : ExercisesService {
    override fun getExercisesInfo(exerciseID: UUID): ExerciseInfo {
        return transactionManager.runBlock(
            block = {
                it.exerciseRepository.getExercise(exerciseID = exerciseID) ?: throw ExerciseNotExists
            }
        )
    }

    override fun getExercises(exerciseType: ExerciseType?, skip : Int, limit : Int): List<ExerciseInfo> {
        return transactionManager.runBlock(
            block = {
                if (exerciseType == null)
                    it.exerciseRepository.getExercises(skip = skip, limit = limit)
                else
                    it.exerciseRepository.getExerciseByType(type = exerciseType, skip = skip, limit = limit)
            }
        )
    }

    override fun getExerciseVideo(exerciseID: UUID) : ByteArray{
        return transactionManager.runBlock(
            block = {
                it.cloudStorage.downloadExampleVideo(exerciseID = exerciseID)
            }
        )
    }
}
