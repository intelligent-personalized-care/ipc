package pt.ipc.services.exercisesService

import org.springframework.stereotype.Service
import pt.ipc.domain.exercises.ExerciseInfo
import pt.ipc.domain.exercises.ExerciseType
import pt.ipc.domain.plan.PlanOutput
import pt.ipc.domain.exceptions.ClientDontHavePlan
import pt.ipc.domain.exceptions.ClientNotPostedVideo
import pt.ipc.domain.exceptions.ExerciseNotExists
import pt.ipc.domain.exceptions.ForbiddenRequest
import pt.ipc.domain.plan.VideoFeedBack
import pt.ipc.storage.transaction.TransactionManager
import java.time.LocalDate
import java.util.UUID

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

    override fun getExercises(exerciseType: ExerciseType?, skip: Int, limit: Int): List<ExerciseInfo> {
        return transactionManager.runBlock(
            block = {
                if (exerciseType == null) {
                    it.exerciseRepository.getExercises(skip = skip, limit = limit)
                } else {
                    it.exerciseRepository.getExerciseByType(type = exerciseType, skip = skip, limit = limit)
                }
            }
        )
    }

    override fun getExercisePreviewVideo(exerciseID: UUID): ByteArray {
        return transactionManager.runBlock(
            block = {
                it.cloudStorage.downloadExampleVideo(exerciseID = exerciseID)
            }
        )
    }

    override fun getClientVideo(clientID: UUID, /*userID: UUID,*/ planID: Int, dailyList: Int, dailyExercise: Int, set: Int): ByteArray {
        return transactionManager.runBlock(
            block = {
                /*
                if (userID != clientID) {
                    if (!it.monitorRepository.isMonitorOfClient(monitorID = userID, clientID = clientID)) throw ForbiddenRequest
                }
                 */

                val videoID = it.exerciseRepository.getClientVideoID(
                    clientID = clientID,
                    planID = planID,
                    dailyListID = dailyList,
                    dailyExerciseID = dailyExercise,
                    set = set
                ) ?: throw ClientNotPostedVideo

                it.cloudStorage.downloadClientVideo(fileName = videoID)
            }
        )
    }

    override fun getVideoFeedback(
        clientID: UUID,
        userID: UUID,
        planID: Int,
        dailyList: Int,
        dailyExercise: Int,
        set: Int
    ): VideoFeedBack {
        return transactionManager.runBlock(
            block = {
                if (userID != clientID) {
                    if (!it.monitorRepository.isMonitorOfClient(monitorID = userID, clientID = clientID)) throw ForbiddenRequest
                }

                val videoID = it.exerciseRepository.getClientVideoID(
                    clientID = clientID,
                    planID = planID,
                    dailyListID = dailyList,
                    dailyExerciseID = dailyExercise,
                    set = set
                ) ?: throw ClientNotPostedVideo

                it.exerciseRepository.getVideoFeedback(videoID = videoID)
            }
        )
    }

    override fun getPlanOfClientContainingDate(userID: UUID, clientID: UUID, date: LocalDate): PlanOutput =
        transactionManager.runBlock(
            block = {
                if (userID != clientID) {
                    if (!it.monitorRepository.isMonitorOfClient(monitorID = userID, clientID = clientID)) throw ForbiddenRequest
                }

                it.plansRepository.getPlanOfClientContainingDate(clientID = clientID, date = date) ?: throw ClientDontHavePlan
            }
        )
}
