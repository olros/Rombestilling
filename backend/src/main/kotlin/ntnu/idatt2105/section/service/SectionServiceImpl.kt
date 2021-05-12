package ntnu.idatt2105.section.service

import ntnu.idatt2105.section.dto.SectionCreateDto
import ntnu.idatt2105.section.dto.SectionDto
import ntnu.idatt2105.section.exception.SectionNotFoundException
import ntnu.idatt2105.section.model.Section
import ntnu.idatt2105.section.repository.SectionRepository
import org.modelmapper.ModelMapper
import java.util.*

class SectionServiceImpl(val sectionRepository: SectionRepository, val modelMapper: ModelMapper): SectionService {
    override fun createSection(section: SectionCreateDto): SectionDto {
        var section = modelMapper.map(section, Section::class.java)
        section = sectionRepository.save(section)
        return modelMapper.map(section, SectionDto::class.java)
    }

    override fun getSectionById(id: UUID): SectionDto {
        val section = sectionRepository.findById(id).orElseThrow{ throw SectionNotFoundException() }
        return modelMapper.map(section, SectionDto::class.java)
    }

    override fun updateSection(id: UUID, section: SectionDto): SectionDto {
        var section = sectionRepository.findById(id).orElseThrow { throw SectionNotFoundException() }.run {
            val updatedSection = this.copy(
                    name =
            )
        }

    }

    override fun deleteSection(id: UUID): SectionDto {
        TODO("Not yet implemented")
    }
}