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
@Table(name = "student_certification")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "StudentCertification.findAll", query = "SELECT s FROM StudentCertification s"),
    @NamedQuery(name = "StudentCertification.findByStudentCertificationId", query = "SELECT s FROM StudentCertification s WHERE s.studentCertificationId = :studentCertificationId"),
    @NamedQuery(name = "StudentCertification.findByDateDelivered", query = "SELECT s FROM StudentCertification s WHERE s.dateDelivered = :dateDelivered")})
public class StudentCertification implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 36)
    @Column(name = "student_certification_id")
    private String studentCertificationId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date_delivered")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateDelivered;
    @JoinColumn(name = "certification_id", referencedColumnName = "certification_id")
    @ManyToOne(optional = false)
    private Certification certificationId;
    @JoinColumn(name = "student_id", referencedColumnName = "student_id")
    @ManyToOne(optional = false)
    private Student studentId;

    public StudentCertification() {
    }

    public StudentCertification(String studentCertificationId) {
        this.studentCertificationId = studentCertificationId;
    }

    public StudentCertification(String studentCertificationId, Date dateDelivered) {
        this.studentCertificationId = studentCertificationId;
        this.dateDelivered = dateDelivered;
    }

    public String getStudentCertificationId() {
        return studentCertificationId;
    }

    public void setStudentCertificationId(String studentCertificationId) {
        this.studentCertificationId = studentCertificationId;
    }

    public Date getDateDelivered() {
        return dateDelivered;
    }

    public void setDateDelivered(Date dateDelivered) {
        this.dateDelivered = dateDelivered;
    }

    public Certification getCertificationId() {
        return certificationId;
    }

    public void setCertificationId(Certification certificationId) {
        this.certificationId = certificationId;
    }

    public Student getStudentId() {
        return studentId;
    }

    public void setStudentId(Student studentId) {
        this.studentId = studentId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (studentCertificationId != null ? studentCertificationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StudentCertification)) {
            return false;
        }
        StudentCertification other = (StudentCertification) object;
        if ((this.studentCertificationId == null && other.studentCertificationId != null) || (this.studentCertificationId != null && !this.studentCertificationId.equals(other.studentCertificationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.supcourses.entity.StudentCertification[ studentCertificationId=" + studentCertificationId + " ]";
    }
    
}
