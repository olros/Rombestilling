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
class ReservationServiceImpl<T>(
    val modelMapper: ModelMapper,
    val sectionRepository: SectionRepository) : ReservationService<T> {

    lateinit var reservationRepository: ReservationRepository<*>
    lateinit var reservationRelationService: ReservationRelationService<T>
    
    override fun getAllReservation(sectionId: UUID, pageable: Pageable, predicate: Predicate): Page<ReservationDto> {
        val reservation = QReservation.reservation
        val newPredicate = ExpressionUtils.allOf(predicate, reservation.section.id.eq(sectionId))!!
        reservationRepository.findAll(newPredicate, pageable).run {
            return this.map { it.toReservationDto() }
        }
    }


    override fun createReservation(sectionId: UUID, reservation: ReservationCreateDto) : ReservationDto {
        if (reservationRepository.existsInterval(reservation.fromTime!!, reservation.toTime!!)){
            throw ApplicationException.throwException(
                    EntityType.RESERVATION, ExceptionType.DUPLICATE_ENTITY, reservation.fromTime.toString(), reservation.toTime.toString())
        }

        var newReservation: Reservation<T> = reservation.toReservation() as Reservation<T>

        val entity = reservationRelationService.getByEntityId(reservation.entityId!!)

        val section = sectionRepository.findById(sectionId).orElseThrow { throw ApplicationException.throwException(
                EntityType.SECTION, ExceptionType.ENTITY_NOT_FOUND, reservation.sectionId.toString()) }

        newReservation.setRelation(entity)
        newReservation.section = section
        newReservation = reservationRepository.save(newReservation)
        return newReservation.toReservationDto()
    }

    override fun getUserReservation(userId: UUID, pageable: Pageable, predicate: Predicate): Page<ReservationDto> {
        val reservation = QReservation.reservation
        val newPredicate = ExpressionUtils.allOf(predicate, reservation.entityId.eq(userId.toString()))!!
        reservationRepository.findAll(newPredicate, pageable).run {
            return this.map { it.toReservationDto() }
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
                this.text = reservation.text
                this. nrOfPeople = reservation.nrOfPeople
                reservationRepository.save(this).run {
                    return this.toReservationDto()
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