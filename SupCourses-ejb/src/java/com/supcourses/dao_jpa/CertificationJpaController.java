/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supcourses.dao_jpa;

import com.supcourses.dao.CertificationDao;
import com.supcourses.dao_jpa.exceptions.IllegalOrphanException;
import com.supcourses.dao_jpa.exceptions.NonexistentEntityException;
import com.supcourses.dao_jpa.exceptions.PreexistingEntityException;
import com.supcourses.dao_jpa.exceptions.RollbackFailureException;
import com.supcourses.entity.Certification;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.supcourses.entity.Course;
import com.supcourses.entity.StudentCertification;
import java.util.ArrayList;
import java.util.Collection;
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
public class CertificationJpaController implements CertificationDao {

    @PersistenceContext(unitName = "SupCourses-PU")
    private EntityManager em;
    
    //@Inject
    //private UserTransaction utx;
    
    @Override
    public Certification create(Certification certification) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (certification.getStudentCertificationCollection() == null) {
            certification.setStudentCertificationCollection(new ArrayList<StudentCertification>());
        }
        try {
            //utx.begin();
            Course courseId = certification.getCourseId();
            if (courseId != null) {
                courseId = em.getReference(courseId.getClass(), courseId.getCourseId());
                certification.setCourseId(courseId);
            }
            Collection<StudentCertification> attachedStudentCertificationCollection = new ArrayList<StudentCertification>();
            for (StudentCertification studentCertificationCollectionStudentCertificationToAttach : certification.getStudentCertificationCollection()) {
                studentCertificationCollectionStudentCertificationToAttach = em.getReference(studentCertificationCollectionStudentCertificationToAttach.getClass(), studentCertificationCollectionStudentCertificationToAttach.getStudentCertificationId());
                attachedStudentCertificationCollection.add(studentCertificationCollectionStudentCertificationToAttach);
            }
            certification.setStudentCertificationCollection(attachedStudentCertificationCollection);
            em.persist(certification);
            if (courseId != null) {
                courseId.getCertificationCollection().add(certification);
                courseId = em.merge(courseId);
            }
            for (StudentCertification studentCertificationCollectionStudentCertification : certification.getStudentCertificationCollection()) {
                Certification oldCertificationIdOfStudentCertificationCollectionStudentCertification = studentCertificationCollectionStudentCertification.getCertificationId();
                studentCertificationCollectionStudentCertification.setCertificationId(certification);
                studentCertificationCollectionStudentCertification = em.merge(studentCertificationCollectionStudentCertification);
                if (oldCertificationIdOfStudentCertificationCollectionStudentCertification != null) {
                    oldCertificationIdOfStudentCertificationCollectionStudentCertification.getStudentCertificationCollection().remove(studentCertificationCollectionStudentCertification);
                    oldCertificationIdOfStudentCertificationCollectionStudentCertification = em.merge(oldCertificationIdOfStudentCertificationCollectionStudentCertification);
                }
            }
            //utx.commit();
            return certification;
        } catch (Exception ex) {
            try {
                //utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findCertification(certification.getCertificationId()) != null) {
                throw new PreexistingEntityException("Certification " + certification + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
//                em.close();
            }
        }
    }

