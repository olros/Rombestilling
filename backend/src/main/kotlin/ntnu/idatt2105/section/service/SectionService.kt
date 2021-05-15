package ntnu.idatt2105.section.service

import com.querydsl.core.types.Predicate
import ntnu.idatt2105.section.dto.SectionCreateDto
import ntnu.idatt2105.section.dto.SectionDto
import ntnu.idatt2105.section.dto.SectionListDto
import ntnu.idatt2105.section.model.Section
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface SectionService {
    fun getAllSections(pageable: Pageable, predicate: Predicate): Page<SectionListDto>
    fun createSection(section: SectionCreateDto): SectionDto
    fun getSectionById(id: UUID): SectionDto
    fun updateSection(id: UUID, section: SectionDto): SectionDto
    fun deleteSection(id: UUID)
    fun addChildToSection(parentId: UUID, child: Section): SectionDto
}
