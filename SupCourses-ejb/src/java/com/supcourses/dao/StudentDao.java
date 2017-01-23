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
import com.supcourses.entity.Student;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jngue
 */

@Local
public interface StudentDao {
    public Student create(Student student) throws PreexistingEntityException, RollbackFailureException, Exception;
    
    public Student edit(Student student) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception;
    
    public Student destroy(String id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception;
    
    public Student findStudentByEmailAndPassword(String eMail, String password);
    
    public List<Student> findStudentEntities();
    
    public List<Student> findStudentEntities(int maxResults, int firstResult);
    
    public Student findStudent(String id);
    
    public int getStudentCount();
}
