package de.structuremade.ms.classservice.api.json.answer;

import de.structuremade.ms.classservice.api.json.answer.array.ClassArray;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetAllClasses {
    private List<ClassArray> classes;
}
