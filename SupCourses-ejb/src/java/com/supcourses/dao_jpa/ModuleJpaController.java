/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supcourses.dao_jpa;

import com.supcourses.dao.ModuleDao;
import com.supcourses.dao_jpa.exceptions.IllegalOrphanException;
import com.supcourses.dao_jpa.exceptions.NonexistentEntityException;
import com.supcourses.dao_jpa.exceptions.PreexistingEntityException;
import com.supcourses.dao_jpa.exceptions.RollbackFailureException;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.supcourses.entity.Course;
import com.supcourses.entity.Module;
import com.supcourses.entity.Quizz;
import java.util.ArrayList;
import java.util.Collection;
import com.supcourses.entity.Topic;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.transaction.UserTransaction;

/**
 *
 * @author jngue
 */
@Stateless
public class ModuleJpaController implements ModuleDao {
    
    @PersistenceContext(unitName = "SupCourses-PU")
    private EntityManager em;
    
    //@Inject
    //private UserTransaction utx;

    @Override
    public Module create(Module module) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (module.getQuizzCollection() == null) {
            module.setQuizzCollection(new ArrayList<Quizz>());
        }
        if (module.getTopicCollection() == null) {
            module.setTopicCollection(new ArrayList<Topic>());
        }
        //EntityManager em = null;
        try {
            //utx.begin();
            //em = getEntityManager();
            Course courseId = module.getCourseId();
            if (courseId != null) {
                courseId = em.getReference(courseId.getClass(), courseId.getCourseId());
                module.setCourseId(courseId);
            }
            Collection<Quizz> attachedQuizzCollection = new ArrayList<Quizz>();
            for (Quizz quizzCollectionQuizzToAttach : module.getQuizzCollection()) {
                quizzCollectionQuizzToAttach = em.getReference(quizzCollectionQuizzToAttach.getClass(), quizzCollectionQuizzToAttach.getQuizzId());
                attachedQuizzCollection.add(quizzCollectionQuizzToAttach);
            }
            module.setQuizzCollection(attachedQuizzCollection);
            Collection<Topic> attachedTopicCollection = new ArrayList<Topic>();
            for (Topic topicCollectionTopicToAttach : module.getTopicCollection()) {
                topicCollectionTopicToAttach = em.getReference(topicCollectionTopicToAttach.getClass(), topicCollectionTopicToAttach.getTopicId());
                attachedTopicCollection.add(topicCollectionTopicToAttach);
            }
            module.setTopicCollection(attachedTopicCollection);
            em.persist(module);
            if (courseId != null) {
                courseId.getModuleCollection().add(module);
                courseId = em.merge(courseId);
            }
            for (Quizz quizzCollectionQuizz : module.getQuizzCollection()) {
                Module oldModuleIdOfQuizzCollectionQuizz = quizzCollectionQuizz.getModuleId();
                quizzCollectionQuizz.setModuleId(module);
                quizzCollectionQuizz = em.merge(quizzCollectionQuizz);
                if (oldModuleIdOfQuizzCollectionQuizz != null) {
                    oldModuleIdOfQuizzCollectionQuizz.getQuizzCollection().remove(quizzCollectionQuizz);
                    oldModuleIdOfQuizzCollectionQuizz = em.merge(oldModuleIdOfQuizzCollectionQuizz);
                }
            }
            for (Topic topicCollectionTopic : module.getTopicCollection()) {
                Module oldModuleIdOfTopicCollectionTopic = topicCollectionTopic.getModuleId();
                topicCollectionTopic.setModuleId(module);
                topicCollectionTopic = em.merge(topicCollectionTopic);
                if (oldModuleIdOfTopicCollectionTopic != null) {
                    oldModuleIdOfTopicCollectionTopic.getTopicCollection().remove(topicCollectionTopic);
                    oldModuleIdOfTopicCollectionTopic = em.merge(oldModuleIdOfTopicCollectionTopic);
                }
            }
            //utx.commit();
            return module;
        } catch (Exception ex) {
            try {
                //utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findModule(module.getModuleId()) != null) {
                throw new PreexistingEntityException("Module " + module + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
//                em.close();
            }
        }
    }

