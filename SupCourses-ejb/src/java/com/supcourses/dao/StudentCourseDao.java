/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supcourses.dao;

import com.supcourses.dao_jpa.exceptions.IllegalOrphanException;
import com.supcourses.dao_jpa.exceptions.NonexistentEntityException;
import com.supcourses.dao_jpa.exceptions.PreexistingEntityException;
import com.supcourses.dao_jpa.exceptions.RollbackFailureException;
import com.supcourses.entity.Course;
import com.supcourses.entity.Student;
import com.supcourses.entity.StudentCourse;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jngue
 */

@Local
public interface StudentCourseDao {
    public StudentCourse create(StudentCourse studentCourse) throws PreexistingEntityException, RollbackFailureException, Exception;
    
    public StudentCourse edit(StudentCourse studentCourse) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception;
    
    public StudentCourse destroy(String id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception;
    
    public List<StudentCourse> findStudentCoursesByCourse(Course course);
    
    public List<StudentCourse> findStudentCoursesByStudent(Student student);
    
    public StudentCourse findStudentCourseByStudentAndCourse(Student student, Course course);
    
    public List<StudentCourse> findStudentCourseEntities();
    
    public List<StudentCourse> findStudentCourseEntities(int maxResults, int firstResult);
    
    public StudentCourse findStudentCourse(String id);
    
    public int getStudentCourseCount();
}
