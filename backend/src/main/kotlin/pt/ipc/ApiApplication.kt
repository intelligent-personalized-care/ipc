package pt.ipc

import com.zaxxer.hikari.HikariDataSource
import org.jdbi.v3.core.Jdbi
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.FileSystemResource
import org.springframework.web.multipart.commons.CommonsMultipartResolver
import pt.ipc.storage.repositories.jdbi.configure
import java.io.File
import java.util.*

@Configuration
class AppConfig {

    private val maxVideoSize: Long = -1
    private val maxInMemory: Int = -1

    @Bean
    fun multipartResolver(): CommonsMultipartResolver {
        val resolver = CommonsMultipartResolver()
        resolver.setDefaultEncoding("UTF-8")
        resolver.setResolveLazily(true)
        resolver.setMaxUploadSizePerFile(maxVideoSize)
        resolver.setMaxUploadSize(maxVideoSize)
        resolver.setMaxInMemorySize(maxInMemory)
        resolver.setUploadTempDir(FileSystemResource(File(System.getProperty("java.io.tmpdir"))))
        return resolver
    }
}

@SpringBootApplication(exclude = [SecurityAutoConfiguration::class])
class ApiApplication {

    @Bean
    fun jdbi(): Jdbi {
        val jdbcURL = System.getenv("jdbcURL")
        val connProps = Properties()

        connProps.setProperty("sslmode", "disable")
        connProps.setProperty("socketFactory", "com.google.cloud.sql.postgres.SocketFactory")
        connProps.setProperty("cloudSqlInstance", System.getenv("cloudSqlInstance"))

        val dataSource = HikariDataSource()
        dataSource.jdbcUrl = jdbcURL
        dataSource.username = System.getenv("postgresql_username")
        dataSource.password = System.getenv("postgresql_password")
        dataSource.dataSourceProperties = connProps

        return Jdbi.create(dataSource).configure()
    }
}

fun main(args: Array<String>) {
    runApplication<ApiApplication>(*args)
}
