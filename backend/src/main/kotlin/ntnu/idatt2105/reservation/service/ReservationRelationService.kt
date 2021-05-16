package ntnu.idatt2105.reservation.service

import java.util.*

interface ReservationRelationService <T> {
    fun getByEntityId(id: UUID) : T
}