package pt.ipc_app

import org.junit.Test
import pt.ipc_app.domain.user.User

class UserTests {

    private val user = User(
        name = "User Test",
        email = "usertest@gmail.com",
        password = "@Password1"
    )

    @Test(expected = IllegalArgumentException::class)
    fun `create instance with blank name throws`() {
        User(name = "", email = user.email, password = user.password)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `create instance with invalid email format throws`() {
        User(name = user.name, email = "abcdef", password = user.password)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `create instance with insecure password throws`() {
        User(name = user.name, email = user.email, password = "1234")
    }
}