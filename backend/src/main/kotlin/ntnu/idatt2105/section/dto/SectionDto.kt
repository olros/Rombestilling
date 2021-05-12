package ntnu.idatt2105.section.dto

import ntnu.idatt2105.section.model.Section
import java.util.*
import javax.persistence.FetchType
import javax.persistence.ManyToOne
import javax.persistence.OneToMany

data class SectionDto(
                    var id: UUID = UUID.randomUUID(),
                    var name: String = "",
                    var capacity: Int = 0,
                    var picture: String = "",
                    var children: MutableList<SectionListDto>? = mutableListOf())