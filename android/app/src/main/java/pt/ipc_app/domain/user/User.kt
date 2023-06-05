package pt.ipc_app.domain.user

/**
 * Represents the user.
 *
 * @property [name] the user's name
 */
open class User(
    val name: String,
    val email: String,
    val password: String
) {
    init {
        require(validateUser(name, email, password))
    }

    companion object {
        val NAME_LENGTH_RANGE = 3..30
        val EMAIL_LENGTH_RANGE = 5..255

        private const val PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}\$"
        private const val EMAIL_REGEX = "^[A-Za-z\\d+_.-]+@(.+)$"

        /**
         * Returns an [User] instance with the received values or null, if those
         * values are invalid.
         */
        fun userOrNull(
            name: String,
            email: String,
            password: String
        ): User? =
            if (validateUser(name, email, password))
                User(name, email, password)
            else null

        /**
         * Checks whether the received values are acceptable as [User]
         * instance fields.
         */
        fun validateUser(
            name: String,
            email: String,
            password: String
        ) = validateUsername(name) && validateEmail(email) && validatePassword(password)

        /**
         * Validates the username.
         */
        fun validateUsername(username: String): Boolean =
            username.length in NAME_LENGTH_RANGE

        /**
         * Validates the email.
         */
        fun validateEmail(email: String): Boolean =
            email.matches(Regex(EMAIL_REGEX)) && email.length in EMAIL_LENGTH_RANGE

        /**
         * Validates the password.
         */
        fun validatePassword(password: String): Boolean =
            password.matches(Regex(PASSWORD_REGEX))
    }
}
