package br.com.devcave.security.config

import br.com.devcave.security.entity.Authority
import br.com.devcave.security.entity.User
import br.com.devcave.security.repository.AuthorityRepository
import br.com.devcave.security.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class UserDataLoader(
    private val userRepository: UserRepository,
    private val authorityRepository: AuthorityRepository,
    private val passwordEncoder: PasswordEncoder
) : CommandLineRunner {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun run(vararg args: String?) {
        if (authorityRepository.count() == 0L) {
            loadSecurityData()
        }
    }

    private fun loadSecurityData() {
        val admin = authorityRepository.save(
            Authority(role = "ADMIN")
        )
        val userRole = authorityRepository.save(
            Authority(role = "USER")
        )
        val customer = authorityRepository.save(
            Authority(role = "CUSTOMER")
        )

        userRepository.save(
            User(
                username = "admin",
                password = passwordEncoder.encode("supersecret"),
                authorities = setOf(admin)
            )
        )
        userRepository.save(
            User(
                username = "random",
                password = passwordEncoder.encode("secretrandom"),
                authorities = setOf(userRole)
            )
        )
        userRepository.save(
            User(
                username = "scott",
                password = passwordEncoder.encode("tiger"),
                authorities = setOf(customer)
            )
        )
        logger.info("persist user total= ${userRepository.count()}")
    }
}