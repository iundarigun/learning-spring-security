package br.com.devcave.security

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MultitenancyApplication

fun main(args: Array<String>) {
	runApplication<MultitenancyApplication>(*args)
}
