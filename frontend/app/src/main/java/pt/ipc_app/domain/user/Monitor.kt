package pt.ipc_app.domain.user

/**
 * Represents the monitor.
 *
 * @property [name] the monitor's name
 */
class Monitor(
    name: String,
    email: String,
    password: String
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
        ): Monitor? =
            if (validateMonitor(name, email, password))
                Monitor(name, email, password)
            else null

        /**
         * Checks whether the received values are acceptable as [Monitor]
         * instance fields.
         */
        fun validateMonitor(
            name: String,
            email: String,
            password: String
        ) =
            validateUsername(name) && validateEmail(email) && validatePassword(password)
    }
}
