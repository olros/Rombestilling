package ntnu.idatt2105.section.model

import ntnu.idatt2105.util.UUIDModel
import java.util.UUID
import javax.persistence.*

data class Section(
        var name: String = "",
        var capacity: Int = 0,
        var picture: String,
        @OneToMany(fetch = FetchType.LAZY,mappedBy = "parent" )
        var children: MutableList<Section> = mutableListOf(),
        @ManyToOne
        var parent: Section?
        ) : UUIDModel()
