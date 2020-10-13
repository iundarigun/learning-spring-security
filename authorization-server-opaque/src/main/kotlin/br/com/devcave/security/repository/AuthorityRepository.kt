package br.com.devcave.security.repository

import br.com.devcave.security.entity.Authority
import br.com.devcave.security.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface AuthorityRepository : JpaRepository<Authority, Long>