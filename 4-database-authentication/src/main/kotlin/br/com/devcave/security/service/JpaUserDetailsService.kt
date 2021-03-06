package br.com.devcave.security.service

import br.com.devcave.security.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class JpaUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun loadUserByUsername(username: String): UserDetails {
        logger.info("loading from databse $username")
        val user = userRepository.findByUsername(username).orElseThrow {
            UsernameNotFoundException("Username $username not found")
        }
        return User(
            user.username,
            user.password,
            user.enabled,
            user.accountNonExpired,
            user.credentialsNonExpired,
            user.accountNonLocked,
            user.authorities.map { SimpleGrantedAuthority(it.role) }
        )
    }
}