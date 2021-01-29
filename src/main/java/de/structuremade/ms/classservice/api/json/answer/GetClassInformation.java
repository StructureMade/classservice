package de.structuremade.ms.classservice.api.json.answer;

import de.structuremade.ms.classservice.api.json.answer.array.Lesson;
import de.structuremade.ms.classservice.api.json.answer.array.Teacher;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetClassInformation {
    private String name;
    private List<Lesson> lessons;
    private Teacher teacher;
    private List<Teacher> students;
}
