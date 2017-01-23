/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supcourses.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jngue
 */
@Entity
@Table(name = "student_course")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "StudentCourse.findAll", query = "SELECT s FROM StudentCourse s"),
    @NamedQuery(name = "StudentCourse.findByStudentCourseId", query = "SELECT s FROM StudentCourse s WHERE s.studentCourseId = :studentCourseId"),
    @NamedQuery(name = "StudentCourse.findBySubscriptionDate", query = "SELECT s FROM StudentCourse s WHERE s.subscriptionDate = :subscriptionDate")})
public class StudentCourse implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 36)
    @Column(name = "student_course_id")
    private String studentCourseId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "subscription_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date subscriptionDate;
    @JoinColumn(name = "student_id", referencedColumnName = "student_id")
    @ManyToOne(optional = false)
    private Student studentId;
    @JoinColumn(name = "course_id", referencedColumnName = "course_id")
    @ManyToOne(optional = false)
    private Course courseId;

    public StudentCourse() {
    }

    public StudentCourse(String studentCourseId) {
        this.studentCourseId = studentCourseId;
    }

    public StudentCourse(String studentCourseId, Date subscriptionDate) {
        this.studentCourseId = studentCourseId;
        this.subscriptionDate = subscriptionDate;
    }

    public String getStudentCourseId() {
        return studentCourseId;
    }

    public void setStudentCourseId(String studentCourseId) {
        this.studentCourseId = studentCourseId;
    }

    public Date getSubscriptionDate() {
        return subscriptionDate;
    }

    public void setSubscriptionDate(Date subscriptionDate) {
        this.subscriptionDate = subscriptionDate;
    }

    public Student getStudentId() {
        return studentId;
    }

    public void setStudentId(Student studentId) {
        this.studentId = studentId;
    }

    public Course getCourseId() {
        return courseId;
    }

    public void setCourseId(Course courseId) {
        this.courseId = courseId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (studentCourseId != null ? studentCourseId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StudentCourse)) {
            return false;
        }
        StudentCourse other = (StudentCourse) object;
        if ((this.studentCourseId == null && other.studentCourseId != null) || (this.studentCourseId != null && !this.studentCourseId.equals(other.studentCourseId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.supcourses.entity.StudentCourse[ studentCourseId=" + studentCourseId + " ]";
    }
    
}
