/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supcourses.dao_jpa;

import com.supcourses.dao.ResponseDao;
import com.supcourses.dao_jpa.exceptions.IllegalOrphanException;
import com.supcourses.dao_jpa.exceptions.NonexistentEntityException;
import com.supcourses.dao_jpa.exceptions.PreexistingEntityException;
import com.supcourses.dao_jpa.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.supcourses.entity.ResponseType;
import com.supcourses.entity.Question;
import com.supcourses.entity.Response;
import com.supcourses.entity.StudentResponse;
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
public class ResponseJpaController implements ResponseDao {

    @PersistenceContext(unitName = "SupCourses-PU")
    private EntityManager em;
    
    //@Inject
    //private UserTransaction utx;


    @Override
    public void create(Response response) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (response.getStudentResponseCollection() == null) {
            response.setStudentResponseCollection(new ArrayList<StudentResponse>());
        }
        //EntityManager em = null;
        try {
            //utx.begin();
            //em = getEntityManager();
            ResponseType responseTypeId = response.getResponseTypeId();
            if (responseTypeId != null) {
                responseTypeId = em.getReference(responseTypeId.getClass(), responseTypeId.getResponseTypeId());
                response.setResponseTypeId(responseTypeId);
            }
            Question questionId = response.getQuestionId();
            if (questionId != null) {
                questionId = em.getReference(questionId.getClass(), questionId.getQuestionId());
                response.setQuestionId(questionId);
            }
            Collection<StudentResponse> attachedStudentResponseCollection = new ArrayList<StudentResponse>();
            for (StudentResponse studentResponseCollectionStudentResponseToAttach : response.getStudentResponseCollection()) {
                studentResponseCollectionStudentResponseToAttach = em.getReference(studentResponseCollectionStudentResponseToAttach.getClass(), studentResponseCollectionStudentResponseToAttach.getStudentResponseId());
                attachedStudentResponseCollection.add(studentResponseCollectionStudentResponseToAttach);
            }
            response.setStudentResponseCollection(attachedStudentResponseCollection);
            em.persist(response);
            if (responseTypeId != null) {
                responseTypeId.getResponseCollection().add(response);
                responseTypeId = em.merge(responseTypeId);
            }
            if (questionId != null) {
                questionId.getResponseCollection().add(response);
                questionId = em.merge(questionId);
            }
            for (StudentResponse studentResponseCollectionStudentResponse : response.getStudentResponseCollection()) {
                Response oldResponseIdOfStudentResponseCollectionStudentResponse = studentResponseCollectionStudentResponse.getResponseId();
                studentResponseCollectionStudentResponse.setResponseId(response);
                studentResponseCollectionStudentResponse = em.merge(studentResponseCollectionStudentResponse);
                if (oldResponseIdOfStudentResponseCollectionStudentResponse != null) {
                    oldResponseIdOfStudentResponseCollectionStudentResponse.getStudentResponseCollection().remove(studentResponseCollectionStudentResponse);
                    oldResponseIdOfStudentResponseCollectionStudentResponse = em.merge(oldResponseIdOfStudentResponseCollectionStudentResponse);
                }
            }
            //utx.commit();
        } catch (Exception ex) {
            try {
                //utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findResponse(response.getResponseId()) != null) {
                throw new PreexistingEntityException("Response " + response + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public void edit(Response response) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        //EntityManager em = null;
        try {
            //utx.begin();
            //em = getEntityManager();
            Response persistentResponse = em.find(Response.class, response.getResponseId());
            ResponseType responseTypeIdOld = persistentResponse.getResponseTypeId();
            ResponseType responseTypeIdNew = response.getResponseTypeId();
            Question questionIdOld = persistentResponse.getQuestionId();
            Question questionIdNew = response.getQuestionId();
            Collection<StudentResponse> studentResponseCollectionOld = persistentResponse.getStudentResponseCollection();
            Collection<StudentResponse> studentResponseCollectionNew = response.getStudentResponseCollection();
            List<String> illegalOrphanMessages = null;
            for (StudentResponse studentResponseCollectionOldStudentResponse : studentResponseCollectionOld) {
                if (!studentResponseCollectionNew.contains(studentResponseCollectionOldStudentResponse)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain StudentResponse " + studentResponseCollectionOldStudentResponse + " since its responseId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (responseTypeIdNew != null) {
                responseTypeIdNew = em.getReference(responseTypeIdNew.getClass(), responseTypeIdNew.getResponseTypeId());
                response.setResponseTypeId(responseTypeIdNew);
            }
            if (questionIdNew != null) {
                questionIdNew = em.getReference(questionIdNew.getClass(), questionIdNew.getQuestionId());
                response.setQuestionId(questionIdNew);
            }
            Collection<StudentResponse> attachedStudentResponseCollectionNew = new ArrayList<StudentResponse>();
            for (StudentResponse studentResponseCollectionNewStudentResponseToAttach : studentResponseCollectionNew) {
                studentResponseCollectionNewStudentResponseToAttach = em.getReference(studentResponseCollectionNewStudentResponseToAttach.getClass(), studentResponseCollectionNewStudentResponseToAttach.getStudentResponseId());
                attachedStudentResponseCollectionNew.add(studentResponseCollectionNewStudentResponseToAttach);
            }
            studentResponseCollectionNew = attachedStudentResponseCollectionNew;
            response.setStudentResponseCollection(studentResponseCollectionNew);
            response = em.merge(response);
            if (responseTypeIdOld != null && !responseTypeIdOld.equals(responseTypeIdNew)) {
                responseTypeIdOld.getResponseCollection().remove(response);
                responseTypeIdOld = em.merge(responseTypeIdOld);
            }
            if (responseTypeIdNew != null && !responseTypeIdNew.equals(responseTypeIdOld)) {
                responseTypeIdNew.getResponseCollection().add(response);
                responseTypeIdNew = em.merge(responseTypeIdNew);
            }
            if (questionIdOld != null && !questionIdOld.equals(questionIdNew)) {
                questionIdOld.getResponseCollection().remove(response);
                questionIdOld = em.merge(questionIdOld);
            }
            if (questionIdNew != null && !questionIdNew.equals(questionIdOld)) {
                questionIdNew.getResponseCollection().add(response);
                questionIdNew = em.merge(questionIdNew);
            }
            for (StudentResponse studentResponseCollectionNewStudentResponse : studentResponseCollectionNew) {
                if (!studentResponseCollectionOld.contains(studentResponseCollectionNewStudentResponse)) {
                    Response oldResponseIdOfStudentResponseCollectionNewStudentResponse = studentResponseCollectionNewStudentResponse.getResponseId();
                    studentResponseCollectionNewStudentResponse.setResponseId(response);
                    studentResponseCollectionNewStudentResponse = em.merge(studentResponseCollectionNewStudentResponse);
                    if (oldResponseIdOfStudentResponseCollectionNewStudentResponse != null && !oldResponseIdOfStudentResponseCollectionNewStudentResponse.equals(response)) {
                        oldResponseIdOfStudentResponseCollectionNewStudentResponse.getStudentResponseCollection().remove(studentResponseCollectionNewStudentResponse);
                        oldResponseIdOfStudentResponseCollectionNewStudentResponse = em.merge(oldResponseIdOfStudentResponseCollectionNewStudentResponse);
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
                String id = response.getResponseId();
                if (findResponse(id) == null) {
                    throw new NonexistentEntityException("The response with id " + id + " no longer exists.");
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
            Response response;
            try {
                response = em.getReference(Response.class, id);
                response.getResponseId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The response with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<StudentResponse> studentResponseCollectionOrphanCheck = response.getStudentResponseCollection();
            for (StudentResponse studentResponseCollectionOrphanCheckStudentResponse : studentResponseCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Response (" + response + ") cannot be destroyed since the StudentResponse " + studentResponseCollectionOrphanCheckStudentResponse + " in its studentResponseCollection field has a non-nullable responseId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            ResponseType responseTypeId = response.getResponseTypeId();
            if (responseTypeId != null) {
                responseTypeId.getResponseCollection().remove(response);
                responseTypeId = em.merge(responseTypeId);
            }
            Question questionId = response.getQuestionId();
            if (questionId != null) {
                questionId.getResponseCollection().remove(response);
                questionId = em.merge(questionId);
            }
            em.remove(response);
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
    public List<Response> findResponseEntities() {
        return findResponseEntities(true, -1, -1);
    }

    @Override
    public List<Response> findResponseEntities(int maxResults, int firstResult) {
        return findResponseEntities(false, maxResults, firstResult);
    }

    private List<Response> findResponseEntities(boolean all, int maxResults, int firstResult) {
        //EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Response.class));
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
    public Response findResponse(String id) {
        //EntityManager em = getEntityManager();
        try {
            return em.find(Response.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public int getResponseCount() {
        //EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Response> rt = cq.from(Response.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
