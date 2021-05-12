package ntnu.idatt2105.user.controller


import com.fasterxml.jackson.databind.ObjectMapper
import io.github.serpro69.kfaker.Faker
import ntnu.idatt2105.factories.UserFactory
import ntnu.idatt2105.sercurity.config.JWTConfig
import ntnu.idatt2105.user.dto.UserRegistrationDto
import ntnu.idatt2105.user.model.User
import ntnu.idatt2105.user.repository.UserRepository
import ntnu.idatt2105.user.service.UserDetailsImplBuilder
import org.hamcrest.Matchers.hasItem
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.http.MediaType
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDate
import java.util.stream.Stream


@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {
    private val URI = "/users/"

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private val jwtConfig: JWTConfig? = null

    @Autowired
    private lateinit var userRepository: UserRepository
    private lateinit var user: User
    private val userFactory: UserFactory = UserFactory()
    private lateinit var firstName: String
    private lateinit var surname: String
    private lateinit var birthDate: LocalDate
    private lateinit var userDetails: UserDetails

    /**
     * Setting up variables that is the same for all tests
     */
    @BeforeEach
    @Throws(Exception::class)
    fun setUp() {
        user = userFactory.getObject()
        userRepository.save(user)
        firstName = "Test"
        surname = "Testersen"
        birthDate = LocalDate.now()
        userDetails = UserDetailsImplBuilder(id=user.id, email=user.email)
            .build()
    }

    /**
     * Cleans up the saved users after each test
     */
    @AfterEach
    fun cleanUp() {
        userRepository.deleteAll()
    }

    /**
     * Test that you can create a user with valid input
     *
     * @throws Exception from post request
     */
    @ParameterizedTest
    @MethodSource("provideValidEmails")
    @Throws(Exception::class)
    fun testCreateUserWithValidEmailAndPassword(email: String) {
        val password = "ValidPassword123"
        val validUser = UserRegistrationDto(
            firstName=firstName,
            surname=surname,
            password=password,
            email=email,
            phoneNumber=Faker().phoneNumber.phoneNumber()
        )
        mockMvc.perform(
            post(URI)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validUser))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.firstName").value(validUser.firstName))
            .andExpect(jsonPath("$.data.password").doesNotExist())
            .andExpect(jsonPath("$.data.email").doesNotExist())
    }

    @Test
    @WithMockUser(value = "spring")
    @Throws(Exception::class)
    fun testGetUserByUserId() {
        val testUser: User = userRepository.save(userFactory.getObject())
        mockMvc.perform(
            get(URI + testUser.id.toString() + "/")
                .contentType(MediaType.APPLICATION_JSON).with(SecurityMockMvcRequestPostProcessors.csrf())
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.firstName").value(testUser.firstName))
    }

    @Test
    @WithMockUser(value = "spring")
    @Throws(Exception::class)
    fun testGetAllUsers() {
        mockMvc.perform(
            get(URI)
                .contentType(MediaType.APPLICATION_JSON).with(SecurityMockMvcRequestPostProcessors.csrf())
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content.[*].firstName", hasItem(user.firstName)))
    }

    /**
     * Test that a user can be created, but the same email cannot be used two times
     *
     * @throws Exception from post request
     */
    @Test
    @Throws(Exception::class)
    fun testCreateUserTwoTimesFails() {
        val user: User = userFactory.getObject()
        userRepository.save<User>(user)
        val email: String = user.email
        val password = "ValidPassword123"
        val validUser = UserRegistrationDto(
            firstName=firstName,
            surname=surname,
            password=password,
            email=email,
            phoneNumber=Faker().phoneNumber.phoneNumber()
        )
        mockMvc.perform(
            post(URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validUser))
        )
            .andExpect(status().isForbidden)
            .andExpect(
                jsonPath("$.message").value("Email is already associated with another user")
            )
    }

    /**
     * Test that a user cannot be created if email is on a wrong format
     *
     * @throws Exception
     */
    @ParameterizedTest
    @MethodSource("provideInvalidEmails")
    @Throws(Exception::class)
    fun testCreateUserWithInvalidEmail(email: String) {
        val password = "ValidPassword123"
        val invalidUser = UserRegistrationDto(
            firstName=firstName,
            surname=surname,
            password=password,
            email=email,
            phoneNumber=Faker().phoneNumber.phoneNumber()
        )

        mockMvc.perform(
            post(URI)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidUser))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.message").value("One or more method arguments are invalid"))
            .andExpect(jsonPath("$.data.email").exists())
    }

    /**
     * Test that a user cannot be created if password is too weak
     *
     * @throws Exception
     */
    @Test
    @Throws(Exception::class)
    fun testCreateUserWithInvalidPassword() {
        val password = "abc123"

        val invalidUser = UserRegistrationDto(
            firstName=firstName,
            surname=surname,
            password=password,
            email="test@testersen.com",
            phoneNumber=Faker().phoneNumber.phoneNumber()
        )
        mockMvc.perform(
            post(URI)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidUser))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.message").value("One or more method arguments are invalid"))
            .andExpect(jsonPath("$.data.password").exists())
    }

    /**
     * Tests that get return a correct user according to token
     *
     * @throws Exception
     */
    @Test
    @Throws(Exception::class)
    fun testGetUserReturnsCorrectUser() {
        val userDetails: UserDetails = UserDetailsImplBuilder(email=user.email).build()
        mockMvc.perform(
            get(URI + "me/")
                .with(user(userDetails))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(user.id.toString()))
    }

    /**
     * Tests that put updated a user and returns the updated user info
     *
     * @throws Exception
     */
    @Test
    @Throws(Exception::class)
    fun testUpdateUserUpdatesUserAndReturnUpdatedData() {
        user.surname = Faker().name.lastName()
        val userDetails: UserDetails = UserDetailsImplBuilder(email=user.email).build()

        mockMvc.perform(
            put(URI + user.id.toString() + "/")
                .with(user(userDetails))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(user.id.toString()))
    }

    @Test
    @WithMockUser(value = "spring")
    @Throws(Exception::class)
    fun testDeleteUserAndReturnsOk() {
        var userToDelete: User = userFactory.getObject()
        userToDelete = userRepository.save(userToDelete)
        val userDetails: UserDetails = UserDetailsImplBuilder(email=userToDelete.email).build()
        mockMvc.perform(
            delete(URI + "me/")
                .with(user(userDetails))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.message").value("User has been deleted"))
    }

    companion object {
        /**
         * Provides a stream of Valid emails to provide parameterized test
         *
         * @return Stream of valid emails
         */
        @JvmStatic
        private fun provideValidEmails(): Stream<Arguments> {
            return Stream.of(
                Arguments.of("test123@mail.com"),
                Arguments.of("test1.testesen@mail.com"),
                Arguments.of("test_1234-testesen@mail.com")
            )
        }

        /**
         * Provides a stream of Invalid emails to provide parameterized test
         *
         * @return Stream of invalid emails
         */
        @JvmStatic
        private fun provideInvalidEmails(): Stream<Arguments> {
            return Stream.of(
                Arguments.of("test123.no"),
                Arguments.of("test@"),
                Arguments.of("test@mail..com")
            )
        }
    }
}