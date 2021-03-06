package br.com.devcave.security.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("no-auth")
class NoAuthController {

    @GetMapping
    fun getPing(): String {
        return "no-auth ping"
    }
}