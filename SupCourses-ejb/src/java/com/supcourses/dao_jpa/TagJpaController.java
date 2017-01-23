/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supcourses.dao_jpa;

import com.supcourses.dao.TagDao;
import com.supcourses.dao_jpa.exceptions.IllegalOrphanException;
import com.supcourses.dao_jpa.exceptions.NonexistentEntityException;
import com.supcourses.dao_jpa.exceptions.PreexistingEntityException;
import com.supcourses.dao_jpa.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.supcourses.entity.Course;
import com.supcourses.entity.Tag;
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
public class TagJpaController implements TagDao {

    @PersistenceContext(unitName = "SupCourses-PU")
    private EntityManager em;
    
    //@Inject
    //private UserTransaction utx;


    @Override
    public void create(Tag tag) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (tag.getCourseCollection() == null) {
            tag.setCourseCollection(new ArrayList<Course>());
        }
        //EntityManager em = null;
        try {
            //utx.begin();
            //em = getEntityManager();
            Collection<Course> attachedCourseCollection = new ArrayList<Course>();
            for (Course courseCollectionCourseToAttach : tag.getCourseCollection()) {
                courseCollectionCourseToAttach = em.getReference(courseCollectionCourseToAttach.getClass(), courseCollectionCourseToAttach.getCourseId());
                attachedCourseCollection.add(courseCollectionCourseToAttach);
            }
            tag.setCourseCollection(attachedCourseCollection);
            em.persist(tag);
            for (Course courseCollectionCourse : tag.getCourseCollection()) {
                Tag oldTagIdOfCourseCollectionCourse = courseCollectionCourse.getTagId();
                courseCollectionCourse.setTagId(tag);
                courseCollectionCourse = em.merge(courseCollectionCourse);
                if (oldTagIdOfCourseCollectionCourse != null) {
                    oldTagIdOfCourseCollectionCourse.getCourseCollection().remove(courseCollectionCourse);
                    oldTagIdOfCourseCollectionCourse = em.merge(oldTagIdOfCourseCollectionCourse);
                }
            }
            //utx.commit();
        } catch (Exception ex) {
            try {
                //utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findTag(tag.getTagId()) != null) {
                throw new PreexistingEntityException("Tag " + tag + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public void edit(Tag tag) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        //EntityManager em = null;
        try {
            //utx.begin();
            //em = getEntityManager();
            Tag persistentTag = em.find(Tag.class, tag.getTagId());
            Collection<Course> courseCollectionOld = persistentTag.getCourseCollection();
            Collection<Course> courseCollectionNew = tag.getCourseCollection();
            List<String> illegalOrphanMessages = null;
            for (Course courseCollectionOldCourse : courseCollectionOld) {
                if (!courseCollectionNew.contains(courseCollectionOldCourse)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Course " + courseCollectionOldCourse + " since its tagId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Course> attachedCourseCollectionNew = new ArrayList<Course>();
            for (Course courseCollectionNewCourseToAttach : courseCollectionNew) {
                courseCollectionNewCourseToAttach = em.getReference(courseCollectionNewCourseToAttach.getClass(), courseCollectionNewCourseToAttach.getCourseId());
                attachedCourseCollectionNew.add(courseCollectionNewCourseToAttach);
            }
            courseCollectionNew = attachedCourseCollectionNew;
            tag.setCourseCollection(courseCollectionNew);
            tag = em.merge(tag);
            for (Course courseCollectionNewCourse : courseCollectionNew) {
                if (!courseCollectionOld.contains(courseCollectionNewCourse)) {
                    Tag oldTagIdOfCourseCollectionNewCourse = courseCollectionNewCourse.getTagId();
                    courseCollectionNewCourse.setTagId(tag);
                    courseCollectionNewCourse = em.merge(courseCollectionNewCourse);
                    if (oldTagIdOfCourseCollectionNewCourse != null && !oldTagIdOfCourseCollectionNewCourse.equals(tag)) {
                        oldTagIdOfCourseCollectionNewCourse.getCourseCollection().remove(courseCollectionNewCourse);
                        oldTagIdOfCourseCollectionNewCourse = em.merge(oldTagIdOfCourseCollectionNewCourse);
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
                String id = tag.getTagId();
                if (findTag(id) == null) {
                    throw new NonexistentEntityException("The tag with id " + id + " no longer exists.");
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
            Tag tag;
            try {
                tag = em.getReference(Tag.class, id);
                tag.getTagId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tag with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Course> courseCollectionOrphanCheck = tag.getCourseCollection();
            for (Course courseCollectionOrphanCheckCourse : courseCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tag (" + tag + ") cannot be destroyed since the Course " + courseCollectionOrphanCheckCourse + " in its courseCollection field has a non-nullable tagId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tag);
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
    public List<Tag> findTagEntities() {
        return findTagEntities(true, -1, -1);
    }

    @Override
    public List<Tag> findTagEntities(int maxResults, int firstResult) {
        return findTagEntities(false, maxResults, firstResult);
    }

    private List<Tag> findTagEntities(boolean all, int maxResults, int firstResult) {
        //EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tag.class));
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
    public Tag findTag(String id) {
        //EntityManager em = getEntityManager();
        try {
            return em.find(Tag.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public int getTagCount() {
        //EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tag> rt = cq.from(Tag.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
