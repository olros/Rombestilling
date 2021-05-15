package ntnu.idatt2105.section.service

import com.querydsl.core.types.ExpressionUtils
import com.querydsl.core.types.Predicate
import ntnu.idatt2105.dto.response.Response
import ntnu.idatt2105.exception.ApplicationException
import ntnu.idatt2105.exception.EntityType
import ntnu.idatt2105.exception.ExceptionType
import ntnu.idatt2105.reservation.model.QReservation
import ntnu.idatt2105.section.dto.SectionCreateDto
import ntnu.idatt2105.section.dto.SectionDto
import ntnu.idatt2105.section.dto.SectionListDto
import ntnu.idatt2105.section.model.QSection
import ntnu.idatt2105.section.model.Section
import ntnu.idatt2105.section.repository.SectionRepository
import org.modelmapper.ModelMapper
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*


@Service
class SectionServiceImpl(val sectionRepository: SectionRepository, val modelMapper: ModelMapper): SectionService {
    val logger = LoggerFactory.getLogger("SectionService")

    override fun getAllSections(pageable: Pageable, predicate: Predicate): Page<SectionListDto> {
        val sections = sectionRepository.findAll(predicate,pageable)
        logger.debug("Finding all sections")
        return sections.map{ modelMapper.map(it, SectionListDto::class.java)}
    }

    override fun createSection(section: SectionCreateDto): SectionDto {
        logger.debug("Trying to create a new section with name ${section.name}")
        var newSection = Section(section.id, section.name, section.description, section.capacity, section.image)
        if(section.parentId != null) newSection.parent = sectionRepository.findById(section.parentId!!)
                .orElseThrow { throw ApplicationException.throwException(
                        EntityType.SECTION, ExceptionType.ENTITY_NOT_FOUND, section.parentId.toString())  }
        newSection.children = mutableListOf()
        newSection = sectionRepository.save(newSection)
        logger.debug("Section ${newSection.id} was created")
        val savedSection = modelMapper.map(newSection, SectionDto::class.java)
        if(section.parentId != null) return addChildToSection(section.parentId!!, newSection)
        return savedSection
    }


    override fun getSectionById(id: UUID): SectionDto {
        val section = sectionRepository.findById(id).orElseThrow{ throw ApplicationException.throwException(
                EntityType.SECTION, ExceptionType.ENTITY_NOT_FOUND, id.toString())  }
        logger.debug("Finding section by id: $id")
        return modelMapper.map(section, SectionDto::class.java)
    }

    override fun updateSection(id: UUID, section: SectionDto): SectionDto {
        sectionRepository.findById(id).orElseThrow { throw ApplicationException.throwException(
                EntityType.SECTION, ExceptionType.ENTITY_NOT_FOUND, id.toString())  }.run {
            var updatedSection = this.copy(
                    name = section.name,
                    capacity = section.capacity,
                image = section.image,
                    description = section.description
            )
            updatedSection = sectionRepository.save(updatedSection)
            logger.debug("Section: ${section.name} was updated")
            return modelMapper.map(updatedSection, SectionDto::class.java)
        }

    }

    override fun deleteSection(id: UUID): Response {
        sectionRepository.findById(id).orElseThrow { throw ApplicationException.throwException(
                EntityType.SECTION, ExceptionType.ENTITY_NOT_FOUND, id.toString()) }.run {
            sectionRepository.delete(this)
            logger.debug("Section with id: $id was deleted")
            return Response("Section was deleted")
        }

    }

    override fun addChildToSection(parentId: UUID, child: Section) : SectionDto{
        val parent = sectionRepository.findById(parentId).orElseThrow { throw ApplicationException.throwException(
                EntityType.SECTION, ExceptionType.ENTITY_NOT_FOUND, parentId.toString())  }
        parent.children.add(child)
        sectionRepository.save(parent)
        logger.debug("Child section with id: ${child.id} was added to parent with id: $parentId")
        return modelMapper.map(child, SectionDto::class.java)
    }
}
