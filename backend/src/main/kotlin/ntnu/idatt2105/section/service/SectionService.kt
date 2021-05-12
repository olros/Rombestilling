package ntnu.idatt2105.section.service

import ntnu.idatt2105.section.dto.SectionCreateDto
import ntnu.idatt2105.section.dto.SectionDto
import ntnu.idatt2105.section.dto.SectionListDto
import ntnu.idatt2105.section.model.Section
import ntnu.idatt2105.user.dto.UserDto
import ntnu.idatt2105.user.dto.UserRegistrationDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface SectionService {
    fun getAllSections(pageable: Pageable): Page<SectionListDto>
    fun createSection(section: SectionCreateDto): SectionDto
    fun createChild(section: SectionCreateDto, parent: Section): Section
    fun getSectionById(id: UUID): SectionDto
    fun updateSection(id: UUID, section: SectionDto): SectionDto
    fun deleteSection(id: UUID)
    fun addChildToSection(parentId: UUID, child: SectionCreateDto) : SectionDto
}