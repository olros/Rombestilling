package ntnu.idatt2105.section.dto

import ntnu.idatt2105.reservation.dto.ReservationDto
import ntnu.idatt2105.util.SectionType
import java.util.UUID

data class SectionDto(
        var id: UUID = UUID.randomUUID(),
        var name: String = "",
        var capacity: Int = 0,
        var description: String = "",
        var image: String = "",
        var type: String = SectionType.ROOM,
        var parent: SectionChildrenDto? = null,
        var children: MutableList<SectionChildrenDto> = mutableListOf(),
)
