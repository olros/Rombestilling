package ntnu.idatt2105.section.service

import com.querydsl.core.types.ExpressionUtils
import com.querydsl.core.types.Predicate
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
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class SectionServiceImpl(
    val sectionRepository: SectionRepository,
    val modelMapper: ModelMapper
) : SectionService {

    override fun getAllSections(pageable: Pageable, predicate: Predicate): Page<SectionListDto> {
        val sections = sectionRepository.findAll(predicate,pageable)
        return sections.map{ modelMapper.map(it, SectionListDto::class.java)}
    }

	override fun createSection(section: SectionCreateDto): SectionDto {
		var newSection = Section(section.id, section.name, section.description, section.capacity, section.image)
		if (section.parentId != null) newSection.parent = sectionRepository.findById(section.parentId!!)
			.orElseThrow {
				throw ApplicationException.throwException(
					EntityType.SECTION, ExceptionType.ENTITY_NOT_FOUND, section.parentId.toString()
				)
			}
		newSection.children = mutableListOf()
		newSection = sectionRepository.save(newSection)
		val savedSection = modelMapper.map(newSection, SectionDto::class.java)
		if (section.parentId != null) return addChildToSection(section.parentId!!, newSection)
		return savedSection
	}

	override fun getSectionById(id: UUID): SectionDto {
		val section = sectionRepository.findById(id).orElseThrow {
			throw ApplicationException.throwException(
				EntityType.SECTION, ExceptionType.ENTITY_NOT_FOUND, id.toString()
			)
		}
		return modelMapper.map(section, SectionDto::class.java)
	}

	override fun updateSection(id: UUID, section: SectionDto): SectionDto {
		sectionRepository.findById(id).orElseThrow {
			throw ApplicationException.throwException(
				EntityType.SECTION, ExceptionType.ENTITY_NOT_FOUND, id.toString()
			)
		}.run {
			var updatedSection = this.copy(
				name = section.name,
				capacity = section.capacity,
				image = section.image,
				description = section.description
			)
			updatedSection = sectionRepository.save(updatedSection)
			return modelMapper.map(updatedSection, SectionDto::class.java)
		}
	}

	override fun deleteSection(id: UUID) {
		sectionRepository.findById(id).orElseThrow {
			throw ApplicationException.throwException(
				EntityType.SECTION, ExceptionType.ENTITY_NOT_FOUND, id.toString()
			)
		}.run {
			sectionRepository.delete(this)
		}
	}

	override fun addChildToSection(parentId: UUID, child: Section): SectionDto {
		val parent = sectionRepository.findById(parentId).orElseThrow {
			throw ApplicationException.throwException(
				EntityType.SECTION, ExceptionType.ENTITY_NOT_FOUND, parentId.toString()
			)
		}
		parent.children.add(child)
		sectionRepository.save(parent)
		return modelMapper.map(child, SectionDto::class.java)
	}
}
