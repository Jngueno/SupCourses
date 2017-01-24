/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supcourses.service;

import com.supcourses.dao.QuestionDao;
import com.supcourses.dao_jpa.exceptions.NonexistentEntityException;
import com.supcourses.dao_jpa.exceptions.RollbackFailureException;
import com.supcourses.entity.Question;
import com.supcourses.entity.Quizz;
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
public class QuestionService {
    
    @EJB
    private QuestionDao questionDao;

    public Question create(Question question) {
        try {
            return questionDao.create(question);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(QuestionService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(QuestionService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Question edit(Question question) {
        try {
            return questionDao.edit(question);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(QuestionService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (RollbackFailureException ex) {
            Logger.getLogger(QuestionService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(QuestionService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Question remove(Question question) {
        try {
            return questionDao.destroy(question.getQuestionId());
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(QuestionService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (RollbackFailureException ex) {
            Logger.getLogger(QuestionService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(QuestionService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public List<Question> findQuestionsByQuizz(Quizz quizz) {
        return questionDao.findQuestionsbyQuizz(quizz);
    }

    public Question find(String id) {
        return questionDao.findQuestion(id);
    }

    public List<Question> findAll() {
        return questionDao.findQuestionEntities();
    }

    public List<Question> findRange(int[] range) {
        return questionDao.findQuestionEntities(range[0], range[1]);
    }

    public int count() {
        return questionDao.getQuestionCount();
    }
    
}
