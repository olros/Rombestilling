package ntnu.idatt2105.util

import java.util.*
import javax.persistence.Column
import javax.persistence.Id


open class UUIDModel(@Id
                     @Column(columnDefinition = "CHAR(32)")
                     private val id: UUID = UUID.randomUUID())


