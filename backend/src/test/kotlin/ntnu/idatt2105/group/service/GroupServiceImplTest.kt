package ntnu.idatt2105.group.service

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.serpro69.kfaker.Faker
import ntnu.idatt2105.factories.GroupFactory
import ntnu.idatt2105.factories.ReservationFactory
import ntnu.idatt2105.group.model.Group
import ntnu.idatt2105.group.repository.GroupRepository
import ntnu.idatt2105.reservation.model.Reservation
import ntnu.idatt2105.user.model.User
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.assertj.core.api.Assertions.assertThat

import org.springframework.test.web.servlet.MockMvc
import java.util.*

@ExtendWith(MockitoExtension::class)
class GroupServiceImplTest {


    private val faker = Faker()

    @Mock
    private lateinit var  groupRepository : GroupRepository

    @InjectMocks
    private lateinit var groupService: GroupServiceImpl

    private lateinit var group: Group

    @BeforeEach
    fun setUp(){
        group = GroupFactory().`object`
        Mockito.lenient().`when`(groupRepository.findById(Mockito.any(UUID::class.java))).thenReturn(Optional.of(group))
        Mockito.lenient().`when`(groupRepository.save(Mockito.any(Group::class.java))).thenReturn(group)
    }

    @Test
    fun `test create group returns a group`(){
        assertThat(groupService.createGroup(group).name).isEqualTo(group.name)

    }

    @Test
    fun `test update group returns a updated group`(){
        group.name = faker.app.author()
        assertThat(groupService.updateGroup(group.id,group).name).isEqualTo(group.name)

    }

    @Test
    fun `test get group returns a group`(){
        assertThat(groupService.getGroup(group.id).name).isEqualTo(group.name)
    }
}