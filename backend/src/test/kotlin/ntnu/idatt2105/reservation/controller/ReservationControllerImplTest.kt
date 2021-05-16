package ntnu.idatt2105.reservation.controller

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.serpro69.kfaker.Faker
import ntnu.idatt2105.factories.ReservationFactory
import ntnu.idatt2105.factories.RoleFactory
import ntnu.idatt2105.reservation.dto.ReservationCreateDto
import ntnu.idatt2105.reservation.model.Reservation
import ntnu.idatt2105.reservation.repository.ReservationRepository
import ntnu.idatt2105.section.model.Section
import ntnu.idatt2105.section.repository.SectionRepository
import ntnu.idatt2105.user.model.RoleType
import ntnu.idatt2105.user.repository.UserRepository
import ntnu.idatt2105.user.service.UserDetailsImplBuilder
import ntnu.idatt2105.util.ReservationConstants
import org.hamcrest.Matchers
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user


import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.LocalTime
import java.time.ZonedDateTime


@SpringBootTest
@AutoConfigureMockMvc
class ReservationControllerImplTest {

    private fun getURL(section: Section) = "/sections/${section.id}/reservations/"

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var mvc: MockMvc

    @Autowired
    private lateinit var reservationRepository:  ReservationRepository

    @Autowired
    private lateinit var userRepository:  UserRepository

    @Autowired
    private lateinit var userDetailsService: UserDetailsService

    @Autowired
    private lateinit var sectionRepository:  SectionRepository


    private lateinit var reservation : Reservation

    @Autowired
    private lateinit var modelMapper: ModelMapper

    private val faker = Faker()

