package br.com.devcave.security.controller

import br.com.devcave.security.entity.User
import br.com.devcave.security.repository.CustomerRepository
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("multi/{customerId}")
class MultiController(
    private val customerRepository: CustomerRepository
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping("{id}")
    @PreAuthorize(
        "hasAuthority('multi.read') OR hasAuthority('customer.multi.read') " +
                "AND @customAuthenticationManager.customerIdMatches(authentication, #customerId)"
    )
    fun getMulti(
        @PathVariable customerId: String,
        @PathVariable id: String,
        @AuthenticationPrincipal user: User
    ): String {
        logger.info("user ${user.username}")
        return "get Multi: $customerId"
    }

    @PostMapping("{id}")
    @PreAuthorize(
        "hasAuthority('multi.create') OR hasAuthority('customer.multi.create') " +
                "AND @customAuthenticationManager.customerIdMatches(authentication, #customerId)"
    )
    fun postMulti(
        @PathVariable customerId: String,
        @PathVariable id: String
    ): String {
        return "post Multi: $customerId"
    }

    @PutMapping("{id}")
    @PreAuthorize(
        "hasAuthority('multi.update') OR hasAuthority('customer.multi.update') " +
                "AND @customAuthenticationManager.customerIdMatches(authentication, #customerId)"
    )
    fun putMulti(
        @PathVariable customerId: String,
        @PathVariable id: String
    ): String {
        return "put Multi: $customerId"
    }

    @DeleteMapping("{id}")
    @PreAuthorize(
        "hasAuthority('multi.delete') OR hasAuthority('customer.multi.delete') " +
                "AND @customAuthenticationManager.customerIdMatches(authentication, #customerId)"
    )
    fun deleteMulti(
        @PathVariable customerId: String,
        @PathVariable id: String
    ): String {
        return "delete Multi: $customerId"
    }

    @GetMapping
    fun getCustomerNameInformation(@PathVariable customerId: Long): String {
        return customerRepository.findNameById(customerId).orElseThrow {
            @ResponseStatus(value = HttpStatus.NOT_FOUND)
            object : RuntimeException() {

            }
        }
    }
}