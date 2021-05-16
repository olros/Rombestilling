package ntnu.idatt2105.group.repository

import ntnu.idatt2105.group.model.Group
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface GroupRepository: JpaRepository<Group, UUID>