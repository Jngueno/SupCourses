/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supcourses.dao_jpa;

import com.supcourses.dao.StudentCourseDao;
import com.supcourses.dao_jpa.exceptions.NonexistentEntityException;
import com.supcourses.dao_jpa.exceptions.PreexistingEntityException;
import com.supcourses.dao_jpa.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.supcourses.entity.Student;
import com.supcourses.entity.Course;
import com.supcourses.entity.StudentCourse;
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
public class StudentCourseJpaController implements StudentCourseDao {

    @PersistenceContext(unitName = "SupCourses-PU")
    private EntityManager em;
    
//    @Inject
//    private UserTransaction utx;


    @Override
    public StudentCourse create(StudentCourse studentCourse) throws PreexistingEntityException, RollbackFailureException, Exception {
        //EntityManager em = null;
        try {
            //utx.begin();
            //em = getEntityManager();
            Student studentId = studentCourse.getStudentId();
            if (studentId != null) {
                studentId = em.getReference(studentId.getClass(), studentId.getStudentId());
                studentCourse.setStudentId(studentId);
            }
            Course courseId = studentCourse.getCourseId();
            if (courseId != null) {
                courseId = em.getReference(courseId.getClass(), courseId.getCourseId());
                studentCourse.setCourseId(courseId);
            }
            em.persist(studentCourse);
            if (studentId != null) {
                studentId.getStudentCourseCollection().add(studentCourse);
                studentId = em.merge(studentId);
            }
            if (courseId != null) {
                courseId.getStudentCourseCollection().add(studentCourse);
                courseId = em.merge(courseId);
            }
            return studentCourse;
            //utx.commit();
        } catch (Exception ex) {
            try {
                //utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findStudentCourse(studentCourse.getStudentCourseId()) != null) {
                throw new PreexistingEntityException("StudentCourse " + studentCourse + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public StudentCourse edit(StudentCourse studentCourse) throws NonexistentEntityException, RollbackFailureException, Exception {
        //EntityManager em = null;
        try {
            //utx.begin();
            //em = getEntityManager();
            StudentCourse persistentStudentCourse = em.find(StudentCourse.class, studentCourse.getStudentCourseId());
            Student studentIdOld = persistentStudentCourse.getStudentId();
            Student studentIdNew = studentCourse.getStudentId();
            Course courseIdOld = persistentStudentCourse.getCourseId();
            Course courseIdNew = studentCourse.getCourseId();
            if (studentIdNew != null) {
                studentIdNew = em.getReference(studentIdNew.getClass(), studentIdNew.getStudentId());
                studentCourse.setStudentId(studentIdNew);
            }
            if (courseIdNew != null) {
                courseIdNew = em.getReference(courseIdNew.getClass(), courseIdNew.getCourseId());
                studentCourse.setCourseId(courseIdNew);
            }
            studentCourse = em.merge(studentCourse);
            if (studentIdOld != null && !studentIdOld.equals(studentIdNew)) {
                studentIdOld.getStudentCourseCollection().remove(studentCourse);
                studentIdOld = em.merge(studentIdOld);
            }
            if (studentIdNew != null && !studentIdNew.equals(studentIdOld)) {
                studentIdNew.getStudentCourseCollection().add(studentCourse);
                studentIdNew = em.merge(studentIdNew);
            }
            if (courseIdOld != null && !courseIdOld.equals(courseIdNew)) {
                courseIdOld.getStudentCourseCollection().remove(studentCourse);
                courseIdOld = em.merge(courseIdOld);
            }
            if (courseIdNew != null && !courseIdNew.equals(courseIdOld)) {
                courseIdNew.getStudentCourseCollection().add(studentCourse);
                courseIdNew = em.merge(courseIdNew);
            }
            return studentCourse;
            //utx.commit();
        } catch (Exception ex) {
            try {
                //utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = studentCourse.getStudentCourseId();
                if (findStudentCourse(id) == null) {
                    throw new NonexistentEntityException("The studentCourse with id " + id + " no longer exists.");
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
    public StudentCourse destroy(String id) throws NonexistentEntityException, RollbackFailureException, Exception {
        //EntityManager em = null;
        try {
            //utx.begin();
            //em = getEntityManager();
            StudentCourse studentCourse;
            try {
                studentCourse = em.getReference(StudentCourse.class, id);
                studentCourse.getStudentCourseId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The studentCourse with id " + id + " no longer exists.", enfe);
            }
            Student studentId = studentCourse.getStudentId();
            if (studentId != null) {
                studentId.getStudentCourseCollection().remove(studentCourse);
                studentId = em.merge(studentId);
            }
            Course courseId = studentCourse.getCourseId();
            if (courseId != null) {
                courseId.getStudentCourseCollection().remove(studentCourse);
                courseId = em.merge(courseId);
            }
            em.remove(studentCourse);
            return studentCourse;
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
    public List<StudentCourse> findStudentCourseEntities() {
        return findStudentCourseEntities(true, -1, -1);
    }

    @Override
    public List<StudentCourse> findStudentCourseEntities(int maxResults, int firstResult) {
        return findStudentCourseEntities(false, maxResults, firstResult);
    }

    private List<StudentCourse> findStudentCourseEntities(boolean all, int maxResults, int firstResult) {
        //EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(StudentCourse.class));
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
    public StudentCourse findStudentCourse(String id) {
        //EntityManager em = getEntityManager();
        try {
            return em.find(StudentCourse.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public int getStudentCourseCount() {
        //EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<StudentCourse> rt = cq.from(StudentCourse.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    @Override
    public List<StudentCourse> findStudentCoursesByCourse(Course course) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root<StudentCourse> rt = cq.from(StudentCourse.class);
            cq.select(rt).where(cb.equal(rt.get("course_id"), course.getCourseId()));
            Query q = em.createQuery(cq);
            List<StudentCourse> studentCourses = (List<StudentCourse>) q.getResultList();
            return studentCourses;
        } finally {
            em.close();
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<StudentCourse> findStudentCoursesByStudent(Student student) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root<StudentCourse> rt = cq.from(StudentCourse.class);
            cq.select(rt).where(cb.equal(rt.get("student_id"), student.getStudentId()));
            Query q = em.createQuery(cq);
            List<StudentCourse> studentCourses = (List<StudentCourse>) q.getResultList();
            return studentCourses;
        } finally {
            em.close();
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public StudentCourse findStudentCourseByStudentAndCourse(Student student, Course course) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root<StudentCourse> rt = cq.from(StudentCourse.class);
            cq.select(rt).where(cb.equal(rt.get("student_id"), student.getStudentId()), cb.equal(rt.get("course_id"), course.getCourseId()));
            Query q = em.createQuery(cq);
            StudentCourse studentCourse = (StudentCourse) q.getSingleResult();
        return studentCourse;
        } finally {
            em.close();
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
