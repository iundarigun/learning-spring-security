package br.com.devcave.security.repository

import br.com.devcave.security.entity.Role
import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository : JpaRepository<Role, Long>