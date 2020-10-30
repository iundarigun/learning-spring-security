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

    val permission: String,

    @ManyToMany(mappedBy = "authorities")
    val roles: Set<Role> = emptySet()
){

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Authority

        if (permission != other.permission) return false

        return true
    }

    override fun hashCode(): Int {
        return permission.hashCode()
    }

    override fun toString(): String {
        return "Authority(id=$id, role='$permission')"
    }
}
