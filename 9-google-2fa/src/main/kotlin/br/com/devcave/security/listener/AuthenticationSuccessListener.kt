package br.com.devcave.security.listener

import br.com.devcave.security.entity.User
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.event.AuthenticationSuccessEvent
import org.springframework.stereotype.Component

@Component
class AuthenticationSuccessListener {

    private val logger = LoggerFactory.getLogger(javaClass)

    @EventListener
    fun listen(event: AuthenticationSuccessEvent) {
        logger.info("user log in is Okay")
        if (event.source is UsernamePasswordAuthenticationToken &&
            (event.source as UsernamePasswordAuthenticationToken).principal is User) {
            val user = (event.source as UsernamePasswordAuthenticationToken).principal as User
            logger.info("user logger : ${user.username}")
        }
    }
}