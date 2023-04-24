package pt.ipc_app.domain

import java.util.*

/**
 * Represents the user information.
 *
 * @property [name] the user's name
 */
data class UserInfo(
    val name: String,
    val email: String,
    val password: String,
    val birthDate: String,
    val weight: Int,
    val height: Int,
    val physicalCondition: String
) {
    init {
        require(validateUserInfo(name, email, password, birthDate, weight, height, physicalCondition))
    }

    companion object {
        val NAME_LENGTH_RANGE = 5..30

        private const val PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}\$"
        private const val EMAIL_REGEX = "^[A-Za-z\\d+_.-]+@(.+)$"

        /**
         * Returns an [UserInfo] instance with the received values or null, if those
         * values are invalid.
         */
        fun userInfoOrNull(
            name: String,
            email: String,
            password: String,
            birthDate: String,
            weight: Int,
            height: Int,
            physicalCondition: String
        ): UserInfo? =
            if (validateUserInfo(name, email, password, birthDate, weight, height, physicalCondition))
                UserInfo(name, email, password, birthDate, weight, height, physicalCondition)
            else null

        /**
         * Checks whether the received values are acceptable as [UserInfo]
         * instance fields.
         */
        fun validateUserInfo(
            name: String,
            email: String,
            password: String,
            birthDate: String,
            weight: Int,
            height: Int,
            physicalCondition: String
        ) =
            validateUsername(name) && validateEmail(email) && validatePassword(password)

        /**
         * Validates the username.
         */
        fun validateUsername(username: String): Boolean =
            username.length in NAME_LENGTH_RANGE

        /**
         * Validates the email.
         */
        fun validateEmail(email: String): Boolean =
            email.matches(Regex(EMAIL_REGEX))

        /**
         * Validates the password.
         */
        fun validatePassword(password: String): Boolean =
            password.matches(Regex(PASSWORD_REGEX))
    }
}