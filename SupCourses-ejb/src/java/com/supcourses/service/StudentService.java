/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supcourses.service;

import com.supcourses.dao.StudentDao;
import com.supcourses.dao_jpa.exceptions.NonexistentEntityException;
import com.supcourses.dao_jpa.exceptions.RollbackFailureException;
import com.supcourses.entity.Student;
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
public class StudentService {

    @EJB
    private StudentDao studentDao;
    
    public Student create(Student student) {
        try {
            return studentDao.create(student);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(StudentService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(StudentService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Student edit(Student student) {
        try {
            return studentDao.edit(student);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(StudentService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (RollbackFailureException ex) {
            Logger.getLogger(StudentService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(StudentService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Student remove(Student student) {
        try {
            return studentDao.destroy(student.getStudentId());
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(StudentService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (RollbackFailureException ex) {
            Logger.getLogger(StudentService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(StudentService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Student findStudentByEmailAndPassword(String eMail, String password) {
        return studentDao.findStudentByEmailAndPassword(eMail, password);
    }
    
    public Student find(String id) {
        return studentDao.findStudent(id);
    }

    public List<Student> findAll() {
        return studentDao.findStudentEntities();
    }

    public List<Student> findRange(int[] range) {
        return studentDao.findStudentEntities(range[0], range[1]);
    }

    public int count() {
        return studentDao.getStudentCount();
    }
    
}
