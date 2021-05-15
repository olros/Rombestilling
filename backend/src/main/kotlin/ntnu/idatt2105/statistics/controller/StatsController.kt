package ntnu.idatt2105.statistics.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import ntnu.idatt2105.section.model.Section
import org.springframework.data.querydsl.binding.QuerydslPredicate
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import java.util.*
import com.querydsl.core.types.Predicate
import ntnu.idatt2105.statistics.dto.StatsDto

interface StatsController {

    @Operation(
        summary = "Get statistics from a section", responses = [
            ApiResponse(responseCode = "200", description = "Success"),
            ApiResponse(responseCode = "400", description = "Bad request: could not get statistics"),
        ]
    )
    @GetMapping("{sectionId}")
    fun getStatistics(
        @PathVariable sectionId: UUID,
        @QuerydslPredicate(root = Section::class) predicate: Predicate
    ): ResponseEntity<StatsDto>
}
