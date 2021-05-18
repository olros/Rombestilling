package ntnu.idatt2105.group.repository

import ntnu.idatt2105.group.model.Group
import ntnu.idatt2105.reservation.model.Reservation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import java.util.*

interface GroupRepository: JpaRepository<Group, UUID>, QuerydslPredicateExecutor<Group>