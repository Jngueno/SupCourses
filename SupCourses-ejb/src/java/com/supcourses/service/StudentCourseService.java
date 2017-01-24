/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supcourses.service;

import com.supcourses.dao.StudentCourseDao;
import com.supcourses.dao_jpa.exceptions.NonexistentEntityException;
import com.supcourses.dao_jpa.exceptions.RollbackFailureException;
import com.supcourses.entity.Course;
import com.supcourses.entity.Student;
import com.supcourses.entity.StudentCourse;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;

/**
 *
 * @author jngue
 */
@Stateless
public class StudentCourseService {

    @EJB
    private StudentCourseDao studentCourseDao;
    
    public StudentCourse create(StudentCourse studentCourse) {
        try {
            return studentCourseDao.create(studentCourse);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(StudentCourseService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(StudentCourseService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public StudentCourse edit(StudentCourse studentCourse) {
        try {
            return studentCourseDao.edit(studentCourse);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(StudentCourseService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (RollbackFailureException ex) {
            Logger.getLogger(StudentCourseService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(StudentCourseService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public StudentCourse remove(StudentCourse studentCourse) {
        try {
            return studentCourseDao.destroy(studentCourse.getStudentCourseId());
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(StudentCourseService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (RollbackFailureException ex) {
            Logger.getLogger(StudentCourseService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(StudentCourseService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public List<StudentCourse> findStudentCoursesByCourse(Course course) {
        return studentCourseDao.findStudentCoursesByCourse(course);
    }
    
    public List<StudentCourse> findStudentCoursesByStudent(Student student) {
        return studentCourseDao.findStudentCoursesByStudent(student);
    }
    
    public StudentCourse findStudentCourseByStudentAndCourse(Student student, Course course) {
        return studentCourseDao.findStudentCourseByStudentAndCourse(student, course);
    }
    
    public StudentCourse find(String id) {
        return studentCourseDao.findStudentCourse(id);
    }

    public List<StudentCourse> findAll() {
        return studentCourseDao.findStudentCourseEntities();
    }

    public List<StudentCourse> findRange(int[] range) {
        return studentCourseDao.findStudentCourseEntities(range[0], range[1]);
    }

    public int count() {
        return studentCourseDao.getStudentCourseCount();
    }
    
}
