package pt.ipc_app.domain.user

/**
 * Represents the monitor.
 *
 * @property [name] the monitor's name
 */
class Monitor(
    name: String,
    email: String,
    password: String,
    val credential: ByteArray?,
    val title: String? = null,
    val stars: Float? = 0f
): User(name, email, password) {

    companion object {

        /**
         * Returns an [Monitor] instance with the received values or null, if those
         * values are invalid.
         */
        fun monitorOrNull(
            name: String,
            email: String,
            password: String,
            credential: ByteArray
        ): Monitor? =
            if (validateMonitor(name, email, password, credential))
                Monitor(name, email, password, credential)
            else null

        /**
         * Checks whether the received values are acceptable as [Monitor]
         * instance fields.
         */
        fun validateMonitor(
            name: String,
            email: String,
            password: String,
            credential: ByteArray
        ) =
            validateUsername(name) && validateEmail(email) && validatePassword(password)
    }
}
