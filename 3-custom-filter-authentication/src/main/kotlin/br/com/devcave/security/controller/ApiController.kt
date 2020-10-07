package br.com.devcave.security.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api")
class ApiController {

    @GetMapping("something/{something}")
    fun getSomething(@PathVariable something: String): String {
        return "get something: $something"
    }

    @PostMapping("something/{something}")
    fun postSomething(@PathVariable something: String): String {
        return "post something: $something"
    }

    @GetMapping("anything/{anything}")
    fun getAnything(@PathVariable anything: String): String {
        return "get anything: $anything"
    }
}