package br.com.devcave.security.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Client(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0,
    val clientId: String,
    val clientSecret: String,
    // Separate by comma
    val resources: String = "",
    // Separate by comma
    val scopes: String = "",
    // Separate by comma
    val authorizedGrantTypes: String = "",
    // Separate by comma
    val authorities: String = "",
    val accessTokenValiditySeconds: Int? = null,
    val refreshTokenValiditySeconds: Int? = null
)