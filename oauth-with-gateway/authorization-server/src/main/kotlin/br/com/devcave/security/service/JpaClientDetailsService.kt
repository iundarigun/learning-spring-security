package br.com.devcave.security.service

import br.com.devcave.security.repository.ClientRepository
import org.slf4j.LoggerFactory
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.oauth2.provider.ClientDetails
import org.springframework.security.oauth2.provider.ClientDetailsService
import org.springframework.security.oauth2.provider.client.BaseClientDetails
import org.springframework.security.oauth2.provider.client.InMemoryClientDetailsService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class JpaClientDetailsService(
    private val clientRepository: ClientRepository
) : ClientDetailsService {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun loadClientByClientId(clientId: String): ClientDetails {
        logger.info("loading form database clientId=$clientId")
        val client = clientRepository.findByClientId(clientId).orElseThrow {
            UsernameNotFoundException("ClientId $clientId not found")
        }
        return BaseClientDetails(
            client.clientId, client.resources,
            client.scopes, client.authorizedGrantTypes, client.authorities
        ).also {
            it.clientSecret = client.clientSecret
            it.refreshTokenValiditySeconds = client.refreshTokenValiditySeconds
            it.accessTokenValiditySeconds = client.accessTokenValiditySeconds
        }
    }
}