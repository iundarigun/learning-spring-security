package br.com.devcave.security

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Google2FAApplication

fun main(args: Array<String>) {
	runApplication<Google2FAApplication>(*args)
}
