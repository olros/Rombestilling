package ntnu.idatt2105.user.exception

import javax.persistence.EntityNotFoundException

class UserNotFoundException(private val errorMessage: String) : EntityNotFoundException(errorMessage){
    constructor():this("User not found")
}