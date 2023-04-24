package pt.ipc.http.pipeline.authentication

import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import pt.ipc.domain.*
import pt.ipc.http.controllers.ClientController
import pt.ipc.http.controllers.MonitorsController
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AuthenticationInterceptor(
    private val authorizationHeaderProcessor : AuthorizationHeaderProcessor,
)  : HandlerInterceptor{

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean{
        if(
            (handler is HandlerMethod) &&
            (
                    handler.hasMethodAnnotation(Authentication::class.java) || handler.method.declaringClass.isAnnotationPresent(Authentication::class.java)
                    )
        ) {
            val cookies = request.cookies ?: throw Unauthenticated()

            val tokenCookie = cookies.find { it.name == "token" }

            val (user, userRole) = authorizationHeaderProcessor.process(tokenCookie) ?: throw Unauthenticated()

            val necessaryRole = handler.method.getAnnotation(Authentication::class.java) ?: throw Exception("Isto é um erro de backend")

            if (necessaryRole.role != userRole) throw Unauthorized()

            if (handler.methodParameters.any { it.parameterType == User::class.java }) UserArgumentResolver.addUserTo(user, request)

        }
        return true
    }

}