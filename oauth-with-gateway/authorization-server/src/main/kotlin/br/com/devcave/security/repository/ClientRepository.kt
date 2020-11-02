package br.com.devcave.security.repository

import br.com.devcave.security.entity.Client
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface ClientRepository : JpaRepository<Client, Long> {

    fun findByClientId(clientId: String): Optional<Client>
}