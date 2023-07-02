package pt.ipc_app.mlkit.exercises

import com.google.mlkit.vision.pose.PoseLandmark


/**
 * Class to represent different exercises
 * */
data class ExerciseLogic(
    val firstPoint: PoseLandmark,
    val midPoint: PoseLandmark,
    val lastPoint: PoseLandmark,
    val rightHandPos: Float,
    val leftHandPos: Float,
    val ratio: Float,
   /* val pointOne: PoseLandmark,
    val pointTwo: PoseLandmark,*/
    val currentHeight: Float,
    val minSize: Float,
    //conditions
    val condOne: Int,
    val condTwo: Int = 0,
    val condThree: Double = 0.5,

    val condCurrentWeight: Int?,
    val condMinSize: Int?,

    //line text of conditions
    val lTextCondOne: String,
    val lTextCondTwo: String,
    val lTextCondThree: String,

    val sets: Int?,
    val reps: Int?,

    )
