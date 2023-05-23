package pt.ipc.http.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pt.ipc.domain.ExerciseInfo
import pt.ipc.domain.ExerciseType
import pt.ipc.http.pipeline.authentication.Authentication
import pt.ipc.http.pipeline.exceptionHandler.Problem
import pt.ipc.http.utils.Uris
import pt.ipc.services.ExercisesService
import pt.ipc.services.dtos.ExerciseVideo
import java.util.*

@RestController
@RequestMapping(produces = ["application/json", Problem.PROBLEM_MEDIA_TYPE])
class ExercisesController(private val exercisesService: ExercisesService) {

    //@Authentication
    @GetMapping(Uris.EXERCISES)
    fun getExercises(@RequestParam(required = false) exerciseType: ExerciseType?): ResponseEntity<List<ExerciseInfo>> {
        val exercises = exercisesService.getExercises(exerciseType)
        return ResponseEntity.ok(exercises)
    }

    @Authentication
    @GetMapping(Uris.EXERCISES_INFO)
    fun getExerciseInfo(@PathVariable exercise_id: UUID): ResponseEntity<ExerciseVideo> {
        val exerciseVideo = exercisesService.getExercisesInfo(exerciseID = exercise_id)
        return ResponseEntity.ok(exerciseVideo)
    }


}
