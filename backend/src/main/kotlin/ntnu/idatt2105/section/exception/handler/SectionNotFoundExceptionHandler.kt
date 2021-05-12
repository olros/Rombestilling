package ntnu.idatt2105.section.exception.handler

import ntnu.idatt2105.section.exception.SectionNotFoundException
import ntnu.idatt2105.util.Response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest

@ControllerAdvice
class SectionNotFoundExceptionHandler {

    @ExceptionHandler(SectionNotFoundException::class)
    protected fun emailInUseException(ex: SectionNotFoundException, request: WebRequest): ResponseEntity<Any> =
            ResponseEntity(Response(ex.message!!) , HttpStatus.BAD_REQUEST)
}