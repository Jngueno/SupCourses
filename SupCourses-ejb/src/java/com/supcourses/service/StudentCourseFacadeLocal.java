/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supcourses.service;

import com.supcourses.entity.StudentCourse;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jngue
 */
@Local
public interface StudentCourseFacadeLocal {

    void create(StudentCourse studentCourse);

    void edit(StudentCourse studentCourse);

    void remove(StudentCourse studentCourse);

    StudentCourse find(Object id);

    List<StudentCourse> findAll();

    List<StudentCourse> findRange(int[] range);

    int count();
    
}
