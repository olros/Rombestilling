package ntnu.idatt2105.section.service

import ntnu.idatt2105.section.dto.SectionCreateDto
import ntnu.idatt2105.section.dto.SectionDto
import ntnu.idatt2105.section.exception.SectionNotFoundException
import ntnu.idatt2105.section.repository.SectionRepository
import org.modelmapper.ModelMapper
import java.util.*

class SectionServiceImpl(val sectionRepository: SectionRepository, val modelMapper: ModelMapper): SectionService {
    override fun createSection(user: SectionCreateDto): SectionDto {
        TODO("Not yet implemented")
    }

    override fun getSectionById(id: UUID): SectionDto {
        sectionRepository.findById(id).ifPresent {
            return@ifPresent modelMapper.map(this, SectionDto::class.java)
        }
        throw SectionNotFoundException()
    }

    override fun updateSection(id: UUID, section: SectionDto): SectionDto {
        TODO("Not yet implemented")
    }

    override fun deleteSection(id: UUID): SectionDto {
        TODO("Not yet implemented")
    }
}