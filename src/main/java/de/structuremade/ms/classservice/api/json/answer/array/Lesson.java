package de.structuremade.ms.classservice.api.json.answer.array;

import de.structuremade.ms.classservice.utils.database.entities.LessonRoles;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Lesson {
    private String id;
    private String name;
    private Teacher teacher;

    public Lesson(LessonRoles lr){
        this.id = lr.getId();
        this.name = lr.getName();
        this.teacher = new Teacher(lr.getTeacher());
    }
}
