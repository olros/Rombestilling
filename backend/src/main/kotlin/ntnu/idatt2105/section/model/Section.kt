package ntnu.idatt2105.section.model

import java.util.*
import javax.persistence.*

@Entity
data class Section(
        @Id
        @Column(columnDefinition = "CHAR(32)")
        var id: UUID = UUID.randomUUID(),
        var name: String = "",
        var capacity: Int = 0,
        var picture: String,
        @OneToMany(fetch = FetchType.LAZY,mappedBy = "parent" )
        var children: MutableList<Section>? = mutableListOf(),
        @ManyToOne
        var parent: Section?
        )

