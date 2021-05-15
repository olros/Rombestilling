package ntnu.idatt2105.section.dto

import java.util.*
import javax.validation.constraints.Positive

data class SectionCreateDto(var id: UUID = UUID.randomUUID(),
                            var name: String = "",
                            var description: String = "",
                            @get:Positive(message = "Section capacity must be positive")
                            var capacity: Int = 1,
                            var image: String = "",
                            var parentId: UUID? = null)
