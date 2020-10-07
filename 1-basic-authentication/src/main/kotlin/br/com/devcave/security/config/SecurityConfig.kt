package br.com.devcave.security.config

import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

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
}