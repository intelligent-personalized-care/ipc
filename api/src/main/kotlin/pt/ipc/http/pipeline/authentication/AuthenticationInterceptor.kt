package pt.ipc.http.pipeline.authentication

import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import pt.ipc.domain.Role
import pt.ipc.domain.Unauthenticated
import pt.ipc.domain.Unauthorized
import pt.ipc.domain.User
import pt.ipc.http.controllers.ClientController
import pt.ipc.http.controllers.MonitorsController
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AuthenticationInterceptor(
    private val authorizationHeaderProcessor: AuthorizationHeaderProcessor
) : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (handler is HandlerMethod && handler.hasMethodAnnotation(Authentication::class.java)) {
            val cookies = request.cookies ?: throw Unauthenticated()
            val tokenCookie = cookies.find { it.name == "token" }

            val (user, role) = authorizationHeaderProcessor.process(tokenCookie) ?: throw Unauthenticated()

            return if (
                handler.method.declaringClass == ClientController::class.java && role != Role.CLIENT ||
                handler.method.declaringClass == MonitorsController::class.java && role != Role.MONITOR
            ) {
                throw Unauthorized()
            } else {
                if (handler.methodParameters.any { it.parameterType == User::class.java }) {
                    UserArgumentResolver.addUserTo(user, request)
                }
                true
            }
        }
        return true
    }
}
