package ntnu.idatt2105.factories

import io.github.serpro69.kfaker.Faker
import ntnu.idatt2105.section.model.Section
import org.springframework.beans.factory.FactoryBean
import java.util.Random
import java.util.UUID
class SectionFactory : FactoryBean<Section> {

	val faker = Faker()

	override fun getObjectType(): Class<*> {
		return Section::class.java
	}

	override fun isSingleton(): Boolean {
		return false
	}

	override fun getObject(): Section {
		return Section(UUID.randomUUID(), faker.name.name(), faker.backToTheFuture.quotes(), Random(1).nextInt(), "", mutableListOf(), null)
	}
}
