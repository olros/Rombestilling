package ntnu.idatt2105.reservation.repository

import ntnu.idatt2105.reservation.model.GroupReservation
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface GroupReservationRepository : ReservationRepository<GroupReservation> {


}