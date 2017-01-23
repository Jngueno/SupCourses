/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supcourses.dao_jpa;

import com.supcourses.dao.StudentDao;
import com.supcourses.dao_jpa.exceptions.IllegalOrphanException;
import com.supcourses.dao_jpa.exceptions.NonexistentEntityException;
import com.supcourses.dao_jpa.exceptions.PreexistingEntityException;
import com.supcourses.dao_jpa.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.supcourses.entity.Score;
import com.supcourses.entity.Student;
import java.util.ArrayList;
import java.util.Collection;
import com.supcourses.entity.StudentCertification;
import com.supcourses.entity.StudentResponse;
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
public class StudentJpaController implements StudentDao {

    @PersistenceContext(unitName = "SupCourses-PU")
    private EntityManager em;
    
    //@Inject
    //private UserTransaction utx;


    @Override
    public Student create(Student student) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (student.getScoreCollection() == null) {
            student.setScoreCollection(new ArrayList<Score>());
        }
        if (student.getStudentCertificationCollection() == null) {
            student.setStudentCertificationCollection(new ArrayList<StudentCertification>());
        }
        if (student.getStudentResponseCollection() == null) {
            student.setStudentResponseCollection(new ArrayList<StudentResponse>());
        }
        if (student.getStudentCourseCollection() == null) {
            student.setStudentCourseCollection(new ArrayList<StudentCourse>());
        }
        //EntityManager em = null;
        try {
            //utx.begin();
            //em = getEntityManager();
            Collection<Score> attachedScoreCollection = new ArrayList<Score>();
            for (Score scoreCollectionScoreToAttach : student.getScoreCollection()) {
                scoreCollectionScoreToAttach = em.getReference(scoreCollectionScoreToAttach.getClass(), scoreCollectionScoreToAttach.getScoreId());
                attachedScoreCollection.add(scoreCollectionScoreToAttach);
            }
            student.setScoreCollection(attachedScoreCollection);
            Collection<StudentCertification> attachedStudentCertificationCollection = new ArrayList<StudentCertification>();
            for (StudentCertification studentCertificationCollectionStudentCertificationToAttach : student.getStudentCertificationCollection()) {
                studentCertificationCollectionStudentCertificationToAttach = em.getReference(studentCertificationCollectionStudentCertificationToAttach.getClass(), studentCertificationCollectionStudentCertificationToAttach.getStudentCertificationId());
                attachedStudentCertificationCollection.add(studentCertificationCollectionStudentCertificationToAttach);
            }
            student.setStudentCertificationCollection(attachedStudentCertificationCollection);
            Collection<StudentResponse> attachedStudentResponseCollection = new ArrayList<StudentResponse>();
            for (StudentResponse studentResponseCollectionStudentResponseToAttach : student.getStudentResponseCollection()) {
                studentResponseCollectionStudentResponseToAttach = em.getReference(studentResponseCollectionStudentResponseToAttach.getClass(), studentResponseCollectionStudentResponseToAttach.getStudentResponseId());
                attachedStudentResponseCollection.add(studentResponseCollectionStudentResponseToAttach);
            }
            student.setStudentResponseCollection(attachedStudentResponseCollection);
            Collection<StudentCourse> attachedStudentCourseCollection = new ArrayList<StudentCourse>();
            for (StudentCourse studentCourseCollectionStudentCourseToAttach : student.getStudentCourseCollection()) {
                studentCourseCollectionStudentCourseToAttach = em.getReference(studentCourseCollectionStudentCourseToAttach.getClass(), studentCourseCollectionStudentCourseToAttach.getStudentCourseId());
                attachedStudentCourseCollection.add(studentCourseCollectionStudentCourseToAttach);
            }
            student.setStudentCourseCollection(attachedStudentCourseCollection);
            em.persist(student);
            for (Score scoreCollectionScore : student.getScoreCollection()) {
                Student oldStudentIdOfScoreCollectionScore = scoreCollectionScore.getStudentId();
                scoreCollectionScore.setStudentId(student);
                scoreCollectionScore = em.merge(scoreCollectionScore);
                if (oldStudentIdOfScoreCollectionScore != null) {
                    oldStudentIdOfScoreCollectionScore.getScoreCollection().remove(scoreCollectionScore);
                    oldStudentIdOfScoreCollectionScore = em.merge(oldStudentIdOfScoreCollectionScore);
                }
            }
            for (StudentCertification studentCertificationCollectionStudentCertification : student.getStudentCertificationCollection()) {
                Student oldStudentIdOfStudentCertificationCollectionStudentCertification = studentCertificationCollectionStudentCertification.getStudentId();
                studentCertificationCollectionStudentCertification.setStudentId(student);
                studentCertificationCollectionStudentCertification = em.merge(studentCertificationCollectionStudentCertification);
                if (oldStudentIdOfStudentCertificationCollectionStudentCertification != null) {
                    oldStudentIdOfStudentCertificationCollectionStudentCertification.getStudentCertificationCollection().remove(studentCertificationCollectionStudentCertification);
                    oldStudentIdOfStudentCertificationCollectionStudentCertification = em.merge(oldStudentIdOfStudentCertificationCollectionStudentCertification);
                }
            }
            for (StudentResponse studentResponseCollectionStudentResponse : student.getStudentResponseCollection()) {
                Student oldStudentIdOfStudentResponseCollectionStudentResponse = studentResponseCollectionStudentResponse.getStudentId();
                studentResponseCollectionStudentResponse.setStudentId(student);
                studentResponseCollectionStudentResponse = em.merge(studentResponseCollectionStudentResponse);
                if (oldStudentIdOfStudentResponseCollectionStudentResponse != null) {
                    oldStudentIdOfStudentResponseCollectionStudentResponse.getStudentResponseCollection().remove(studentResponseCollectionStudentResponse);
                    oldStudentIdOfStudentResponseCollectionStudentResponse = em.merge(oldStudentIdOfStudentResponseCollectionStudentResponse);
                }
            }
            for (StudentCourse studentCourseCollectionStudentCourse : student.getStudentCourseCollection()) {
                Student oldStudentIdOfStudentCourseCollectionStudentCourse = studentCourseCollectionStudentCourse.getStudentId();
                studentCourseCollectionStudentCourse.setStudentId(student);
                studentCourseCollectionStudentCourse = em.merge(studentCourseCollectionStudentCourse);
                if (oldStudentIdOfStudentCourseCollectionStudentCourse != null) {
                    oldStudentIdOfStudentCourseCollectionStudentCourse.getStudentCourseCollection().remove(studentCourseCollectionStudentCourse);
                    oldStudentIdOfStudentCourseCollectionStudentCourse = em.merge(oldStudentIdOfStudentCourseCollectionStudentCourse);
                }
            }
            return student;
            //utx.commit();
        } catch (Exception ex) {
            try {
                //utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findStudent(student.getStudentId()) != null) {
                throw new PreexistingEntityException("Student " + student + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public Student edit(Student student) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        //EntityManager em = null;
        try {
            //utx.begin();
            //em = getEntityManager();
            Student persistentStudent = em.find(Student.class, student.getStudentId());
            Collection<Score> scoreCollectionOld = persistentStudent.getScoreCollection();
            Collection<Score> scoreCollectionNew = student.getScoreCollection();
            Collection<StudentCertification> studentCertificationCollectionOld = persistentStudent.getStudentCertificationCollection();
            Collection<StudentCertification> studentCertificationCollectionNew = student.getStudentCertificationCollection();
            Collection<StudentResponse> studentResponseCollectionOld = persistentStudent.getStudentResponseCollection();
            Collection<StudentResponse> studentResponseCollectionNew = student.getStudentResponseCollection();
            Collection<StudentCourse> studentCourseCollectionOld = persistentStudent.getStudentCourseCollection();
            Collection<StudentCourse> studentCourseCollectionNew = student.getStudentCourseCollection();
            List<String> illegalOrphanMessages = null;
            for (Score scoreCollectionOldScore : scoreCollectionOld) {
                if (!scoreCollectionNew.contains(scoreCollectionOldScore)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Score " + scoreCollectionOldScore + " since its studentId field is not nullable.");
                }
            }
            for (StudentCertification studentCertificationCollectionOldStudentCertification : studentCertificationCollectionOld) {
                if (!studentCertificationCollectionNew.contains(studentCertificationCollectionOldStudentCertification)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain StudentCertification " + studentCertificationCollectionOldStudentCertification + " since its studentId field is not nullable.");
                }
            }
            for (StudentResponse studentResponseCollectionOldStudentResponse : studentResponseCollectionOld) {
                if (!studentResponseCollectionNew.contains(studentResponseCollectionOldStudentResponse)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain StudentResponse " + studentResponseCollectionOldStudentResponse + " since its studentId field is not nullable.");
                }
            }
            for (StudentCourse studentCourseCollectionOldStudentCourse : studentCourseCollectionOld) {
                if (!studentCourseCollectionNew.contains(studentCourseCollectionOldStudentCourse)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain StudentCourse " + studentCourseCollectionOldStudentCourse + " since its studentId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Score> attachedScoreCollectionNew = new ArrayList<Score>();
            for (Score scoreCollectionNewScoreToAttach : scoreCollectionNew) {
                scoreCollectionNewScoreToAttach = em.getReference(scoreCollectionNewScoreToAttach.getClass(), scoreCollectionNewScoreToAttach.getScoreId());
                attachedScoreCollectionNew.add(scoreCollectionNewScoreToAttach);
            }
            scoreCollectionNew = attachedScoreCollectionNew;
            student.setScoreCollection(scoreCollectionNew);
            Collection<StudentCertification> attachedStudentCertificationCollectionNew = new ArrayList<StudentCertification>();
            for (StudentCertification studentCertificationCollectionNewStudentCertificationToAttach : studentCertificationCollectionNew) {
                studentCertificationCollectionNewStudentCertificationToAttach = em.getReference(studentCertificationCollectionNewStudentCertificationToAttach.getClass(), studentCertificationCollectionNewStudentCertificationToAttach.getStudentCertificationId());
                attachedStudentCertificationCollectionNew.add(studentCertificationCollectionNewStudentCertificationToAttach);
            }
            studentCertificationCollectionNew = attachedStudentCertificationCollectionNew;
            student.setStudentCertificationCollection(studentCertificationCollectionNew);
            Collection<StudentResponse> attachedStudentResponseCollectionNew = new ArrayList<StudentResponse>();
            for (StudentResponse studentResponseCollectionNewStudentResponseToAttach : studentResponseCollectionNew) {
                studentResponseCollectionNewStudentResponseToAttach = em.getReference(studentResponseCollectionNewStudentResponseToAttach.getClass(), studentResponseCollectionNewStudentResponseToAttach.getStudentResponseId());
                attachedStudentResponseCollectionNew.add(studentResponseCollectionNewStudentResponseToAttach);
            }
            studentResponseCollectionNew = attachedStudentResponseCollectionNew;
            student.setStudentResponseCollection(studentResponseCollectionNew);
            Collection<StudentCourse> attachedStudentCourseCollectionNew = new ArrayList<StudentCourse>();
            for (StudentCourse studentCourseCollectionNewStudentCourseToAttach : studentCourseCollectionNew) {
                studentCourseCollectionNewStudentCourseToAttach = em.getReference(studentCourseCollectionNewStudentCourseToAttach.getClass(), studentCourseCollectionNewStudentCourseToAttach.getStudentCourseId());
                attachedStudentCourseCollectionNew.add(studentCourseCollectionNewStudentCourseToAttach);
            }
            studentCourseCollectionNew = attachedStudentCourseCollectionNew;
            student.setStudentCourseCollection(studentCourseCollectionNew);
            student = em.merge(student);
            for (Score scoreCollectionNewScore : scoreCollectionNew) {
                if (!scoreCollectionOld.contains(scoreCollectionNewScore)) {
                    Student oldStudentIdOfScoreCollectionNewScore = scoreCollectionNewScore.getStudentId();
                    scoreCollectionNewScore.setStudentId(student);
                    scoreCollectionNewScore = em.merge(scoreCollectionNewScore);
                    if (oldStudentIdOfScoreCollectionNewScore != null && !oldStudentIdOfScoreCollectionNewScore.equals(student)) {
                        oldStudentIdOfScoreCollectionNewScore.getScoreCollection().remove(scoreCollectionNewScore);
                        oldStudentIdOfScoreCollectionNewScore = em.merge(oldStudentIdOfScoreCollectionNewScore);
                    }
                }
            }
            for (StudentCertification studentCertificationCollectionNewStudentCertification : studentCertificationCollectionNew) {
                if (!studentCertificationCollectionOld.contains(studentCertificationCollectionNewStudentCertification)) {
                    Student oldStudentIdOfStudentCertificationCollectionNewStudentCertification = studentCertificationCollectionNewStudentCertification.getStudentId();
                    studentCertificationCollectionNewStudentCertification.setStudentId(student);
                    studentCertificationCollectionNewStudentCertification = em.merge(studentCertificationCollectionNewStudentCertification);
                    if (oldStudentIdOfStudentCertificationCollectionNewStudentCertification != null && !oldStudentIdOfStudentCertificationCollectionNewStudentCertification.equals(student)) {
                        oldStudentIdOfStudentCertificationCollectionNewStudentCertification.getStudentCertificationCollection().remove(studentCertificationCollectionNewStudentCertification);
                        oldStudentIdOfStudentCertificationCollectionNewStudentCertification = em.merge(oldStudentIdOfStudentCertificationCollectionNewStudentCertification);
                    }
                }
            }
            for (StudentResponse studentResponseCollectionNewStudentResponse : studentResponseCollectionNew) {
                if (!studentResponseCollectionOld.contains(studentResponseCollectionNewStudentResponse)) {
                    Student oldStudentIdOfStudentResponseCollectionNewStudentResponse = studentResponseCollectionNewStudentResponse.getStudentId();
                    studentResponseCollectionNewStudentResponse.setStudentId(student);
                    studentResponseCollectionNewStudentResponse = em.merge(studentResponseCollectionNewStudentResponse);
                    if (oldStudentIdOfStudentResponseCollectionNewStudentResponse != null && !oldStudentIdOfStudentResponseCollectionNewStudentResponse.equals(student)) {
                        oldStudentIdOfStudentResponseCollectionNewStudentResponse.getStudentResponseCollection().remove(studentResponseCollectionNewStudentResponse);
                        oldStudentIdOfStudentResponseCollectionNewStudentResponse = em.merge(oldStudentIdOfStudentResponseCollectionNewStudentResponse);
                    }
                }
            }
            for (StudentCourse studentCourseCollectionNewStudentCourse : studentCourseCollectionNew) {
                if (!studentCourseCollectionOld.contains(studentCourseCollectionNewStudentCourse)) {
                    Student oldStudentIdOfStudentCourseCollectionNewStudentCourse = studentCourseCollectionNewStudentCourse.getStudentId();
                    studentCourseCollectionNewStudentCourse.setStudentId(student);
                    studentCourseCollectionNewStudentCourse = em.merge(studentCourseCollectionNewStudentCourse);
                    if (oldStudentIdOfStudentCourseCollectionNewStudentCourse != null && !oldStudentIdOfStudentCourseCollectionNewStudentCourse.equals(student)) {
                        oldStudentIdOfStudentCourseCollectionNewStudentCourse.getStudentCourseCollection().remove(studentCourseCollectionNewStudentCourse);
                        oldStudentIdOfStudentCourseCollectionNewStudentCourse = em.merge(oldStudentIdOfStudentCourseCollectionNewStudentCourse);
                    }
                }
            }
            return student;
            //utx.commit();
        } catch (Exception ex) {
            try {
                //utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = student.getStudentId();
                if (findStudent(id) == null) {
                    throw new NonexistentEntityException("The student with id " + id + " no longer exists.");
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
    public Student destroy(String id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        //EntityManager em = null;
        try {
            //utx.begin();
            //em = getEntityManager();
            Student student;
            try {
                student = em.getReference(Student.class, id);
                student.getStudentId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The student with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Score> scoreCollectionOrphanCheck = student.getScoreCollection();
            for (Score scoreCollectionOrphanCheckScore : scoreCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Student (" + student + ") cannot be destroyed since the Score " + scoreCollectionOrphanCheckScore + " in its scoreCollection field has a non-nullable studentId field.");
            }
            Collection<StudentCertification> studentCertificationCollectionOrphanCheck = student.getStudentCertificationCollection();
            for (StudentCertification studentCertificationCollectionOrphanCheckStudentCertification : studentCertificationCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Student (" + student + ") cannot be destroyed since the StudentCertification " + studentCertificationCollectionOrphanCheckStudentCertification + " in its studentCertificationCollection field has a non-nullable studentId field.");
            }
            Collection<StudentResponse> studentResponseCollectionOrphanCheck = student.getStudentResponseCollection();
            for (StudentResponse studentResponseCollectionOrphanCheckStudentResponse : studentResponseCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Student (" + student + ") cannot be destroyed since the StudentResponse " + studentResponseCollectionOrphanCheckStudentResponse + " in its studentResponseCollection field has a non-nullable studentId field.");
            }
            Collection<StudentCourse> studentCourseCollectionOrphanCheck = student.getStudentCourseCollection();
            for (StudentCourse studentCourseCollectionOrphanCheckStudentCourse : studentCourseCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Student (" + student + ") cannot be destroyed since the StudentCourse " + studentCourseCollectionOrphanCheckStudentCourse + " in its studentCourseCollection field has a non-nullable studentId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(student);
            return student;
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
    public List<Student> findStudentEntities() {
        return findStudentEntities(true, -1, -1);
    }

    @Override
    public List<Student> findStudentEntities(int maxResults, int firstResult) {
        return findStudentEntities(false, maxResults, firstResult);
    }

    private List<Student> findStudentEntities(boolean all, int maxResults, int firstResult) {
        //EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Student.class));
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
    public Student findStudent(String id) {
        //EntityManager em = getEntityManager();
        try {
            return em.find(Student.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public int getStudentCount() {
        //EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Student> rt = cq.from(Student.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    @Override
    public Student findStudentByEmailAndPassword(String eMail, String password) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root<Student> rt = cq.from(Student.class);
            cq.select(rt).where(cb.equal(rt.get("e-mail"), eMail), cb.equal(rt.get("password"), password));
            Query q = em.createQuery(cq);
            Student student = (Student) q.getResultList();
            return student;
        } finally {
            em.close();
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
