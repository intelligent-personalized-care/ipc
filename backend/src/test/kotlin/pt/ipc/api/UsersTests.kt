package pt.ipc.api

import org.jdbi.v3.core.Jdbi
import org.junit.jupiter.api.Test
import org.postgresql.ds.PGSimpleDataSource
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.util.UriComponentsBuilder
import pt.ipc.http.controllers.clients.models.RegisterClientInput
import pt.ipc.http.pipeline.exceptionHandler.Problem
import pt.ipc.http.utils.Uris
import pt.ipc.services.dtos.CredentialsOutput
import pt.ipc.services.dtos.RegisterInput
import pt.ipc.storage.repositories.jdbi.configure
import java.util.*
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UsersTests {

    @TestConfiguration
    @Component
    class TestConfig {
        @Bean
        @Primary
        fun testJdbi(): Jdbi = Jdbi.create(
            PGSimpleDataSource().apply {
                setURL("jdbc:postgresql://localhost:5432/testes?user=postgres&password=lsverao")
            }
        ).configure()
    }

    @LocalServerPort
    var port: Int = 0

    private fun registerClientInput(): RegisterClientInput {
        val uuid = UUID.randomUUID()
        return RegisterClientInput(name = uuid.toString(), email = "$uuid@gmail.com", password = "@Password12")
    }

    private fun registerInput(): RegisterInput {
        val uuid = UUID.randomUUID()
        return RegisterInput(name = uuid.toString(), email = "$uuid@gmail.com", password = "@Password12")
    }

    private val monitorID = "e4d09ca1-e010-4088-9bda-3ba127b4259e"
    private val monitorToken = "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VySUQiOiJlNGQwOWNhMS1lMDEwLTQwODgtOWJkYS0zYmExMjdiNDI1OWUiLCJyb2xlIjoiTU9OSVRPUiJ9.eVD9y5ESue0CAL9Pb5O4PU5kPGZ5mPOL0SpIBCuZwSA432eK1_L3w7J1xfGRZDLLWobU_hp3d0zMigqSeb1Utg"

    @Test
    fun `Create Client`() {
        val httpClient = WebTestClient.bindToServer().baseUrl("http://localhost:$port").build()

        val registerClientInput = RegisterClientInput(
            name = "Test123",
            email = UUID.randomUUID().toString() + "@gmail.com",
            password = "@Password1"
        )

        httpClient
            .post()
            .uri(Uris.CLIENT_REGISTER)
            .bodyValue(
                registerClientInput
            )
            .exchange()
            .expectStatus().isCreated
            .expectBody(CredentialsOutput::class.java)
    }

    @Test
    fun `Bad Email`() {
        val httpClient = WebTestClient.bindToServer().baseUrl("http://localhost:$port").build()

        val registerClientInput = RegisterClientInput(
            name = "Test",
            email = "bad email",
            password = "@Password1"
        )

        val result = httpClient
            .post()
            .uri(Uris.CLIENT_REGISTER)
            .bodyValue(
                registerClientInput
            )
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(Problem::class.java)
            .returnResult().responseBody

        assertEquals(result?.title, "Bad Email")
    }

    @Test
    fun `Bad Password`() {
        val httpClient = WebTestClient.bindToServer().baseUrl("http://localhost:$port").build()

        val uuid = UUID.randomUUID()

        val registerClientInput = RegisterClientInput(
            name = uuid.toString(),
            email = "$uuid@gmail.com",
            password = "bad password"
        )

        httpClient
            .post()
            .uri(Uris.CLIENT_REGISTER)
            .bodyValue(
                registerClientInput
            )
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(Problem::class.java) // Specify the expected response body type
    }

    @Test
    fun `create Same User`() {
        val registerClientInput = registerClientInput()

        val httpClient = WebTestClient.bindToServer().baseUrl("http://localhost:$port").build()

        httpClient.post()
            .uri(Uris.CLIENT_REGISTER)
            .bodyValue(registerClientInput)
            .exchange()
            .expectStatus().isCreated
            .expectBody(CredentialsOutput::class.java)

        httpClient.post()
            .uri(Uris.CLIENT_REGISTER)
            .bodyValue(registerClientInput)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(Problem::class.java)
    }

    @Test
    fun `create Monitor`() {
        val registerInput = registerInput()

        val httpClient = WebTestClient.bindToServer().baseUrl("http://localhost:$port").build()

        httpClient.post()
            .uri(Uris.MONITORS)
            .bodyValue(registerInput)
            .exchange()
            .expectStatus().isCreated
            .expectBody(CredentialsOutput::class.java)
    }

    @Test
    fun `create Same Monitor`() {
        val registerInput = registerInput()

        val httpClient = WebTestClient.bindToServer().baseUrl("http://localhost:$port").build()

        httpClient.post()
            .uri(Uris.MONITORS)
            .bodyValue(registerInput)
            .exchange()
            .expectStatus().isCreated
            .expectBody(CredentialsOutput::class.java)

        httpClient.post()
            .uri(Uris.MONITORS)
            .bodyValue(registerInput)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(Problem::class.java)
    }

    @Test
    fun `Try Operation Without being verified`() {
        val registerInput = registerInput()

        val httpClient = WebTestClient.bindToServer().baseUrl("http://localhost:$port").build()

        val credentialsOutput =
            httpClient.post()
                .uri(Uris.MONITORS)
                .bodyValue(registerInput)
                .exchange()
                .expectStatus().isCreated
                .expectBody(CredentialsOutput::class.java)
                .returnResult()
                .responseBody!!

        val uri = UriComponentsBuilder.fromPath(Uris.CLIENTS_OF_MONITOR)
            .buildAndExpand(credentialsOutput.id).toUriString()

        httpClient.post()
            .uri(uri)
            .bodyValue(registerInput)
            .header(HttpHeaders.AUTHORIZATION, "Bearer ${credentialsOutput.accessToken}")
            .exchange()
            .expectStatus().is4xxClientError
            .expectBody(Problem::class.java)
    }

    @Test
    fun `Try Requesting monitor and accepting`() {
        val registerClientInput = registerClientInput()

        val httpClient = WebTestClient.bindToServer().baseUrl("http://localhost:$port").build()

        val client = httpClient.post()
            .uri(Uris.CLIENT_REGISTER)
            .bodyValue(registerClientInput)
            .exchange()
            .expectStatus().isCreated
            .expectBody(CredentialsOutput::class.java)
            .returnResult()
            .responseBody!!
    }
}
