package pt.ipc.api.http.pipeline

import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import pt.ipc.domain.User
import javax.servlet.http.HttpServletRequest

@Component
class UserArgumentResolver: HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter) =
        parameter.parameterType == User::class.java

    override fun resolveArgument(parameter: MethodParameter, mavContainer: ModelAndViewContainer?,
                                 webRequest: NativeWebRequest, binderFactory: WebDataBinderFactory?): Any? {

        val request = webRequest.getNativeRequest(HttpServletRequest::class.java)
            ?: throw IllegalStateException() // TODO(gave an Exception name)
        return getUserFrom(request) ?: throw IllegalStateException() // TODO(gave an Exception name)
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
