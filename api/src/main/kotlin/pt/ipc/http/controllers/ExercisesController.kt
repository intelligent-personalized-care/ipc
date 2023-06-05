package pt.ipc.http.controllers

import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pt.ipc.domain.ExerciseInfo
import pt.ipc.domain.ExerciseType
import pt.ipc.domain.User
import pt.ipc.http.models.ListOfExercisesInfo
import pt.ipc.http.pipeline.authentication.Authentication
import pt.ipc.http.pipeline.exceptionHandler.Problem
import pt.ipc.http.utils.Uris
import pt.ipc.services.ExercisesService
import java.util.*

@RestController
@RequestMapping(produces = ["application/json", Problem.PROBLEM_MEDIA_TYPE])
class ExercisesController(private val exercisesService: ExercisesService) {

    @Authentication
    @GetMapping(Uris.EXERCISES)
    fun getExercises(
        @RequestParam(required = false) exerciseType: ExerciseType?,
        @RequestParam(required = false, defaultValue = DEFAULT_SKIP) skip: Int,
        @RequestParam(required = false, defaultValue = DEFAULT_LIMIT) limit: Int
    ): ResponseEntity<ListOfExercisesInfo> {

        val exercises = exercisesService.getExercises(exerciseType = exerciseType, skip = skip, limit = limit )
        return ResponseEntity.ok(
            ListOfExercisesInfo(exercises = exercises)
        )

    }

    @Authentication
    @GetMapping(Uris.EXERCISES_INFO)
    fun getExerciseInfo(@PathVariable exerciseID: UUID): ResponseEntity<ExerciseInfo> {

        val exerciseInfo = exercisesService.getExercisesInfo(exerciseID = exerciseID)
        return ResponseEntity.ok(exerciseInfo)

    }

    @GetMapping(Uris.EXERCISES_INFO_VIDEO)
    fun getExerciseVideo(@PathVariable exerciseID: UUID) : ResponseEntity<ByteArray>{
        val exerciseVideo = exercisesService.getExerciseVideo(exerciseID = exerciseID)
        val headers = HttpHeaders()

        headers.contentType = MediaType.parseMediaType("video/mp4")
        headers.contentLength = exerciseVideo.size.toLong()

        return ResponseEntity.ok().headers(headers).body(exerciseVideo)

    }

    @Authentication
    @GetMapping(Uris.VIDEO_OF_EXERCISE)
    fun getVideoOfExercise(
        @PathVariable clientID: UUID,
        @PathVariable dailyListID: Int,
        @PathVariable exerciseID: Int,
        @PathVariable planID: Int,
        user: User
    ): ResponseEntity<ByteArray>{
        TODO()
    }

    companion object{
        const val DEFAULT_SKIP = "0"
        const val DEFAULT_LIMIT = "10"
    }
}
