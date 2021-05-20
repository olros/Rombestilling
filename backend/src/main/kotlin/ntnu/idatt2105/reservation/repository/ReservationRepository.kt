package ntnu.idatt2105.reservation.repository

import com.querydsl.core.types.Predicate
import ntnu.idatt2105.reservation.model.QReservation
import ntnu.idatt2105.reservation.model.Reservation
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer
import org.springframework.data.querydsl.binding.QuerydslBindings
import org.springframework.data.querydsl.binding.QuerydslPredicate
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.ZonedDateTime
import java.util.*

@Repository
interface ReservationRepository : JpaRepository<Reservation, UUID>, QuerydslPredicateExecutor<Reservation>, QuerydslBinderCustomizer<QReservation> {
    fun findReservationsByUserId(userId: UUID, pageable: Pageable) : Page<Reservation>
    @Query("SELECT COUNT(ent) > 0 FROM Reservation ent WHERE ent.fromTime < :toTime AND ent.toTime > :fromTime AND ent.section.id = :sectionId")
    fun existsInterval(@Param("fromTime") fromTime: ZonedDateTime, @Param("toTime") toTime: ZonedDateTime, @Param("sectionId") sectionId: UUID): Boolean
    fun findReservationsBySectionId(sectionId: UUID, pageable: Pageable) : Page<Reservation>
    fun findReservationByIdAndSectionId(userId: UUID, sectionId: UUID) : Reservation?

    @JvmDefault
    override fun customize(bindings: QuerydslBindings, reservation: QReservation) {
        bindings.bind(reservation.fromTimeAfter).first { path, value -> reservation.fromTime.after(value) }
        bindings.bind(reservation.toTimeBefore).first { path, value -> reservation.toTime.before(value) }
    }


}
