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
class InMemoryAuthenticationApplicationTests {

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
		mockMvc.perform(MockMvcRequestBuilders.get("/test"))
			.andExpect(MockMvcResultMatchers.status().isOk)
	}

	/**
	 * Here we are testing passing user and password
	 * */
	@Test
	fun simpleBasicHttpWithUserAndPassword() {
		mockMvc.perform(MockMvcRequestBuilders.get("/test")
			.with(SecurityMockMvcRequestPostProcessors.httpBasic("random", "secretrandom")))
			.andExpect(MockMvcResultMatchers.status().isOk)
	}

	/**
	 * Here we are testing url config with no auth
	 * */
	@Test
	fun simpleBasicHttpNoAuthURL() {
		mockMvc.perform(MockMvcRequestBuilders.get("/no-auth/")
			.with(SecurityMockMvcRequestPostProcessors.anonymous()))
			.andExpect(MockMvcResultMatchers.status().isOk)
	}

	/**
	 * Here we are testing api get without auth
	 * */
	@Test
	fun simpleBasicHttpAPIGet() {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/something/banana")
			.with(SecurityMockMvcRequestPostProcessors.anonymous()))
			.andExpect(MockMvcResultMatchers.status().isOk)
	}

	/**
	 * Here we are testing api post without auth and returns error
	 * */
	@Test
	fun simpleBasicHttpAPIPost() {
		mockMvc.perform(MockMvcRequestBuilders.post("/api/something/banana")
			.with(SecurityMockMvcRequestPostProcessors.anonymous()))
			.andExpect(MockMvcResultMatchers.status().isForbidden)
	}

	/**
	 * Here we are testing api get anything (using mvcmatcher) without auth
	 * */
	@Test
	fun simpleBasicHttpAPIAnythingGet() {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/anything/banana")
			.with(SecurityMockMvcRequestPostProcessors.anonymous()))
			.andExpect(MockMvcResultMatchers.status().isOk)
	}

	/**
	 * Here we are testing passing user and password
	 * */
	@Test
	fun passwordEncodingTests() {
		mockMvc.perform(MockMvcRequestBuilders.get("/test")
			.with(SecurityMockMvcRequestPostProcessors.httpBasic("random", "secretrandom")))
			.andExpect(MockMvcResultMatchers.status().isOk)
		mockMvc.perform(MockMvcRequestBuilders.get("/test")
			.with(SecurityMockMvcRequestPostProcessors.httpBasic("scott", "tiger")))
			.andExpect(MockMvcResultMatchers.status().isOk)
		mockMvc.perform(MockMvcRequestBuilders.get("/test")
			.with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "supersecret")))
			.andExpect(MockMvcResultMatchers.status().isOk)
	}
}
