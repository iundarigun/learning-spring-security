package br.com.devcave.security

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@SpringBootTest
class RolesApplicationTests {

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(webApplicationContext)
            .apply<DefaultMockMvcBuilder>(SecurityMockMvcConfigurers.springSecurity())
            .build()
    }

    @Test
    fun roleTestDeleteOnlyAdmin(){
        mockMvc.perform(
            MockMvcRequestBuilders.delete("/play-with-rol/admin")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("random", "secretrandom"))
        ).andExpect(MockMvcResultMatchers.status().isForbidden)

        mockMvc.perform(
            MockMvcRequestBuilders.delete("/play-with-rol/admin")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("scott", "tiger"))
        ).andExpect(MockMvcResultMatchers.status().isForbidden)

        mockMvc.perform(
            MockMvcRequestBuilders.delete("/play-with-rol/admin")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "supersecret"))
        ).andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
    }

    @Test
    fun roleTestGetForCustomer(){
        mockMvc.perform(
            MockMvcRequestBuilders.get("/play-with-rol/customer")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("random", "secretrandom"))
        ).andExpect(MockMvcResultMatchers.status().isForbidden)

        mockMvc.perform(
            MockMvcRequestBuilders.get("/play-with-rol/customer")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("scott", "tiger"))
        ).andExpect(MockMvcResultMatchers.status().is2xxSuccessful)

        mockMvc.perform(
            MockMvcRequestBuilders.get("/play-with-rol/customer")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "supersecret"))
        ).andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
    }

    @Test
    fun roleTestSecured(){
        mockMvc.perform(
            MockMvcRequestBuilders.get("/play-with-rol/annotation/secured")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("random", "secretrandom"))
        ).andExpect(MockMvcResultMatchers.status().isForbidden)

        mockMvc.perform(
            MockMvcRequestBuilders.get("/play-with-rol/annotation/secured")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("scott", "tiger"))
        ).andExpect(MockMvcResultMatchers.status().is2xxSuccessful)

        mockMvc.perform(
            MockMvcRequestBuilders.get("/play-with-rol/annotation/secured")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "supersecret"))
        ).andExpect(MockMvcResultMatchers.status().is2xxSuccessful)

        mockMvc.perform(
            MockMvcRequestBuilders.get("/play-with-rol/annotation/secured")
                .with(SecurityMockMvcRequestPostProcessors.anonymous())
        ).andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }

    @Test
    fun roleTestPreautorize(){
        mockMvc.perform(
            MockMvcRequestBuilders.get("/play-with-rol/annotation/expression")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("random", "secretrandom"))
        ).andExpect(MockMvcResultMatchers.status().isForbidden)

        mockMvc.perform(
            MockMvcRequestBuilders.get("/play-with-rol/annotation/expression")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("scott", "tiger"))
        ).andExpect(MockMvcResultMatchers.status().isForbidden)

        mockMvc.perform(
            MockMvcRequestBuilders.get("/play-with-rol/annotation/expression")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "supersecret"))
        ).andExpect(MockMvcResultMatchers.status().is2xxSuccessful)

        mockMvc.perform(
            MockMvcRequestBuilders.get("/play-with-rol/annotation/secured")
                .with(SecurityMockMvcRequestPostProcessors.anonymous())
        ).andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }
}
