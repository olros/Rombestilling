package ntnu.idatt2105.statistics.service

import com.querydsl.core.types.Predicate
import ntnu.idatt2105.exception.ApplicationException
import ntnu.idatt2105.exception.EntityType
import ntnu.idatt2105.exception.ExceptionType
import ntnu.idatt2105.section.model.Section
import ntnu.idatt2105.section.repository.SectionRepository
import ntnu.idatt2105.statistics.dto.StatsDto
import ntnu.idatt2105.user.model.User
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.Duration
import java.util.*

@Service
class StatsServiceImpl(val sectionRepository: SectionRepository) : StatsService {
    val logger = LoggerFactory.getLogger("StatsService")

    override fun getStatisticsForSection(sectionID: UUID, predicate: Predicate): StatsDto {
        val section: Section = sectionRepository.findById(sectionID).orElseThrow {
            throw ApplicationException.throwException(
                EntityType.SECTION, ExceptionType.ENTITY_NOT_FOUND, sectionID.toString()
            )
        }

        logger.info("Getting statistics for sections")
        return StatsDto(
            nrOfReservation = getNrOfReservations(section),
            hoursOfReservation = getHoursOfReservation(section),
            daysWithReservation = getDaysWithReservation(section),
            userReservationCount = getUserReservationCount(section),
        )
    }

    private fun getUserReservationCount(section: Section): Int {
        val listOfUsers: MutableList<User> = mutableListOf()
        section.reservation.forEach {
            it.user?.let { it1 -> listOfUsers.add(it1) }
        }
        return listOfUsers.distinct().size
    }

    private fun getDaysWithReservation(section: Section): Int {
        val count = 0
        section.reservation.forEach{

        }

        return count
    }

    private fun getNrOfReservations(section: Section): Int {
        return section.reservation.size
    }

    private fun getHoursOfReservation(sections: Section): Double {
        var count = 0.0
        sections.reservation.forEach {
            count += (Duration.between(it.fromTime, it.toTime)).toHours()
        }
        return count
    }
}
