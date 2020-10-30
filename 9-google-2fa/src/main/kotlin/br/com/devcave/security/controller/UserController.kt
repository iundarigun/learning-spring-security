package br.com.devcave.security.controller

import br.com.devcave.security.entity.User
import br.com.devcave.security.repository.UserRepository
import com.warrenstrange.googleauth.GoogleAuthenticator
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("user")
class UserController(
    private val userRepository: UserRepository,
    private val googleAuthenticator: GoogleAuthenticator
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping("register2fa")
    fun register2fa(): String {
        val user = getUser()

        return GoogleAuthenticatorQRGenerator.getOtpAuthURL(
            "devcave", user.username, googleAuthenticator.createCredentials(user.username)
        )
    }

    @PostMapping("register2fa")
    fun confirm2fa(@RequestParam verifyCode: Int): ResponseEntity<Any?> {
        logger.info("verify code = $verifyCode")
        val user = getUser()
        return if (googleAuthenticator.authorizeUser(user.username, verifyCode)) {
            userRepository.findById(user.id).orElseThrow().also {
                it.userGoogle2FAEnabled = true
                userRepository.save(it)
            }
            ResponseEntity.ok("OK")
        } else {
            ResponseEntity.badRequest().build()
        }
    }

    @PostMapping("verify2fa")
    fun verify2fa(@RequestParam verifyCode: Int): ResponseEntity<Any?> {
        logger.info("verify code = $verifyCode")
        val user = getUser()
        return if (googleAuthenticator.authorizeUser(user.username, verifyCode)) {
            ResponseEntity.ok("OK")
        } else {
            ResponseEntity.badRequest().build()
        }
    }

    private fun getUser(): User =
        SecurityContextHolder.getContext().authentication.principal as User

}