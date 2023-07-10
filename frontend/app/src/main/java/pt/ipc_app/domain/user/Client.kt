package pt.ipc_app.domain.user

/**
 * Represents the client.
 *
 * @property [name] the client's name
 */
class Client(
    name: String,
    email: String,
    password: String,
    val birthDate: String,
    val weight: Int,
    val height: Int,
    val physicalCondition: String
): User(name, email, password) {

    companion object {
        const val WEIGHT_LENGTH_MAX = 3
        const val HEIGHT_LENGTH_MAX = 3
        val PHYSICAL_CONDITION_LENGTH_RANGE = 3..255

        /**
         * Returns an [Client] instance with the received values or null, if those
         * values are invalid.
         */
        fun clientOrNull(
            name: String,
            email: String,
            password: String,
            birthDate: String,
            weight: Int,
            height: Int,
            physicalCondition: String
        ): Client? =
            if (validateClient(name, email, password, birthDate, weight, height, physicalCondition))
                Client(name, email, password, birthDate, weight, height, physicalCondition)
            else null

        /**
         * Checks whether the received values are acceptable as [Client]
         * instance fields.
         */
        private fun validateClient(
            name: String,
            email: String,
            password: String,
            birthDate: String,
            weight: Int,
            height: Int,
            physicalCondition: String
        ) =
            validateUsername(name) && validateEmail(email) && validatePassword(password)
    }
}
