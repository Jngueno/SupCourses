/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supcourses.service;

import com.supcourses.dao.QuizzDao;
import com.supcourses.dao_jpa.exceptions.NonexistentEntityException;
import com.supcourses.dao_jpa.exceptions.RollbackFailureException;
import com.supcourses.entity.Module;
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
public class QuizzService {
    
    @EJB
    private QuizzDao quizzDao;
    
    public Quizz create(Quizz quizz) {
        try {
            return quizzDao.create(quizz);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(QuizzService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(QuizzService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Quizz edit(Quizz quizz) {
        try {
            return quizzDao.edit(quizz);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(QuizzService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (RollbackFailureException ex) {
            Logger.getLogger(QuizzService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(QuizzService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Quizz remove(Quizz quizz) {
        try {
            return quizzDao.destroy(quizz.getQuizzId());
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(QuizzService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (RollbackFailureException ex) {
            Logger.getLogger(QuizzService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(QuizzService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public List<Quizz> findQuizzsByModule(Module module) {
        return quizzDao.findQuizzsByModule(module);
    }

    public Quizz find(String id) {
        return quizzDao.findQuizz(id);
    }

    public List<Quizz> findAll() {
        return quizzDao.findQuizzEntities();
    }

    public List<Quizz> findRange(int[] range) {
        return quizzDao.findQuizzEntities(range[0], range[1]);
    }

    public int count() {
        return quizzDao.getQuizzCount();
    }
    
}
