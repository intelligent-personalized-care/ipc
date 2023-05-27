package pt.ipc.services

import org.springframework.stereotype.Service
import pt.ipc.domain.ExerciseInfo
import pt.ipc.domain.ExerciseType
import pt.ipc.domain.exceptions.ExerciseNotExists
import pt.ipc.services.dtos.ExerciseVideo
import pt.ipc.storage.transaction.TransactionManager
import java.util.*

@Service
class ExercisesServiceImpl(
    private val transactionManager: TransactionManager
) : ExercisesService {
    override fun getExercisesInfo(exerciseID: UUID): ExerciseVideo {
        return transactionManager.runBlock(
            block = {
                val exerciseInfo = it.exerciseRepository.getExercise(exerciseID = exerciseID) ?: throw ExerciseNotExists
                val video = it.cloudStorage.downloadExampleVideo(exerciseName = exerciseID)
                ExerciseVideo(
                    exerciseInfo = exerciseInfo,
                    exerciseVideo = video
                )
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
}
