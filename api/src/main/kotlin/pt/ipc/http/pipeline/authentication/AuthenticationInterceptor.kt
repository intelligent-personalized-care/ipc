package pt.ipc.http.pipeline.authentication

import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import pt.ipc.domain.Unauthorized
import pt.ipc.domain.Client
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AuthenticationInterceptor(
    private val authorizationHeaderProcessor : AuthorizationHeaderProcessor
)  : HandlerInterceptor{

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean{
        if(
            (handler is HandlerMethod) &&
            (
                    handler.hasMethodAnnotation(Authentication::class.java) || handler.method.declaringClass.isAnnotationPresent(
                        Authentication::class.java)
                    )
        ){
            val cookies = request.cookies ?: throw Unauthorized()
            val tokenCookie = cookies.find { it.name == "token" }

            val user = authorizationHeaderProcessor.process(tokenCookie)

            return if(user == null){
                throw Unauthorized()
            }else{
                if(handler.methodParameters.any { it.parameterType == Client::class.java }){
                    UserArgumentResolver.addUserTo(user, request)
                }
                true
            }
        }
        return true
    }

}