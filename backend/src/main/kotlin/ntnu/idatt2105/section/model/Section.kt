package ntnu.idatt2105.section.model

import com.querydsl.core.annotations.PropertyType
import com.querydsl.core.annotations.QueryType
import ntnu.idatt2105.reservation.model.Reservation
import ntnu.idatt2105.user.model.User
import ntnu.idatt2105.util.SectionType
import org.springframework.format.annotation.DateTimeFormat
import java.time.ZonedDateTime
import java.util.*
import javax.persistence.*

@Entity
data class Section(
        @Id
        @Column(columnDefinition = "CHAR(32)")
        var id: UUID = UUID.randomUUID(),
        var name: String = "",
        @Column(columnDefinition = "TEXT")
        var description: String = "",
        var capacity: Int = 0,
        var image: String,
        @OneToMany(fetch = FetchType.EAGER, mappedBy = "parent" , cascade =[CascadeType.ALL])
        var children: MutableList<Section> = mutableListOf(),
        @ManyToOne
        var parent: Section? = null,
        @OneToMany(fetch = FetchType.EAGER, mappedBy = "section")
        var reservation: MutableList<Reservation<User>> = mutableListOf()
){
        fun getType(): String{
                if (parent != null)
                        return SectionType.SECTION
                return SectionType.ROOM
        }

    fun isRoom() = getType() == SectionType.ROOM

    fun hasNoCapacityFor(section: Section): Boolean = getPreoccupationDegree() + section.capacity > this.capacity

    private fun getPreoccupationDegree(): Int = children.fold(0, { acc, next -> acc + next.capacity})

    override fun toString(): String {
        return "Section(id=$id, name='$name', description='$description', capacity=$capacity, image='$image', children=$children, reservation=$reservation, from=$from, to=$to)"
    }

    @Transient
        @QueryType(PropertyType.DATETIME)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        var from: ZonedDateTime? = null
        @Transient
        @QueryType(PropertyType.DATETIME)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        var to: ZonedDateTime? = null

}

