package ntnu.idatt2105.group.service

import com.opencsv.bean.CsvToBean
import com.opencsv.bean.CsvToBeanBuilder
import com.querydsl.core.types.ExpressionUtils
import com.querydsl.core.types.Predicate
import ntnu.idatt2105.exception.ApplicationException
import ntnu.idatt2105.exception.EntityType
import ntnu.idatt2105.exception.ExceptionType
import ntnu.idatt2105.group.repository.GroupRepository
import ntnu.idatt2105.user.dto.UserEmailDto
import ntnu.idatt2105.user.dto.UserListDto
import ntnu.idatt2105.user.dto.toUserListDto
import ntnu.idatt2105.user.model.QUser
import ntnu.idatt2105.user.repository.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.*


@Service
class MembershipServiceImpl(val groupRepository: GroupRepository, val userRepository: UserRepository) : MembershipService {

    private fun getGroup(id:UUID) = groupRepository.findById(id).orElseThrow{throw ApplicationException.throwException(
            EntityType.GROUP, ExceptionType.ENTITY_NOT_FOUND, id.toString()) }

    private fun getUser(email :String) = userRepository.findByEmail(email).run {
        if (this != null) return@run this
        throw ApplicationException.throwException(
                EntityType.USER,
                ExceptionType.ENTITY_NOT_FOUND,
                email)
    }

    private fun getUser(id: UUID) = userRepository.findById(id).orElseThrow{throw ApplicationException.throwException(
            EntityType.USER,
            ExceptionType.ENTITY_NOT_FOUND,
            id.toString())}

    override fun getMemberships(groupId: UUID, predicate: Predicate, pageable: Pageable): Page<UserListDto> =
        getGroup(groupId).run {
            val user = QUser.user
            val newPredicate = ExpressionUtils.allOf(predicate, user.groups.any().id.eq(this.id))!!
            return userRepository.findAll(newPredicate, pageable).map { it.toUserListDto() }
        }

    override fun createMemberships(groupId: UUID, userEmail: UserEmailDto, predicate: Predicate, pageable: Pageable): Page<UserListDto> {
        groupRepository.findById(groupId).orElseThrow{throw ApplicationException.throwException(
                EntityType.GROUP, ExceptionType.ENTITY_NOT_FOUND, groupId.toString()) }
                .run {
                    val member = getUser(userEmail.email)
                    member.groups.add(this)
                    userRepository.save(member)
                    val user = QUser.user
                    val newPredicate = ExpressionUtils.allOf(predicate, user.groups.any().id.eq(this.id))!!
                    return userRepository.findAll(newPredicate, pageable).map { it.toUserListDto() }
        }
    }

    override fun deleteMembership(groupId: UUID, userId: UUID) {
        groupRepository.findById(groupId).orElseThrow{throw ApplicationException.throwException(
                EntityType.GROUP, ExceptionType.ENTITY_NOT_FOUND, groupId.toString()) }
                .run {
                    val member = getUser(userId)
                    member.groups.remove(this)
                    userRepository.save(member)
                }
    }

    override fun createMembershipBatch(
        predicate: Predicate,
        pageable: Pageable,
        file: MultipartFile,
        groupId: UUID
    ): Page<UserListDto> {
        val group = groupRepository.findById(groupId).orElseThrow{throw ApplicationException.throwException(EntityType.GROUP, ExceptionType.ENTITY_NOT_FOUND, groupId.toString())}
        throwIfFileEmpty(file)
        var fileReader : BufferedReader? = null

        try{
            fileReader = BufferedReader(InputStreamReader(file.inputStream))
            val csvToBean = createCSVToBean(fileReader)
            val listOfDto: List<UserEmailDto> = csvToBean.parse()
            if(listOfDto.isEmpty()) throw Exception()

            listOfDto.forEach{
                val user = getUser(it.email)
                user.groups.add(group)
                userRepository.save(user)
            }
            val user = QUser.user
            val newPredicate = ExpressionUtils.allOf(predicate, user.groups.any().id.eq(group.id))!!
            return userRepository.findAll(newPredicate, pageable).map { it.toUserListDto() }
        }catch (ex: Exception) {
            throw throw ApplicationException.throwExceptionWithId(EntityType.USER, ExceptionType.NOT_VALID, "batch.invalidFile")
        } finally {
            closeFileReader(fileReader)
        }
    }

    private fun createCSVToBean(fileReader: BufferedReader?): CsvToBean<UserEmailDto> =
        CsvToBeanBuilder<UserEmailDto>(fileReader)
            .withType(UserEmailDto::class.java)
            .withIgnoreLeadingWhiteSpace(true)
            .build()

    private fun closeFileReader(fileReader: BufferedReader?) {
        try {
            fileReader!!.close()
        } catch (ex: IOException) {
            throw RuntimeException("Error during csv import")
        }
    }

    private fun throwIfFileEmpty(file: MultipartFile) {
        if (file.isEmpty) {
            throw RuntimeException("Empty file")
        }
    }


}
