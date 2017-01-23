/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supcourses.dao_jpa;

import com.supcourses.dao.TopicDao;
import com.supcourses.dao_jpa.exceptions.NonexistentEntityException;
import com.supcourses.dao_jpa.exceptions.PreexistingEntityException;
import com.supcourses.dao_jpa.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.supcourses.entity.Module;
import com.supcourses.entity.Topic;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.ParameterExpression;
import javax.transaction.UserTransaction;

/**
 *
 * @author jngue
 */
@Stateless
public class TopicJpaController implements TopicDao {

    @PersistenceContext(unitName = "SupCourses-PU")
    private EntityManager em;
    
    //@Inject
    //private UserTransaction utx;


    @Override
    public Topic create(Topic topic) throws PreexistingEntityException, RollbackFailureException, Exception {
        //EntityManager em = null;
        try {
            //utx.begin();
            //em = getEntityManager();
            Module moduleId = topic.getModuleId();
            if (moduleId != null) {
                moduleId = em.getReference(moduleId.getClass(), moduleId.getModuleId());
                topic.setModuleId(moduleId);
            }
            em.persist(topic);
            if (moduleId != null) {
                moduleId.getTopicCollection().add(topic);
                moduleId = em.merge(moduleId);
            }
            //utx.commit();
            return topic;
            
        } catch (Exception ex) {
            try {
                //utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findTopic(topic.getTopicId()) != null) {
                throw new PreexistingEntityException("Topic " + topic + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public Topic edit(Topic topic) throws NonexistentEntityException, RollbackFailureException, Exception {
        //EntityManager em = null;
        try {
            //utx.begin();
            //em = getEntityManager();
            Topic persistentTopic = em.find(Topic.class, topic.getTopicId());
            Module moduleIdOld = persistentTopic.getModuleId();
            Module moduleIdNew = topic.getModuleId();
            if (moduleIdNew != null) {
                moduleIdNew = em.getReference(moduleIdNew.getClass(), moduleIdNew.getModuleId());
                topic.setModuleId(moduleIdNew);
            }
            topic = em.merge(topic);
            if (moduleIdOld != null && !moduleIdOld.equals(moduleIdNew)) {
                moduleIdOld.getTopicCollection().remove(topic);
                moduleIdOld = em.merge(moduleIdOld);
            }
            if (moduleIdNew != null && !moduleIdNew.equals(moduleIdOld)) {
                moduleIdNew.getTopicCollection().add(topic);
                moduleIdNew = em.merge(moduleIdNew);
            }
            //utx.commit();
            return topic;
        } catch (Exception ex) {
            try {
                //utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = topic.getTopicId();
                if (findTopic(id) == null) {
                    throw new NonexistentEntityException("The topic with id " + id + " no longer exists.");
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
    public Topic destroy(String id) throws NonexistentEntityException, RollbackFailureException, Exception {
        //EntityManager em = null;
        try {
            //utx.begin();
            //em = getEntityManager();
            Topic topic;
            try {
                topic = em.getReference(Topic.class, id);
                topic.getTopicId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The topic with id " + id + " no longer exists.", enfe);
            }
            Module moduleId = topic.getModuleId();
            if (moduleId != null) {
                moduleId.getTopicCollection().remove(topic);
                moduleId = em.merge(moduleId);
            }
            em.remove(topic);
            //utx.commit();
            return topic;
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
    public List<Topic> findTopicEntities() {
        return findTopicEntities(true, -1, -1);
    }

    @Override
    public List<Topic> findTopicEntities(int maxResults, int firstResult) {
        return findTopicEntities(false, maxResults, firstResult);
    }
    
    private List<Topic> findTopicEntities(boolean all, int maxResults, int firstResult) {
        //EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Topic.class));
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
    public Topic findTopic(String id) {
        //EntityManager em = getEntityManager();
        try {
            return em.find(Topic.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public int getTopicCount() {
        //EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Topic> rt = cq.from(Topic.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    @Override
    public Topic findTopicByTitle(String title) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Topic> rt = cq.from(Topic.class);
        ParameterExpression<String> t = cb.parameter(String.class);
        
        cq.select(rt).where(cb.equal(rt.get("title"), t));
        Query q = em.createQuery(cq);
        Topic topic = (Topic) q.getSingleResult();
        return topic;
    }

    @Override
    public List<Topic> findTopicEntities(boolean validate) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
