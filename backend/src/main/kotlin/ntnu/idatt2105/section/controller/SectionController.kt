package ntnu.idatt2105.section.controller

import ntnu.idatt2105.section.dto.SectionCreateDto
import ntnu.idatt2105.section.dto.SectionDto
import ntnu.idatt2105.section.service.SectionService
import ntnu.idatt2105.user.dto.UserDto
import ntnu.idatt2105.user.dto.UserRegistrationDto
import ntnu.idatt2105.util.PaginationConstants
import ntnu.idatt2105.util.Response
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize

import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid


@RestController
@RequestMapping("/sections/")
class SectionController(val sectionService: SectionService) {

    @GetMapping
    fun getAllSections(@PageableDefault(size = PaginationConstants.PAGINATION_SIZE,
                        sort= ["name"], direction = Sort.Direction.DESC) pageable: Pageable) =
            sectionService.getAllSections(pageable)


    @PostMapping
    fun createSection(@RequestBody sectionCreateDto: @Valid SectionCreateDto) =
            ResponseEntity(sectionService.createSection(sectionCreateDto), HttpStatus.CREATED)

    @GetMapping("{sectionId}/")
    fun getSection(@PathVariable sectionId: UUID): ResponseEntity<SectionDto> {
        return ResponseEntity(sectionService.getSectionById(sectionId), HttpStatus.OK)
    }

    @PutMapping("{sectionId}/")
    fun updateSection(@PathVariable sectionId: UUID, @RequestBody section: SectionDto) =
            ResponseEntity(sectionService.updateSection(sectionId, section), HttpStatus.OK)

    @DeleteMapping("{sectionId}/")
    fun deleteSection(@PathVariable sectionId: UUID): ResponseEntity<Response> {
        sectionService.deleteSection(sectionId)
        return ResponseEntity(Response("Section has been deleted"), HttpStatus.OK)
    }
}