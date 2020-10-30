package br.com.devcave.security.config

import br.com.devcave.security.config.google.Google2faFilter
import br.com.devcave.security.config.google.GoogleCredentialRepository
import br.com.devcave.security.config.security.PasswordEncoderFactories
import com.warrenstrange.googleauth.GoogleAuthenticator
import com.warrenstrange.googleauth.GoogleAuthenticatorConfig
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationEventPublisher
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension
import org.springframework.security.web.session.SessionManagementFilter
import org.springframework.util.StringUtils
import java.util.concurrent.TimeUnit

@Configuration
@EnableWebSecurity
// securedEnabled able to use @Secured and prePostEnabled able to use @PreAuthorize
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
class SecurityConfig(
    private val google2faFilter: Google2faFilter
) : WebSecurityConfigurerAdapter() {

    @Bean
    fun googleAuthenticator(credentialRepository: GoogleCredentialRepository): GoogleAuthenticator {
        val configBuilder = GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder()

        configBuilder.setTimeStepSizeInMillis(TimeUnit.SECONDS.toMillis(60))
            .setWindowSize(10)
            .setNumberOfScratchCodes(0)

        return GoogleAuthenticator(configBuilder.build()).also {
            it.credentialRepository = credentialRepository
        }
    }

    // This bean able to use Spring Data JPA Spel security features
    @Bean
    fun securityEvaluationContextExtension(): SecurityEvaluationContextExtension {
        return SecurityEvaluationContextExtension()
    }

    @Bean
    fun authenticationEventPublisher(
        applicationEventPublisher: ApplicationEventPublisher
    ): AuthenticationEventPublisher {
        return DefaultAuthenticationEventPublisher(applicationEventPublisher)
    }

    override fun configure(http: HttpSecurity) {
        http.addFilterBefore(google2faFilter, SessionManagementFilter::class.java)

        http
            // This auth declaration must be before than authenicated to work
            .authorizeRequests {
                it.antMatchers("/no-auth/").permitAll()
                it.antMatchers(
                    "/swagger-ui.html",
                    "/webjars/**",
                    "/swagger-resources/**",
                    "/", "/csrf",
                    "/v2/api-docs",
                    "/configuration/**",
                    "/locations/**"
                ).permitAll()
                // Only GET methods are permitted
                it.antMatchers(HttpMethod.GET, "/api/something/**").permitAll()
                // MVC Matchers is another way
                it.mvcMatchers(HttpMethod.GET, "/api/anything/{anything}").permitAll()
                it.antMatchers("/h2-console/**").permitAll()
            }.authorizeRequests()
            .anyRequest().authenticated()
            .and()
            .formLogin().and()
            .httpBasic()
            // This allows to access to all HTTP methods for now
            .and().csrf().disable()

        // h2 console config frame
        http.headers().frameOptions().sameOrigin()
    }

    @Bean
    fun passwordEncoder() = PasswordEncoderFactories.createDelegatingPasswordEncoder()

    /**
     * We can use this approach, but if our UserDetailsService implementation is on spring context, it use this
     */
//    override fun configure(auth: AuthenticationManagerBuilder) {
//        auth
//            .userDetailsService(jpaUserDetailsService)
//            .passwordEncoder(passwordEncoder())
//    }
}