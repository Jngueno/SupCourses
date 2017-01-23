/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supcourses.dao_jpa;

import com.supcourses.dao.CourseDao;
import com.supcourses.dao_jpa.exceptions.IllegalOrphanException;
import com.supcourses.dao_jpa.exceptions.NonexistentEntityException;
import com.supcourses.dao_jpa.exceptions.PreexistingEntityException;
import com.supcourses.dao_jpa.exceptions.RollbackFailureException;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.supcourses.entity.Tag;
import com.supcourses.entity.Module;
import java.util.ArrayList;
import java.util.Collection;
import com.supcourses.entity.Certification;
import com.supcourses.entity.Course;
import com.supcourses.entity.StudentCourse;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

/**
 *
 * @author jngue
 */
@Stateless
public class CourseJpaController implements CourseDao {
    
    @PersistenceContext(unitName = "SupCourses-PU")
    private EntityManager em;

    //@Inject
    //private UserTransaction utx;

    @Override
    public void create(Course course) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (course.getModuleCollection() == null) {
            course.setModuleCollection(new ArrayList<Module>());
        }
        if (course.getCertificationCollection() == null) {
            course.setCertificationCollection(new ArrayList<Certification>());
        }
        if (course.getStudentCourseCollection() == null) {
            course.setStudentCourseCollection(new ArrayList<StudentCourse>());
        }
        try {
            //utx.begin();
            Tag tagId = course.getTagId();
            if (tagId != null) {
                tagId = em.getReference(tagId.getClass(), tagId.getTagId());
                course.setTagId(tagId);
            }
            Collection<Module> attachedModuleCollection = new ArrayList<Module>();
            for (Module moduleCollectionModuleToAttach : course.getModuleCollection()) {
                moduleCollectionModuleToAttach = em.getReference(moduleCollectionModuleToAttach.getClass(), moduleCollectionModuleToAttach.getModuleId());
                attachedModuleCollection.add(moduleCollectionModuleToAttach);
            }
            course.setModuleCollection(attachedModuleCollection);
            Collection<Certification> attachedCertificationCollection = new ArrayList<Certification>();
            for (Certification certificationCollectionCertificationToAttach : course.getCertificationCollection()) {
                certificationCollectionCertificationToAttach = em.getReference(certificationCollectionCertificationToAttach.getClass(), certificationCollectionCertificationToAttach.getCertificationId());
                attachedCertificationCollection.add(certificationCollectionCertificationToAttach);
            }
            course.setCertificationCollection(attachedCertificationCollection);
            Collection<StudentCourse> attachedStudentCourseCollection = new ArrayList<StudentCourse>();
            for (StudentCourse studentCourseCollectionStudentCourseToAttach : course.getStudentCourseCollection()) {
                studentCourseCollectionStudentCourseToAttach = em.getReference(studentCourseCollectionStudentCourseToAttach.getClass(), studentCourseCollectionStudentCourseToAttach.getStudentCourseId());
                attachedStudentCourseCollection.add(studentCourseCollectionStudentCourseToAttach);
            }
            course.setStudentCourseCollection(attachedStudentCourseCollection);
            em.persist(course);
            if (tagId != null) {
                tagId.getCourseCollection().add(course);
                tagId = em.merge(tagId);
            }
            for (Module moduleCollectionModule : course.getModuleCollection()) {
                Course oldCourseIdOfModuleCollectionModule = moduleCollectionModule.getCourseId();
                moduleCollectionModule.setCourseId(course);
                moduleCollectionModule = em.merge(moduleCollectionModule);
                if (oldCourseIdOfModuleCollectionModule != null) {
                    oldCourseIdOfModuleCollectionModule.getModuleCollection().remove(moduleCollectionModule);
                    oldCourseIdOfModuleCollectionModule = em.merge(oldCourseIdOfModuleCollectionModule);
                }
            }
            for (Certification certificationCollectionCertification : course.getCertificationCollection()) {
                Course oldCourseIdOfCertificationCollectionCertification = certificationCollectionCertification.getCourseId();
                certificationCollectionCertification.setCourseId(course);
                certificationCollectionCertification = em.merge(certificationCollectionCertification);
                if (oldCourseIdOfCertificationCollectionCertification != null) {
                    oldCourseIdOfCertificationCollectionCertification.getCertificationCollection().remove(certificationCollectionCertification);
                    oldCourseIdOfCertificationCollectionCertification = em.merge(oldCourseIdOfCertificationCollectionCertification);
                }
            }
            for (StudentCourse studentCourseCollectionStudentCourse : course.getStudentCourseCollection()) {
                Course oldCourseIdOfStudentCourseCollectionStudentCourse = studentCourseCollectionStudentCourse.getCourseId();
                studentCourseCollectionStudentCourse.setCourseId(course);
                studentCourseCollectionStudentCourse = em.merge(studentCourseCollectionStudentCourse);
                if (oldCourseIdOfStudentCourseCollectionStudentCourse != null) {
                    oldCourseIdOfStudentCourseCollectionStudentCourse.getStudentCourseCollection().remove(studentCourseCollectionStudentCourse);
                    oldCourseIdOfStudentCourseCollectionStudentCourse = em.merge(oldCourseIdOfStudentCourseCollectionStudentCourse);
                }
            }
            //utx.commit();
        } catch (Exception ex) {
            try {
                //utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findCourse(course.getCourseId()) != null) {
                throw new PreexistingEntityException("Course " + course + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public void edit(Course course) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        try {
            //utx.begin();
            //em = getEntityManager();
            Course persistentCourse = em.find(Course.class, course.getCourseId());
            Tag tagIdOld = persistentCourse.getTagId();
            Tag tagIdNew = course.getTagId();
            Collection<Module> moduleCollectionOld = persistentCourse.getModuleCollection();
            Collection<Module> moduleCollectionNew = course.getModuleCollection();
            Collection<Certification> certificationCollectionOld = persistentCourse.getCertificationCollection();
            Collection<Certification> certificationCollectionNew = course.getCertificationCollection();
            Collection<StudentCourse> studentCourseCollectionOld = persistentCourse.getStudentCourseCollection();
            Collection<StudentCourse> studentCourseCollectionNew = course.getStudentCourseCollection();
            List<String> illegalOrphanMessages = null;
            for (Module moduleCollectionOldModule : moduleCollectionOld) {
                if (!moduleCollectionNew.contains(moduleCollectionOldModule)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Module " + moduleCollectionOldModule + " since its courseId field is not nullable.");
                }
            }
            for (Certification certificationCollectionOldCertification : certificationCollectionOld) {
                if (!certificationCollectionNew.contains(certificationCollectionOldCertification)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Certification " + certificationCollectionOldCertification + " since its courseId field is not nullable.");
                }
            }
            for (StudentCourse studentCourseCollectionOldStudentCourse : studentCourseCollectionOld) {
                if (!studentCourseCollectionNew.contains(studentCourseCollectionOldStudentCourse)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain StudentCourse " + studentCourseCollectionOldStudentCourse + " since its courseId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (tagIdNew != null) {
                tagIdNew = em.getReference(tagIdNew.getClass(), tagIdNew.getTagId());
                course.setTagId(tagIdNew);
            }
            Collection<Module> attachedModuleCollectionNew = new ArrayList<Module>();
            for (Module moduleCollectionNewModuleToAttach : moduleCollectionNew) {
                moduleCollectionNewModuleToAttach = em.getReference(moduleCollectionNewModuleToAttach.getClass(), moduleCollectionNewModuleToAttach.getModuleId());
                attachedModuleCollectionNew.add(moduleCollectionNewModuleToAttach);
            }
            moduleCollectionNew = attachedModuleCollectionNew;
            course.setModuleCollection(moduleCollectionNew);
            Collection<Certification> attachedCertificationCollectionNew = new ArrayList<Certification>();
            for (Certification certificationCollectionNewCertificationToAttach : certificationCollectionNew) {
                certificationCollectionNewCertificationToAttach = em.getReference(certificationCollectionNewCertificationToAttach.getClass(), certificationCollectionNewCertificationToAttach.getCertificationId());
                attachedCertificationCollectionNew.add(certificationCollectionNewCertificationToAttach);
            }
            certificationCollectionNew = attachedCertificationCollectionNew;
            course.setCertificationCollection(certificationCollectionNew);
            Collection<StudentCourse> attachedStudentCourseCollectionNew = new ArrayList<StudentCourse>();
            for (StudentCourse studentCourseCollectionNewStudentCourseToAttach : studentCourseCollectionNew) {
                studentCourseCollectionNewStudentCourseToAttach = em.getReference(studentCourseCollectionNewStudentCourseToAttach.getClass(), studentCourseCollectionNewStudentCourseToAttach.getStudentCourseId());
                attachedStudentCourseCollectionNew.add(studentCourseCollectionNewStudentCourseToAttach);
            }
            studentCourseCollectionNew = attachedStudentCourseCollectionNew;
            course.setStudentCourseCollection(studentCourseCollectionNew);
            course = em.merge(course);
            if (tagIdOld != null && !tagIdOld.equals(tagIdNew)) {
                tagIdOld.getCourseCollection().remove(course);
                tagIdOld = em.merge(tagIdOld);
            }
            if (tagIdNew != null && !tagIdNew.equals(tagIdOld)) {
                tagIdNew.getCourseCollection().add(course);
                tagIdNew = em.merge(tagIdNew);
            }
            for (Module moduleCollectionNewModule : moduleCollectionNew) {
                if (!moduleCollectionOld.contains(moduleCollectionNewModule)) {
                    Course oldCourseIdOfModuleCollectionNewModule = moduleCollectionNewModule.getCourseId();
                    moduleCollectionNewModule.setCourseId(course);
                    moduleCollectionNewModule = em.merge(moduleCollectionNewModule);
                    if (oldCourseIdOfModuleCollectionNewModule != null && !oldCourseIdOfModuleCollectionNewModule.equals(course)) {
                        oldCourseIdOfModuleCollectionNewModule.getModuleCollection().remove(moduleCollectionNewModule);
                        oldCourseIdOfModuleCollectionNewModule = em.merge(oldCourseIdOfModuleCollectionNewModule);
                    }
                }
            }
            for (Certification certificationCollectionNewCertification : certificationCollectionNew) {
                if (!certificationCollectionOld.contains(certificationCollectionNewCertification)) {
                    Course oldCourseIdOfCertificationCollectionNewCertification = certificationCollectionNewCertification.getCourseId();
                    certificationCollectionNewCertification.setCourseId(course);
                    certificationCollectionNewCertification = em.merge(certificationCollectionNewCertification);
                    if (oldCourseIdOfCertificationCollectionNewCertification != null && !oldCourseIdOfCertificationCollectionNewCertification.equals(course)) {
                        oldCourseIdOfCertificationCollectionNewCertification.getCertificationCollection().remove(certificationCollectionNewCertification);
                        oldCourseIdOfCertificationCollectionNewCertification = em.merge(oldCourseIdOfCertificationCollectionNewCertification);
                    }
                }
            }
            for (StudentCourse studentCourseCollectionNewStudentCourse : studentCourseCollectionNew) {
                if (!studentCourseCollectionOld.contains(studentCourseCollectionNewStudentCourse)) {
                    Course oldCourseIdOfStudentCourseCollectionNewStudentCourse = studentCourseCollectionNewStudentCourse.getCourseId();
                    studentCourseCollectionNewStudentCourse.setCourseId(course);
                    studentCourseCollectionNewStudentCourse = em.merge(studentCourseCollectionNewStudentCourse);
                    if (oldCourseIdOfStudentCourseCollectionNewStudentCourse != null && !oldCourseIdOfStudentCourseCollectionNewStudentCourse.equals(course)) {
                        oldCourseIdOfStudentCourseCollectionNewStudentCourse.getStudentCourseCollection().remove(studentCourseCollectionNewStudentCourse);
                        oldCourseIdOfStudentCourseCollectionNewStudentCourse = em.merge(oldCourseIdOfStudentCourseCollectionNewStudentCourse);
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
                String id = course.getCourseId();
                if (findCourse(id) == null) {
                    throw new NonexistentEntityException("The course with id " + id + " no longer exists.");
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
        try {
            //utx.begin();
            Course course;
            try {
                course = em.getReference(Course.class, id);
                course.getCourseId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The course with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Module> moduleCollectionOrphanCheck = course.getModuleCollection();
            for (Module moduleCollectionOrphanCheckModule : moduleCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Course (" + course + ") cannot be destroyed since the Module " + moduleCollectionOrphanCheckModule + " in its moduleCollection field has a non-nullable courseId field.");
            }
            Collection<Certification> certificationCollectionOrphanCheck = course.getCertificationCollection();
            for (Certification certificationCollectionOrphanCheckCertification : certificationCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Course (" + course + ") cannot be destroyed since the Certification " + certificationCollectionOrphanCheckCertification + " in its certificationCollection field has a non-nullable courseId field.");
            }
            Collection<StudentCourse> studentCourseCollectionOrphanCheck = course.getStudentCourseCollection();
            for (StudentCourse studentCourseCollectionOrphanCheckStudentCourse : studentCourseCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Course (" + course + ") cannot be destroyed since the StudentCourse " + studentCourseCollectionOrphanCheckStudentCourse + " in its studentCourseCollection field has a non-nullable courseId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Tag tagId = course.getTagId();
            if (tagId != null) {
                tagId.getCourseCollection().remove(course);
                tagId = em.merge(tagId);
            }
            em.remove(course);
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
    public List<Course> findCourseEntities() {
        return findCourseEntities(true, -1, -1);
    }

    @Override
    public List<Course> findCourseEntities(int maxResults, int firstResult) {
        return findCourseEntities(false, maxResults, firstResult);
    }

    private List<Course> findCourseEntities(boolean all, int maxResults, int firstResult) {
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Course.class));
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
    public Course findCourse(String id) {
        try {
            return em.find(Course.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public int getCourseCount() {
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Course> rt = cq.from(Course.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
