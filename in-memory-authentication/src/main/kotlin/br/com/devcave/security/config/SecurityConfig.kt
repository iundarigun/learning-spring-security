package br.com.devcave.security.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.provisioning.InMemoryUserDetailsManager

@Configuration
@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter() {
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
            }.authorizeRequests()
            .anyRequest().authenticated()
            .and()
            .formLogin().and()
            .httpBasic()
    }

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
                .password("{noop}secretrandom")
                .roles("USER")

        // We dont need use and again if we dont want. We can simply use again auth
        auth.inMemoryAuthentication()
            .withUser("scott")
            .password("{noop}tiger")
            .roles("CUSTOMER")

        // @formatter:on
    }
}