package br.com.devcave.security.config

import br.com.devcave.security.config.security.PasswordEncoderFactories
import br.com.devcave.security.config.security.RestHeaderAuthFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

@Configuration
@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter() {

    fun restHeaderAuthFilter(authenticationManager: AuthenticationManager): RestHeaderAuthFilter {
        return RestHeaderAuthFilter(AntPathRequestMatcher("/api/**")).also {
            it.setAuthenticationManager(authenticationManager)
        }
    }

    override fun configure(http: HttpSecurity) {
        http.addFilterBefore(restHeaderAuthFilter(authenticationManager()),
            UsernamePasswordAuthenticationFilter::class.java)
            .csrf().disable()

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
            }.authorizeRequests()
            .anyRequest().authenticated()
            .and()
            .formLogin().and()
            .httpBasic()
    }

    @Bean
    fun passwordEncoder() = PasswordEncoderFactories.createDelegatingPasswordEncoder()

    /**
     * Overriding this method, the spring application.yml properties are ignored.
     * IMPORTANT: Not use InMemory implementation in production
     * We need mark as bean because we need it on Spring Context
     * IMPORTANT : We have an other way to configure using fluent API, see configure method
     */
    /*@Bean
    override fun userDetailsService(): UserDetailsService {
        val userAdmin = User.withDefaultPasswordEncoder()
            .username("admin")
            .password("supersecret")
            .roles("ADMIN")
            .build()

        val userRandom = User.withDefaultPasswordEncoder()
            .username("random")
            .password("secretrandom")
            .roles("USER")
            .build()
        return InMemoryUserDetailsManager(userAdmin, userRandom)
    }*/

    /**
     * We are using {noop} keyword to say that we didn't use password encoder
     */
    override fun configure(auth: AuthenticationManagerBuilder) {
        // @formatter:off
        auth.inMemoryAuthentication()
                .withUser("admin")
                .password("{noop}supersecret")
                .roles("ADMIN")
            .and()
                .withUser("random")
                .password("{sha256}c1c8f3ccee9c70be6f77b02e1dca988c043bc022568eeb35bdd066bf5c2c520b6846911fcdf5bdbe")
                .roles("USER")

        // We dont need use and again if we dont want. We can simply use again auth
        auth.inMemoryAuthentication()
            .withUser("scott")
            .password("{bcrypt}\$2a\$10\$FyCPTgRSUeiT2oMMBLaH.eaYm0XME5XlLeOXQErHkCrioZ/Uv/2qC")
            .roles("CUSTOMER")

        // @formatter:on
    }
}