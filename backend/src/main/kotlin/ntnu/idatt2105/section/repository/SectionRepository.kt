package ntnu.idatt2105.section.repository

import com.querydsl.core.BooleanBuilder
import com.querydsl.jpa.JPAExpressions
import ntnu.idatt2105.reservation.model.QReservation
import ntnu.idatt2105.section.model.QSection
import ntnu.idatt2105.section.model.Section
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer
import org.springframework.data.querydsl.binding.QuerydslBindings
import java.util.*


interface SectionRepository : JpaRepository<Section, UUID>, QuerydslPredicateExecutor<Section>,
    QuerydslBinderCustomizer<QSection> {

    @JvmDefault
    override fun customize(bindings: QuerydslBindings, section: QSection) {
        val reservation = QReservation.reservation
        //        var available: BooleanExpression = section.id.`in`(section.reservation.any().id)
//        val available: BooleanExpression = section.id.`in`(
//            JPAExpressions.selectFrom(reservation)
//                .where(reservation.fromTime.before(section.to))
//                .where(reservation.toTime.before(section.interval))
//                .select(reservation.section.id)
//        )
//        var exp: BooleanExpression = section.reservation.any().supplier.number.`in`(
//            JPAExpressions.selectFrom<Any>(company).where(company.active.isTrue()).select(company.nu‌​ mber))
        bindings.bind(section.name).first { path, value -> section.name.contains(value) }
//        bindings.bind(section.from, section.to).first { path, value -> available }
        bindings.bind(section.from, section.to).all { path, values ->
            val predicate = BooleanBuilder()
            if (values.size == 2) {
                val from = values.elementAt(0)
                val to = values.elementAt(1)
                predicate.and(
                    section.id.`in`(
                        JPAExpressions.selectFrom(reservation)
                            .where(reservation.fromTime.before(to))
                            .where(reservation.toTime.after(from))
                            .select(reservation.section.id)
                    )
                    .or(
                        section.parent.id.isNotNull.and(section.parent.id.`in`(
                            JPAExpressions.selectFrom(reservation)
                                .where(reservation.fromTime.before(to))
                                .where(reservation.toTime.after(from))
                                .select(reservation.section.id)
                        ))
                    )
                ).not()

            }
            Optional.of(predicate)
        }
//        bindings.bind(section.from).first { path, value -> (section.reservation.any().toTime.after(value).and(section.reservation.any().fromTime.before(value))).not() }
//        bindings.bind(section.to).first { path, value -> (section.reservation.any().toTime.after(value).and(section.reservation.any().fromTime.before(value))).not() }
    }
}
