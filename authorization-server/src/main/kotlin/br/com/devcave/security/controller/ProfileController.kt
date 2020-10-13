package br.com.devcave.security.controller

import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("profile")
class ProfileController {

    @GetMapping("me")
    fun getMeInfo(): HttpEntity<Any?> {
        return ResponseEntity.ok(SecurityContextHolder.getContext().authentication?.principal ?: "")
    }
}