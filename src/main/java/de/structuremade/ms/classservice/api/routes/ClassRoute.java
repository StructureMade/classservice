package de.structuremade.ms.classservice.api.routes;

import com.google.gson.Gson;
import de.structuremade.ms.classservice.api.json.CreateClass;
import de.structuremade.ms.classservice.api.json.EditClass;
import de.structuremade.ms.classservice.api.json.SetClass;
import de.structuremade.ms.classservice.api.json.answer.GetAllClasses;
import de.structuremade.ms.classservice.api.json.answer.GetClassInformation;
import de.structuremade.ms.classservice.api.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("service/class")
public class ClassRoute {

    @Autowired
    ClassService service;

    private Gson gson;

    @CrossOrigin
    @PostMapping("/create")
    public void create(@RequestBody CreateClass cc, HttpServletResponse response, HttpServletRequest request) {
        switch (service.create(cc.getName(), cc.getUser(), request.getHeader("Authorization").substring(7))) {
            case 0 -> response.setStatus(HttpStatus.CREATED.value());
            case 1 -> response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @CrossOrigin
    @PutMapping(name = "/edit")
    public void edit(@RequestBody EditClass ec, HttpServletResponse response) {
        switch (service.edit(ec)) {
            case 0 -> response.setStatus(HttpStatus.OK.value());
            case 1 -> response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @CrossOrigin
    @PostMapping("/setuser/{userid}")
    public void set(@PathVariable String userid, @RequestBody SetClass sc, HttpServletResponse response) {
        switch (service.set(userid, sc)) {
            case 0 -> response.setStatus(HttpStatus.OK.value());
            case 1 -> response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @CrossOrigin
    @PostMapping("setlesson/{lessonrole}")
    public void setLessonrole(@PathVariable String lessonid, SetClass sc, HttpServletResponse response) {
        switch (service.setLesson(lessonid, sc)) {
            case 0 -> response.setStatus(HttpStatus.OK.value());
            case 1 -> response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @CrossOrigin
    @GetMapping("/get")
    public Object getClasses(HttpServletResponse response, HttpServletRequest request) {
        GetAllClasses getAllClasses = service.getClasses(request.getHeader("Authorization").substring(7));
        if (getAllClasses != null) {
            response.setStatus(HttpStatus.OK.value());
            return gson.toJson(getAllClasses);
        }
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return null;
    }

    @CrossOrigin
    @GetMapping("/getclassinformation/{classid}")
    public Object getClassInformation(@PathVariable String classId, HttpServletResponse response) {
        GetClassInformation getClassInformation = service.getClassInformation(classId);
        if (getClassInformation != null){
            response.setStatus(HttpStatus.OK.value());
            return gson.toJson(getClassInformation);
        }
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return null;
    }

}
