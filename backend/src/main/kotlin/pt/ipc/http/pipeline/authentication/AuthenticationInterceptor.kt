package pt.ipc.http.pipeline.authentication

import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import pt.ipc.domain.User
import pt.ipc.domain.exceptions.ForbiddenRequest
import pt.ipc.domain.exceptions.Unauthenticated
import pt.ipc.http.controllers.admin.AdminController
import pt.ipc.http.controllers.clients.ClientsController
import pt.ipc.http.controllers.monitors.MonitorsController
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AuthenticationInterceptor(
    private val authorizationHeaderProcessor: AuthorizationHeaderProcessor
) : HandlerInterceptor {

    private val uuidRegex = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}"
    private val monitorCredentialRegex = "/users/monitors/$uuidRegex/credential".toRegex()
    private val monitorProfileRegex = "/users/monitors/$uuidRegex/profile".toRegex()
    private val subscribeURI = "/users/subscribe".toRegex()
    private val unsubscribeURI = "/users/subscribe".toRegex()

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (handler is HandlerMethod && handler.hasMethodAnnotation(Authentication::class.java)) {
            val authorizationValue = request.getHeader(NAME_AUTHORIZATION_HEADER)

            val (user, role) = authorizationHeaderProcessor.process(authorizationValue = authorizationValue) ?: throw Unauthenticated

            val uri = request.requestURI

            if (
                role.isMonitor() &&
                (
                    !(uri.matches(monitorCredentialRegex) && request.method == "POST") && // Inputting Credential
                        !(uri.matches(monitorProfileRegex) && request.method == "GET") &&
                        !(uri.matches(subscribeURI) && request.method == "GET") &&
                        !(uri.matches(unsubscribeURI) && request.method == "POST")
                    )
            ) {
                authorizationHeaderProcessor.checkIfMonitorIsVerified(monitorID = user.id)
            }

            if (
                handler.method.declaringClass == ClientsController::class.java && role.notClient() ||
                handler.method.declaringClass == MonitorsController::class.java && role.notMonitor() ||
                handler.method.declaringClass == AdminController::class.java && role.notAdmin()
            ) {
                throw ForbiddenRequest
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
