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
import com.supcourses.entity.Module;
import com.supcourses.entity.Quizz;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jngue
 */

@Local
public interface QuizzDao {
    public Quizz create(Quizz quizz) throws PreexistingEntityException, RollbackFailureException, Exception;
    
    public Quizz edit(Quizz quizz) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception;
    
    public Quizz destroy(String id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception;
    
    public List<Quizz> findQuizzsByModule(Module module);
    
    public List<Quizz> findQuizzEntities();
    
    public List<Quizz> findQuizzEntities(int maxResults, int firstResult);
    
    public Quizz findQuizz(String id);
    
    public int getQuizzCount();
}
