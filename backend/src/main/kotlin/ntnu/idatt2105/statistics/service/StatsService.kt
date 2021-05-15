package ntnu.idatt2105.statistics.service

import com.querydsl.core.types.Predicate
import ntnu.idatt2105.statistics.dto.StatsDto
import java.util.*

interface StatsService {
    fun getStatisticsForSection(sectionID: UUID, predicate: Predicate): StatsDto
}
