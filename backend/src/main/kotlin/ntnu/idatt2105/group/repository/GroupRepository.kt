package ntnu.idatt2105.group.repository

import ntnu.idatt2105.group.model.Group
import ntnu.idatt2105.group.model.QGroup
import ntnu.idatt2105.reservation.model.QReservation
import ntnu.idatt2105.reservation.model.Reservation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer
import org.springframework.data.querydsl.binding.QuerydslBindings
import java.util.*

interface GroupRepository: JpaRepository<Group, UUID>, QuerydslPredicateExecutor<Group>, QuerydslBinderCustomizer<QGroup> {
    @JvmDefault
    override fun customize(bindings: QuerydslBindings, group: QGroup) {
        bindings.bind(group.name).first{_, value -> group.name.contains(value)}

    }

}