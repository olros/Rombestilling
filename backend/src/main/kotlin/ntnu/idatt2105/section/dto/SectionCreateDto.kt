package ntnu.idatt2105.section.dto

import java.util.*

data class SectionCreateDto(var id: UUID = UUID.randomUUID(),
                            var name: String = "",
                            var description: String = "",
                            var capacity: Int = 0,
                            var image: String = "",
                            var parentId: UUID? = null)
