package ntnu.idatt2105.section.dto

import ntnu.idatt2105.section.model.Section
import ntnu.idatt2105.util.SectionType
import java.util.*
import javax.persistence.FetchType
import javax.persistence.ManyToOne
import javax.persistence.OneToMany

data class SectionDto(

        var id: UUID = UUID.randomUUID(),
        var name: String = "",
        var capacity: Int = 0,
        var description: String = "",
        var picture: String = "",
        var type: SectionType = SectionType.ROOM,
        var children: MutableList<SectionListDto> = mutableListOf()
)