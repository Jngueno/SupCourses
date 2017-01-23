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
import com.supcourses.entity.Question;
import com.supcourses.entity.Quizz;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jngue
 */

@Local
public interface QuestionDao {
    public Question create(Question question) throws PreexistingEntityException, RollbackFailureException, Exception;
    
    public Question edit(Question question) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception;
    
    public Question destroy(String id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception;
    
    public List<Question> findQuestionsbyQuizz(Quizz quizz);
    
    public List<Question> findQuestionEntities();
    
    public List<Question> findQuestionEntities(int maxResults, int firstResult);
    
    public Question findQuestion(String id);
    
    public int getQuestionCount();
}
