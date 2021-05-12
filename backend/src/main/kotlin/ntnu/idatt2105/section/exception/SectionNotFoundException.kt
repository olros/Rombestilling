package ntnu.idatt2105.section.exception

import java.lang.RuntimeException
import javax.persistence.EntityNotFoundException

class SectionNotFoundException(val error: String): EntityNotFoundException(error) {
    constructor(): this("Can't find section")
}