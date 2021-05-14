package ntnu.idatt2105.factories

import io.github.serpro69.kfaker.Faker
import ntnu.idatt2105.reservation.model.Reservation
import ntnu.idatt2105.util.ReservationConstants
import org.springframework.beans.factory.FactoryBean
import java.time.ZonedDateTime
import java.util.Random
import java.util.UUID

class ReservationFactory : FactoryBean<Reservation> {

	val faker = Faker()
	val userFactory = UserFactory()
	val sectionFactory = SectionFactory()

	override fun getObjectType(): Class<*> {
		return Reservation::class.java
	}

	override fun isSingleton(): Boolean {
		return false
	}

	override fun getObject(): Reservation {
		val user = userFactory.`object`
		val section = sectionFactory.`object`
		val tomorrow = ZonedDateTime.now().plusDays(1)
		val earliestStartTimeTomorrow = tomorrow.with(ReservationConstants.EARLIEST_RESERVATION_TIME_OF_DAY)

		return Reservation(
			id = UUID.randomUUID(),
			user,
			section,
			earliestStartTimeTomorrow.plusHours(1),
			earliestStartTimeTomorrow.plusHours(5),
			faker.bojackHorseman.characters(),
			Random(1).nextInt()
		)
	}
}
