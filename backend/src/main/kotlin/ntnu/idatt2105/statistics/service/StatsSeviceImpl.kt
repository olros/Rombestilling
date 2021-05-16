package ntnu.idatt2105.statistics.service

import com.querydsl.core.types.ExpressionUtils
import com.querydsl.core.types.Predicate
import ntnu.idatt2105.exception.ApplicationException
import ntnu.idatt2105.exception.EntityType
import ntnu.idatt2105.exception.ExceptionType
import ntnu.idatt2105.reservation.model.QReservation
import ntnu.idatt2105.reservation.model.Reservation
import ntnu.idatt2105.reservation.repository.ReservationRepository
import ntnu.idatt2105.section.model.Section
import ntnu.idatt2105.section.repository.SectionRepository
import ntnu.idatt2105.statistics.dto.StatsDto
import ntnu.idatt2105.user.model.User
import org.apache.commons.collections4.IterableUtils
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.LocalDate
import java.time.ZonedDateTime
import java.util.*

@Service
class StatsServiceImpl(
    val sectionRepository: SectionRepository,
    val reservationRepository: ReservationRepository
) : StatsService {
    val logger = LoggerFactory.getLogger("StatsService")

    override fun getStatisticsForSection(sectionID: UUID, predicate: Predicate): StatsDto {
        val reservation = QReservation.reservation
        val newPredicate = ExpressionUtils.allOf(predicate, reservation.section.id.eq(sectionID))!!
        val reservations: Iterable<Reservation> = reservationRepository.findAll(newPredicate)
        if (IterableUtils.size(reservations) == 0) throw ApplicationException.throwException(
            EntityType.RESERVATION, ExceptionType.ENTITY_NOT_FOUND, sectionID.toString()
        )

        logger.info("Getting statistics for sections")
        return StatsDto(
            nrOfReservation = getNrOfReservations(reservations),
            hoursOfReservation = getHoursOfReservation(reservations),
            daysWithReservation = getDaysWithReservation(reservations),
            userReservationCount = getUserReservationCount(reservations),
        )
    }

    private fun getUserReservationCount(reservations: Iterable<Reservation>): Int {
        val listOfUsers: MutableList<User> = mutableListOf()
        reservations.forEach {
            it.user?.let { it1 -> listOfUsers.add(it1) }

        }
        return listOfUsers.distinct().size
    }

    private fun getDaysWithReservation(reservations: Iterable<Reservation>): Int {
        val setOfDates = mutableSetOf<LocalDate>()
        reservations.forEach {
            setOfDates.add(it.fromTime!!.toLocalDate())
        }
        return setOfDates.size
    }

    private fun getNrOfReservations(reservations: Iterable<Reservation>): Int {
        return IterableUtils.size(reservations)
    }

    private fun getHoursOfReservation(reservations: Iterable<Reservation>): Long {
        var count = 0L
        reservations.forEach {
            count += (Duration.between(it.fromTime, it.toTime)).toHours()
        }
        return count
    }
}
