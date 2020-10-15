package br.com.devcave.security.config

import br.com.devcave.security.config.security.PasswordEncoderFactories
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension

@Configuration
@EnableWebSecurity
// securedEnabled able to use @Secured and prePostEnabled able to use @PreAuthorize
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
class SecurityConfig : WebSecurityConfigurerAdapter() {

    @Bean
    fun securityEvaluationContextyExtension(): SecurityEvaluationContextExtension {
        return SecurityEvaluationContextExtension()
    }

    override fun configure(http: HttpSecurity) {
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