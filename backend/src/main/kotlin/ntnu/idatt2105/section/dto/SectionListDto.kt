package ntnu.idatt2105.section.dto

import ntnu.idatt2105.section.model.Section
import java.util.*

data class SectionListDto (
        var id: UUID = UUID.randomUUID(),
        var name: String = "",
       )
