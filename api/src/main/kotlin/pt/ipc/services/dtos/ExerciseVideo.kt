package pt.ipc.services.dtos

import pt.ipc.domain.ExerciseInfo

data class ExerciseVideo(val exerciseInfo: ExerciseInfo, val exerciseVideo: ByteArray) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ExerciseVideo

        if (exerciseInfo != other.exerciseInfo) return false
        if (!exerciseVideo.contentEquals(other.exerciseVideo)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = exerciseInfo.hashCode()
        result = 31 * result + exerciseVideo.contentHashCode()
        return result
    }
}
