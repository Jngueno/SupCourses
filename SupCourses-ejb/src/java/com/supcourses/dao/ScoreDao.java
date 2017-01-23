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
import com.supcourses.entity.Quizz;
import com.supcourses.entity.Score;
import com.supcourses.entity.Student;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jngue
 */

@Local
public interface ScoreDao {
    public Score create(Score score) throws PreexistingEntityException, RollbackFailureException, Exception;
    
    public Score edit(Score score) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception;
    
    public Score destroy(String id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception;
    
    public List<Score> findScoresByStudent(Student student);
    
    public List<Score> findScoresByQuizz(Quizz quizz);
    
    public Score findScoreByStudentAndQuizz(Student student, Quizz quizz);
    
    public List<Score> findScoreEntities();
    
    public List<Score> findScoreEntities(int maxResults, int firstResult);
    
    public Score findScore(String id);
    
    public int getScoreCount();
}
