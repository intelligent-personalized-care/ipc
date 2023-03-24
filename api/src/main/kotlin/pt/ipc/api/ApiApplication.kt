package pt.ipc.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import pt.ipc.api.http.pipeline.AuthenticationInterceptor
import pt.ipc.api.http.pipeline.UserArgumentResolver

@SpringBootApplication
class ApiApplication(
	val authInterceptor: AuthenticationInterceptor,
	val userArgumentResolver: UserArgumentResolver
) : WebMvcConfigurer {

	override fun addInterceptors(registry: InterceptorRegistry) {
		registry.addInterceptor(authInterceptor)
	}

	override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
		resolvers.add(userArgumentResolver)
	}

	override fun addCorsMappings(registry: CorsRegistry) {
		registry.addMapping("/**")
			.allowCredentials(true)
			.allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
			.allowedOrigins("http://localhost")
	}
}

fun main(args: Array<String>) {
	runApplication<ApiApplication>(*args)
}
