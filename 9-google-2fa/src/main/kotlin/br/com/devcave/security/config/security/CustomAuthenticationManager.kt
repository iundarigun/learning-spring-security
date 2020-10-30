package br.com.devcave.security.config.security

import br.com.devcave.security.entity.User
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

@Component
class CustomAuthenticationManager {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun customerIdMatches(authentication: Authentication, customerId: Long): Boolean {
        val user = authentication.principal as User
        logger.info("Auth user customerId=${user.customer.id} and customerId=$customerId")
        return user.customer.id == customerId
    }
}