package br.com.devcave.security

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@WebMvcTest
class BasicAuthenticationApplicationTests {

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

	/**
	 * This test is mocking user, not using real httpbasic authentication
	 * for anotation @WithMockUser
	 */
	@Test
	@WithMockUser("spring")
	fun simpleBasicHttpSecurity() {
		mockMvc.perform(MockMvcRequestBuilders.get("/swagger-ui.html"))
			.andExpect(MockMvcResultMatchers.status().isOk)
	}

	/**
	 * Here we are testing passing user and password
	 * */
	@Test
	fun simpleBasicHttpWithUserAndPassword() {
		mockMvc.perform(MockMvcRequestBuilders.get("/swagger-ui.html")
			.with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "supersecret")))
			.andExpect(MockMvcResultMatchers.status().isOk)
	}
}
