package br.com.devcave.security.config.security

import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.util.matcher.RequestMatcher
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class RestHeaderAuthFilter(
    requiresAuthenticationRequestMatcher: RequestMatcher
) : AbstractAuthenticationProcessingFilter(requiresAuthenticationRequestMatcher) {

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication? {
        val username = request.getHeader("Api-Key") ?: ""
        val password = request.getHeader("Api-Secret") ?: ""

        logger.info("attemptAuthentication, user=$username, pass=$password")

        val token = UsernamePasswordAuthenticationToken(username, password)
        return if (username.isNotEmpty()) {
            this.authenticationManager.authenticate(token)
        } else {
            null
        }
    }

    override fun doFilter(req: ServletRequest, res: ServletResponse, chain: FilterChain) {
        val request = req as HttpServletRequest
        val response = res as HttpServletResponse

        if (logger.isDebugEnabled) {
            logger.debug("Request is to process authentication")
        }

         kotlin.runCatching {
            attemptAuthentication(request, response)
        }.onFailure {
             when (it){
                 is AuthenticationException -> unsuccessfulAuthentication(request, response, it)
                 else -> throw it
             }
        }.onSuccess {
            it?.let {
                successfulAuthentication(request, response, chain, it)
            } ?: chain.doFilter(request, response)
        }
    }

    override fun unsuccessfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        failed: AuthenticationException
    ) {
        SecurityContextHolder.clearContext()

        if (logger.isDebugEnabled) {
            logger.debug("Authentication request failed: $failed", failed)
            logger.debug("Updated SecurityContextHolder to contain null Authentication")
        }
        response.sendError(
            HttpStatus.UNAUTHORIZED.value(),
            HttpStatus.UNAUTHORIZED.reasonPhrase
        )
    }

    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authResult: Authentication
    ) {
        SecurityContextHolder.getContext().authentication = authResult
    }
}