package ntnu.idatt2105.section.repository

import ntnu.idatt2105.section.model.Section
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface SectionRepository: JpaRepository<Section, UUID> {
}