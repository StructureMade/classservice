package de.structuremade.ms.classservice.api.json;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateClass {
    @NotNull
    private String name;

    private String user;
}
