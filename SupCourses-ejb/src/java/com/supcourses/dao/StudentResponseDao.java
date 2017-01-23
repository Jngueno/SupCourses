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
import com.supcourses.entity.Response;
import com.supcourses.entity.Student;
import com.supcourses.entity.StudentResponse;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jngue
 */

@Local
public interface StudentResponseDao {
    public StudentResponse create(StudentResponse studentResponse) throws PreexistingEntityException, RollbackFailureException, Exception;
    
    public StudentResponse edit(StudentResponse studentResponse) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception;
    
    public StudentResponse destroy(String id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception;
    
    public List<StudentResponse> findStudentResponsesByStudent(Student student);
    
    public List<StudentResponse> findStudentResponsesByResponse(Response response);
    
    public StudentResponse findStudentResponseByStudentAndResponse(Student student, Response response);
    
    public List<StudentResponse> findStudentResponseEntities();
    
    public List<StudentResponse> findStudentResponseEntities(int maxResults, int firstResult);
    
    public StudentResponse findStudentResponse(String id);
    
    public int getStudentResponseCount();
}
