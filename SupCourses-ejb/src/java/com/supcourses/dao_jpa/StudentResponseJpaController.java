/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supcourses.dao_jpa;

import com.supcourses.dao.StudentResponseDao;
import com.supcourses.dao_jpa.exceptions.NonexistentEntityException;
import com.supcourses.dao_jpa.exceptions.PreexistingEntityException;
import com.supcourses.dao_jpa.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.supcourses.entity.Student;
import com.supcourses.entity.Response;
import com.supcourses.entity.StudentResponse;
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
public class StudentResponseJpaController implements StudentResponseDao {

    @PersistenceContext(unitName = "SupCourses-PU")
    private EntityManager em;
    
    //@Inject
    //private UserTransaction utx;


    @Override
    public void create(StudentResponse studentResponse) throws PreexistingEntityException, RollbackFailureException, Exception {
        //EntityManager em = null;
        try {
            //utx.begin();
            //em = getEntityManager();
            Student studentId = studentResponse.getStudentId();
            if (studentId != null) {
                studentId = em.getReference(studentId.getClass(), studentId.getStudentId());
                studentResponse.setStudentId(studentId);
            }
            Response responseId = studentResponse.getResponseId();
            if (responseId != null) {
                responseId = em.getReference(responseId.getClass(), responseId.getResponseId());
                studentResponse.setResponseId(responseId);
            }
            em.persist(studentResponse);
            if (studentId != null) {
                studentId.getStudentResponseCollection().add(studentResponse);
                studentId = em.merge(studentId);
            }
            if (responseId != null) {
                responseId.getStudentResponseCollection().add(studentResponse);
                responseId = em.merge(responseId);
            }
            //utx.commit();
        } catch (Exception ex) {
            try {
                //utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findStudentResponse(studentResponse.getStudentResponseId()) != null) {
                throw new PreexistingEntityException("StudentResponse " + studentResponse + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public void edit(StudentResponse studentResponse) throws NonexistentEntityException, RollbackFailureException, Exception {
        //EntityManager em = null;
        try {
            //utx.begin();
            //em = getEntityManager();
            StudentResponse persistentStudentResponse = em.find(StudentResponse.class, studentResponse.getStudentResponseId());
            Student studentIdOld = persistentStudentResponse.getStudentId();
            Student studentIdNew = studentResponse.getStudentId();
            Response responseIdOld = persistentStudentResponse.getResponseId();
            Response responseIdNew = studentResponse.getResponseId();
            if (studentIdNew != null) {
                studentIdNew = em.getReference(studentIdNew.getClass(), studentIdNew.getStudentId());
                studentResponse.setStudentId(studentIdNew);
            }
            if (responseIdNew != null) {
                responseIdNew = em.getReference(responseIdNew.getClass(), responseIdNew.getResponseId());
                studentResponse.setResponseId(responseIdNew);
            }
            studentResponse = em.merge(studentResponse);
            if (studentIdOld != null && !studentIdOld.equals(studentIdNew)) {
                studentIdOld.getStudentResponseCollection().remove(studentResponse);
                studentIdOld = em.merge(studentIdOld);
            }
            if (studentIdNew != null && !studentIdNew.equals(studentIdOld)) {
                studentIdNew.getStudentResponseCollection().add(studentResponse);
                studentIdNew = em.merge(studentIdNew);
            }
            if (responseIdOld != null && !responseIdOld.equals(responseIdNew)) {
                responseIdOld.getStudentResponseCollection().remove(studentResponse);
                responseIdOld = em.merge(responseIdOld);
            }
            if (responseIdNew != null && !responseIdNew.equals(responseIdOld)) {
                responseIdNew.getStudentResponseCollection().add(studentResponse);
                responseIdNew = em.merge(responseIdNew);
            }
            //utx.commit();
        } catch (Exception ex) {
            try {
               // utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = studentResponse.getStudentResponseId();
                if (findStudentResponse(id) == null) {
                    throw new NonexistentEntityException("The studentResponse with id " + id + " no longer exists.");
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
    public void destroy(String id) throws NonexistentEntityException, RollbackFailureException, Exception {
        //EntityManager em = null;
        try {
            //utx.begin();
            //em = getEntityManager();
            StudentResponse studentResponse;
            try {
                studentResponse = em.getReference(StudentResponse.class, id);
                studentResponse.getStudentResponseId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The studentResponse with id " + id + " no longer exists.", enfe);
            }
            Student studentId = studentResponse.getStudentId();
            if (studentId != null) {
                studentId.getStudentResponseCollection().remove(studentResponse);
                studentId = em.merge(studentId);
            }
            Response responseId = studentResponse.getResponseId();
            if (responseId != null) {
                responseId.getStudentResponseCollection().remove(studentResponse);
                responseId = em.merge(responseId);
            }
            em.remove(studentResponse);
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
    public List<StudentResponse> findStudentResponseEntities() {
        return findStudentResponseEntities(true, -1, -1);
    }

    @Override
    public List<StudentResponse> findStudentResponseEntities(int maxResults, int firstResult) {
        return findStudentResponseEntities(false, maxResults, firstResult);
    }

    private List<StudentResponse> findStudentResponseEntities(boolean all, int maxResults, int firstResult) {
        //EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(StudentResponse.class));
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
    public StudentResponse findStudentResponse(String id) {
        //EntityManager em = getEntityManager();
        try {
            return em.find(StudentResponse.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public int getStudentResponseCount() {
        //EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<StudentResponse> rt = cq.from(StudentResponse.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
