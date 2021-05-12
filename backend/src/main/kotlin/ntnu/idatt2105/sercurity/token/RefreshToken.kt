package ntnu.idatt2105.sercurity.token

import org.hibernate.annotations.Type
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToOne

@Entity
data class RefreshToken(
        @Id
        @Type(type = "uuid-char")
        val jti: UUID,

        var isValid:Boolean,

        @OneToOne
        var next: RefreshToken?

)