package ntnu.idatt2105.user.model

import java.time.LocalDate
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
@Entity
data class User (@Id
                 var id: UUID,
                var firstName:String,
                 var surname:String,
                 @Column(unique = true)
                 var email: String,
                 var birthDate: LocalDate,
                 var password: String)