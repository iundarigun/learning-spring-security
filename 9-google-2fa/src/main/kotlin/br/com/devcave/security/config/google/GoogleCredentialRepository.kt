package br.com.devcave.security.config.google

import br.com.devcave.security.repository.UserRepository
import com.warrenstrange.googleauth.ICredentialRepository
import org.springframework.stereotype.Component

@Component
class GoogleCredentialRepository(
    private val userRepository: UserRepository
) : ICredentialRepository {

    override fun saveUserCredentials(
        userName: String,
        secretKey: String,
        validationCode: Int,
        scratchCodes: MutableList<Int>?
    ) {
        userRepository.findByUsername(userName).orElseThrow().let{
            user ->
            user.google2FASecret = secretKey
            user.userGoogle2FAEnabled = true
            userRepository.save(user)
        }
    }

    override fun getSecretKey(userName: String): String {
        return userRepository.findByUsername(userName)
            .orElseThrow()
            .google2FASecret ?: throw RuntimeException()
    }
}