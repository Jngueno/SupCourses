/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supcourses.dao_jpa;

import com.supcourses.dao.ResponseTypeDao;
import com.supcourses.dao_jpa.exceptions.IllegalOrphanException;
import com.supcourses.dao_jpa.exceptions.NonexistentEntityException;
import com.supcourses.dao_jpa.exceptions.PreexistingEntityException;
import com.supcourses.dao_jpa.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.supcourses.entity.Response;
import com.supcourses.entity.ResponseType;
import java.util.ArrayList;
import java.util.Collection;
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
public class ResponseTypeJpaController implements ResponseTypeDao {

    @PersistenceContext(unitName = "SupCourses-PU")
    private EntityManager em;
    
    //@Inject
    //private UserTransaction utx;


    @Override
    public ResponseType create(ResponseType responseType) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (responseType.getResponseCollection() == null) {
            responseType.setResponseCollection(new ArrayList<Response>());
        }
        //EntityManager em = null;
        try {
            //utx.begin();
            //em = getEntityManager();
            Collection<Response> attachedResponseCollection = new ArrayList<Response>();
            for (Response responseCollectionResponseToAttach : responseType.getResponseCollection()) {
                responseCollectionResponseToAttach = em.getReference(responseCollectionResponseToAttach.getClass(), responseCollectionResponseToAttach.getResponseId());
                attachedResponseCollection.add(responseCollectionResponseToAttach);
            }
            responseType.setResponseCollection(attachedResponseCollection);
            em.persist(responseType);
            for (Response responseCollectionResponse : responseType.getResponseCollection()) {
                ResponseType oldResponseTypeIdOfResponseCollectionResponse = responseCollectionResponse.getResponseTypeId();
                responseCollectionResponse.setResponseTypeId(responseType);
                responseCollectionResponse = em.merge(responseCollectionResponse);
                if (oldResponseTypeIdOfResponseCollectionResponse != null) {
                    oldResponseTypeIdOfResponseCollectionResponse.getResponseCollection().remove(responseCollectionResponse);
                    oldResponseTypeIdOfResponseCollectionResponse = em.merge(oldResponseTypeIdOfResponseCollectionResponse);
                }
            }
            //utx.commit();
            return responseType;
        } catch (Exception ex) {
            try {
                //utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findResponseType(responseType.getResponseTypeId()) != null) {
                throw new PreexistingEntityException("ResponseType " + responseType + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
//                em.close();
            }
        }
    }

    @Override
    public ResponseType edit(ResponseType responseType) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        //EntityManager em = null;
        try {
            //utx.begin();
            //em = getEntityManager();
            ResponseType persistentResponseType = em.find(ResponseType.class, responseType.getResponseTypeId());
            Collection<Response> responseCollectionOld = persistentResponseType.getResponseCollection();
            Collection<Response> responseCollectionNew = responseType.getResponseCollection();
            List<String> illegalOrphanMessages = null;
            for (Response responseCollectionOldResponse : responseCollectionOld) {
                if (!responseCollectionNew.contains(responseCollectionOldResponse)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Response " + responseCollectionOldResponse + " since its responseTypeId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Response> attachedResponseCollectionNew = new ArrayList<Response>();
            for (Response responseCollectionNewResponseToAttach : responseCollectionNew) {
                responseCollectionNewResponseToAttach = em.getReference(responseCollectionNewResponseToAttach.getClass(), responseCollectionNewResponseToAttach.getResponseId());
                attachedResponseCollectionNew.add(responseCollectionNewResponseToAttach);
            }
            responseCollectionNew = attachedResponseCollectionNew;
            responseType.setResponseCollection(responseCollectionNew);
            responseType = em.merge(responseType);
            for (Response responseCollectionNewResponse : responseCollectionNew) {
                if (!responseCollectionOld.contains(responseCollectionNewResponse)) {
                    ResponseType oldResponseTypeIdOfResponseCollectionNewResponse = responseCollectionNewResponse.getResponseTypeId();
                    responseCollectionNewResponse.setResponseTypeId(responseType);
                    responseCollectionNewResponse = em.merge(responseCollectionNewResponse);
                    if (oldResponseTypeIdOfResponseCollectionNewResponse != null && !oldResponseTypeIdOfResponseCollectionNewResponse.equals(responseType)) {
                        oldResponseTypeIdOfResponseCollectionNewResponse.getResponseCollection().remove(responseCollectionNewResponse);
                        oldResponseTypeIdOfResponseCollectionNewResponse = em.merge(oldResponseTypeIdOfResponseCollectionNewResponse);
                    }
                }
            }
            //utx.commit();
            return responseType;
        } catch (Exception ex) {
            try {
                //utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = responseType.getResponseTypeId();
                if (findResponseType(id) == null) {
                    throw new NonexistentEntityException("The responseType with id " + id + " no longer exists.");
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
    public ResponseType destroy(String id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        //EntityManager em = null;
        try {
            //utx.begin();
            //em = getEntityManager();
            ResponseType responseType;
            try {
                responseType = em.getReference(ResponseType.class, id);
                responseType.getResponseTypeId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The responseType with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Response> responseCollectionOrphanCheck = responseType.getResponseCollection();
            for (Response responseCollectionOrphanCheckResponse : responseCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ResponseType (" + responseType + ") cannot be destroyed since the Response " + responseCollectionOrphanCheckResponse + " in its responseCollection field has a non-nullable responseTypeId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(responseType);
            //utx.commit();
            return responseType;
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
    public List<ResponseType> findResponseTypeEntities() {
        return findResponseTypeEntities(true, -1, -1);
    }

    @Override
    public List<ResponseType> findResponseTypeEntities(int maxResults, int firstResult) {
        return findResponseTypeEntities(false, maxResults, firstResult);
    }

    private List<ResponseType> findResponseTypeEntities(boolean all, int maxResults, int firstResult) {
        //EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ResponseType.class));
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
    public ResponseType findResponseType(String id) {
        //EntityManager em = getEntityManager();
        try {
            return em.find(ResponseType.class, id);
        } finally {
//            em.close();
        }
    }

    @Override
    public int getResponseTypeCount() {
        //EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ResponseType> rt = cq.from(ResponseType.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
//            em.close();
        }
    }

    @Override
    public ResponseType findResponseTypeByResponseTypeContent(String responseTypeContent) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root<ResponseType> rt = cq.from(ResponseType.class);
            cq.select(rt).where(cb.equal(rt.get("response_type_content"), responseTypeContent));
            Query q = em.createQuery(cq);
            ResponseType responseType = (ResponseType) q.getSingleResult();
            return responseType;
        } finally {
//            em.close();
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
