package ntnu.idatt2105.reservation.repository

import ntnu.idatt2105.reservation.model.UserReservation
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserReservationRepository : ReservationRepository<UserReservation>