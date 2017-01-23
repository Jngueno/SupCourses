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
import com.supcourses.entity.Certification;
import com.supcourses.entity.Student;
import com.supcourses.entity.StudentCertification;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jngue
 */

@Local
public interface StudentCertificationDao {
    public StudentCertification create(StudentCertification studentCertification) throws PreexistingEntityException, RollbackFailureException, Exception;
    
    public StudentCertification edit(StudentCertification studentCertification) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception;
    
    public StudentCertification destroy(String id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception;
    
    public List<StudentCertification> findStudentCertificationsByStudent(Student student);
    
    public List<StudentCertification> findStudentCertificationsByCertification(Certification certification);
    
    public StudentCertification findStudentCertificationByStudentAndCertification(Student student, Certification certification);
    
    public List<StudentCertification> findStudentCertificationEntities();
    
    public List<StudentCertification> findStudentCertificationEntities(int maxResults, int firstResult);
    
    public StudentCertification findStudentCertification(String id);
    
    public int getStudentCertificationCount();
}
