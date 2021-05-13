package ntnu.idatt2105.reservation.controller

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.serpro69.kfaker.Faker
import ntnu.idatt2105.factories.ReservationFactory
import ntnu.idatt2105.factories.SectionFactory
import ntnu.idatt2105.reservation.dto.ReservationCreateDto
import ntnu.idatt2105.reservation.model.Reservation
import ntnu.idatt2105.reservation.repository.ReservationRepository
import ntnu.idatt2105.section.model.Section
import ntnu.idatt2105.section.repository.SectionRepository
import ntnu.idatt2105.user.repository.UserRepository
import org.hamcrest.Matchers
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.modelmapper.ModelMapper
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
class ReservationControllerTest {

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
    @WithMockUser(value = "spring")
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
    @WithMockUser(value = "spring")
    fun `test reservation controller POST creates new Reservation`() {
        val newReservation = ReservationFactory().`object`
        userRepository.save(newReservation.user!!)
        sectionRepository.save(newReservation.section!!)
        val reservationCreate = ReservationCreateDto(newReservation.user?.id,
                newReservation.section?.id,
                reservation.fromTime?.plusHours(10),
                reservation.toTime?.plusHours(20),
                reservation.text, reservation.nrOfPeople)
        this.mvc.perform(MockMvcRequestBuilders.post(getURL(reservation.section!!))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservationCreate)))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.text").value(reservation.text))
    }

    @Test
    @WithMockUser(value = "spring")
    fun `test reservation controller PUT updates Reservation`() {
        val text = faker.breakingBad.episode()
        reservation.text = text
        this.mvc.perform(MockMvcRequestBuilders.put("${getURL(reservation.section!!)}${reservation.id}/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservation)))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.text").value(text))
    }

    @Test
    @WithMockUser(value = "spring")
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
        this.mvc.perform(MockMvcRequestBuilders.delete("${getURL(reservation.section!!)}${reservation.id}/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").exists())
    }

    @Test
    @WithMockUser(value = "spring")
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
    @WithMockUser(value = "spring")
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
}