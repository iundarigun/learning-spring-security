package br.com.devcave.security.entity

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany

@Entity
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0L,

    val username: String,
    val password: String,
    @ManyToMany(cascade = [CascadeType.MERGE])
    @JoinTable(
        name = "user_authority",
        joinColumns = [JoinColumn(name = "USER_ID", referencedColumnName = "ID")],
        inverseJoinColumns = [JoinColumn(name = "AUTHORITY_ID", referencedColumnName = "ID")]
    )
    val authorities: Set<Authority>,
    val accountNonExpired: Boolean = true,
    val accountNonLocked: Boolean = true,
    val credentialsNonExpired: Boolean = true,
    val enabled: Boolean = true
)