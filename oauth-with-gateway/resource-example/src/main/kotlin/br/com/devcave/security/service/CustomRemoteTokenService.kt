package br.com.devcave.security.service

import br.com.devcave.security.client.AuthorizationServerClient
import org.slf4j.LoggerFactory
import org.springframework.security.oauth2.common.OAuth2AccessToken
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices

class CustomRemoteTokenService(
    private val authorizationServerClient: AuthorizationServerClient
) : ResourceServerTokenServices {

    private val logger = LoggerFactory.getLogger(javaClass)

    private val tokenConverter = DefaultAccessTokenConverter()

    override fun loadAuthentication(accessToken: String): OAuth2Authentication {
        return runCatching {
            authorizationServerClient.checkToken(accessToken)
        }.onFailure {
            logger.warn("something wrong validate token", it)
            throw InvalidTokenException("Token not allowed")
        }.getOrNull()?.let {
            tokenConverter.extractAuthentication(it)
        } ?: throw InvalidTokenException("Token not allowed")
    }

    override fun readAccessToken(accessToken: String): OAuth2AccessToken {
        throw UnsupportedOperationException("Not supported: read access token")
    }
}
