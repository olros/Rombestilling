package ntnu.idatt2105.section.controller

import com.querydsl.core.types.Predicate
import ntnu.idatt2105.section.dto.SectionCreateDto
import ntnu.idatt2105.section.dto.SectionDto
import ntnu.idatt2105.section.service.SectionService
import ntnu.idatt2105.user.dto.UserDto
import ntnu.idatt2105.user.dto.UserRegistrationDto
import ntnu.idatt2105.util.PaginationConstants
import ntnu.idatt2105.dto.response.Response
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
class SectionControllerImpl(val sectionService: SectionService) : SectionController {

    override fun getAllSections(predicate: Predicate, pageable: Pageable) =
        sectionService.getAllSections(pageable, predicate)

    override fun createSection(sectionCreateDto: SectionCreateDto) =
            ResponseEntity(sectionService.createSection(sectionCreateDto), HttpStatus.CREATED)

    override fun getSection(sectionId: UUID) =
        ResponseEntity(sectionService.getSectionById(sectionId), HttpStatus.OK)

    override fun updateSection(sectionId: UUID, section: SectionDto) =
            ResponseEntity(sectionService.updateSection(sectionId, section), HttpStatus.OK)

    override fun deleteSection(sectionId: UUID): ResponseEntity<Response> {
        sectionService.deleteSection(sectionId)
        return ResponseEntity(Response("Section has been deleted"), HttpStatus.OK)
    }
}