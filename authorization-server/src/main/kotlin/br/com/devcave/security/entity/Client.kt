package br.com.devcave.security.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Client (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0,
    val clientId: String,
    val clientSecret: String
//    val
//    val secretRequired: Boolean = true,
//    val scoped: Boolean = false
//    val resourceId: Set<String> = emptySet(),
//    val scope: Set<String> = emptySet(),
//    val authorizedGrantTypes: Set<String> = emptySet(),
//    val registeredRedirectUri: Set<String> = emptySet(),
//    val authorities: Collection<GrantedAuthority> = emptySet(),
//    val accessTokenValiditySeconds: Integer,
//    val refreshTokenValiditySeconds: Integer
)