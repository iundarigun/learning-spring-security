package br.com.devcave.security.entity

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany

@Entity
data class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0L,

    val name: String,

    @ManyToMany(mappedBy = "roles")
    val users: Set<User> = emptySet(),

    @ManyToMany(cascade = [CascadeType.MERGE, CascadeType.PERSIST], fetch = FetchType.EAGER)
    @JoinTable(
        name = "role_authority",
        joinColumns = [JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")],
        inverseJoinColumns = [JoinColumn(name = "AUTHORITY_ID", referencedColumnName = "ID")]
    )
    val authorities: Set<Authority> = emptySet()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Role

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}