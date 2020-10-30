package br.com.devcave.security

import br.com.devcave.security.repository.CustomerRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithUserDetails
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@SpringBootTest
class MulitenancyApplicationTests {

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(webApplicationContext)
            .apply<DefaultMockMvcBuilder>(SecurityMockMvcConfigurers.springSecurity())
            .build()
    }

    @Test
    @WithUserDetails("admin")
    fun userTestAdmin() {
        val customer = customerRepository.findByName("scott").orElseThrow()
        mockMvc.perform(
            MockMvcRequestBuilders.get("/multi/${customer.id}/123")
        ).andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
    }

    @Test
    @WithUserDetails("scott")
    fun userTestRandomSuccess() {
        val customer = customerRepository.findByName("scott").orElseThrow()
        mockMvc.perform(
            MockMvcRequestBuilders.get("/multi/${customer.id}/123")
        ).andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
    }

    @Test
    @WithUserDetails("scott")
    fun userTestRandomUnsuccess() {
        val customer = customerRepository.findByName("scott").orElseThrow()
        mockMvc.perform(
            MockMvcRequestBuilders.get("/multi/${customer.id + 1}/123")
        ).andExpect(MockMvcResultMatchers.status().isForbidden)
    }

    @Test
    fun userTestAnonymous() {
        val customer = customerRepository.findByName("scott").orElseThrow()
        mockMvc.perform(
            MockMvcRequestBuilders.get("/multi/${customer.id}/123")
        ).andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }

    @Test
    @WithUserDetails("scott")
    fun userTestGetNameUnsuccess() {
        val customer = customerRepository.findByName("admin").orElseThrow()
        mockMvc.perform(
            MockMvcRequestBuilders.get("/multi/${customer.id}")
        ).andExpect(MockMvcResultMatchers.status().isNotFound)

    }

    @Test
    @WithUserDetails("scott")
    fun userTestGetNameSuccess() {
        val customer = customerRepository.findByName("scott").orElseThrow()
        mockMvc.perform(
            MockMvcRequestBuilders.get("/multi/${customer.id}")
        ).andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
    }

    @Test
    @WithUserDetails("admin")
    fun userTestGetNameWithAdminUser() {
        val customer = customerRepository.findByName("scott").orElseThrow()
        mockMvc.perform(
            MockMvcRequestBuilders.get("/multi/${customer.id}")
        ).andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
    }
}
