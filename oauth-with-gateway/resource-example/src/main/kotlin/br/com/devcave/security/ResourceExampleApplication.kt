package br.com.devcave.security

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@EnableFeignClients
@SpringBootApplication
class ResourceExampleApplication

fun main(args: Array<String>) {
	runApplication<ResourceExampleApplication>(*args)
}
