/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supcourses.dao_jpa;

import com.supcourses.dao.QuestionDao;
import com.supcourses.dao_jpa.exceptions.IllegalOrphanException;
import com.supcourses.dao_jpa.exceptions.NonexistentEntityException;
import com.supcourses.dao_jpa.exceptions.PreexistingEntityException;
import com.supcourses.dao_jpa.exceptions.RollbackFailureException;
import com.supcourses.entity.Question;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.supcourses.entity.Quizz;
import com.supcourses.entity.Response;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

/**
 *
 * @author jngue
 */
@Stateless
public class QuestionJpaController implements QuestionDao {

    @PersistenceContext(unitName = "SupCourses-PU")
    private EntityManager em;
    
    //@Inject
    //private UserTransaction utx;

    @Override
    public void create(Question question) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (question.getResponseCollection() == null) {
            question.setResponseCollection(new ArrayList<Response>());
        }
        try {
            //utx.begin();
            Quizz quizzId = question.getQuizzId();
            if (quizzId != null) {
                quizzId = em.getReference(quizzId.getClass(), quizzId.getQuizzId());
                question.setQuizzId(quizzId);
            }
            Collection<Response> attachedResponseCollection = new ArrayList<Response>();
            for (Response responseCollectionResponseToAttach : question.getResponseCollection()) {
                responseCollectionResponseToAttach = em.getReference(responseCollectionResponseToAttach.getClass(), responseCollectionResponseToAttach.getResponseId());
                attachedResponseCollection.add(responseCollectionResponseToAttach);
            }
            question.setResponseCollection(attachedResponseCollection);
            em.persist(question);
            if (quizzId != null) {
                quizzId.getQuestionCollection().add(question);
                quizzId = em.merge(quizzId);
            }
            for (Response responseCollectionResponse : question.getResponseCollection()) {
                Question oldQuestionIdOfResponseCollectionResponse = responseCollectionResponse.getQuestionId();
                responseCollectionResponse.setQuestionId(question);
                responseCollectionResponse = em.merge(responseCollectionResponse);
                if (oldQuestionIdOfResponseCollectionResponse != null) {
                    oldQuestionIdOfResponseCollectionResponse.getResponseCollection().remove(responseCollectionResponse);
                    oldQuestionIdOfResponseCollectionResponse = em.merge(oldQuestionIdOfResponseCollectionResponse);
                }
            }
            //utx.commit();
        } catch (Exception ex) {
            try {
                //utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findQuestion(question.getQuestionId()) != null) {
                throw new PreexistingEntityException("Question " + question + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public void edit(Question question) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        //EntityManager em = null;
        try {
            //utx.begin();
            //em = getEntityManager();
            Question persistentQuestion = em.find(Question.class, question.getQuestionId());
            Quizz quizzIdOld = persistentQuestion.getQuizzId();
            Quizz quizzIdNew = question.getQuizzId();
            Collection<Response> responseCollectionOld = persistentQuestion.getResponseCollection();
            Collection<Response> responseCollectionNew = question.getResponseCollection();
            List<String> illegalOrphanMessages = null;
            for (Response responseCollectionOldResponse : responseCollectionOld) {
                if (!responseCollectionNew.contains(responseCollectionOldResponse)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Response " + responseCollectionOldResponse + " since its questionId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (quizzIdNew != null) {
                quizzIdNew = em.getReference(quizzIdNew.getClass(), quizzIdNew.getQuizzId());
                question.setQuizzId(quizzIdNew);
            }
            Collection<Response> attachedResponseCollectionNew = new ArrayList<Response>();
            for (Response responseCollectionNewResponseToAttach : responseCollectionNew) {
                responseCollectionNewResponseToAttach = em.getReference(responseCollectionNewResponseToAttach.getClass(), responseCollectionNewResponseToAttach.getResponseId());
                attachedResponseCollectionNew.add(responseCollectionNewResponseToAttach);
            }
            responseCollectionNew = attachedResponseCollectionNew;
            question.setResponseCollection(responseCollectionNew);
            question = em.merge(question);
            if (quizzIdOld != null && !quizzIdOld.equals(quizzIdNew)) {
                quizzIdOld.getQuestionCollection().remove(question);
                quizzIdOld = em.merge(quizzIdOld);
            }
            if (quizzIdNew != null && !quizzIdNew.equals(quizzIdOld)) {
                quizzIdNew.getQuestionCollection().add(question);
                quizzIdNew = em.merge(quizzIdNew);
            }
            for (Response responseCollectionNewResponse : responseCollectionNew) {
                if (!responseCollectionOld.contains(responseCollectionNewResponse)) {
                    Question oldQuestionIdOfResponseCollectionNewResponse = responseCollectionNewResponse.getQuestionId();
                    responseCollectionNewResponse.setQuestionId(question);
                    responseCollectionNewResponse = em.merge(responseCollectionNewResponse);
                    if (oldQuestionIdOfResponseCollectionNewResponse != null && !oldQuestionIdOfResponseCollectionNewResponse.equals(question)) {
                        oldQuestionIdOfResponseCollectionNewResponse.getResponseCollection().remove(responseCollectionNewResponse);
                        oldQuestionIdOfResponseCollectionNewResponse = em.merge(oldQuestionIdOfResponseCollectionNewResponse);
                    }
                }
            }
            //utx.commit();
        } catch (Exception ex) {
            try {
                //utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = question.getQuestionId();
                if (findQuestion(id) == null) {
                    throw new NonexistentEntityException("The question with id " + id + " no longer exists.");
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
    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        //EntityManager em = null;
        try {
            //utx.begin();
            //em = getEntityManager();
            Question question;
            try {
                question = em.getReference(Question.class, id);
                question.getQuestionId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The question with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Response> responseCollectionOrphanCheck = question.getResponseCollection();
            for (Response responseCollectionOrphanCheckResponse : responseCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Question (" + question + ") cannot be destroyed since the Response " + responseCollectionOrphanCheckResponse + " in its responseCollection field has a non-nullable questionId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Quizz quizzId = question.getQuizzId();
            if (quizzId != null) {
                quizzId.getQuestionCollection().remove(question);
                quizzId = em.merge(quizzId);
            }
            em.remove(question);
            //utx.commit();
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
    public List<Question> findQuestionEntities() {
        return findQuestionEntities(true, -1, -1);
    }

    @Override
    public List<Question> findQuestionEntities(int maxResults, int firstResult) {
        return findQuestionEntities(false, maxResults, firstResult);
    }

    private List<Question> findQuestionEntities(boolean all, int maxResults, int firstResult) {
        //EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Question.class));
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
    public Question findQuestion(String id) {
        //EntityManager em = getEntityManager();
        try {
            return em.find(Question.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public int getQuestionCount() {
        //EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Question> rt = cq.from(Question.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
