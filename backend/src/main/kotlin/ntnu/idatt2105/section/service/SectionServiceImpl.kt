package ntnu.idatt2105.section.service

import ntnu.idatt2105.section.dto.SectionCreateDto
import ntnu.idatt2105.section.dto.SectionDto
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

    override fun getAllSections(pageable: Pageable): Page<SectionDto> {
        val sections = sectionRepository.findAll(pageable)
        return sections.map{ modelMapper.map(it, SectionDto::class.java)}
    }

    override fun createSection(section: SectionCreateDto): SectionDto {
        var newSection = modelMapper.map(section, Section::class.java)
        newSection = sectionRepository.save(newSection)
        return modelMapper.map(newSection, SectionDto::class.java)
    }

    override fun createChild(section: SectionCreateDto, parent: Section): Section {
        var newSection = modelMapper.map(section, Section::class.java)
        newSection.parent = parent
        return sectionRepository.save(newSection)
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

    override fun addChildToSection(parentId: UUID, child: SectionCreateDto) : SectionDto{
        var parent = sectionRepository.findById(parentId).orElseThrow { throw SectionNotFoundException() }
        val newChild = createChild(child, parent)
        parent.children?.add(newChild)
        sectionRepository.save(parent)
        return modelMapper.map(newChild, SectionDto::class.java)

    }

}