    @Override
    public Certification edit(Certification certification) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        try {
            //utx.begin();
            Certification persistentCertification = em.find(Certification.class, certification.getCertificationId());
            Course courseIdOld = persistentCertification.getCourseId();
            Course courseIdNew = certification.getCourseId();
            Collection<StudentCertification> studentCertificationCollectionOld = persistentCertification.getStudentCertificationCollection();
            Collection<StudentCertification> studentCertificationCollectionNew = certification.getStudentCertificationCollection();
            List<String> illegalOrphanMessages = null;
            for (StudentCertification studentCertificationCollectionOldStudentCertification : studentCertificationCollectionOld) {
                if (!studentCertificationCollectionNew.contains(studentCertificationCollectionOldStudentCertification)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain StudentCertification " + studentCertificationCollectionOldStudentCertification + " since its certificationId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (courseIdNew != null) {
                courseIdNew = em.getReference(courseIdNew.getClass(), courseIdNew.getCourseId());
                certification.setCourseId(courseIdNew);
            }
            Collection<StudentCertification> attachedStudentCertificationCollectionNew = new ArrayList<StudentCertification>();
            for (StudentCertification studentCertificationCollectionNewStudentCertificationToAttach : studentCertificationCollectionNew) {
                studentCertificationCollectionNewStudentCertificationToAttach = em.getReference(studentCertificationCollectionNewStudentCertificationToAttach.getClass(), studentCertificationCollectionNewStudentCertificationToAttach.getStudentCertificationId());
                attachedStudentCertificationCollectionNew.add(studentCertificationCollectionNewStudentCertificationToAttach);
            }
            studentCertificationCollectionNew = attachedStudentCertificationCollectionNew;
            certification.setStudentCertificationCollection(studentCertificationCollectionNew);
            certification = em.merge(certification);
            if (courseIdOld != null && !courseIdOld.equals(courseIdNew)) {
                courseIdOld.getCertificationCollection().remove(certification);
                courseIdOld = em.merge(courseIdOld);
            }
            if (courseIdNew != null && !courseIdNew.equals(courseIdOld)) {
                courseIdNew.getCertificationCollection().add(certification);
                courseIdNew = em.merge(courseIdNew);
            }
            for (StudentCertification studentCertificationCollectionNewStudentCertification : studentCertificationCollectionNew) {
                if (!studentCertificationCollectionOld.contains(studentCertificationCollectionNewStudentCertification)) {
                    Certification oldCertificationIdOfStudentCertificationCollectionNewStudentCertification = studentCertificationCollectionNewStudentCertification.getCertificationId();
                    studentCertificationCollectionNewStudentCertification.setCertificationId(certification);
                    studentCertificationCollectionNewStudentCertification = em.merge(studentCertificationCollectionNewStudentCertification);
                    if (oldCertificationIdOfStudentCertificationCollectionNewStudentCertification != null && !oldCertificationIdOfStudentCertificationCollectionNewStudentCertification.equals(certification)) {
                        oldCertificationIdOfStudentCertificationCollectionNewStudentCertification.getStudentCertificationCollection().remove(studentCertificationCollectionNewStudentCertification);
                        oldCertificationIdOfStudentCertificationCollectionNewStudentCertification = em.merge(oldCertificationIdOfStudentCertificationCollectionNewStudentCertification);
                    }
                }
            }
            //utx.commit();
            return certification;
        } catch (Exception ex) {
            try {
                //utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = certification.getCertificationId();
                if (findCertification(id) == null) {
                    throw new NonexistentEntityException("The certification with id " + id + " no longer exists.");
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
    public Certification destroy(String id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        try {
            //utx.begin();
            Certification certification;
            try {
                certification = em.getReference(Certification.class, id);
                certification.getCertificationId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The certification with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<StudentCertification> studentCertificationCollectionOrphanCheck = certification.getStudentCertificationCollection();
            for (StudentCertification studentCertificationCollectionOrphanCheckStudentCertification : studentCertificationCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Certification (" + certification + ") cannot be destroyed since the StudentCertification " + studentCertificationCollectionOrphanCheckStudentCertification + " in its studentCertificationCollection field has a non-nullable certificationId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Course courseId = certification.getCourseId();
            if (courseId != null) {
                courseId.getCertificationCollection().remove(certification);
                courseId = em.merge(courseId);
            }
            em.remove(certification);
            //utx.commit();
            return certification;
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
    public List<Certification> findCertificationEntities() {
        return findCertificationEntities(true, -1, -1);
    }

    @Override
    public List<Certification> findCertificationEntities(int maxResults, int firstResult) {
        return findCertificationEntities(false, maxResults, firstResult);
    }

    private List<Certification> findCertificationEntities(boolean all, int maxResults, int firstResult) {
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Certification.class));
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
    public Certification findCertification(String id) {
        try {
            return em.find(Certification.class, id);
        } finally {
//            em.close();
        }
    }

    @Override
    public int getCertificationCount() {
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Certification> rt = cq.from(Certification.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
//            em.close();
        }
    }
    
}
