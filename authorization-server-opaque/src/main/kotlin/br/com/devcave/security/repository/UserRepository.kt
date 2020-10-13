package br.com.devcave.security.repository

import br.com.devcave.security.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface UserRepository : JpaRepository<User, Long> {

    fun findByUsername(username: String): Optional<User>
}