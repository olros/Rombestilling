package ntnu.idatt2105.section.repository

import ntnu.idatt2105.section.model.QSection
import ntnu.idatt2105.section.model.Section
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer
import org.springframework.data.querydsl.binding.QuerydslBindings
import java.util.*

interface SectionRepository: JpaRepository<Section, UUID>, QuerydslPredicateExecutor<Section>, QuerydslBinderCustomizer<QSection> {

    @JvmDefault
    override fun customize(bindings: QuerydslBindings, section: QSection) {
        bindings.bind(section.name).first { path, value -> section.name.contains(value) }
        bindings.bind(section.from, section.to).first { path, value -> section.reservation.any().fromTime.before(value) }
        bindings.bind(section.to).first { path, value ->!section.reservation.any().toTime.after(value) }

    }
}
