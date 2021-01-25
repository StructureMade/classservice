package de.structuremade.ms.classservice.api.json.answer.array;

import de.structuremade.ms.classservice.utils.database.entities.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Teacher {
    private String id;
    private String abbreviation;
    private String firstname;
    private String name;

    public Teacher (User user){
        this.id = user.getId();
        this.abbreviation = user.getAbbreviation();
        this.firstname = user.getFirstname();
        this.name = user.getName();
    }
}
