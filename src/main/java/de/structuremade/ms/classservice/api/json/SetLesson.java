package de.structuremade.ms.classservice.api.json;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class SetLesson {
    @NotNull
    private String classId;

    @NotNull
    private List<String> students;
}
