package ntnu.idatt2105.section.controller

import com.querydsl.core.types.Predicate
import ntnu.idatt2105.section.dto.SectionCreateDto
import ntnu.idatt2105.section.dto.SectionDto
import ntnu.idatt2105.section.service.SectionService
import ntnu.idatt2105.util.PaginationConstants
import ntnu.idatt2105.dto.response.Response
import ntnu.idatt2105.section.model.Section
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import javax.validation.Valid

@RestController
@RequestMapping("/sections/")
class SectionController(val sectionService: SectionService) {

    @GetMapping
    fun getAllSections(
            @QuerydslPredicate(root = Section::class) predicate: Predicate,
            @PageableDefault(size = PaginationConstants.PAGINATION_SIZE,
            sort= ["name"], direction = Sort.Direction.DESC) pageable: Pageable) =
            sectionService.getAllSections(pageable,predicate)

	@PostMapping
	fun createSection(@RequestBody sectionCreateDto: @Valid SectionCreateDto) =
		ResponseEntity(sectionService.createSection(sectionCreateDto), HttpStatus.CREATED)

	@GetMapping("{sectionId}/")
	fun getSection(@PathVariable sectionId: UUID): ResponseEntity<SectionDto> {
		return ResponseEntity(sectionService.getSectionById(sectionId), HttpStatus.OK)
	}

	@PutMapping("{sectionId}/")
	fun updateSection(
	    @PathVariable sectionId: UUID,
	    @RequestBody section: SectionDto
	) =
		ResponseEntity(sectionService.updateSection(sectionId, section), HttpStatus.OK)

	@DeleteMapping("{sectionId}/")
	fun deleteSection(@PathVariable sectionId: UUID): ResponseEntity<Response> {
		sectionService.deleteSection(sectionId)
		return ResponseEntity(Response("Section has been deleted"), HttpStatus.OK)
	}
}
