/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supcourses.service.soap;

import com.supcourses.entity.Course;
import com.supcourses.entity.Module;
import com.supcourses.service.CourseService;
import com.supcourses.service.ModuleService;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 *
 * @author jngue
 */
@WebService(serviceName="soap/courses")
public class ListCoursesRessource {
    @EJB
    private CourseService cs;
    /**
     * Web service operation
     * @return 
     */
    @WebMethod(operationName = "listCourses")
    public List<Course> listCourses() {
        //TODO write your implementation code here:
        return cs.findAll();
    }
    
}
