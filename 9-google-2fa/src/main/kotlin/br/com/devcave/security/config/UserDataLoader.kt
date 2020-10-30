package br.com.devcave.security.config

import br.com.devcave.security.entity.Authority
import br.com.devcave.security.entity.Customer
import br.com.devcave.security.entity.Role
import br.com.devcave.security.entity.User
import br.com.devcave.security.repository.AuthorityRepository
import br.com.devcave.security.repository.CustomerRepository
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
    private val customerRepository: CustomerRepository,
    private val passwordEncoder: PasswordEncoder
) : CommandLineRunner {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Transactional
    override fun run(vararg args: String?) {
        if (authorityRepository.count() == 0L) {
            loadSecurityData()
        }
    }

    private fun loadSecurityData() {
        val customerRandom = customerRepository.save(
            Customer(name = "random")
        )
        val customerScott = customerRepository.save(
            Customer(name = "scott")
        )
        val customerAdmin = customerRepository.save(
            Customer(name = "admin")
        )
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
        // Multi
        val multiCreate = authorityRepository.save(
            Authority(permission = "multi.create")
        )
        val multiUpdate = authorityRepository.save(
            Authority(permission = "multi.update")
        )
        val multiDelete = authorityRepository.save(
            Authority(permission = "multi.delete")
        )
        val multiRead = authorityRepository.save(
            Authority(permission = "multi.read")
        )
        val customerMultiCreate = authorityRepository.save(
            Authority(permission = "customer.multi.create")
        )
        val customerMultiUpdate = authorityRepository.save(
            Authority(permission = "customer.multi.update")
        )
        val customerMultiDelete = authorityRepository.save(
            Authority(permission = "customer.multi.delete")
        )
        val customerMultiRead = authorityRepository.save(
            Authority(permission = "customer.multi.read")
        )

        val admin = roleRepository.save(
            Role(
                name = "ADMIN",
                authorities = setOf(somethingCreate, somethingRead, somethingDelete, somethingUpdate,
                    otherthingCreate, otherthingRead, otherthingDelete, otherthingUpdate,
                    multiCreate, multiRead, multiDelete, multiUpdate)
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
                authorities = setOf(somethingRead, otherthingRead,
                    customerMultiCreate, customerMultiRead, customerMultiDelete, customerMultiUpdate)
            )
        )

        userRepository.save(
            User(
                username = "admin",
                password = passwordEncoder.encode("supersecret"),
                roles = setOf(admin),
                customer = customerAdmin,
                userGoogle2FAEnabled = false
            )
        )
        userRepository.save(
            User(
                username = "random",
                password = passwordEncoder.encode("secretrandom"),
                roles = setOf(userRole),
                customer = customerRandom,
                userGoogle2FAEnabled = false
            )
        )
        userRepository.save(
            User(
                username = "scott",
                password = passwordEncoder.encode("tiger"),
                roles = setOf(customer),
                customer = customerScott,
                userGoogle2FAEnabled = false
            )
        )
        logger.info("persist user total= ${userRepository.count()}")
    }
}