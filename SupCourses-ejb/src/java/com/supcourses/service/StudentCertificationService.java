/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supcourses.service;

import com.supcourses.dao.StudentCertificationDao;
import com.supcourses.dao_jpa.exceptions.RollbackFailureException;
import com.supcourses.entity.Student;
import com.supcourses.entity.StudentCertification;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Local;

/**
 *
 * @author jngue
 */
@Local
public class StudentCertificationService {

//    void create(StudentCertification studentCertification);
//
//    void edit(StudentCertification studentCertification);
//
//    void remove(StudentCertification studentCertification);
//
//    StudentCertification find(Object id);
//
//    List<StudentCertification> findAll();
//
//    List<StudentCertification> findRange(int[] range);
//
//    int count();
    
    @EJB
    private PrinterCertificationService printerCertificationService;
    
    @EJB
    private StudentCertificationDao studentCertificationDao;
    
    public void processStudentCertification(StudentCertification studentCertification) {
        try {
            studentCertificationDao.create(studentCertification);
            sendEmail(studentCertification.getStudentId());
            printerCertificationService.printCertification(studentCertification.getCertificationId());
        } catch (RollbackFailureException ex) {
            Logger.getLogger(StudentCertificationService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(StudentCertificationService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Asynchronous
    public void sendEmail(Student student) {
        System.out.println("Send mail to " + student.getFirstName()
                + " " + student.getLastName() + " at " + student.getEMail());
    }
}
