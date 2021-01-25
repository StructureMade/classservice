package de.structuremade.ms.classservice.utils.database.repo;

import de.structuremade.ms.classservice.utils.database.entities.Class;
import de.structuremade.ms.classservice.utils.database.entities.School;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClassRepo extends JpaRepository<Class, String> {
    Iterable<Class> findAllIdAndNameBySchool(School schoolid);
}
