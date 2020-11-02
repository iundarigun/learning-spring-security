package br.com.devcave.security.controller

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("test")
class TestController {

    @GetMapping
    fun ping(): String {
        return "ping"
    }

    @GetMapping("admin")
    @PreAuthorize("hasRole('ADMIN')")
    fun adminPing(): String {
        return "admin ping"
    }

    @GetMapping("scope")
    @PreAuthorize("#oauth2.hasScope('any')")
    fun scopePing(): String {
        return "scope ping"
    }

    @GetMapping("authority")
    @PreAuthorize("hasAuthority('read')")
    fun authorityPing(): String {
        return "authority ping"
    }
}