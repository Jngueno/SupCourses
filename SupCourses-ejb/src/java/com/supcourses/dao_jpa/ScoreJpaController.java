/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supcourses.dao_jpa;

import com.supcourses.dao.ScoreDao;
import com.supcourses.dao_jpa.exceptions.NonexistentEntityException;
import com.supcourses.dao_jpa.exceptions.PreexistingEntityException;
import com.supcourses.dao_jpa.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.supcourses.entity.Student;
import com.supcourses.entity.Quizz;
import com.supcourses.entity.Score;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.transaction.UserTransaction;

/**
 *
 * @author jngue
 */
@Stateless
public class ScoreJpaController implements ScoreDao {

    @PersistenceContext(unitName = "SupCourses-PU")
    private EntityManager em;
    
    //@Inject
    //private UserTransaction utx;


    @Override
    public Score create(Score score) throws PreexistingEntityException, RollbackFailureException, Exception {
        //EntityManager em = null;
        try {
            //utx.begin();
            //em = getEntityManager();
            Student studentId = score.getStudentId();
            if (studentId != null) {
                studentId = em.getReference(studentId.getClass(), studentId.getStudentId());
                score.setStudentId(studentId);
            }
            Quizz quizzId = score.getQuizzId();
            if (quizzId != null) {
                quizzId = em.getReference(quizzId.getClass(), quizzId.getQuizzId());
                score.setQuizzId(quizzId);
            }
            em.persist(score);
            if (studentId != null) {
                studentId.getScoreCollection().add(score);
                studentId = em.merge(studentId);
            }
            if (quizzId != null) {
                quizzId.getScoreCollection().add(score);
                quizzId = em.merge(quizzId);
            }
            //utx.commit();
            return score;
        } catch (Exception ex) {
            try {
                //utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findScore(score.getScoreId()) != null) {
                throw new PreexistingEntityException("Score " + score + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
//                em.close();
            }
        }
    }

    @Override
    public Score edit(Score score) throws NonexistentEntityException, RollbackFailureException, Exception {
        //EntityManager em = null;
        try {
            //utx.begin();
            //em = getEntityManager();
            Score persistentScore = em.find(Score.class, score.getScoreId());
            Student studentIdOld = persistentScore.getStudentId();
            Student studentIdNew = score.getStudentId();
            Quizz quizzIdOld = persistentScore.getQuizzId();
            Quizz quizzIdNew = score.getQuizzId();
            if (studentIdNew != null) {
                studentIdNew = em.getReference(studentIdNew.getClass(), studentIdNew.getStudentId());
                score.setStudentId(studentIdNew);
            }
            if (quizzIdNew != null) {
                quizzIdNew = em.getReference(quizzIdNew.getClass(), quizzIdNew.getQuizzId());
                score.setQuizzId(quizzIdNew);
            }
            score = em.merge(score);
            if (studentIdOld != null && !studentIdOld.equals(studentIdNew)) {
                studentIdOld.getScoreCollection().remove(score);
                studentIdOld = em.merge(studentIdOld);
            }
            if (studentIdNew != null && !studentIdNew.equals(studentIdOld)) {
                studentIdNew.getScoreCollection().add(score);
                studentIdNew = em.merge(studentIdNew);
            }
            if (quizzIdOld != null && !quizzIdOld.equals(quizzIdNew)) {
                quizzIdOld.getScoreCollection().remove(score);
                quizzIdOld = em.merge(quizzIdOld);
            }
            if (quizzIdNew != null && !quizzIdNew.equals(quizzIdOld)) {
                quizzIdNew.getScoreCollection().add(score);
                quizzIdNew = em.merge(quizzIdNew);
            }
            //utx.commit();
            return score;
        } catch (Exception ex) {
            try {
                //utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = score.getScoreId();
                if (findScore(id) == null) {
                    throw new NonexistentEntityException("The score with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
//                em.close();
            }
        }
    }

    @Override
    public Score destroy(String id) throws NonexistentEntityException, RollbackFailureException, Exception {
        //EntityManager em = null;
        try {
            //utx.begin();
            //em = getEntityManager();
            Score score;
            try {
                score = em.getReference(Score.class, id);
                score.getScoreId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The score with id " + id + " no longer exists.", enfe);
            }
            Student studentId = score.getStudentId();
            if (studentId != null) {
                studentId.getScoreCollection().remove(score);
                studentId = em.merge(studentId);
            }
            Quizz quizzId = score.getQuizzId();
            if (quizzId != null) {
                quizzId.getScoreCollection().remove(score);
                quizzId = em.merge(quizzId);
            }
            em.remove(score);
            //utx.commit();
            return score;
        } catch (Exception ex) {
            try {
                //utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
//                em.close();
            }
        }
    }

    @Override
    public List<Score> findScoreEntities() {
        return findScoreEntities(true, -1, -1);
    }

    @Override
    public List<Score> findScoreEntities(int maxResults, int firstResult) {
        return findScoreEntities(false, maxResults, firstResult);
    }

    private List<Score> findScoreEntities(boolean all, int maxResults, int firstResult) {
        //EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Score.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
//            em.close();
        }
    }

    @Override
    public Score findScore(String id) {
        //EntityManager em = getEntityManager();
        try {
            return em.find(Score.class, id);
        } finally {
//            em.close();
        }
    }

    @Override
    public int getScoreCount() {
        //EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Score> rt = cq.from(Score.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
//            em.close();
        }
    }

    @Override
    public List<Score> findScoresByStudent(Student student) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root<Score> rt = cq.from(Score.class);
            cq.select(rt).where(cb.equal(rt.get("student_id"), student.getStudentId()));
            Query q = em.createQuery(cq);
            List<Score> scores = (List<Score>) q.getResultList();
            return scores;
        } finally {
//            em.close();
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Score> findScoresByQuizz(Quizz quizz) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root<Score> rt = cq.from(Score.class);
            cq.select(rt).where(cb.equal(rt.get("quizz_id"), quizz.getQuizzId()));
            Query q = em.createQuery(cq);
            List<Score> scores = (List<Score>) q.getResultList();
            return scores;
        } finally {
//            em.close();
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Score findScoreByStudentAndQuizz(Student student, Quizz quizz) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root<Score> rt = cq.from(Score.class);
            cq.select(rt).where(cb.equal(rt.get("student_id"), student.getStudentId()), cb.equal(rt.get("quizz_id"), quizz.getQuizzId()));
            Query q = em.createQuery(cq);
            Score score = (Score) q.getSingleResult();
            return score;
        } finally {
//            em.close();
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
