package ntnu.idatt2105.user.repository

import com.querydsl.core.BooleanBuilder
import ntnu.idatt2105.user.model.QUser
import ntnu.idatt2105.user.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer
import org.springframework.data.querydsl.binding.QuerydslBindings
import java.util.UUID
import java.util.function.Consumer

interface UserRepository : JpaRepository<User, UUID>, QuerydslPredicateExecutor<User>, QuerydslBinderCustomizer<QUser> {
    fun findByEmail(email: String): User?
    fun existsByEmail(email: String): Boolean

    @JvmDefault
    override fun customize(bindings: QuerydslBindings, user: QUser) {
        bindings.bind(user.search).first { path, value ->
            val predicate = BooleanBuilder()
            val searchWords: List<String> = value.trim().split("\\s+")
            searchWords.forEach(Consumer { searchWord: String? ->
                if (searchWord == " ") return@Consumer
                predicate
                        .or(user.firstName.containsIgnoreCase(searchWord))
                        .or(user.surname.containsIgnoreCase(searchWord))
                        .or(user.phoneNumber.containsIgnoreCase(searchWord))
                        .or(user.email.containsIgnoreCase(searchWord))
            })
            return@first predicate
        }
    }
}
