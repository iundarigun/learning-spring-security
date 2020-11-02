package br.com.devcave.security.config.data

import br.com.devcave.security.entity.Authority
import br.com.devcave.security.entity.Client
import br.com.devcave.security.entity.Role
import br.com.devcave.security.entity.User
import br.com.devcave.security.repository.AuthorityRepository
import br.com.devcave.security.repository.ClientRepository
import br.com.devcave.security.repository.RoleRepository
import br.com.devcave.security.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class UserDataLoader(
    private val userRepository: UserRepository,
    private val authorityRepository: AuthorityRepository,
    private val roleRepository: RoleRepository,
    private val clientRepository: ClientRepository,
    private val passwordEncoder: PasswordEncoder
) : CommandLineRunner {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Transactional
    override fun run(vararg args: String?) {
        if (authorityRepository.count() == 0L) {
            loadSecurityData()
        }
        if (clientRepository.count() == 0L) {
            loadClientsData()
        }
    }

    private fun loadClientsData() {
        clientRepository.save(
            Client(
                clientId = "app1",
                clientSecret = passwordEncoder.encode("supersecret"),
                resources = "resource1,resource3,resource-example",
                scopes = "any",
                authorizedGrantTypes = "password,client_credentials,authorization_code,refresh_token",
                authorities = "read,write,delete,ROLE_ADMIN",
                accessTokenValiditySeconds = 5000,
                refreshTokenValiditySeconds = 50000
            )
        )
        clientRepository.save(
            Client(
                clientId = "app2",
                clientSecret = passwordEncoder.encode("supersecret"),
                resources = "resource1,resource2,resource3,resource-example",
                scopes = "",
                authorizedGrantTypes = "password,client_credentials,authorization_code,refresh_token",
                authorities = "read",
                accessTokenValiditySeconds = 6000,
                refreshTokenValiditySeconds = 60000
            )
        )
    }

    private fun loadSecurityData() {
        val somethingCreate = authorityRepository.save(
            Authority(permission = "something.create")
        )
        val somethingUpdate = authorityRepository.save(
            Authority(permission = "something.update")
        )
        val somethingDelete = authorityRepository.save(
            Authority(permission = "something.delete")
        )
        val somethingRead = authorityRepository.save(
            Authority(permission = "something.read")
        )
        val otherthingCreate = authorityRepository.save(
            Authority(permission = "otherthing.create")
        )
        val otherthingUpdate = authorityRepository.save(
            Authority(permission = "otherthing.update")
        )
        val otherthingDelete = authorityRepository.save(
            Authority(permission = "otherthing.delete")
        )
        val otherthingRead = authorityRepository.save(
            Authority(permission = "otherthing.read")
        )
        val adminRole = authorityRepository.save(
            Authority(permission = "ROLE_ADMIN")
        )
        val admin = roleRepository.save(
            Role(
                name = "ADMIN",
                authorities = setOf(
                    somethingCreate, somethingRead, somethingDelete, somethingUpdate,
                    otherthingCreate, otherthingRead, otherthingDelete, otherthingUpdate,
                    adminRole
                )
            )
        )
        val userRole = roleRepository.save(
            Role(
                name = "USER",
                authorities = setOf(somethingRead)
            )
        )
        val customer = roleRepository.save(
            Role(
                name = "CUSTOMER",
                authorities = setOf(somethingRead, otherthingRead)
            )
        )

        userRepository.save(
            User(
                username = "admin",
                password = passwordEncoder.encode("supersecret"),
                roles = setOf(admin)
            )
        )
        userRepository.save(
            User(
                username = "random",
                password = passwordEncoder.encode("secretrandom"),
                roles = setOf(userRole)
            )
        )
        userRepository.save(
            User(
                username = "scott",
                password = passwordEncoder.encode("tiger"),
                roles = setOf(customer)
            )
        )
        logger.info("persist user total= ${userRepository.count()}")
    }
}