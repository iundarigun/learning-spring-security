package br.com.devcave.security.repository

import br.com.devcave.security.entity.Customer
import br.com.devcave.security.entity.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.Optional
import javax.swing.text.html.Option

interface CustomerRepository : JpaRepository<Customer, Long> {
    fun findByName(name: String) : Optional<Customer>

    @Query("select c.name from Customer c where c.id = :customerId and " +
            "(true = :#{hasAuthority('multi.read')} or c.id = ?#{principal?.customer?.id})")
    fun findNameById(customerId: Long): Optional<String>
}