package pt.ipc.services.users.dtos


data class RegisterMonitorInput(val email : String, val name : String, val password : String, val credential : ByteArray) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RegisterMonitorInput

        if (email != other.email) return false
        if (name != other.name) return false
        if (password != other.password) return false
        if (!credential.contentEquals(other.credential)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = email.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + password.hashCode()
        result = 31 * result + credential.contentHashCode()
        return result
    }

}