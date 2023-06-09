package pt.ipc.http.pipeline.authentication

import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import pt.ipc.domain.User
import pt.ipc.domain.exceptions.Unauthenticated
import javax.servlet.http.HttpServletRequest

@Component
class UserArgumentResolver : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter) =
        parameter.parameterType == User::class.java

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {
        val request = webRequest.getNativeRequest(HttpServletRequest::class.java)
            ?: throw Unauthenticated
        return getUserFrom(request) ?: throw Unauthenticated
    }

    companion object {
        private const val KEY = "UserArgumentResolver"

        fun addUserTo(user: User, request: HttpServletRequest) =
            request.setAttribute(KEY, user)

        fun getUserFrom(request: HttpServletRequest): User? =
            request.getAttribute(KEY)?.let {
                it as? User
            }
    }
}
