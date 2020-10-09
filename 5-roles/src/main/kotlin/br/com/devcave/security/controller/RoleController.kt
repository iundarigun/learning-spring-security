package br.com.devcave.security.controller

import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.http.HttpResponse

@RestController
@RequestMapping("play-with-rol")
class RoleController {

    private val logger = LoggerFactory.getLogger(javaClass)

    @DeleteMapping("admin")
    fun delete(): ResponseEntity<String> {
        logger.info("delete")
        return ResponseEntity.ok("delete")
    }

    @GetMapping("customer")
    fun get(): ResponseEntity<String> {
        logger.info("get Customer")
        return ResponseEntity.ok("get Customer")
    }

    @Secured(value = ["ROLE_ADMIN", "ROLE_CUSTOMER"])
    @GetMapping("annotation/secured")
    fun getByAnnotation(): ResponseEntity<String> {
        logger.info("getByAnnotation secured")
        return ResponseEntity.ok("getByAnnotation secured")
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("annotation/expression")
    fun getByAnnotation2(): ResponseEntity<String> {
        logger.info("getByAnnotation2 expression")
        return ResponseEntity.ok("getByAnnotation expression")
    }
}