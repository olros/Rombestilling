package ntnu.idatt2105.util

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable


class JpaUtils {

    fun getDefaultPageable(): Pageable {
        return PageRequest.of(0, 25)
    }
}