    @BeforeEach
    fun setUp(){
        reservation = ReservationFactory().`object`
        userRepository.save(reservation.user!!)
        sectionRepository.save(reservation.section!!)
        reservation = reservationRepository.save(reservation)
    }
    @AfterEach
    fun cleanUp(){
        reservationRepository.deleteAll()
        sectionRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    @WithMockUser(value = "spring", roles = [RoleType.USER, RoleType.ADMIN])
    fun `test reservation controller GET all returns OK and page of reservation for given section`() {
        val newReservation = ReservationFactory().`object`
        userRepository.save(newReservation.user!!)
        sectionRepository.save(newReservation.section!!)
        reservation = reservationRepository.save(newReservation)
        this.mvc.perform(MockMvcRequestBuilders.get(getURL(reservation.section!!)))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[*].text", Matchers.hasItem(reservation.text)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[*].text", Matchers.hasItem(newReservation.text)))

    }


    @Test
    @WithMockUser(value = "spring", roles = [RoleType.USER, RoleType.ADMIN])
    fun `test reservation controller GET returns OK and reservation`() {
        this.mvc.perform(MockMvcRequestBuilders.get("${getURL(reservation.section!!)}${reservation.id}/"))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.text").value(reservation.text))

    }

    @Test
    @WithMockUser(value = "spring", roles = [RoleType.USER, RoleType.ADMIN])
    fun `test reservation controller POST creates new Reservation`() {
        val newReservation = ReservationFactory().`object`
        userRepository.save(newReservation.user!!)

        sectionRepository.save(newReservation.section!!)
        val reservationCreate = ReservationCreateDto(newReservation.user?.id,
                newReservation.section?.id,
                reservation.fromTime?.plusDays(1),
                reservation.toTime?.plusDays(1),
                reservation.text,
                reservation.nrOfPeople)

        this.mvc.perform(MockMvcRequestBuilders.post(getURL(reservation.section!!))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservationCreate)))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.text").value(reservation.text))
    }

    @Test
    @WithMockUser(value = "spring", roles = [RoleType.USER, RoleType.ADMIN])
    fun `test reservation controller PUT updates Reservation`() {
        val text = faker.breakingBad.episode()
        reservation.text = text
        val userDetails = userDetailsService.loadUserByUsername(reservation.user?.email)
        this.mvc.perform(MockMvcRequestBuilders.put("${getURL(reservation.section!!)}${reservation.id}/")
                .with(user(userDetails))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservation)))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.text").value(text))
    }

    @Test
    @WithMockUser(value = "spring", roles = [RoleType.USER, RoleType.ADMIN])
    fun `test reservation controller POST can't creates new Reservation in a existing interval different end time`() {
        val newReservation = ReservationFactory().`object`
        userRepository.save(newReservation.user!!)
        sectionRepository.save(newReservation.section!!)
        val reservationCreate = ReservationCreateDto(newReservation.user?.id,
                newReservation.section?.id,
                reservation.fromTime,
                reservation.toTime?.plusHours(20),
                reservation.text, reservation.nrOfPeople)
        this.mvc.perform(MockMvcRequestBuilders.post(getURL(reservation.section!!))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservationCreate)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest)

    }
    @Test
    @WithMockUser(value = "spring")
    fun `test reservation controller DELETE updates Reservation`() {
        val userDetails = userDetailsService.loadUserByUsername(reservation.user?.email)
        this.mvc.perform(MockMvcRequestBuilders.delete("${getURL(reservation.section!!)}${reservation.id}/")
                .with(user(userDetails))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").exists())
    }

    @Test
    @WithMockUser(value = "spring", roles = [RoleType.USER, RoleType.ADMIN])
    fun `test reservation controller POST can't creates new Reservation in a existing interval different start time`() {
        val newReservation = ReservationFactory().`object`
        userRepository.save(newReservation.user!!)
        sectionRepository.save(newReservation.section!!)
        val reservationCreate = ReservationCreateDto(newReservation.user?.id,
                newReservation.section?.id,
                reservation.fromTime?.plusHours(2),
                reservation.toTime,
                reservation.text, reservation.nrOfPeople)
        this.mvc.perform(MockMvcRequestBuilders.post(getURL(reservation.section!!))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservationCreate)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest)

    }
    @Test
    @WithMockUser(value = "spring", roles = [RoleType.USER, RoleType.ADMIN])
    fun `test reservation controller POST can't creates new Reservation in a existing interval`() {
        val newReservation = ReservationFactory().`object`
        userRepository.save(newReservation.user!!)
        sectionRepository.save(newReservation.section!!)
        val reservationCreate = ReservationCreateDto(newReservation.user?.id,
                newReservation.section?.id,
                reservation.fromTime,
                reservation.toTime,
                reservation.text, reservation.nrOfPeople)
        this.mvc.perform(MockMvcRequestBuilders.post(getURL(reservation.section!!))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservationCreate)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest)

    }

    @Test
    @WithMockUser(value = "spring", roles = [RoleType.USER, RoleType.ADMIN])
    fun `test reservation controller GET all returns OK and page of reservation for given section with time filter`() {
        var newReservation = ReservationFactory().`object`
        userRepository.save(newReservation.user!!)
        sectionRepository.save(newReservation.section!!)
        newReservation.fromTime = ZonedDateTime.now().minusDays(50)
        newReservation.toTime = ZonedDateTime.now().minusDays(25)
        newReservation = reservationRepository.save(newReservation)
        this.mvc.perform(MockMvcRequestBuilders.get(getURL(reservation.section!!))
                .param("toTimeBefore", reservation.toTime?.plusHours(1).toString())
                .param("fromTimeAfter", reservation.fromTime?.minusHours(1).toString()))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[*].text", Matchers.hasItem(reservation.text)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[*].text", Matchers.not(newReservation.text)))

    }

    @Test
    @WithMockUser(roles = [RoleType.USER, RoleType.ADMIN])
    fun `test that creating a reservation with a negative number of people returns http 400`() {
        val newReservation = ReservationFactory().`object`
        userRepository.save(newReservation.user!!)
        sectionRepository.save(newReservation.section!!)

        val reservationWithNegativeNrOfPeople = newReservation.copy(nrOfPeople = -1)
        val reservationCreateRequest = modelMapper.map(reservationWithNegativeNrOfPeople, ReservationCreateDto::class.java)

        mvc.perform(MockMvcRequestBuilders.post(getURL(reservation.section!!))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservationCreateRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    @WithMockUser(roles = [RoleType.USER, RoleType.ADMIN])
    fun `test that creating a reservation when from time is in the past returns http 400`() {
        val newReservation = ReservationFactory().`object`
        userRepository.save(newReservation.user!!)
        sectionRepository.save(newReservation.section!!)

        val reservationStartingInThePast = newReservation.copy(fromTime = ZonedDateTime.now().minusDays(1))
        val reservationCreateRequest = modelMapper.map(reservationStartingInThePast, ReservationCreateDto::class.java)

        mvc.perform(MockMvcRequestBuilders.post(getURL(reservation.section!!))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservationCreateRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    @WithMockUser(roles = [RoleType.USER, RoleType.ADMIN])
    fun `test that creating a reservation when from time is before 0600 returns http 400`() {
        val newReservation = ReservationFactory().`object`
        userRepository.save(newReservation.user!!)
        sectionRepository.save(newReservation.section!!)

        val reservationStartingInThePast = newReservation.copy(fromTime = reservation.fromTime?.with(LocalTime.of(5, 0)))
        val reservationCreateRequest = modelMapper.map(reservationStartingInThePast, ReservationCreateDto::class.java)

        mvc.perform(MockMvcRequestBuilders.post(getURL(reservation.section!!))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservationCreateRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    @WithMockUser(roles = [RoleType.USER, RoleType.ADMIN])
    fun `test that creating a reservation when from time is after 2000 returns http 400`() {
        val newReservation = ReservationFactory().`object`
        userRepository.save(newReservation.user!!)
        sectionRepository.save(newReservation.section!!)

        val reservationStartingInThePast = newReservation.copy(fromTime = reservation.fromTime?.with(LocalTime.of(21, 0)))
        val reservationCreateRequest = modelMapper.map(reservationStartingInThePast, ReservationCreateDto::class.java)

        mvc.perform(MockMvcRequestBuilders.post(getURL(reservation.section!!))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservationCreateRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    @WithMockUser(roles = [RoleType.USER])
    fun `test that creating a reservation when from time is more than max months in the future as user returns http 400`() {
        val newReservation = ReservationFactory().`object`
        userRepository.save(newReservation.user!!)
        sectionRepository.save(newReservation.section!!)

        val reservationStartingInThePast = newReservation.copy(
                fromTime = reservation.fromTime?.plusMonths(ReservationConstants.MAX_MONTHS_FOR_USER_RESERVING_IN_FUTURE)
        )
        val reservationCreateRequest = modelMapper.map(reservationStartingInThePast, ReservationCreateDto::class.java)

        mvc.perform(MockMvcRequestBuilders.post(getURL(reservation.section!!))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservationCreateRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    @WithMockUser(roles = [RoleType.USER, RoleType.ADMIN])
    fun `test that creating a reservation when from time is more than max months in the future as admin returns http 400`() {
        val newReservation = ReservationFactory().`object`
        userRepository.save(newReservation.user!!)
        sectionRepository.save(newReservation.section!!)

        val reservationStartingInThePast = newReservation.copy(
                fromTime = reservation.fromTime?.plusMonths(ReservationConstants.MAX_MONTHS_FOR_ADMIN_RESERVING_IN_FUTURE + 1)
        )
        val reservationCreateRequest = modelMapper.map(reservationStartingInThePast, ReservationCreateDto::class.java)

        mvc.perform(MockMvcRequestBuilders.post(getURL(reservation.section!!))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservationCreateRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    @WithMockUser(roles = [RoleType.USER, RoleType.ADMIN])
    fun `test that creating a reservation when from time is before end time returns http 400`() {
        val newReservation = ReservationFactory().`object`
        userRepository.save(newReservation.user!!)
        sectionRepository.save(newReservation.section!!)

        val reservationWithNegatedTimeInterval = newReservation.copy(fromTime = reservation.toTime, toTime = reservation.fromTime)
        val reservationCreateRequest = modelMapper.map(reservationWithNegatedTimeInterval, ReservationCreateDto::class.java)

        mvc.perform(MockMvcRequestBuilders.post(getURL(reservation.section!!))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservationCreateRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    @WithMockUser(roles = [RoleType.USER, RoleType.ADMIN])
    fun `test that creating a reservation when time interval is greater than maximum allowed duration returns http 400`() {
        val newReservation = ReservationFactory().`object`
        userRepository.save(newReservation.user!!)
        sectionRepository.save(newReservation.section!!)

        val reservationWithLongerThanMaxDuration = newReservation.copy(toTime = reservation.toTime?.withHour(ReservationConstants.MAX_DURATION + 1))
        val reservationCreateRequest = modelMapper.map(reservationWithLongerThanMaxDuration, ReservationCreateDto::class.java)

        mvc.perform(MockMvcRequestBuilders.post(getURL(reservation.section!!))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservationCreateRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }
}