/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supcourses.bean;

import com.supcourses.entity.Course;
import com.supcourses.service.CourseService;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.inject.Named;
import javax.faces.bean.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

/**
 *
 * @author jngue
 */
@ManagedBean
@SessionScoped
public class CourseBean {

    @EJB
    private CourseService courseService;
    
    private List<Course> resultList = new ArrayList<Course>();
    
    public List<com.supcourses.entity.Course> listAllCourse () {
        resultList = courseService.findAll();
        return resultList;
    }
    
    /**
     * Creates a new instance of CourseBean
     */
    public CourseBean() {
    }
    
}
