package br.com.devcave.security.config.google

import br.com.devcave.security.entity.User
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.boot.autoconfigure.security.servlet.StaticResourceRequest
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationTrustResolverImpl
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.stereotype.Component
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class Google2faFilter : GenericFilterBean() {

    private val authenticationTrustResolver = AuthenticationTrustResolverImpl()

    private val urlIs2fa = AntPathRequestMatcher("/user/verify2fa")

    override fun doFilter(servletRequest: ServletRequest, servletResponse: ServletResponse, chain: FilterChain) {
        val request =  servletRequest as HttpServletRequest
        val response = servletResponse as HttpServletResponse

        val matcher = PathRequest.toStaticResources().atCommonLocations()

        if (urlIs2fa.matches(request) || matcher.matcher(request).isMatch){
            chain.doFilter(request, response)
            return
        }

        val authentication: Authentication? = SecurityContextHolder.getContext().authentication
        if (authentication != null && authenticationTrustResolver.isAnonymous(authentication)){
            if (authentication.principal != null && authentication.principal is User) {
                val user = authentication.principal as User

                if (user.userGoogle2FAEnabled && user.userGoogle2FARequired) {
                    response.sendError(
                        HttpStatus.UNAUTHORIZED.value(),
                        HttpStatus.UNAUTHORIZED.reasonPhrase
                    )
                    return
                }
            }
        }
        chain.doFilter(request, response)
    }
}