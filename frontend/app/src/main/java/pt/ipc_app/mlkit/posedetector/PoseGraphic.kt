/*
 * Copyright 2020 Google LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pt.ipc_app.mlkit.posedetector

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.text.TextUtils
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark
import pt.ipc_app.domain.exercise.Exercise
import pt.ipc_app.mlkit.GraphicOverlay
import pt.ipc_app.mlkit.exercises.ExerciseLogic
import pt.ipc_app.ui.screens.exercises.ExercisesViewModel
import kotlin.math.abs
import kotlin.math.atan2

private const val SECONDS_PER_MINUTE = 60

/** Draw the detected pose in preview.  */
class PoseGraphic internal constructor(
  overlay: GraphicOverlay,
  private val pose: Pose,
  private val exercise: Exercise,
  private  val viewModel: ExercisesViewModel
): GraphicOverlay.Graphic(overlay) {

  private val leftPaint: Paint
  private val rightPaint: Paint
  private val whitePaint: Paint
  private val tipPaint: Paint
  private var toDraw = false
  private val textSizeCoordinateX = 1.5f
  private val textSizeCoordinateY = 6
  override fun draw(canvas: Canvas) {
    val landmarks = pose.allPoseLandmarks
    if (landmarks.isEmpty()) return

    val nose = pose.getPoseLandmark(PoseLandmark.NOSE)
    val leftEyeInner = pose.getPoseLandmark(PoseLandmark.LEFT_EYE_INNER)
    val leftEye = pose.getPoseLandmark(PoseLandmark.LEFT_EYE)
    val leftEyeOuter = pose.getPoseLandmark(PoseLandmark.LEFT_EYE_OUTER)
    val rightEyeInner = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE_INNER)
    val rightEye = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE)
    val rightEyeOuter = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE_OUTER)
    val leftEar = pose.getPoseLandmark(PoseLandmark.LEFT_EAR)
    val rightEar = pose.getPoseLandmark(PoseLandmark.RIGHT_EAR)
    val leftMouth = pose.getPoseLandmark(PoseLandmark.LEFT_MOUTH)
    val rightMouth = pose.getPoseLandmark(PoseLandmark.RIGHT_MOUTH)

    val leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)
    val rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER)
    val leftElbow = pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW)
    val rightElbow = pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW)
    val leftWrist = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST)
    val rightWrist = pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST)
    val leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP)
    val rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP)
    val leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE)
    val rightKnee = pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE)
    val leftAnkle = pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE)
    val rightAnkle = pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE)

    val leftPinky = pose.getPoseLandmark(PoseLandmark.LEFT_PINKY)
    val rightPinky = pose.getPoseLandmark(PoseLandmark.RIGHT_PINKY)
    val leftIndex = pose.getPoseLandmark(PoseLandmark.LEFT_INDEX)
    val rightIndex = pose.getPoseLandmark(PoseLandmark.RIGHT_INDEX)
    val leftThumb = pose.getPoseLandmark(PoseLandmark.LEFT_THUMB)
    val rightThumb = pose.getPoseLandmark(PoseLandmark.RIGHT_THUMB)
    val leftHeel = pose.getPoseLandmark(PoseLandmark.LEFT_HEEL)
    val rightHeel = pose.getPoseLandmark(PoseLandmark.RIGHT_HEEL)
    val leftFootIndex = pose.getPoseLandmark(PoseLandmark.LEFT_FOOT_INDEX)
    val rightFootIndex = pose.getPoseLandmark(PoseLandmark.RIGHT_FOOT_INDEX)

    //Calculate whether the distance between the shoulder and the foot is the same width
    val ratio = ratio(leftShoulder!!.position.x, rightShoulder!!.position.x, leftAnkle!!.position.x, rightAnkle!!.position.x)

    //to divide each exercise logic
    when(exercise.exeTitle){
        "Squats" -> {

          //Calculate whether the hand exceeds the shoulder
          val yRightHand = differenceBetweenCoordinates(rightWrist!!.position.y, rightShoulder.position.y)
          val yLeftHand = differenceBetweenCoordinates(leftWrist!!.position.y, leftShoulder.position.y)

          //defining specific exercise logic
          val exerciseLogic = ExerciseLogic(rightHip!!,rightKnee!!,rightAnkle,yRightHand,yLeftHand,ratio,
            rightShoulder.position.y + leftShoulder.position.y, rightAnkle.position.y - rightHip.position.y, 5, 0, 0.5,
            2, 5,
            "Please stand up straight", "Please hold your hands behind your head", "Please spread your feet shoulder-width apart")

          doExerciseLogic(exerciseLogic)

          toDraw = !toDraw
        }
        "Push ups" -> {

          //Calculate whether the hand is in front of the shoulder
          val yRightHand = differenceBetweenCoordinates(rightShoulder.position.y,rightWrist!!.position.y)
          val yLeftHand = differenceBetweenCoordinates(leftShoulder.position.y, leftWrist!!.position.y)

          //defining specific exercise logic
          val exerciseLogic = ExerciseLogic(rightWrist,rightElbow!!,rightShoulder,yRightHand,yLeftHand,ratio,
            rightShoulder.position.y + leftShoulder.position.y , rightShoulder.position.y + leftShoulder.position.y, 25, 0, 0.5,
            2, 12,
            "Please keep in a push up position", "Please hold your hands straight out in front of your body ", "Please spread your feet shoulder-width apart")

          doExerciseLogic(exerciseLogic)
          toDraw = !toDraw

        }
        "Shoulder press" -> {

          //Calculate whether the hand exceeds the shoulder
          val yRightHand = differenceBetweenCoordinates(rightWrist!!.position.y, rightShoulder.position.y)
          val yLeftHand = differenceBetweenCoordinates(leftWrist!!.position.y, leftShoulder.position.y)

          //defining specific exercise logic
          val exerciseLogic = ExerciseLogic(rightWrist,rightElbow!!,rightShoulder,yRightHand,yLeftHand,ratio,
            rightWrist.position.y + leftWrist.position.y ,rightElbow.position.y + leftElbow!!.position.y, 110, 0, 0.5,
            2, 12,
            "Please put your elbows slightly above shoulder height", "Please hold your hands above your shoulders ", "Please spread your feet shoulder-width apart")

          doExerciseLogic(exerciseLogic)

          toDraw = !toDraw
        }
        else -> {
          //presents a text saying that this exercise is not yet supported
          drawText(canvas,"Exercise not yet supported",3, -3)
        }
    }


    if(toDraw) {
      //draw points
      //left
      drawPoint(canvas, nose!!.position, whitePaint)
      drawPoint(canvas, leftEyeInner!!.position, leftPaint)
      drawPoint(canvas, leftEye!!.position, leftPaint)
      drawPoint(canvas, leftEyeOuter!!.position, leftPaint)
      drawPoint(canvas, leftEar!!.position, leftPaint)
      drawPoint(canvas, leftMouth!!.position, leftPaint)
      drawPoint(canvas, leftShoulder.position, leftPaint)
      drawPoint(canvas, leftElbow!!.position, leftPaint)
      drawPoint(canvas, leftWrist!!.position, leftPaint)
      drawPoint(canvas, leftHip!!.position, leftPaint)
      drawPoint(canvas, leftKnee!!.position, leftPaint)
      drawPoint(canvas, leftAnkle.position, leftPaint)
      drawPoint(canvas, leftPinky!!.position, leftPaint)
      drawPoint(canvas, leftIndex!!.position, leftPaint)
      drawPoint(canvas, leftThumb!!.position, leftPaint)
      drawPoint(canvas, leftHeel!!.position, leftPaint)
      drawPoint(canvas, leftFootIndex!!.position, leftPaint)

      //right
      drawPoint(canvas, rightEyeInner!!.position, rightPaint)
      drawPoint(canvas, rightEye!!.position, rightPaint)
      drawPoint(canvas, rightEyeOuter!!.position, rightPaint)
      drawPoint(canvas, rightEar!!.position, rightPaint)
      drawPoint(canvas, rightMouth!!.position, rightPaint)
      drawPoint(canvas, rightShoulder.position, rightPaint)
      drawPoint(canvas, rightElbow!!.position, rightPaint)
      drawPoint(canvas, rightWrist!!.position, rightPaint)
      drawPoint(canvas, rightHip!!.position, rightPaint)
      drawPoint(canvas, rightKnee!!.position, rightPaint)
      drawPoint(canvas, rightAnkle.position, rightPaint)
      drawPoint(canvas, rightPinky!!.position, rightPaint)
      drawPoint(canvas, rightIndex!!.position, rightPaint)
      drawPoint(canvas, rightThumb!!.position, rightPaint)
      drawPoint(canvas, rightHeel!!.position, rightPaint)
      drawPoint(canvas, rightFootIndex!!.position, rightPaint)


      //draw lines between points
      // Face
      drawLine(canvas, nose.position, leftEyeInner.position, whitePaint)
      drawLine(canvas, leftEyeInner.position, leftEye.position, whitePaint)
      drawLine(canvas, leftEye.position, leftEyeOuter.position, whitePaint)
      drawLine(canvas, leftEyeOuter.position, leftEar.position, whitePaint)
      drawLine(canvas, nose.position, rightEyeInner.position, whitePaint)
      drawLine(canvas, rightEyeInner.position, rightEye.position, whitePaint)
      drawLine(canvas, rightEye.position, rightEyeOuter.position, whitePaint)
      drawLine(canvas, rightEyeOuter.position, rightEar.position, whitePaint)
      drawLine(canvas, leftMouth.position, rightMouth.position, whitePaint)

      drawLine(canvas, leftShoulder.position, rightShoulder.position, whitePaint)
      drawLine(canvas, leftHip.position, rightHip.position, whitePaint)

      // Left body
      drawLine(canvas, leftShoulder.position, leftElbow.position, leftPaint)
      drawLine(canvas, leftElbow.position, leftWrist.position, leftPaint)
      drawLine(canvas, leftShoulder.position, leftHip.position, leftPaint)
      drawLine(canvas, leftHip.position, leftKnee.position, leftPaint)
      drawLine(canvas, leftKnee.position, leftAnkle.position, leftPaint)
      drawLine(canvas, leftWrist.position, leftThumb.position, leftPaint)
      drawLine(canvas, leftWrist.position, leftPinky.position, leftPaint)
      drawLine(canvas, leftWrist.position, leftIndex.position, leftPaint)
      drawLine(canvas, leftIndex.position, leftPinky.position, leftPaint)
      drawLine(canvas, leftAnkle.position, leftHeel.position, leftPaint)
      drawLine(canvas, leftHeel.position, leftFootIndex.position, leftPaint)
      drawLine(canvas, leftAnkle.position, leftFootIndex.position, leftPaint)

      // Right body
      drawLine(canvas, rightShoulder.position, rightElbow.position, rightPaint)
      drawLine(canvas, rightElbow.position, rightWrist.position, rightPaint)
      drawLine(canvas, rightShoulder.position, rightHip.position, rightPaint)
      drawLine(canvas, rightHip.position, rightKnee.position, rightPaint)
      drawLine(canvas, rightKnee.position, rightAnkle.position, rightPaint)
      drawLine(canvas, rightWrist.position, rightThumb.position, rightPaint)
      drawLine(canvas, rightWrist.position, rightPinky.position, rightPaint)
      drawLine(canvas, rightWrist.position, rightIndex.position, rightPaint)
      drawLine(canvas, rightIndex.position, rightPinky.position, rightPaint)
      drawLine(canvas, rightAnkle.position, rightHeel.position, rightPaint)
      drawLine(canvas, rightHeel.position, rightFootIndex.position, rightPaint)
      drawLine(canvas, rightAnkle.position, rightFootIndex.position, rightPaint)
    }

    //draws all the info regarding the exercise
    drawInfoOnScreen(canvas,toDraw)

  }

  /**
   * Resets the base parameters
   * */
  private fun reInitParams() {
    lineOneText = ""
    lineTwoText = ""
    shoulderHeight = 0f
    minSize = 0f
    isCount = false
    isUp = false
    isDown = false
    upCount = 0
    downCount = 0
  }

  /**
   * Draws a point of the pose
   * */
  private fun drawPoint(canvas: Canvas, point: PointF?, paint: Paint?) {
    if (point == null) return

    canvas.drawCircle(
      translateX(point.x),
      translateY(point.y),
      DOT_RADIUS,
      paint!!
    )
  }

  /**
   * Draws a line between points of the pose
   * */
  private fun drawLine(
    canvas: Canvas,
    start: PointF?,
    end: PointF?,
    paint: Paint?,
    strokeWidth: Float = 7f //to get adjustable and thicker lines
  ) {
    if (start == null || end == null) return

    paint?.strokeWidth = strokeWidth
    canvas.drawLine(
      translateX(start.x), translateY(start.y), translateX(end.x), translateY(end.y), paint!!
    )
  }

  /**
   * Draws the text received in the specific line
   * */
  private fun drawText(canvas: Canvas, text:String, xline: Int?, yline: Int) {
    if (TextUtils.isEmpty(text)) return

    xline?.let {canvas.drawText(text, TEXT_SIZE * textSizeCoordinateX * it, TEXT_SIZE * textSizeCoordinateY + TEXT_SIZE * yline, tipPaint)}
      ?: canvas.drawText(text, TEXT_SIZE * textSizeCoordinateX, TEXT_SIZE * textSizeCoordinateY + TEXT_SIZE * yline, tipPaint)
  }

  companion object {
    private const val DOT_RADIUS = 11.0f
    private const val IN_FRAME_LIKELIHOOD_TEXT_SIZE = 30.0f
    private const val TEXT_SIZE = 70.0f

    var isUp = false
    var isDown = false
    var upCount = 0 //up times
    var downCount = 0 //down times
    var isCount = false //is counting
    var lineOneText = ""
    var lineTwoText = ""
    var shoulderHeight = 0f
    var minSize = 0f
    var lastHeight = 0f
  }

  init {
    whitePaint = Paint()
    whitePaint.color = Color.WHITE
    whitePaint.textSize = IN_FRAME_LIKELIHOOD_TEXT_SIZE
    leftPaint = Paint()
    leftPaint.color = Color.GREEN
    rightPaint = Paint()
    rightPaint.color = Color.YELLOW

    tipPaint = Paint()
    tipPaint.color = Color.BLACK
    tipPaint.textSize = 40f
  }

  /**
   * Gets the angle between 3 points of the poseLandMark
   * */
  private fun getAngle(firstPoint: PoseLandmark?, midPoint: PoseLandmark?, lastPoint: PoseLandmark?): Double {
    var result = Math.toDegrees(
      atan2(
        1.0 * lastPoint!!.position.y - midPoint!!.position.y,
        1.0 * lastPoint.position.x - midPoint.position.x) -
              atan2(
                firstPoint!!.position.y - midPoint.position.y,
                firstPoint.position.x - midPoint.position.x
              )
    )
    result = abs(result) // Angle should never be negative
    if (result > 180) result = 360.0 - result // Always get the acute representation of the angle

    return result
  }


  /**
   * Returns the difference between coordinates
   * */
  private fun differenceBetweenCoordinates(firstCoordinate :Float, secondCoordinate :Float) = firstCoordinate - secondCoordinate

  /**
   * Returns the ratio between two distances
   * */

  private fun ratio(firstCoordinate :Float, secondCoordinate :Float, thirdCoordinate :Float, fourthCoordinate :Float) =
      differenceBetweenCoordinates(thirdCoordinate,fourthCoordinate) / differenceBetweenCoordinates(firstCoordinate,secondCoordinate)


  /**
   * Implements the exercise logic
   * */
  private fun doExerciseLogic(exerciseLogic: ExerciseLogic){

    val angle = getAngle(exerciseLogic.firstPoint, exerciseLogic.midPoint, exerciseLogic.lastPoint)

    if (((180 - abs(angle)) > exerciseLogic.condOne) && !isCount) {
      reInitParams()
      lineOneText = exerciseLogic.lTextCondOne
    } else if (exerciseLogic.leftHandPos > exerciseLogic.condTwo || exerciseLogic.rightHandPos > exerciseLogic.condTwo) {
      reInitParams()
      lineOneText = exerciseLogic.lTextCondTwo
    } else if (exerciseLogic.ratio < exerciseLogic.condThree && !isCount) {
      reInitParams()
      lineOneText = exerciseLogic.lTextCondThree
    } else {
      val currentHeight =
        if(exerciseLogic.condCurrentWeight != null) exerciseLogic.currentHeight / exerciseLogic.condCurrentWeight //Judging up and down by current height
        else exerciseLogic.currentHeight

      if (!isCount) {
        shoulderHeight = currentHeight
        minSize = exerciseLogic.condMinSize?.let { exerciseLogic.minSize / it } ?: exerciseLogic.minSize
        isCount = true
        lastHeight = currentHeight
        lineOneText = "Gesture ready, control each rep"
      }
      if (!isDown && (currentHeight - lastHeight) > minSize) {
        isDown = true
        isUp = false
        downCount++
        lastHeight = currentHeight
        lineTwoText = "start down"
      } else if ((currentHeight - lastHeight) > minSize) {
        lineTwoText = "downing"
        lastHeight = currentHeight
      }
      if (!isUp && (upCount < downCount) && (lastHeight - currentHeight) > minSize) {
        isUp = true
        isDown = false
        upCount++
        lastHeight = currentHeight
        lineTwoText = "start up"
      } else if ((lastHeight - currentHeight) > minSize) {
        lineTwoText = "uping"
        lastHeight = currentHeight
      }
    }
  }

  /**
   * Draw all the text needed in pose detection screen
   * */
  private fun drawInfoOnScreen(canvas: Canvas, isPoseDetectionSupported: Boolean) {
    if(isPoseDetectionSupported) {
      drawText(canvas, lineOneText, null, -3)
      drawText(canvas, lineTwoText, null, -2)
      drawText(canvas, "Rep count: $upCount/${exercise.exeReps}", null, -1)
    }else{
      drawText(canvas, "Reps to do: ${exercise.exeReps}", null, -1)
    }

    drawText(canvas, "Sets done: ${viewModel.nrSetDone.value - 1}/${exercise.exeSets}", null, 1)

    if (viewModel.isResting()) {
      drawText(canvas, "Rest Time: ${timeFormat(viewModel.restTime.value)}",null, 3)
      drawText(canvas, "Time to REST!",null, 4)
    }

    if(viewModel.isRecording()) {
      drawText(canvas, "Record Time: ${timeFormat(viewModel.recordTime.value)}",null, 3)
    }

    if(!viewModel.isResting()) drawText(canvas, "GO! Do the next set",null, 4)
  }

  /**
   * Formats the parameter received into MM:SS format
   * */
  private fun timeFormat(totalSecs: Int) : String{
    val minutes = (totalSecs % (SECONDS_PER_MINUTE * SECONDS_PER_MINUTE)) / SECONDS_PER_MINUTE;
    val seconds = totalSecs % SECONDS_PER_MINUTE;

    return String.format("%02d:%02d", minutes, seconds);
  }
}