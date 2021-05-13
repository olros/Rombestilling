package ntnu.idatt2105.reservation.repository

import ntnu.idatt2105.reservation.model.Reservation
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.data.repository.query.Param
import java.time.ZonedDateTime
import java.util.*

interface ReservationRepository : JpaRepository<Reservation, UUID> {
    fun findReservationsByUserId(userId: UUID, pageable: Pageable) : Page<Reservation>
    @Query( "SELECT COUNT(ent) > 0 FROM Reservation ent WHERE ent.fromTime <= :toTime AND ent.toTime >= :fromTime")
    fun existsInterval(@Param("fromTime")fromTime: ZonedDateTime, @Param("toTime")toTime: ZonedDateTime): Boolean
    fun findReservationsBySectionId(sectionId: UUID, pageable: Pageable) : Page<Reservation>
    fun findReservationByIdAndSectionId(userId: UUID, sectionId: UUID) : Reservation?
}