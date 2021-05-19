package ntnu.idatt2105.reservation.service

import com.querydsl.core.types.ExpressionUtils
import com.querydsl.core.types.Predicate
import ntnu.idatt2105.exception.ApplicationException
import ntnu.idatt2105.exception.EntityType
import ntnu.idatt2105.exception.ExceptionType
import ntnu.idatt2105.reservation.dto.ReservationCreateDto
import ntnu.idatt2105.reservation.dto.ReservationDto
import ntnu.idatt2105.reservation.model.QReservation
import ntnu.idatt2105.reservation.model.Reservation
import ntnu.idatt2105.reservation.repository.ReservationRepository
import ntnu.idatt2105.section.repository.SectionRepository
import ntnu.idatt2105.user.model.User
import ntnu.idatt2105.user.service.UserService
import org.modelmapper.ModelMapper
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*


@Service
class ReservationServiceImpl(
    val reservationRepository: ReservationRepository,
    val modelMapper: ModelMapper,
    val userService: UserService,
    val sectionRepository: SectionRepository) : ReservationService {

    override fun getAllReservation(sectionId: UUID, pageable: Pageable, predicate: Predicate): Page<ReservationDto> {
        val reservation = QReservation.reservation
        val newPredicate = ExpressionUtils.allOf(predicate, reservation.section.id.eq(sectionId))!!
        reservationRepository.findAll(newPredicate, pageable).run {
            return this.map { modelMapper.map(it, ReservationDto::class.java) }
        }
    }


    override fun createReservation(sectionId: UUID, reservation: ReservationCreateDto) : ReservationDto {
        // Check for overlapping reservations at same section
        if (reservationRepository.existsInterval(reservation.fromTime!!, reservation.toTime!!, sectionId)){
            throw ApplicationException.throwException(
                    EntityType.RESERVATION, ExceptionType.DUPLICATE_ENTITY, reservation.fromTime.toString(), reservation.toTime.toString())
        }
        val section = sectionRepository.findById(sectionId).orElseThrow { throw ApplicationException.throwException(
            EntityType.SECTION, ExceptionType.ENTITY_NOT_FOUND, reservation.sectionId.toString()) }
        // Check for overlapping reservations at parent sections
        if (section.parent != null && reservationRepository.existsInterval(reservation.fromTime, reservation.toTime, section.parent!!.id)){
            throw ApplicationException.throwException(
                EntityType.RESERVATION, ExceptionType.DUPLICATE_ENTITY, reservation.fromTime.toString(), reservation.toTime.toString())
        }
        // Check for overlapping reservations at child sections
        section.children.forEach { child -> if (reservationRepository.existsInterval(reservation.fromTime, reservation.toTime, child.id)) {
                throw ApplicationException.throwException(
                    EntityType.RESERVATION, ExceptionType.DUPLICATE_ENTITY, reservation.fromTime.toString(), reservation.toTime.toString())
            }
        }
        var newReservation = Reservation(id = UUID.randomUUID(),
                toTime = reservation.toTime,
                fromTime = reservation.fromTime,
                text = reservation.text,
                nrOfPeople = reservation.nrOfPeople)

        val user = userService.getUser(reservation.userId!!, User::class.java)

        newReservation.user = user
        newReservation.section = section
        newReservation = reservationRepository.save(newReservation)
        return modelMapper.map(newReservation, ReservationDto::class.java)
    }

    override fun getUserReservation(userId: UUID, pageable: Pageable, predicate: Predicate): Page<ReservationDto> {
        val reservation = QReservation.reservation
        val newPredicate = ExpressionUtils.allOf(predicate, reservation.user.id.eq(userId))!!
        reservationRepository.findAll(newPredicate, pageable).run {
            return this.map { modelMapper.map(it, ReservationDto::class.java) }
        }
    }


    override fun getReservation(sectionId: UUID, reservationId: UUID): ReservationDto =
        reservationRepository.findById(reservationId).orElseThrow { throw ApplicationException.throwException(
                EntityType.RESERVATION, ExceptionType.ENTITY_NOT_FOUND, reservationId.toString(), sectionId.toString()) }
                .run {
                    return modelMapper.map(this, ReservationDto::class.java)
            }

    override fun updateReservation(sectionId: UUID, reservationId: UUID, reservation: ReservationDto): ReservationDto {
        reservationRepository.findReservationByIdAndSectionId(reservationId, sectionId).run {
            if(this != null){
                val updatedReservation = this.copy(
                        text = reservation.text,
                        nrOfPeople = reservation.nrOfPeople
                )
                reservationRepository.save(updatedReservation).run {
                    return modelMapper.map(this, ReservationDto::class.java)
                }
            }
        }
        throw  ApplicationException.throwException(
                EntityType.RESERVATION, ExceptionType.ENTITY_NOT_FOUND, reservationId.toString(), sectionId.toString())
    }

    override fun deleteReservation(sectionId: UUID, reservationId: UUID) {
        reservationRepository.findById(reservationId).orElseThrow{ throw ApplicationException.throwException(
                EntityType.RESERVATION, ExceptionType.ENTITY_NOT_FOUND, reservationId.toString(), sectionId.toString()) }
                .run {
            reservationRepository.delete(this)

        }
    }
}