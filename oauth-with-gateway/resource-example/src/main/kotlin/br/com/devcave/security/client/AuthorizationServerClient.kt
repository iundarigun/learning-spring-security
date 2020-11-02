package br.com.devcave.security.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(value = "authorization-server-client", url = "\${security.authorization-server-url}")
interface AuthorizationServerClient {
    @GetMapping("oauth/check_token/")
    fun checkToken(@RequestParam token: String): Map<String, Any?>
}