package br.com.devcave.security.entity

import org.springframework.security.core.CredentialsContainer
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.ManyToOne

@Entity
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0L,

    private val username: String,

    private var password: String,

    @ManyToMany(cascade = [CascadeType.MERGE, CascadeType.PERSIST], fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_role",
        joinColumns = [JoinColumn(name = "USER_ID", referencedColumnName = "ID")],
        inverseJoinColumns = [JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")]
    )
    val roles: Set<Role> = emptySet(),

    @ManyToOne(fetch = FetchType.EAGER)
    val customer: Customer,

    val accountNonExpired: Boolean = true,
    val accountNonLocked: Boolean = true,
    val credentialsNonExpired: Boolean = true,
    val enabled: Boolean = true
) : UserDetails, CredentialsContainer {
    override fun getAuthorities(): Set<GrantedAuthority> {
        return roles.flatMap { it.authorities }.map { SimpleGrantedAuthority(it.permission) }.toSet()
    }

    override fun isEnabled(): Boolean {
        return this.enabled
    }

    override fun isCredentialsNonExpired(): Boolean {
        return this.credentialsNonExpired
    }

    override fun getUsername(): String {
        return this.username
    }

    override fun getPassword(): String {
        return this.password
    }

    override fun isAccountNonExpired(): Boolean {
        return this.accountNonExpired
    }

    override fun isAccountNonLocked(): Boolean {
        return this.accountNonLocked
    }

    override fun eraseCredentials() {
        this.password = ""
    }
}