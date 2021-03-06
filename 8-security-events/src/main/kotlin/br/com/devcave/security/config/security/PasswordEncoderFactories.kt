package br.com.devcave.security.config.security

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.DelegatingPasswordEncoder
import org.springframework.security.crypto.password.LdapShaPasswordEncoder
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.StandardPasswordEncoder

object PasswordEncoderFactories {
    fun createDelegatingPasswordEncoder(): DelegatingPasswordEncoder {
        val encoders =
            mapOf(
                "bcrypt15" to BCryptPasswordEncoder(15),
                "bcrypt" to BCryptPasswordEncoder(),
                "ldap" to LdapShaPasswordEncoder(),
                "noop" to NoOpPasswordEncoder.getInstance(),
                "sha256" to StandardPasswordEncoder()
            )

        return DelegatingPasswordEncoder("bcrypt15", encoders)
    }
}