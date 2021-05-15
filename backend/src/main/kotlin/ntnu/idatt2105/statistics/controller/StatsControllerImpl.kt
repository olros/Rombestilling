package ntnu.idatt2105.statistics.controller

import com.querydsl.core.types.Predicate
import ntnu.idatt2105.statistics.dto.StatsDto
import ntnu.idatt2105.statistics.service.StatsService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.util.*

class StatsControllerImpl(val statsService: StatsService): StatsController {

    override fun getStatistics(sectionId: UUID, predicate: Predicate): ResponseEntity<StatsDto> =
        ResponseEntity(statsService.getStatisticsForSection(sectionId, predicate), HttpStatus.OK)
}
