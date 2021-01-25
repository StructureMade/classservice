package de.structuremade.ms.classservice.utils.database.repo;

import de.structuremade.ms.classservice.utils.database.entities.School;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepo extends JpaRepository<School, String> {
}
