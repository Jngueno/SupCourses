/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supcourses.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jngue
 */
@Entity
@Table(name = "certification")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Certification.findAll", query = "SELECT c FROM Certification c"),
    @NamedQuery(name = "Certification.findByCertificationId", query = "SELECT c FROM Certification c WHERE c.certificationId = :certificationId"),
    @NamedQuery(name = "Certification.findByScore", query = "SELECT c FROM Certification c WHERE c.score = :score")})
public class Certification implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 36)
    @Column(name = "certification_id")
    private String certificationId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "score")
    private int score;
    @JoinColumn(name = "course_id", referencedColumnName = "course_id")
    @ManyToOne(optional = false)
    private Course courseId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "certificationId")
    private Collection<StudentCertification> studentCertificationCollection;

    public Certification() {
    }

    public Certification(String certificationId) {
        this.certificationId = certificationId;
    }

    public Certification(String certificationId, int score) {
        this.certificationId = certificationId;
        this.score = score;
    }

    public String getCertificationId() {
        return certificationId;
    }

    public void setCertificationId(String certificationId) {
        this.certificationId = certificationId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Course getCourseId() {
        return courseId;
    }

    public void setCourseId(Course courseId) {
        this.courseId = courseId;
    }

    @XmlTransient
    public Collection<StudentCertification> getStudentCertificationCollection() {
        return studentCertificationCollection;
    }

    public void setStudentCertificationCollection(Collection<StudentCertification> studentCertificationCollection) {
        this.studentCertificationCollection = studentCertificationCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (certificationId != null ? certificationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Certification)) {
            return false;
        }
        Certification other = (Certification) object;
        if ((this.certificationId == null && other.certificationId != null) || (this.certificationId != null && !this.certificationId.equals(other.certificationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.supcourses.entity.Certification[ certificationId=" + certificationId + " ]";
    }
    
}
