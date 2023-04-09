package pt.ipc.http.pipeline.authentication

import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import pt.ipc.domain.Unauthorized
import pt.ipc.domain.Client
import pt.ipc.domain.User
import javax.servlet.http.HttpServletRequest

@Component
class UserArgumentResolver: HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter) =
        parameter.parameterType == Client::class.java

    override fun resolveArgument(parameter: MethodParameter, mavContainer: ModelAndViewContainer?,
                                 webRequest: NativeWebRequest, binderFactory: WebDataBinderFactory?): Any? {

        val request = webRequest.getNativeRequest(HttpServletRequest::class.java)
            ?: throw Unauthorized()
        return getUserFrom(request) ?: throw Unauthorized()
    }

    companion object {
        private const val KEY = "UserArgumentResolver"

        fun addUserTo(client: User, request: HttpServletRequest) =
            request.setAttribute(KEY, client)

        fun getUserFrom(request: HttpServletRequest): User? =
            request.getAttribute(KEY)?.let {
                it as? User
            }
    }
}
