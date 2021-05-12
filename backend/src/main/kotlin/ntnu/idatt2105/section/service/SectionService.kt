package ntnu.idatt2105.section.service

import ntnu.idatt2105.section.dto.SectionCreateDto
import ntnu.idatt2105.section.dto.SectionDto
import ntnu.idatt2105.user.dto.UserDto
import ntnu.idatt2105.user.dto.UserRegistrationDto
import java.util.*

interface SectionService {
    fun createSection(user: SectionCreateDto): SectionDto
    fun getSectionById(id: UUID): SectionDto
    fun updateSection(id: UUID, section: SectionDto): SectionDto
    fun deleteSection(id: UUID): SectionDto
}