package ntnu.idatt2105.reservation.service

import ntnu.idatt2105.exception.ApplicationException
import ntnu.idatt2105.exception.EntityType
import ntnu.idatt2105.exception.ExceptionType
import ntnu.idatt2105.reservation.dto.ReservationCreateDto
import ntnu.idatt2105.reservation.dto.ReservationDto
import ntnu.idatt2105.reservation.model.Reservation
import ntnu.idatt2105.reservation.model.ReservationId
import ntnu.idatt2105.reservation.repository.ReservationRepository
import ntnu.idatt2105.section.repository.SectionRepository
import ntnu.idatt2105.user.repository.UserRepository
import org.modelmapper.ModelMapper
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*


@Service
class ReservationServiceImpl(
    val reservationRepository: ReservationRepository,
    val modelMapper: ModelMapper,
    val userRepository: UserRepository,
    val sectionRepository: SectionRepository) : ReservationService {

    override fun getAllReservation(sectionId: UUID, pageable: Pageable): Page<ReservationDto> =
            reservationRepository.findReservationsBySectionId(sectionId, pageable).run {
            return this.map { modelMapper.map(it, ReservationDto::class.java) }
    }

    override fun createReservation(sectionId: UUID, reservation: ReservationCreateDto) : ReservationDto {
        if (reservationRepository.existsInterval(reservation.fromTime!!, reservation.toTime!!)){
            throw ApplicationException.throwException(
                    EntityType.RESERVATION, ExceptionType.DUPLICATE_ENTITY, reservation.fromTime.toString(), reservation.toTime.toString())
        }
        var newReservation = modelMapper.map(reservation, Reservation::class.java)

        val user = userRepository.findById(reservation.userId!!).orElseThrow { throw ApplicationException.throwException(
                EntityType.USER, ExceptionType.ENTITY_NOT_FOUND, reservation.userId.toString()) }

        val section = sectionRepository.findById(sectionId).orElseThrow { throw ApplicationException.throwException(
                EntityType.SECTION, ExceptionType.ENTITY_NOT_FOUND, reservation.sectionId.toString()) }

        newReservation.user = user
        newReservation.section = section
        newReservation = reservationRepository.save(newReservation)
        return modelMapper.map(newReservation, ReservationDto::class.java)
    }

    override fun getUserReservation(userId: UUID, pageable: Pageable): Page<ReservationDto> =
        reservationRepository.findReservationsByUserId(userId, pageable).run {
            return this.map { modelMapper.map(it, ReservationDto::class.java) }
        }

    override fun updateReservation(sectionId: UUID, reservationId: UUID, reservation: ReservationDto): ReservationDto {
        reservationRepository.findReservationByIdAndSectionId(reservationId, sectionId).run {
            if(this != null){
                val updatedReservation = this.copy(
                        fromTime = reservation.fromTime,
                        toTime = reservation.toTime,
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