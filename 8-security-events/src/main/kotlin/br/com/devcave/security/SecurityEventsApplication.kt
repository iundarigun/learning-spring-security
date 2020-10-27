package br.com.devcave.security

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SecurityEventsApplication

fun main(args: Array<String>) {
	runApplication<SecurityEventsApplication>(*args)
}
