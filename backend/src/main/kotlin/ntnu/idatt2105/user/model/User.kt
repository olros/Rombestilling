package ntnu.idatt2105.user.model

import com.querydsl.core.annotations.PropertyType
import com.querydsl.core.annotations.QueryType
import ntnu.idatt2105.security.token.PasswordResetToken
import java.time.LocalDate
import java.util.UUID
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.OneToOne

@Entity
data class User(
    @Id
    @Column(columnDefinition = "CHAR(32)")
    var id: UUID = UUID.randomUUID(),
    var firstName: String = "",
    var surname: String = "",
    @Column(unique = true)
    var email: String = "",
    var phoneNumber: String = "",
    var image: String = "",
    var expirationDate: LocalDate = LocalDate.EPOCH,
    var password: String = "",
    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = [JoinColumn(name = "USER_ID", referencedColumnName = "id")],
            inverseJoinColumns = [JoinColumn(name = "ROLE_ID", referencedColumnName = "id")])
    val roles: Set<Role> = mutableSetOf(),
    @OneToOne(mappedBy = "user", cascade = [CascadeType.ALL])
    val pwdToken: PasswordResetToken? = null
) {
    @Transient
    @QueryType(PropertyType.STRING)
    val search: String? = null
}
