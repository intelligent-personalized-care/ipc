package pt.ipc

import org.jdbi.v3.core.Jdbi
import org.postgresql.ds.PGSimpleDataSource
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.FileSystemResource
import org.springframework.web.multipart.commons.CommonsMultipartResolver
import pt.ipc.database_storage.cloudStorageUtils.CloudStorageConfiguration
import pt.ipc.database_storage.cloudStorageUtils.CloudStorageUtilsImpl
import pt.ipc.database_storage.repositories.jdbi.configure
import java.io.File
import java.util.*

@Configuration
class AppConfig {

	private val maxVideoSize : Long = 33_868_800
	private val maxInMemory : Int = 1_048_576

	@Bean
	fun multipartResolver(): CommonsMultipartResolver {
		val resolver = CommonsMultipartResolver()
		resolver.setDefaultEncoding("UTF-8")
		resolver.setResolveLazily(true)
		resolver.setMaxUploadSizePerFile(maxVideoSize) // 10 MB
		resolver.setMaxUploadSize(maxVideoSize) // 10 MB
		resolver.setMaxInMemorySize(maxInMemory) // 1 MB
		resolver.setUploadTempDir(FileSystemResource(File(System.getProperty("java.io.tmpdir"))))
		return resolver
	}
}


@SpringBootApplication
class ApiApplication{

	@Bean
	fun jdbi(): Jdbi = Jdbi.create(
		PGSimpleDataSource().apply {
			setURL(System.getenv("postgresql_database"))
		}
	).configure()
}


fun main(args: Array<String>) {

	val cl = CloudStorageUtilsImpl(CloudStorageConfiguration("intelligente-personalized-care"))
	cl.deleteWithID(UUID.fromString("35d8b08e-05ff-46f1-afef-e77a6f962455"))

	//runApplication<ApiApplication>(*args)
}



