/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supcourses.dao_jpa;

import com.supcourses.dao.StudentCertificationDao;
import com.supcourses.dao_jpa.exceptions.NonexistentEntityException;
import com.supcourses.dao_jpa.exceptions.PreexistingEntityException;
import com.supcourses.dao_jpa.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.supcourses.entity.Certification;
import com.supcourses.entity.Student;
import com.supcourses.entity.StudentCertification;
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
public class StudentCertificationJpaController implements StudentCertificationDao {

    @PersistenceContext(unitName = "SupCourses-PU")
    private EntityManager em;
    
    //@Inject
    //private UserTransaction utx;

    
    @Override
    public StudentCertification create(StudentCertification studentCertification) throws PreexistingEntityException, RollbackFailureException, Exception {
        //EntityManager em = null;
        try {
            //utx.begin();
            //em = getEntityManager();
            Certification certificationId = studentCertification.getCertificationId();
            if (certificationId != null) {
                certificationId = em.getReference(certificationId.getClass(), certificationId.getCertificationId());
                studentCertification.setCertificationId(certificationId);
            }
            Student studentId = studentCertification.getStudentId();
            if (studentId != null) {
                studentId = em.getReference(studentId.getClass(), studentId.getStudentId());
                studentCertification.setStudentId(studentId);
            }
            em.persist(studentCertification);
            if (certificationId != null) {
                certificationId.getStudentCertificationCollection().add(studentCertification);
                certificationId = em.merge(certificationId);
            }
            if (studentId != null) {
                studentId.getStudentCertificationCollection().add(studentCertification);
                studentId = em.merge(studentId);
            }
            //utx.commit();
            return studentCertification;
        } catch (Exception ex) {
            try {
                //utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findStudentCertification(studentCertification.getStudentCertificationId()) != null) {
                throw new PreexistingEntityException("StudentCertification " + studentCertification + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public StudentCertification edit(StudentCertification studentCertification) throws NonexistentEntityException, RollbackFailureException, Exception {
        //EntityManager em = null;
        try {
            //utx.begin();
            //em = getEntityManager();
            StudentCertification persistentStudentCertification = em.find(StudentCertification.class, studentCertification.getStudentCertificationId());
            Certification certificationIdOld = persistentStudentCertification.getCertificationId();
            Certification certificationIdNew = studentCertification.getCertificationId();
            Student studentIdOld = persistentStudentCertification.getStudentId();
            Student studentIdNew = studentCertification.getStudentId();
            if (certificationIdNew != null) {
                certificationIdNew = em.getReference(certificationIdNew.getClass(), certificationIdNew.getCertificationId());
                studentCertification.setCertificationId(certificationIdNew);
            }
            if (studentIdNew != null) {
                studentIdNew = em.getReference(studentIdNew.getClass(), studentIdNew.getStudentId());
                studentCertification.setStudentId(studentIdNew);
            }
            studentCertification = em.merge(studentCertification);
            if (certificationIdOld != null && !certificationIdOld.equals(certificationIdNew)) {
                certificationIdOld.getStudentCertificationCollection().remove(studentCertification);
                certificationIdOld = em.merge(certificationIdOld);
            }
            if (certificationIdNew != null && !certificationIdNew.equals(certificationIdOld)) {
                certificationIdNew.getStudentCertificationCollection().add(studentCertification);
                certificationIdNew = em.merge(certificationIdNew);
            }
            if (studentIdOld != null && !studentIdOld.equals(studentIdNew)) {
                studentIdOld.getStudentCertificationCollection().remove(studentCertification);
                studentIdOld = em.merge(studentIdOld);
            }
            if (studentIdNew != null && !studentIdNew.equals(studentIdOld)) {
                studentIdNew.getStudentCertificationCollection().add(studentCertification);
                studentIdNew = em.merge(studentIdNew);
            }
            //utx.commit();
            return studentCertification;
        } catch (Exception ex) {
            try {
                //utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = studentCertification.getStudentCertificationId();
                if (findStudentCertification(id) == null) {
                    throw new NonexistentEntityException("The studentCertification with id " + id + " no longer exists.");
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
    public StudentCertification destroy(String id) throws NonexistentEntityException, RollbackFailureException, Exception {
        //EntityManager em = null;
        try {
            //utx.begin();
            //em = getEntityManager();
            StudentCertification studentCertification;
            try {
                studentCertification = em.getReference(StudentCertification.class, id);
                studentCertification.getStudentCertificationId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The studentCertification with id " + id + " no longer exists.", enfe);
            }
            Certification certificationId = studentCertification.getCertificationId();
            if (certificationId != null) {
                certificationId.getStudentCertificationCollection().remove(studentCertification);
                certificationId = em.merge(certificationId);
            }
            Student studentId = studentCertification.getStudentId();
            if (studentId != null) {
                studentId.getStudentCertificationCollection().remove(studentCertification);
                studentId = em.merge(studentId);
            }
            em.remove(studentCertification);
            //utx.commit();
            return studentCertification;
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
    public List<StudentCertification> findStudentCertificationEntities() {
        return findStudentCertificationEntities(true, -1, -1);
    }

    @Override
    public List<StudentCertification> findStudentCertificationEntities(int maxResults, int firstResult) {
        return findStudentCertificationEntities(false, maxResults, firstResult);
    }

    private List<StudentCertification> findStudentCertificationEntities(boolean all, int maxResults, int firstResult) {
        //EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(StudentCertification.class));
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
    public StudentCertification findStudentCertification(String id) {
        //EntityManager em = getEntityManager();
        try {
            return em.find(StudentCertification.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public int getStudentCertificationCount() {
        //EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<StudentCertification> rt = cq.from(StudentCertification.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    @Override
    public List<StudentCertification> findStudentCertificationsByStudent(Student student) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root<StudentCertification> rt = cq.from(StudentCertification.class);
            cq.select(rt).where(cb.equal(rt.get("student_id"), student.getStudentId()));
            Query q = em.createQuery(cq);
            List<StudentCertification> studentCertifications = (List<StudentCertification>) q.getResultList();
            return studentCertifications;
        } finally {
            em.close();
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<StudentCertification> findStudentCertificationsByCertification(Certification certification) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root<StudentCertification> rt = cq.from(StudentCertification.class);
            cq.select(rt).where(cb.equal(rt.get("certification_id"), certification.getCertificationId()));
            Query q = em.createQuery(cq);
            List<StudentCertification> studentCertifications = (List<StudentCertification>) q.getResultList();
            return studentCertifications;
        } finally {
            em.close();
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public StudentCertification findStudentCertificationByStudentAndCertification(Student student, Certification certification) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root<StudentCertification> rt = cq.from(StudentCertification.class);
            cq.select(rt).where(cb.equal(rt.get("student_id"), student.getStudentId()), cb.equal(rt.get("certification_id"), certification.getCertificationId()));
            Query q = em.createQuery(cq);
            StudentCertification studentCertification = (StudentCertification) q.getSingleResult();
            return studentCertification;
        } finally {
            em.close();
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
