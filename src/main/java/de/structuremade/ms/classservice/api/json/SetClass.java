package de.structuremade.ms.classservice.api.json;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class SetClass {

    @NotNull
    private String classId;

    @NotNull
    private List<String> lessons;
}
