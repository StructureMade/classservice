package de.structuremade.ms.classservice.utils.database.repo;

import de.structuremade.ms.classservice.utils.database.entities.LessonRoles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRolesRepo extends JpaRepository<LessonRoles, String> {
}
