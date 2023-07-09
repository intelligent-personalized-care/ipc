package pt.ipc.http.controllers

import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import pt.ipc.domain.ExerciseInfo
import pt.ipc.domain.ExerciseType
import pt.ipc.domain.PlanOutput
import pt.ipc.domain.User
import pt.ipc.http.models.ListOfExercisesInfo
import pt.ipc.http.models.VideoFeedBack
import pt.ipc.http.pipeline.authentication.Authentication
import pt.ipc.http.pipeline.exceptionHandler.Problem
import pt.ipc.http.utils.Uris
import pt.ipc.services.ExercisesService
import java.time.LocalDate
import java.util.UUID

@RestController
@RequestMapping(produces = ["application/json", "video/mp4", Problem.PROBLEM_MEDIA_TYPE])
class ExercisesController(private val exercisesService: ExercisesService) {

    @GetMapping(Uris.EXERCISES)
    fun getExercises(
        @RequestParam(required = false) exerciseType: ExerciseType?,
        @RequestParam(required = false, defaultValue = DEFAULT_SKIP) skip: Int,
        @RequestParam(required = false, defaultValue = DEFAULT_LIMIT) limit: Int
    ): ResponseEntity<ListOfExercisesInfo> {
        val exercises = exercisesService.getExercises(exerciseType = exerciseType, skip = skip, limit = limit)
        return ResponseEntity.ok(
            ListOfExercisesInfo(exercises = exercises)
        )
    }

    @GetMapping(Uris.EXERCISES_INFO)
    fun getExerciseInfo(@PathVariable exerciseID: UUID): ResponseEntity<ExerciseInfo> {
        val exerciseInfo = exercisesService.getExercisesInfo(exerciseID = exerciseID)
        return ResponseEntity.ok(exerciseInfo)
    }

    // @Authentication TODO
    @GetMapping(Uris.EXERCISES_INFO_VIDEO)
    fun getExerciseVideo(@PathVariable exerciseID: UUID): ResponseEntity<ByteArray> {
        val exerciseVideo = exercisesService.getExercisePreviewVideo(exerciseID = exerciseID)
        val headers = HttpHeaders()

        headers.contentType = MediaType.parseMediaType("video/mp4")
        headers.contentLength = exerciseVideo.size.toLong()

        return ResponseEntity.ok().headers(headers).body(exerciseVideo)
    }

    // @Authentication TODO
    @GetMapping(Uris.VIDEO_OF_EXERCISE)
    fun getClientVideoOfExercise(
        @PathVariable clientID: UUID,
        @PathVariable planID: Int,
        @PathVariable dailyListID: Int,
        @PathVariable exerciseID: Int,
        @RequestParam(required = true) set: Int
        // user: User
    ): ResponseEntity<ByteArray> {
        val clientVideo = exercisesService.getClientVideo(
            clientID = clientID,
            // userID = user.id,
            planID = planID,
            dailyList = dailyListID,
            dailyExercise = exerciseID,
            set = set
        )

        val headers = HttpHeaders()

        headers.contentType = MediaType.parseMediaType("video/mp4")
        headers.contentLength = clientVideo.size.toLong()

        return ResponseEntity.ok().headers(headers).body(clientVideo)
    }

    @Authentication
    @GetMapping(Uris.EXERCISE_FEEDBACK)
    fun getVideoFeedBack(
        @PathVariable clientID: UUID,
        @PathVariable dailyListID: Int,
        @PathVariable exerciseID: Int,
        @PathVariable planID: Int,
        @RequestParam(required = true) set: Int,
        user: User
    ): ResponseEntity<VideoFeedBack> {
        val videoFeedBack = exercisesService.getVideoFeedback(
            clientID = clientID,
            userID = user.id,
            planID = planID,
            dailyList = dailyListID,
            dailyExercise = exerciseID,
            set = set
        )

        return ResponseEntity.ok(videoFeedBack)
    }

    @Authentication
    @GetMapping(Uris.PLAN_CURRENT)
    fun getPlanOfClientContainingDate(
        @PathVariable clientID: UUID,
        @RequestParam(required = false)
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        date: LocalDate?,
        user: User
    ): ResponseEntity<PlanOutput> {
        val planOutput: PlanOutput =
            exercisesService.getPlanOfClientContainingDate(userID = user.id, clientID = clientID, date = date ?: LocalDate.now())

        return ResponseEntity.ok(planOutput)
    }

    companion object {
        const val DEFAULT_SKIP = "0"
        const val DEFAULT_LIMIT = "10"
    }
}
