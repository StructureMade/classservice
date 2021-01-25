package de.structuremade.ms.classservice.api.json;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class EditClass {

    @NotNull
   private String id;

    @NotNull
    private String name;

    @NotNull
    private String user;

}