    @Override
    public Module edit(Module module) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        //EntityManager em = null;
        try {
            //utx.begin();
            //em = getEntityManager();
            Module persistentModule = em.find(Module.class, module.getModuleId());
            Course courseIdOld = persistentModule.getCourseId();
            Course courseIdNew = module.getCourseId();
            Collection<Quizz> quizzCollectionOld = persistentModule.getQuizzCollection();
            Collection<Quizz> quizzCollectionNew = module.getQuizzCollection();
            Collection<Topic> topicCollectionOld = persistentModule.getTopicCollection();
            Collection<Topic> topicCollectionNew = module.getTopicCollection();
            List<String> illegalOrphanMessages = null;
            for (Quizz quizzCollectionOldQuizz : quizzCollectionOld) {
                if (!quizzCollectionNew.contains(quizzCollectionOldQuizz)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Quizz " + quizzCollectionOldQuizz + " since its moduleId field is not nullable.");
                }
            }
            for (Topic topicCollectionOldTopic : topicCollectionOld) {
                if (!topicCollectionNew.contains(topicCollectionOldTopic)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Topic " + topicCollectionOldTopic + " since its moduleId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (courseIdNew != null) {
                courseIdNew = em.getReference(courseIdNew.getClass(), courseIdNew.getCourseId());
                module.setCourseId(courseIdNew);
            }
            Collection<Quizz> attachedQuizzCollectionNew = new ArrayList<Quizz>();
            for (Quizz quizzCollectionNewQuizzToAttach : quizzCollectionNew) {
                quizzCollectionNewQuizzToAttach = em.getReference(quizzCollectionNewQuizzToAttach.getClass(), quizzCollectionNewQuizzToAttach.getQuizzId());
                attachedQuizzCollectionNew.add(quizzCollectionNewQuizzToAttach);
            }
            quizzCollectionNew = attachedQuizzCollectionNew;
            module.setQuizzCollection(quizzCollectionNew);
            Collection<Topic> attachedTopicCollectionNew = new ArrayList<Topic>();
            for (Topic topicCollectionNewTopicToAttach : topicCollectionNew) {
                topicCollectionNewTopicToAttach = em.getReference(topicCollectionNewTopicToAttach.getClass(), topicCollectionNewTopicToAttach.getTopicId());
                attachedTopicCollectionNew.add(topicCollectionNewTopicToAttach);
            }
            topicCollectionNew = attachedTopicCollectionNew;
            module.setTopicCollection(topicCollectionNew);
            module = em.merge(module);
            if (courseIdOld != null && !courseIdOld.equals(courseIdNew)) {
                courseIdOld.getModuleCollection().remove(module);
                courseIdOld = em.merge(courseIdOld);
            }
            if (courseIdNew != null && !courseIdNew.equals(courseIdOld)) {
                courseIdNew.getModuleCollection().add(module);
                courseIdNew = em.merge(courseIdNew);
            }
            for (Quizz quizzCollectionNewQuizz : quizzCollectionNew) {
                if (!quizzCollectionOld.contains(quizzCollectionNewQuizz)) {
                    Module oldModuleIdOfQuizzCollectionNewQuizz = quizzCollectionNewQuizz.getModuleId();
                    quizzCollectionNewQuizz.setModuleId(module);
                    quizzCollectionNewQuizz = em.merge(quizzCollectionNewQuizz);
                    if (oldModuleIdOfQuizzCollectionNewQuizz != null && !oldModuleIdOfQuizzCollectionNewQuizz.equals(module)) {
                        oldModuleIdOfQuizzCollectionNewQuizz.getQuizzCollection().remove(quizzCollectionNewQuizz);
                        oldModuleIdOfQuizzCollectionNewQuizz = em.merge(oldModuleIdOfQuizzCollectionNewQuizz);
                    }
                }
            }
            for (Topic topicCollectionNewTopic : topicCollectionNew) {
                if (!topicCollectionOld.contains(topicCollectionNewTopic)) {
                    Module oldModuleIdOfTopicCollectionNewTopic = topicCollectionNewTopic.getModuleId();
                    topicCollectionNewTopic.setModuleId(module);
                    topicCollectionNewTopic = em.merge(topicCollectionNewTopic);
                    if (oldModuleIdOfTopicCollectionNewTopic != null && !oldModuleIdOfTopicCollectionNewTopic.equals(module)) {
                        oldModuleIdOfTopicCollectionNewTopic.getTopicCollection().remove(topicCollectionNewTopic);
                        oldModuleIdOfTopicCollectionNewTopic = em.merge(oldModuleIdOfTopicCollectionNewTopic);
                    }
                }
            }
            //utx.commit();
            return module;
        } catch (Exception ex) {
            try {
                //utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = module.getModuleId();
                if (findModule(id) == null) {
                    throw new NonexistentEntityException("The module with id " + id + " no longer exists.");
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
    public Module destroy(String id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        //EntityManager em = null;
        try {
            //utx.begin();
            //em = getEntityManager();
            Module module;
            try {
                module = em.getReference(Module.class, id);
                module.getModuleId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The module with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Quizz> quizzCollectionOrphanCheck = module.getQuizzCollection();
            for (Quizz quizzCollectionOrphanCheckQuizz : quizzCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Module (" + module + ") cannot be destroyed since the Quizz " + quizzCollectionOrphanCheckQuizz + " in its quizzCollection field has a non-nullable moduleId field.");
            }
            Collection<Topic> topicCollectionOrphanCheck = module.getTopicCollection();
            for (Topic topicCollectionOrphanCheckTopic : topicCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Module (" + module + ") cannot be destroyed since the Topic " + topicCollectionOrphanCheckTopic + " in its topicCollection field has a non-nullable moduleId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Course courseId = module.getCourseId();
            if (courseId != null) {
                courseId.getModuleCollection().remove(module);
                courseId = em.merge(courseId);
            }
            em.remove(module);
            //utx.commit();
            return module;
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
    public List<Module> findModuleEntities() {
        return findModuleEntities(true, -1, -1);
    }

    @Override
    public List<Module> findModuleEntities(int maxResults, int firstResult) {
        return findModuleEntities(false, maxResults, firstResult);
    }

    private List<Module> findModuleEntities(boolean all, int maxResults, int firstResult) {
        //EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Module.class));
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

    /**
     *
     * @param id
     * @return
     */
    @Override
    public Module findModule(String id) {
        //EntityManager em = getEntityManager();
        try {
            return em.find(Module.class, id);
        } finally {
//            em.close();
        }
    }

    @Override
    public int getModuleCount() {
        //EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Module> rt = cq.from(Module.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
//            em.close();
        }
    }

    @Override
    public Module findModuleByName(String name) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Module> findModulesCourse(Course course) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root<Module> rt = cq.from(Module.class);
            cq.select(rt).where(cb.equal(rt.get("course_id"), course.getCourseId()));
            Query q = em.createQuery(cq);
            List<Module> modules = (List<Module>) q.getResultList();
            return modules;
        } finally {
//            em.close();
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
