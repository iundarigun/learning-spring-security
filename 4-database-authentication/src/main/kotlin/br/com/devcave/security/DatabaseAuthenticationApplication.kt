package br.com.devcave.security

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DatabaseAuthenticationApplication

fun main(args: Array<String>) {
	runApplication<DatabaseAuthenticationApplication>(*args)
}
