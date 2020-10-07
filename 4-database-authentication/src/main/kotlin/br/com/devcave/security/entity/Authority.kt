package br.com.devcave.security.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToMany

@Entity
data class Authority(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0L,

    val role: String,

    @ManyToMany(mappedBy = "authorities")
    val users: Set<User> = emptySet()
){

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Authority

        if (role != other.role) return false

        return true
    }

    override fun hashCode(): Int {
        return role.hashCode()
    }

    override fun toString(): String {
        return "Authority(id=$id, role='$role')"
    }
}
