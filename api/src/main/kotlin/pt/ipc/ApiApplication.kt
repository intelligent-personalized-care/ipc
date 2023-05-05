package pt.ipc

import org.jdbi.v3.core.Jdbi
import org.postgresql.ds.PGSimpleDataSource
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.FileSystemResource
import org.springframework.web.multipart.commons.CommonsMultipartResolver
import pt.ipc.storage.repositories.jdbi.configure
import java.io.File

@Configuration
class AppConfig {

    private val maxVideoSize: Long = 33_868_800
    private val maxInMemory: Int = 1

    @Bean
    fun multipartResolver(): CommonsMultipartResolver {
        val resolver = CommonsMultipartResolver()
        resolver.setDefaultEncoding("UTF-8")
        resolver.setResolveLazily(true)
        resolver.setMaxUploadSizePerFile(maxVideoSize) // 10 MB
        resolver.setMaxUploadSize(maxVideoSize) // 10 MB
        resolver.setMaxInMemorySize(maxInMemory)
        resolver.setUploadTempDir(FileSystemResource(File(System.getProperty("java.io.tmpdir"))))
        return resolver
    }
}

@SpringBootApplication(exclude = [SecurityAutoConfiguration::class])
class ApiApplication {

    @Bean
    fun jdbi(): Jdbi = Jdbi.create(
        PGSimpleDataSource().apply {
            setURL(System.getenv("postgresql_database"))
        }
    ).configure()
}


fun main(args: Array<String>) {
    runApplication<ApiApplication>(*args)
}
