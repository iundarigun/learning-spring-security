package br.com.devcave.security.listener

import br.com.devcave.security.entity.User
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent
import org.springframework.security.authentication.event.AuthenticationSuccessEvent
import org.springframework.stereotype.Component

@Component
class AuthenticationFailureListener {

    private val logger = LoggerFactory.getLogger(javaClass)

    @EventListener
    fun listen(event: AuthenticationFailureBadCredentialsEvent) {
        logger.info("user log in failure")
        if (event.source is UsernamePasswordAuthenticationToken &&
            (event.source as UsernamePasswordAuthenticationToken).principal is String) {
            val user = (event.source as UsernamePasswordAuthenticationToken).principal as String
            logger.info("attempt user log: $user")
        }
    }
}