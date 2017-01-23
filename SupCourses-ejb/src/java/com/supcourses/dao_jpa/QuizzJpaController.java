/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supcourses.dao_jpa;

import com.supcourses.dao.QuizzDao;
import com.supcourses.dao_jpa.exceptions.IllegalOrphanException;
import com.supcourses.dao_jpa.exceptions.NonexistentEntityException;
import com.supcourses.dao_jpa.exceptions.PreexistingEntityException;
import com.supcourses.dao_jpa.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.supcourses.entity.Module;
import com.supcourses.entity.Question;
import com.supcourses.entity.Quizz;
import java.util.ArrayList;
import java.util.Collection;
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
public class QuizzJpaController implements QuizzDao {

    @PersistenceContext(unitName = "SupCourses-PU")
    private EntityManager em;
    
    //@Inject
    //private UserTransaction utx;


    @Override
    public Quizz create(Quizz quizz) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (quizz.getQuestionCollection() == null) {
            quizz.setQuestionCollection(new ArrayList<Question>());
        }
        if (quizz.getScoreCollection() == null) {
            quizz.setScoreCollection(new ArrayList<Score>());
        }
        //EntityManager em = null;
        try {
            //utx.begin();
            //em = getEntityManager();
            Module moduleId = quizz.getModuleId();
            if (moduleId != null) {
                moduleId = em.getReference(moduleId.getClass(), moduleId.getModuleId());
                quizz.setModuleId(moduleId);
            }
            Collection<Question> attachedQuestionCollection = new ArrayList<Question>();
            for (Question questionCollectionQuestionToAttach : quizz.getQuestionCollection()) {
                questionCollectionQuestionToAttach = em.getReference(questionCollectionQuestionToAttach.getClass(), questionCollectionQuestionToAttach.getQuestionId());
                attachedQuestionCollection.add(questionCollectionQuestionToAttach);
            }
            quizz.setQuestionCollection(attachedQuestionCollection);
            Collection<Score> attachedScoreCollection = new ArrayList<Score>();
            for (Score scoreCollectionScoreToAttach : quizz.getScoreCollection()) {
                scoreCollectionScoreToAttach = em.getReference(scoreCollectionScoreToAttach.getClass(), scoreCollectionScoreToAttach.getScoreId());
                attachedScoreCollection.add(scoreCollectionScoreToAttach);
            }
            quizz.setScoreCollection(attachedScoreCollection);
            em.persist(quizz);
            if (moduleId != null) {
                moduleId.getQuizzCollection().add(quizz);
                moduleId = em.merge(moduleId);
            }
            for (Question questionCollectionQuestion : quizz.getQuestionCollection()) {
                Quizz oldQuizzIdOfQuestionCollectionQuestion = questionCollectionQuestion.getQuizzId();
                questionCollectionQuestion.setQuizzId(quizz);
                questionCollectionQuestion = em.merge(questionCollectionQuestion);
                if (oldQuizzIdOfQuestionCollectionQuestion != null) {
                    oldQuizzIdOfQuestionCollectionQuestion.getQuestionCollection().remove(questionCollectionQuestion);
                    oldQuizzIdOfQuestionCollectionQuestion = em.merge(oldQuizzIdOfQuestionCollectionQuestion);
                }
            }
            for (Score scoreCollectionScore : quizz.getScoreCollection()) {
                Quizz oldQuizzIdOfScoreCollectionScore = scoreCollectionScore.getQuizzId();
                scoreCollectionScore.setQuizzId(quizz);
                scoreCollectionScore = em.merge(scoreCollectionScore);
                if (oldQuizzIdOfScoreCollectionScore != null) {
                    oldQuizzIdOfScoreCollectionScore.getScoreCollection().remove(scoreCollectionScore);
                    oldQuizzIdOfScoreCollectionScore = em.merge(oldQuizzIdOfScoreCollectionScore);
                }
            }
            //utx.commit();
            return quizz;
        } catch (Exception ex) {
            try {
                //utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findQuizz(quizz.getQuizzId()) != null) {
                throw new PreexistingEntityException("Quizz " + quizz + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public Quizz edit(Quizz quizz) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        //EntityManager em = null;
        try {
            //utx.begin();
            //em = getEntityManager();
            Quizz persistentQuizz = em.find(Quizz.class, quizz.getQuizzId());
            Module moduleIdOld = persistentQuizz.getModuleId();
            Module moduleIdNew = quizz.getModuleId();
            Collection<Question> questionCollectionOld = persistentQuizz.getQuestionCollection();
            Collection<Question> questionCollectionNew = quizz.getQuestionCollection();
            Collection<Score> scoreCollectionOld = persistentQuizz.getScoreCollection();
            Collection<Score> scoreCollectionNew = quizz.getScoreCollection();
            List<String> illegalOrphanMessages = null;
            for (Question questionCollectionOldQuestion : questionCollectionOld) {
                if (!questionCollectionNew.contains(questionCollectionOldQuestion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Question " + questionCollectionOldQuestion + " since its quizzId field is not nullable.");
                }
            }
            for (Score scoreCollectionOldScore : scoreCollectionOld) {
                if (!scoreCollectionNew.contains(scoreCollectionOldScore)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Score " + scoreCollectionOldScore + " since its quizzId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (moduleIdNew != null) {
                moduleIdNew = em.getReference(moduleIdNew.getClass(), moduleIdNew.getModuleId());
                quizz.setModuleId(moduleIdNew);
            }
            Collection<Question> attachedQuestionCollectionNew = new ArrayList<Question>();
            for (Question questionCollectionNewQuestionToAttach : questionCollectionNew) {
                questionCollectionNewQuestionToAttach = em.getReference(questionCollectionNewQuestionToAttach.getClass(), questionCollectionNewQuestionToAttach.getQuestionId());
                attachedQuestionCollectionNew.add(questionCollectionNewQuestionToAttach);
            }
            questionCollectionNew = attachedQuestionCollectionNew;
            quizz.setQuestionCollection(questionCollectionNew);
            Collection<Score> attachedScoreCollectionNew = new ArrayList<Score>();
            for (Score scoreCollectionNewScoreToAttach : scoreCollectionNew) {
                scoreCollectionNewScoreToAttach = em.getReference(scoreCollectionNewScoreToAttach.getClass(), scoreCollectionNewScoreToAttach.getScoreId());
                attachedScoreCollectionNew.add(scoreCollectionNewScoreToAttach);
            }
            scoreCollectionNew = attachedScoreCollectionNew;
            quizz.setScoreCollection(scoreCollectionNew);
            quizz = em.merge(quizz);
            if (moduleIdOld != null && !moduleIdOld.equals(moduleIdNew)) {
                moduleIdOld.getQuizzCollection().remove(quizz);
                moduleIdOld = em.merge(moduleIdOld);
            }
            if (moduleIdNew != null && !moduleIdNew.equals(moduleIdOld)) {
                moduleIdNew.getQuizzCollection().add(quizz);
                moduleIdNew = em.merge(moduleIdNew);
            }
            for (Question questionCollectionNewQuestion : questionCollectionNew) {
                if (!questionCollectionOld.contains(questionCollectionNewQuestion)) {
                    Quizz oldQuizzIdOfQuestionCollectionNewQuestion = questionCollectionNewQuestion.getQuizzId();
                    questionCollectionNewQuestion.setQuizzId(quizz);
                    questionCollectionNewQuestion = em.merge(questionCollectionNewQuestion);
                    if (oldQuizzIdOfQuestionCollectionNewQuestion != null && !oldQuizzIdOfQuestionCollectionNewQuestion.equals(quizz)) {
                        oldQuizzIdOfQuestionCollectionNewQuestion.getQuestionCollection().remove(questionCollectionNewQuestion);
                        oldQuizzIdOfQuestionCollectionNewQuestion = em.merge(oldQuizzIdOfQuestionCollectionNewQuestion);
                    }
                }
            }
            for (Score scoreCollectionNewScore : scoreCollectionNew) {
                if (!scoreCollectionOld.contains(scoreCollectionNewScore)) {
                    Quizz oldQuizzIdOfScoreCollectionNewScore = scoreCollectionNewScore.getQuizzId();
                    scoreCollectionNewScore.setQuizzId(quizz);
                    scoreCollectionNewScore = em.merge(scoreCollectionNewScore);
                    if (oldQuizzIdOfScoreCollectionNewScore != null && !oldQuizzIdOfScoreCollectionNewScore.equals(quizz)) {
                        oldQuizzIdOfScoreCollectionNewScore.getScoreCollection().remove(scoreCollectionNewScore);
                        oldQuizzIdOfScoreCollectionNewScore = em.merge(oldQuizzIdOfScoreCollectionNewScore);
                    }
                }
            }
            //utx.commit();
            return quizz;
        } catch (Exception ex) {
            try {
                //utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = quizz.getQuizzId();
                if (findQuizz(id) == null) {
                    throw new NonexistentEntityException("The quizz with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public Quizz destroy(String id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        //EntityManager em = null;
        try {
            //utx.begin();
            //em = getEntityManager();
            Quizz quizz;
            try {
                quizz = em.getReference(Quizz.class, id);
                quizz.getQuizzId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The quizz with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Question> questionCollectionOrphanCheck = quizz.getQuestionCollection();
            for (Question questionCollectionOrphanCheckQuestion : questionCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Quizz (" + quizz + ") cannot be destroyed since the Question " + questionCollectionOrphanCheckQuestion + " in its questionCollection field has a non-nullable quizzId field.");
            }
            Collection<Score> scoreCollectionOrphanCheck = quizz.getScoreCollection();
            for (Score scoreCollectionOrphanCheckScore : scoreCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Quizz (" + quizz + ") cannot be destroyed since the Score " + scoreCollectionOrphanCheckScore + " in its scoreCollection field has a non-nullable quizzId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Module moduleId = quizz.getModuleId();
            if (moduleId != null) {
                moduleId.getQuizzCollection().remove(quizz);
                moduleId = em.merge(moduleId);
            }
            em.remove(quizz);
            //utx.commit();
            return quizz;
        } catch (Exception ex) {
            try {
                //utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public List<Quizz> findQuizzEntities() {
        return findQuizzEntities(true, -1, -1);
    }

    @Override
    public List<Quizz> findQuizzEntities(int maxResults, int firstResult) {
        return findQuizzEntities(false, maxResults, firstResult);
    }

    private List<Quizz> findQuizzEntities(boolean all, int maxResults, int firstResult) {
        //EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Quizz.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Quizz findQuizz(String id) {
        //EntityManager em = getEntityManager();
        try {
            return em.find(Quizz.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public int getQuizzCount() {
        //EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Quizz> rt = cq.from(Quizz.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Quizz> findQuizzsByModule(Module module) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root<Quizz> rt = cq.from(Quizz.class);
            cq.select(rt).where(cb.equal(rt.get("module_id"), module.getModuleId()));
            Query q = em.createQuery(cq);
            List<Quizz> quizzs = (List<Quizz>) q.getResultList();
            return quizzs;
        } finally {
            em.close();
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
