package ntnu.idatt2105.section.dto

import ntnu.idatt2105.section.model.Section
import ntnu.idatt2105.util.SectionType
import java.util.*

data class SectionListDto (
        var id: UUID = UUID.randomUUID(),
        var name: String = "",
        var capacity: Int = 0,
        var type: String = SectionType.ROOM,
        var parent: SectionListDto? = null,

        )
