package ntnu.idatt2105.section.service

import com.querydsl.core.types.Predicate
import ntnu.idatt2105.factories.SectionFactory
import ntnu.idatt2105.section.dto.SectionCreateDto
import ntnu.idatt2105.section.dto.SectionDto
import ntnu.idatt2105.section.model.Section
import ntnu.idatt2105.section.repository.SectionRepository
import ntnu.idatt2105.util.JpaUtils
import ntnu.idatt2105.util.SectionType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.any
import org.mockito.Mockito.lenient
import org.mockito.Spy
import org.mockito.junit.jupiter.MockitoExtension
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import java.util.Optional
import java.util.UUID

@ExtendWith(MockitoExtension::class)
class SectionServiceTest {

	@InjectMocks
	private lateinit var sectionService: SectionServiceImpl

	@Mock
	private lateinit var sectionRepository: SectionRepository

	@Spy
	@Autowired
	private lateinit var modelMapper: ModelMapper

	private lateinit var section: Section

	@BeforeEach
	fun setUp() {
		section = SectionFactory().`object`
		lenient().`when`(sectionRepository.findById(section.id)).thenReturn(Optional.of(section))
	}

    @Test
    fun `test section service get all returns page of sections`() {
		val testList: List<Section> = mutableListOf(section)
		val page = JpaUtils().getDefaultPageable()
		val predicate = JpaUtils().getEmptyPredicate()
		val sections: Page<Section> = PageImpl(testList, JpaUtils().getDefaultPageable(), testList.size.toLong())
		lenient().`when`(sectionRepository.findAll(any(Predicate::class.java), any(Pageable::class.java))).thenReturn(sections)
		assert(sectionService.getAllSections(page, predicate).content.isNotEmpty())
		assertThat(sectionService.getAllSections(JpaUtils().getDefaultPageable(), predicate).content.size == testList.size)
	}
	@Test
	fun `test section service createSection returns new section`() {
		val newSection = SectionFactory().`object`
		val newSectionDto = SectionCreateDto(UUID.randomUUID(), newSection.name, newSection.description, newSection.capacity, newSection.image)
		lenient().`when`(sectionRepository.save(any(Section::class.java))).thenReturn(newSection)
		assertThat(sectionService.createSection(newSectionDto).name).isEqualTo(newSectionDto.name)
	}

	@Test
	fun `test section service updateSection returns update section`() {
		val name = "newname"
		section.name = name
		val sectionDto = SectionDto(section.id, name, section.capacity, section.image, "", SectionType.ROOM)
		lenient().`when`(sectionRepository.save(any(Section::class.java))).thenReturn(section)
		assertThat(sectionService.updateSection(section.id, sectionDto).name).isEqualTo(name)
	}
}
