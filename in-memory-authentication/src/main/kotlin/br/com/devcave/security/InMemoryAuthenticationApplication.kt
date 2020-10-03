package br.com.devcave.security

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class InMemoryAuthenticationApplication

fun main(args: Array<String>) {
	runApplication<InMemoryAuthenticationApplication>(*args)
}
