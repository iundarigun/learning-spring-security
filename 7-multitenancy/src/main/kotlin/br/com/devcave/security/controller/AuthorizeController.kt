package br.com.devcave.security.controller

import br.com.devcave.security.config.security.OtherthingDeletePerm
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.http.HttpResponse

@RestController
@RequestMapping("play-with-authorize")
class AuthorizeController {

    private val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping
    @PreAuthorize("hasAnyAuthority('otherthing.read')")
    fun get(): ResponseEntity<String> {
        logger.info("get Customer")
        return ResponseEntity.ok("get Customer")
    }

    @DeleteMapping
    @OtherthingDeletePerm
    fun delete(): ResponseEntity<String> {
        logger.info("delete")
        return ResponseEntity.ok("delete")
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('otherthing.create')")
    fun post(): ResponseEntity<String> {
        logger.info("Post")
        return ResponseEntity.ok("post")
    }

    @PutMapping
    @PreAuthorize("hasAnyAuthority('otherthing.update')")
    fun put(): ResponseEntity<String> {
        logger.info("put")
        return ResponseEntity.ok("put")
    }
}