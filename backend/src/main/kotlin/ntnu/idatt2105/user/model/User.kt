package ntnu.idatt2105.user.model

import java.time.LocalDate
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
@Entity
data class User(@Id
                @Column(columnDefinition = "CHAR(32)")
                var id: UUID = UUID.randomUUID(),
                var firstName:String = "",
                var surname:String = "",
                @Column(unique = true)
                var email: String = "",
                var phoneNumber: String = "",
                var image: String = "",
                var expirationDate: LocalDate = LocalDate.EPOCH,
                var password: String = "")