/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supcourses.service.soap;

import com.supcourses.entity.Course;
import com.supcourses.service.CourseService;
import javax.ejb.EJB;
import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 *
 * @author jngue
 */
@WebService(serviceName="soap/course")
public class GetCourseRessource {
    
    @EJB
    private CourseService cs;

    /**
     * Web service operation
     * @param courseId
     */
    @WebMethod(operationName = "getCourse")
    public Course getCourse(String courseId) {
        //TODO write your implementation code here:
        return cs.find(courseId);
    }
    
}
