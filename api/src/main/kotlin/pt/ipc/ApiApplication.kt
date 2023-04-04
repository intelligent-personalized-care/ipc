package pt.ipc

import org.jdbi.v3.core.Jdbi
import org.postgresql.ds.PGSimpleDataSource
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import pt.ipc.repositories.jdbi.configure

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
	runApplication<ApiApplication>(*args)
}
