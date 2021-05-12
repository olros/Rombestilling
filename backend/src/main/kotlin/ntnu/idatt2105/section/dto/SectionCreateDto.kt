package ntnu.idatt2105.section.dto

import java.util.*

data class SectionCreateDto(var id: UUID = UUID.randomUUID(),
                            var name: String = "",
                            var capacity: Int = 0,
                            var picture: String = "",)