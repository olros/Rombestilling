package ntnu.idatt2105.section.dto

import ntnu.idatt2105.util.SectionType
import java.util.*

data class SectionDto(

        var id: UUID = UUID.randomUUID(),
        var name: String = "",
        var capacity: Int = 0,
        var description: String = "",
        var image: String = "",
        var type: String = SectionType.ROOM,
        var parent: SectionChildrenDto? = null,
        var children: MutableList<SectionChildrenDto> = mutableListOf()
)