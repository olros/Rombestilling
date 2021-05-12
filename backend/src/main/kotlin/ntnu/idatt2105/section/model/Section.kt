package ntnu.idatt2105.section.model

import ntnu.idatt2105.util.SectionType
import java.util.*
import javax.persistence.*

@Entity
data class Section(
        @Id
        @Column(columnDefinition = "CHAR(32)")
        var id: UUID = UUID.randomUUID(),
        var name: String = "",
        var description: String = "",
        var capacity: Int = 0,
        var picture: String,
        @OneToMany(fetch = FetchType.EAGER,mappedBy = "parent" , cascade =[CascadeType.ALL])
        var children: MutableList<Section>? = mutableListOf(),
        @ManyToOne
        var parent: Section? = null
        ){
        fun getType(): SectionType{
                if (parent != null)
                        return SectionType.SECTION

                return SectionType.ROOM
        }
}

