/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supcourses.service;

import com.supcourses.dao.StudentResponseDao;
import com.supcourses.dao_jpa.exceptions.NonexistentEntityException;
import com.supcourses.dao_jpa.exceptions.RollbackFailureException;
import com.supcourses.entity.Response;
import com.supcourses.entity.Student;
import com.supcourses.entity.StudentResponse;
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
public class StudentResponseService {
    
    @EJB
    private StudentResponseDao studentResponseDao;

    public StudentResponse create(StudentResponse studentResponse) {
        try {
            return studentResponseDao.create(studentResponse);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(StudentResponseService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(StudentResponseService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public StudentResponse edit(StudentResponse studentResponse) {
        try {
            return studentResponseDao.edit(studentResponse);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(StudentResponseService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (RollbackFailureException ex) {
            Logger.getLogger(StudentResponseService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(StudentResponseService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public StudentResponse remove(StudentResponse studentResponse) {
        try {
            return studentResponseDao.destroy(studentResponse.getStudentResponseId());
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(StudentResponseService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (RollbackFailureException ex) {
            Logger.getLogger(StudentResponseService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(StudentResponseService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public List<StudentResponse> findStudentResponsesByStudent(Student student) {
        return studentResponseDao.findStudentResponsesByStudent(student);
    }
    
    public List<StudentResponse> findStudentResponsesByResponse(Response response) {
        return studentResponseDao.findStudentResponsesByResponse(response);
    }
    
    public StudentResponse findStudentResponseByStudentAndResponse(Student student, Response response) {
        return studentResponseDao.findStudentResponseByStudentAndResponse(student, response);
    }
    
    public StudentResponse find(String id) {
        return studentResponseDao.findStudentResponse(id);
    }

    public List<StudentResponse> findAll() {
        return studentResponseDao.findStudentResponseEntities();
    }

    public List<StudentResponse> findRange(int[] range) {
        return studentResponseDao.findStudentResponseEntities(range[0], range[1]);
    }

    public int count() {
        return studentResponseDao.getStudentResponseCount();
    }
    
}
