package ntnu.idatt2105.reservation.service

import com.querydsl.core.types.Predicate
import io.github.serpro69.kfaker.Faker
import ntnu.idatt2105.factories.ReservationFactory
import ntnu.idatt2105.reservation.dto.ReservationCreateDto
import ntnu.idatt2105.reservation.model.Reservation
import ntnu.idatt2105.reservation.repository.ReservationRepository
import ntnu.idatt2105.section.repository.SectionRepository
import ntnu.idatt2105.user.model.User
import ntnu.idatt2105.user.service.UserService
import ntnu.idatt2105.util.JpaUtils
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Spy
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.mockito.Mockito.any
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.Mockito.any
import org.springframework.data.domain.Pageable
import java.util.*


@ExtendWith(MockitoExtension::class)
class ReservationServiceTest {


    @InjectMocks
    private lateinit var reservationService: ReservationServiceImpl

    @Mock
    private lateinit var sectionRepository: SectionRepository

    @Mock
    private lateinit var reservationRepository: ReservationRepository

    @Mock
    private lateinit var userService: UserService

    @Spy
    @Autowired
    private lateinit var modelMapper: ModelMapper

    private lateinit var reservation : Reservation

    private val faker = Faker()



    @BeforeEach
    fun setUp(){
        reservation = ReservationFactory().`object`
        Mockito.lenient().`when`(reservationRepository.findById(reservation.id)).thenReturn(Optional.of(reservation))
        Mockito.lenient().`when`(sectionRepository.findById(reservation.section?.id!!)).thenReturn(Optional.of(reservation.section!!))
        Mockito.lenient().`when`(userService.getUser(reservation.user?.id!!, User::class.java)).thenReturn(reservation.user!!)
        Mockito.lenient().`when`(reservationRepository.save(any(Reservation::class.java))).thenReturn(reservation)

    }

    @Test
    fun `test get all reservation gets a list of reservations`(){
        val page = JpaUtils().getDefaultPageable()
        val predicate = JpaUtils().getEmptyPredicate()
        val testList: List<Reservation> = mutableListOf(reservation)
        val reservations: Page<Reservation> = PageImpl(testList, page, testList.size.toLong())
        Mockito.lenient().`when`(reservationRepository.findAll(any(Predicate::class.java), any(Pageable::class.java))).thenReturn(reservations)
        assertThat(reservationService.getAllReservation(reservation.section?.id!!, page, predicate).content.size).isEqualTo(testList.size)
    }


    @Test
    fun `test create reservation creates a reservation`(){
        Mockito.lenient().`when`(reservationRepository.existsInterval(reservation.fromTime!!, reservation.toTime!!)).thenReturn(false)
        val text = faker.aquaTeenHungerForce.quote()
        reservation.text = text
        val newReservation = ReservationCreateDto(userId = reservation.user?.id,
                text = text,
                fromTime = reservation.fromTime, toTime = reservation.toTime)
        val test = reservationService.createReservation(reservation.section?.id!!, newReservation)
        //TODO
       //next PR!!!!! assertThat(test.text).isEqualTo(reservation.text)

    }
}