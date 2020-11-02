package br.com.devcave.security.config

import br.com.devcave.security.client.AuthorizationServerClient
import br.com.devcave.security.service.CustomRemoteTokenService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.common.OAuth2AccessToken
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager
import org.springframework.security.oauth2.provider.authentication.TokenExtractor
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken

@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
class ResourceServerRemoteTokenConfiguration(
    private val authorizationServerClient: AuthorizationServerClient
) : ResourceServerConfigurerAdapter() {
    override fun configure(resources: ResourceServerSecurityConfigurer) {
        resources.resourceId("resource-example")
            .authenticationManager(authenticationManagerBean())
            .tokenExtractor(tokenExtractor())
    }

    private fun tokenExtractor(): TokenExtractor {
        return TokenExtractor { request ->
            var preAuthenticatedAuthenticationToken: PreAuthenticatedAuthenticationToken? = null
            val headers = request.getHeaders("Authorization")
            while (headers.hasMoreElements()) { // typically there is only one (most servers enforce that)
                val value = headers.nextElement()
                if (value.toLowerCase().startsWith(OAuth2AccessToken.BEARER_TYPE.toLowerCase())) {
                    var authHeaderValue = value.substring(OAuth2AccessToken.BEARER_TYPE.length).trim()
                    request.setAttribute(
                        OAuth2AuthenticationDetails.ACCESS_TOKEN_TYPE,
                        value.substring(0, OAuth2AccessToken.BEARER_TYPE.length).trim()
                    )
                    val commaIndex = authHeaderValue.indexOf(',')
                    if (commaIndex > 0) {
                        authHeaderValue = authHeaderValue.substring(0, commaIndex)
                    }
                    preAuthenticatedAuthenticationToken = PreAuthenticatedAuthenticationToken(authHeaderValue, "")
                    break
                }
            }
            preAuthenticatedAuthenticationToken
        }
    }

    @Bean
    fun tokenService(): ResourceServerTokenServices {
        return CustomRemoteTokenService(authorizationServerClient)
    }

    @Bean
    @Throws(Exception::class)
    fun authenticationManagerBean(): AuthenticationManager? {
        val authenticationManager = OAuth2AuthenticationManager()
        authenticationManager.setTokenServices(tokenService())
        return authenticationManager
    }

    override fun configure(http: HttpSecurity) {
        http.authorizeRequests()
            .anyRequest().authenticated()
            .and().cors()
            .and().csrf().disable()
            .formLogin().disable()
            .httpBasic().disable()
    }
}