/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supcourses.service;

import com.supcourses.dao.ScoreDao;
import com.supcourses.dao_jpa.exceptions.NonexistentEntityException;
import com.supcourses.dao_jpa.exceptions.RollbackFailureException;
import com.supcourses.entity.Quizz;
import com.supcourses.entity.Score;
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
public class ScoreService {
    
    @EJB
    private ScoreDao scoreDao;

    public Score create(Score score) {
        try {
            return scoreDao.create(score);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(ScoreService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(ScoreService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Score edit(Score score) {
        try {
            return scoreDao.edit(score);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ScoreService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (RollbackFailureException ex) {
            Logger.getLogger(ScoreService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(ScoreService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Score remove(Score score) {
        try {
            return scoreDao.destroy(score.getScoreId());
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ScoreService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (RollbackFailureException ex) {
            Logger.getLogger(ScoreService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(ScoreService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Score find(String id) {
        return scoreDao.findScore(id);
    }
    
    public List<Score> findScoresByStudent(Student student) {
        return scoreDao.findScoresByStudent(student);
    }
    
    public List<Score> findScoresByQuizz(Quizz quizz) {
        return scoreDao.findScoresByQuizz(quizz);
    }
    
    public Score findScoreByStudentAndQuizz(Student student, Quizz quizz) {
        return scoreDao.findScoreByStudentAndQuizz(student, quizz);
    }
    
    public List<Score> findAll() {
        return scoreDao.findScoreEntities();
    }

    public List<Score> findRange(int[] range) {
        return scoreDao.findScoreEntities(range[0], range[1]);
    }

    public int count() {
        return scoreDao.getScoreCount();
    }
}
