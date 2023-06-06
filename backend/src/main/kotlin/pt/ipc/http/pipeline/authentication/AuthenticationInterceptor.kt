package pt.ipc.http.pipeline.authentication

import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import pt.ipc.domain.Role
import pt.ipc.domain.User
import pt.ipc.domain.exceptions.Unauthenticated
import pt.ipc.domain.exceptions.Unauthorized
import pt.ipc.http.controllers.ClientsController
import pt.ipc.http.controllers.MonitorsController
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AuthenticationInterceptor(
    private val authorizationHeaderProcessor: AuthorizationHeaderProcessor
) : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (handler is HandlerMethod && handler.hasMethodAnnotation(Authentication::class.java)) {
            val authorizationValue = request.getHeader(NAME_AUTHORIZATION_HEADER)

            val (user, role) = authorizationHeaderProcessor.process(authorizationValue = authorizationValue) ?: throw Unauthenticated

            if (
                handler.method.declaringClass == ClientsController::class.java && role != Role.CLIENT ||
                handler.method.declaringClass == MonitorsController::class.java && role != Role.MONITOR
            ) {
                throw Unauthorized
            } else {
                if (handler.methodParameters.any { it.parameterType == User::class.java }) {
                    UserArgumentResolver.addUserTo(user, request)
                }
                return true
            }
        }
        return true
    }
    companion object {
        private const val NAME_AUTHORIZATION_HEADER = "Authorization"
    }
}
