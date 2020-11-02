package br.com.devcave.security.config

import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.cloud.gateway.route.builder.filters
import org.springframework.cloud.gateway.route.builder.routes
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RouteConfiguration {

    @Bean
    fun customRouteLocator(builder: RouteLocatorBuilder): RouteLocator {
        return builder.routes {
            route {
                path("/auth/**")
                filters {
                    stripPrefix(1)
                    addRequestHeader("x-api-token", "1234")
                }
                uri("http://localhost:1980")
            }
            route {
                path("/resource-example/**")
                filters {
                    stripPrefix(1)
                    addRequestHeader("x-api-token", "1234")
                }
                uri("http://localhost:1989")
            }
        }
    }
}