package de.structuremade.ms.classservice.utils.database.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "lessonroles")
@Getter
@Setter
public class LessonRoles {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private String id;

    @Column
    private String name;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "teacher", foreignKey = @ForeignKey(name = "fk_teacherid"))
    private User teacher;

    @ManyToOne
    @JoinColumn(name = "school", foreignKey = @ForeignKey(name = "fk_schoolid"))
    private School school;


    @ManyToMany(targetEntity = Class.class)
    @JoinTable(name = "classlessons", schema = "services", joinColumns = @JoinColumn(name = "class", foreignKey = @ForeignKey(name = "fk_class"))
            , inverseJoinColumns = @JoinColumn(name = "lessonrole", foreignKey = @ForeignKey(name = "fk_lessonrole")))
    private List<Class> classOfLesson;
}
