package ntnu.idatt2105.section.service

import ntnu.idatt2105.section.dto.SectionCreateDto
import ntnu.idatt2105.section.dto.SectionDto
import ntnu.idatt2105.section.dto.SectionListDto
import ntnu.idatt2105.section.exception.SectionNotFoundException
import ntnu.idatt2105.section.model.Section
import ntnu.idatt2105.section.repository.SectionRepository
import org.modelmapper.ModelMapper
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*


@Service
class SectionServiceImpl(val sectionRepository: SectionRepository, val modelMapper: ModelMapper): SectionService {

    override fun getAllSections(pageable: Pageable): Page<SectionListDto> {
        val sections = sectionRepository.findAll(pageable)
        return sections.map{ modelMapper.map(it, SectionListDto::class.java)}
    }

    override fun createSection(section: SectionCreateDto): SectionDto {
        var newSection = Section(section.id, section.name, section.description, section.capacity, section.picture)
        if(section.parentId != null) newSection.parent = sectionRepository.findById(section.parentId!!)
                .orElseThrow { throw SectionNotFoundException("Could not find parent") }
        newSection.children = mutableListOf()
        newSection = sectionRepository.save(newSection)
        val savedSection = modelMapper.map(newSection, SectionDto::class.java)
        if(section.parentId != null) return addChildToSection(section.parentId!!, newSection)
        return savedSection
    }


    override fun getSectionById(id: UUID): SectionDto {
        val section = sectionRepository.findById(id).orElseThrow{ throw SectionNotFoundException() }
        return modelMapper.map(section, SectionDto::class.java)
    }

    override fun updateSection(id: UUID, section: SectionDto): SectionDto {
        sectionRepository.findById(id).orElseThrow { throw SectionNotFoundException() }.run {
            var updatedSection = this.copy(
                    name = section.name,
                    capacity = section.capacity,
                    picture = section.picture,
                    description = section.description
            )
            updatedSection = sectionRepository.save(updatedSection)
            return modelMapper.map(updatedSection, SectionDto::class.java)
        }

    }

    override fun deleteSection(id: UUID) {
        sectionRepository.findById(id).orElseThrow { throw SectionNotFoundException() }.run {
            sectionRepository.delete(this)
        }

    }

    override fun addChildToSection(parentId: UUID, child: Section) : SectionDto{
        val parent = sectionRepository.findById(parentId).orElseThrow { throw SectionNotFoundException() }
        parent.children.add(child)
        sectionRepository.save(parent)
        return modelMapper.map(child, SectionDto::class.java)

    }

}