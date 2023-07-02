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
import kotlin.math.abs
import kotlin.math.atan2

/** Draw the detected pose in preview.  */
class PoseGraphic internal constructor(
  overlay: GraphicOverlay,
  private val pose: Pose,
  private val exercise: Exercise
): GraphicOverlay.Graphic(overlay) {

  private val leftPaint: Paint
  private val rightPaint: Paint
  private val whitePaint: Paint
  private val tipPaint: Paint
  private var toDraw = false
  override fun draw(canvas: Canvas) {
    val landmarks = pose.allPoseLandmarks
    if (landmarks.isEmpty()) return

    val nose = pose.getPoseLandmark(PoseLandmark.NOSE)
    val lefyEyeInner = pose.getPoseLandmark(PoseLandmark.LEFT_EYE_INNER)
    val lefyEye = pose.getPoseLandmark(PoseLandmark.LEFT_EYE)
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


    //to divide each exercise logic
    when(exercise.exeTitle){
        "Squats" -> {
          /*//Calculate whether the hand exceeds the shoulder
          val yRightHand = differenceBetweenCoordinates(rightWrist!!.position.y, rightShoulder!!.position.y)
          val yLeftHand = differenceBetweenCoordinates(leftWrist!!.position.y, leftShoulder!!.position.y)
          //Calculate whether the distance between the shoulder and the foot is the same width
          val shoulderDistance = differenceBetweenCoordinates(leftShoulder.position.x, rightShoulder.position.x)
          val footDistance = differenceBetweenCoordinates(leftAnkle!!.position.x, rightAnkle!!.position.x)
          val ratio = footDistance / shoulderDistance
          //angle of point 27-25-23( right hip, right knee and right ankle)
          val angleRhRkRa = getAngle(rightHip, rightKnee, rightAnkle)

          squatLogic(canvas,angleRhRkRa,yLeftHand,yRightHand, ratio, rightShoulder, leftShoulder, rightHip!!, rightAnkle)*/

          //Calculate whether the hand exceeds the shoulder
          val yRightHand = differenceBetweenCoordinates(rightWrist!!.position.y, rightShoulder!!.position.y)
          val yLeftHand = differenceBetweenCoordinates(leftWrist!!.position.y, leftShoulder!!.position.y)

          //Calculate whether the distance between the shoulder and the foot is the same width
          val ratio = ratio(leftShoulder.position.x, rightShoulder.position.x, leftAnkle!!.position.x, rightAnkle!!.position.x)

          val exerciseLogic = ExerciseLogic(rightHip!!,rightKnee!!,rightAnkle,yRightHand,yLeftHand,ratio,
            rightShoulder.position.y + leftShoulder.position.y, rightAnkle.position.y - rightHip.position.y, 5, 0, 0.5,
          2, 5,
            "Please stand up straight", "Please hold your hands behind your head", "Please spread your feet shoulder-width apart",
          null, null)

          doExerciseLogic(canvas, exerciseLogic)

          toDraw = !toDraw
        }
        "Push ups" -> {
         /* //Calculate whether the hand is in front of the shoulder
          val xRightHand = differenceBetweenCoordinates(rightWrist!!.position.x, rightShoulder!!.position.x)
          val xLeftHand = differenceBetweenCoordinates(leftWrist!!.position.x, leftShoulder!!.position.x)
          //Calculate whether the distance between the shoulder and the foot is the same width
          val shoulderDistance = differenceBetweenCoordinates(leftShoulder.position.x, rightShoulder.position.x)
          val footDistance = differenceBetweenCoordinates(leftAnkle!!.position.x, rightAnkle!!.position.x)
          val ratio = footDistance / shoulderDistance
          //angle of point 15-13-11( rightWrist, rightElbow, rightShoulder)
          val angleRwReRs = getAngle(rightWrist, rightElbow, rightShoulder)

          println("xRight $xRightHand")
          println()
          println("xLeft $xLeftHand")
          pushUpLogic(canvas,angleRwReRs,xLeftHand,xRightHand, ratio, rightShoulder, leftShoulder, rightWrist, rightElbow!!)*/

          //Calculate whether the hand is in front of the shoulder
          val xRightHand = differenceBetweenCoordinates(rightWrist!!.position.x, rightShoulder!!.position.x)
          val xLeftHand = differenceBetweenCoordinates(leftWrist!!.position.x, leftShoulder!!.position.x)

          val ratio = ratio(leftShoulder.position.x, rightShoulder.position.x, leftAnkle!!.position.x, rightAnkle!!.position.x)

          println(rightWrist.position.y)
          println(rightShoulder.position.y)
          val exerciseLogic = ExerciseLogic(rightWrist,rightElbow!!,rightShoulder,xRightHand,xLeftHand,ratio,
            rightShoulder.position.y ,rightShoulder.position.y - rightElbow.position.y /*rightShoulder.position.x - rightWrist.position.x*/ , 25, 0, 0.5,
            null, null,
            "Please keep in a push up position", "Please hold your hands straight out in front of your body ", "Please spread your feet shoulder-width apart",
            null, null)

          doExerciseLogic(canvas, exerciseLogic)
          toDraw = !toDraw

        }
        else -> {

        }
    }


    if(toDraw) {
      //draw points
      //left
      drawPoint(canvas, nose!!.position, whitePaint)
      drawPoint(canvas, lefyEyeInner!!.position, leftPaint)
      drawPoint(canvas, lefyEye!!.position, leftPaint)
      drawPoint(canvas, leftEyeOuter!!.position, leftPaint)
      drawPoint(canvas, leftEar!!.position, leftPaint)
      drawPoint(canvas, leftMouth!!.position, leftPaint)
      drawPoint(canvas, leftShoulder!!.position, leftPaint)
      drawPoint(canvas, leftElbow!!.position, leftPaint)
      drawPoint(canvas, leftWrist!!.position, leftPaint)
      drawPoint(canvas, leftHip!!.position, leftPaint)
      drawPoint(canvas, leftKnee!!.position, leftPaint)
      drawPoint(canvas, leftAnkle!!.position, leftPaint)
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
      drawPoint(canvas, rightShoulder!!.position, rightPaint)
      drawPoint(canvas, rightElbow!!.position, rightPaint)
      drawPoint(canvas, rightWrist!!.position, rightPaint)
      drawPoint(canvas, rightHip!!.position, rightPaint)
      drawPoint(canvas, rightKnee!!.position, rightPaint)
      drawPoint(canvas, rightAnkle!!.position, rightPaint)
      drawPoint(canvas, rightPinky!!.position, rightPaint)
      drawPoint(canvas, rightIndex!!.position, rightPaint)
      drawPoint(canvas, rightThumb!!.position, rightPaint)
      drawPoint(canvas, rightHeel!!.position, rightPaint)
      drawPoint(canvas, rightFootIndex!!.position, rightPaint)


      //draw lines between points
      // Face
      drawLine(canvas, nose.position, lefyEyeInner.position, whitePaint)
      drawLine(canvas, lefyEyeInner.position, lefyEye.position, whitePaint)
      drawLine(canvas, lefyEye.position, leftEyeOuter.position, whitePaint)
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
  }

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

  private fun drawPoint(canvas: Canvas, point: PointF?, paint: Paint?) {
    if (point == null) return

    canvas.drawCircle(
      translateX(point.x),
      translateY(point.y),
      DOT_RADIUS,
      paint!!
    )
  }

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

  private fun drawText(canvas: Canvas, text:String, line:Int) {
    if (TextUtils.isEmpty(text)) return

    canvas.drawText(text, TEXT_SIZE * 1.5f, TEXT_SIZE * 3*2 + TEXT_SIZE * line, tipPaint)
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
    tipPaint.color = Color.GREEN
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
  private fun doExerciseLogic(canvas: Canvas, exerciseLogic: ExerciseLogic){

    val angle = getAngle(exerciseLogic.firstPoint, exerciseLogic.midPoint, exerciseLogic.lastPoint)

    if (((180 - abs(angle)) > exerciseLogic.condOne) && !isCount) {
      reInitParams()
      lineOneText = exerciseLogic.lTextCondOne
    } else if (exerciseLogic.leftHandPos > 0 || exerciseLogic.rightHandPos > 0) {
      reInitParams()
      lineOneText = exerciseLogic.lTextCondTwo
    } else if (exerciseLogic.ratio < 0.5 && !isCount) {
      reInitParams()
      lineOneText = exerciseLogic.lTextCondThree
    } else {
      val currentHeight =
        if(exerciseLogic.condCurrentWeight != null) exerciseLogic.currentHeight / exerciseLogic.condCurrentWeight //Judging up and down by shoulder height
        else exerciseLogic.currentHeight

      if (!isCount) {
        shoulderHeight = currentHeight
        minSize = exerciseLogic.condMinSize?.let { exerciseLogic.minSize / it } ?: exerciseLogic.minSize
        isCount = true
        lastHeight = currentHeight
        lineOneText = "Gesture ready"
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
    drawText(canvas, lineOneText, 1)
    drawText(canvas, lineTwoText, 2)
    drawText(canvas, "count: $upCount", 3)
  }
  /**
   * Implements the squat exercise logic
   * */
/*  private fun squatLogic(canvas: Canvas, angle: Double, yLeftHand : Float, yRightHand: Float, ratio: Float, rightShoulder: PoseLandmark, leftShoulder : PoseLandmark, rightHip: PoseLandmark, rightAnkle : PoseLandmark){

      println("Angle ${abs(angle)}")
      if (((180 - abs(angle)) > 5) && !isCount) {
        reInitParams()
        lineOneText = "Please stand up straight"
      } else if (yLeftHand > 0 || yRightHand > 0) {
        reInitParams()
        lineOneText = "Please hold your hands behind your head"
      } else if (ratio < 0.5 && !isCount) {
        reInitParams()
        lineOneText = "Please spread your feet shoulder-width apart"
      } else {
        val currentHeight =
          (rightShoulder.position.y + leftShoulder.position.y) / 2 //Judging up and down by shoulder height

        if (!isCount) {
          shoulderHeight = currentHeight
          minSize = (rightAnkle.position.y - rightHip.position.y) / 5
          isCount = true
          lastHeight = currentHeight
          lineOneText = "Gesture ready"
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
      drawText(canvas, lineOneText, 1)
      drawText(canvas, lineTwoText, 2)
      drawText(canvas, "count: $upCount", 3)
     }*/

  /**
   * Implements the push-up exercise logic
   * */
 /* private fun pushUpLogic(canvas: Canvas, angle: Double, xLeftHand : Float, xRightHand: Float, ratio: Float, rightShoulder: PoseLandmark, leftShoulder : PoseLandmark, rightWrist: PoseLandmark,  rightElbow: PoseLandmark){

    println("Angle ${abs(angle)}")
    if (((180 - abs(angle)) > 25) && !isCount) {
      reInitParams()
      lineOneText = "Please keep in a plank position with the arms straight out"
    } else if (xLeftHand > 0 || xRightHand > 0) {
      reInitParams()
      lineOneText = "Please hold your hands straight out in front of your body "
    } else if (ratio < 0.5 && !isCount) {
      reInitParams()
      lineOneText = "Please spread your feet shoulder-width apart"
    } else {
      val currentHeight = rightElbow.position.y
        //(rightShoulder.position.x + leftShoulder.position.x) / 2 //Judging up and down by shoulder height

      if (!isCount) {
        shoulderHeight = currentHeight
        //println(rightWrist.position.y - rightShoulder.position.y)
        minSize = if (rightShoulder.position.y <= rightElbow.position.y) rightShoulder.position.y else rightElbow.position.y//(rightWrist.position.x - rightShoulder.position.x) / 2
        isCount = true
        lastHeight = currentHeight
        lineOneText = "Gesture ready"
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
    drawText(canvas, lineOneText, 1)
    drawText(canvas, lineTwoText, 2)
    drawText(canvas, "count: $upCount", 3)
  }*/
}