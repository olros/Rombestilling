package ntnu.idatt2105.group.controller

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.serpro69.kfaker.Faker
import ntnu.idatt2105.factories.GroupFactory
import ntnu.idatt2105.factories.UserFactory
import ntnu.idatt2105.group.model.Group
import ntnu.idatt2105.group.repository.GroupRepository
import ntnu.idatt2105.user.dto.UserIdDto
import ntnu.idatt2105.user.model.RoleType
import ntnu.idatt2105.user.model.User
import ntnu.idatt2105.user.repository.UserRepository
import org.hamcrest.Matchers
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers


@SpringBootTest
@AutoConfigureMockMvc
class MembershipControllerImplTest {


    private fun getURI(group: Group) = "/groups/${group.id}/memberships/"

    @Autowired
    private lateinit var  groupRepository : GroupRepository

    @Autowired
    private lateinit var  userRepository: UserRepository

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var mvc: MockMvc

    private lateinit var group: Group

    private lateinit var user: User


    private val faker = Faker()

    @BeforeEach
    fun setup(){
        user = userRepository.save(UserFactory().`object`)
        group = GroupFactory().`object`
        group.members.add(user)
        group = groupRepository.save(group)
    }

    @AfterEach
    fun cleanUp(){
        groupRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    @WithMockUser(value = "spring", roles = [RoleType.USER, RoleType.ADMIN])
    fun `test memberships controller GET returns OK and returns page with members in group`() {
        this.mvc.perform(MockMvcRequestBuilders.get(getURI(group)))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[*].id", Matchers.hasItem(user.id.toString())))

    }

    @Test
    @WithMockUser(value = "spring", roles = [RoleType.USER, RoleType.ADMIN])
    fun `test memberships controller POST returns OK and returns page with new members in group`() {
        val newUser =  userRepository.save(UserFactory().`object`)
        this.mvc.perform(MockMvcRequestBuilders.post(getURI(group))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(UserIdDto(newUser.id))))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[*].id", Matchers.hasItem(newUser.id.toString())))

    }

    @Test
    @WithMockUser(value = "spring", roles = [RoleType.USER, RoleType.ADMIN])
    fun `test memberships controller DELETE returns OK and returns page with updated members in group`() {
        val newUser =  userRepository.save(UserFactory().`object`)
        group.members.add(newUser)
        group = groupRepository.save(group)
        this.mvc.perform(MockMvcRequestBuilders.delete("${getURI(group)}{userId}/", user.id))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[*].id", Matchers.hasItem(newUser.id.toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[*].id", Matchers.not(user.id.toString())))


    }
}