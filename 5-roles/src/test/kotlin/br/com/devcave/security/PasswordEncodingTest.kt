package br.com.devcave.security

import br.com.devcave.security.config.security.PasswordEncoderFactories
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.LdapShaPasswordEncoder
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.StandardPasswordEncoder
import org.springframework.util.DigestUtils

class PasswordEncodingTest {
    companion object {
        const val PASSWORD = "supersecret"
    }
    @Test
    fun delegatingEncodeTest(){
        val encode = PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(PASSWORD)
        Assertions.assertTrue(encode.startsWith("{bcrypt15}"))
    }

    @Test
    fun bcryptTest() {
        val bcrypt = BCryptPasswordEncoder()
        val encode1 = bcrypt.encode(PASSWORD)
        val encode2 = bcrypt.encode(PASSWORD)

        Assertions.assertNotEquals(encode1, encode2, "change all the time")
    }

    @Test
    fun sha256Test() {
        val sha = StandardPasswordEncoder()
        val encode1 = sha.encode(PASSWORD)
        val encode2 = sha.encode(PASSWORD)

        Assertions.assertNotEquals(encode1, encode2, "change all the time")
    }

    @Test
    fun ldapTest() {
        val ldap = LdapShaPasswordEncoder()
        val encode1 = ldap.encode(PASSWORD)
        val encode2 = ldap.encode(PASSWORD)

        Assertions.assertNotEquals(encode1, encode2, "change all the time")
        Assertions.assertTrue(ldap.matches(PASSWORD, encode1))
        Assertions.assertTrue(ldap.matches(PASSWORD, encode2))
    }

    @Test
    fun noopTest() {
        val noop = NoOpPasswordEncoder.getInstance()
        Assertions.assertEquals(PASSWORD, noop.encode(PASSWORD))
    }

    @Test
    fun hashingTest() {
        val encode1 = DigestUtils.md5DigestAsHex(PASSWORD.toByteArray())
        val encode2 = DigestUtils.md5DigestAsHex(PASSWORD.toByteArray())
        Assertions.assertEquals(encode1, encode2, "Hash is always the same")
    }
}