package br.com.devcave.security.config

import br.com.devcave.security.service.JpaClientDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer
import org.springframework.security.oauth2.provider.ClientDetailsService
import org.springframework.security.oauth2.provider.token.DefaultTokenServices
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore


@Configuration
class OAuthServerConfig(
    private val authenticationManager: AuthenticationManager,
    private val jpaClientDetailsService: JpaClientDetailsService,
    private val passwordEncoder: PasswordEncoder
) : AuthorizationServerConfigurerAdapter() {

    override fun configure(clients: ClientDetailsServiceConfigurer) {
        clients.withClientDetails(jpaClientDetailsService)
    }

////    @Bean
//    fun tokenStore(): TokenStore {
//        return InMemoryTokenStore()
//    }
//
//    @Bean
//    fun tokenServices(clientDetailsService: ClientDetailsService): DefaultTokenServices {
//        val tokenServices = DefaultTokenServices()
//        tokenServices.setSupportRefreshToken(true)
//        tokenServices.setTokenStore(tokenStore())
//        tokenServices.setClientDetailsService(clientDetailsService)
//        tokenServices.setAuthenticationManager(authenticationManager)
//        return tokenServices
//    }
//
    override fun configure(endpoints: AuthorizationServerEndpointsConfigurer) {
        endpoints.authenticationManager(authenticationManager)
//               .tokenStore(tokenStore())
    }
//
//    override fun configure(oauthServer: AuthorizationServerSecurityConfigurer) {
//        oauthServer.passwordEncoder(passwordEncoder)
//            .tokenKeyAccess("permitAll()")
//            .checkTokenAccess("isAuthenticated()")
//    }

}