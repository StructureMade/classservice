package de.structuremade.ms.classservice.api.service;

import de.structuremade.ms.classservice.api.json.EditClass;
import de.structuremade.ms.classservice.api.json.SetClass;
import de.structuremade.ms.classservice.api.json.answer.GetAllClasses;
import de.structuremade.ms.classservice.api.json.answer.GetClassInformation;
import de.structuremade.ms.classservice.api.json.answer.array.ClassArray;
import de.structuremade.ms.classservice.api.json.answer.array.Lesson;
import de.structuremade.ms.classservice.api.json.answer.array.Teacher;
import de.structuremade.ms.classservice.utils.JWTUtil;
import de.structuremade.ms.classservice.utils.database.entities.Class;
import de.structuremade.ms.classservice.utils.database.entities.LessonRoles;
import de.structuremade.ms.classservice.utils.database.entities.User;
import de.structuremade.ms.classservice.utils.database.repo.ClassRepo;
import de.structuremade.ms.classservice.utils.database.repo.LessonRolesRepo;
import de.structuremade.ms.classservice.utils.database.repo.SchoolRepo;
import de.structuremade.ms.classservice.utils.database.repo.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClassService {

    private final Logger LOGGER = LoggerFactory.getLogger(ClassService.class);
    @Autowired
    UserRepo userRepo;
    @Autowired
    ClassRepo classRepo;
    @Autowired
    SchoolRepo schoolRepo;
    @Autowired
    LessonRolesRepo lessonRolesRepo;
    @Autowired
    JWTUtil jwtUtil;

    public int create(String name, String teacher, String jwt) {
        try {
            if (jwtUtil.isTokenExpired(jwt)) return 2;
            LOGGER.info("Init class Entity");
            Class createClass = new Class();
            createClass.setName(name);
            LOGGER.info("Check if Teacher Teacher is set");
            if (!teacher.isEmpty()) {
                createClass.setClassTeacher(userRepo.getOne(teacher));
            }
            createClass.setSchool(schoolRepo.getOne(jwtUtil.extractSpecialClaim(jwt, "schoolid")));
            LOGGER.info("Save Class");
            classRepo.save(createClass);
            return 0;
        } catch (Exception e) {
            LOGGER.error("Couldn't create class", e.fillInStackTrace());
            return 1;

        }
    }

    public int edit(EditClass ec, String jwt) {
        try {
            LOGGER.info("Start editing Class");
            Class userClass = classRepo.getOne(ec.getId());
            if (!userClass.getSchool().getId().equals(jwtUtil.extractSpecialClaim(jwt, "schoolid"))) return 2;
            userClass.setName(ec.getName());
            userClass.setClassTeacher(userRepo.getOne(ec.getUser()));
            classRepo.save(userClass);
            return 0;
        } catch (Exception e) {
            LOGGER.error("Couldn't edit class", e.fillInStackTrace());
            return 1;
        }
    }

    public int set(String userid, SetClass sc, String jwt) {
        try {
            if (jwtUtil.isTokenExpired(jwt)) return 2;
            LOGGER.info("Get User");
            User user = userRepo.getOne(userid);
            Class userClass = classRepo.getOne(sc.getClassId());
            if (!user.getSchools().get(0).getId().equals(jwtUtil.extractSpecialClaim(jwt, "schoolid"))) return 2;
            if (!userClass.getSchool().getId().equals(jwtUtil.extractSpecialClaim(jwt, "schoolid"))) return 2;
            LOGGER.info("Set class to user");
            user.setUserClass(userClass);
            LOGGER.info("Save user");
            userRepo.save(user);
            return 0;
        } catch (Exception e) {
            LOGGER.error("Couldn't set class to user", e.fillInStackTrace());
            return 1;
        }
    }

    public int setLesson(String lessonid, SetClass sc, String jwt) {
        List<LessonRoles> lessonRolesList;
        try {
            if (jwtUtil.isTokenExpired(jwt)) return 2;
            LOGGER.info("Get class");
            Class schoolClass = classRepo.getOne(sc.getClassId());
            lessonRolesList = schoolClass.getLessons();
            LessonRoles lr = lessonRolesRepo.getOne(lessonid);
            if (!schoolClass.getSchool().getId().equals(jwtUtil.extractSpecialClaim(jwt, "schoolid"))) return 2;
            if (!lr.getSchool().getId().equals(jwtUtil.extractSpecialClaim(jwt, "schoolid"))) return 2;
            LOGGER.info("Set lesson to class");
            lessonRolesList.add(lr);
            schoolClass.setLessons(lessonRolesList);
            LOGGER.info("Save class");
            classRepo.save(schoolClass);
            return 0;
        } catch (Exception e) {
            LOGGER.error("Couldn't set lesson to class", e.fillInStackTrace());
            return 1;
        }
    }


    public GetAllClasses getClasses(String jwt) {
        List<ClassArray> classes = new ArrayList<>();
        try {
            LOGGER.info("Get all classes by school");
            classRepo.findAllIdAndNameBySchool(schoolRepo.getOne(jwtUtil.extractSpecialClaim(jwt, "schoolid"))).forEach(schoolClass -> {
                classes.add(new ClassArray(schoolClass.getId(), schoolClass.getName()));
            });
            return new GetAllClasses(classes);
        } catch (Exception e) {
            LOGGER.error("Couldn't get the classes of school", e.fillInStackTrace());
            return null;
        }
    }

    @Transactional
    public GetClassInformation getClassInformation(String classId, String jwt) {
        Teacher teacher;
        User user;
        Class schoolClass = null;
        List<Teacher> students = new ArrayList<>();
        List<Lesson> lessons = new ArrayList<>();
        try {
            LOGGER.info("Check if JWT is expired");
            if (jwtUtil.isTokenExpired(jwt)) return new GetClassInformation();
            LOGGER.info("Check if class is not null");
            if ((user = userRepo.getOne(jwtUtil.extractIdOrEmail(jwt))).getUserClass() != null) {
                LOGGER.info("Check if user have rights for get this");
                if (!(schoolClass = user.getUserClass()).getId().equals(classId)) {
                    return new GetClassInformation();
                }
            } else {
                LOGGER.info("Check if user have Childrens");
                if (user.getChildrens().size() == 0) return new GetClassInformation();
                for (User children : user.getChildrens()) {
                    LOGGER.info("Check if children have an class");
                    if ((schoolClass = children.getUserClass()) != null) {
                        LOGGER.info("Check if user have rights for get this");
                        if (!children.getUserClass().getId().equals(classId)) {
                            return new GetClassInformation();
                        }
                    }else return new GetClassInformation();
                }
                if (schoolClass == null) return new GetClassInformation();
            }
            LOGGER.info("Get Teacher");
            teacher = new Teacher(schoolClass.getClassTeacher());
            LOGGER.info("Get Students and set it to List");
            schoolClass.getStudents().forEach(student -> {
                students.add(new Teacher(student));
            });
            LOGGER.info("Get Lessons and set it to List");
            schoolClass.getLessons().forEach(lesson -> {
                lessons.add(new Lesson(lesson));
            });
            return new GetClassInformation(schoolClass.getName(), lessons, teacher, students);
        } catch (Exception e) {
            LOGGER.error("Failed to get Classinformation", e.fillInStackTrace());
            return null;
        }
    }
}
