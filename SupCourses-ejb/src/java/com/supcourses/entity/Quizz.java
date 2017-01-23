/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supcourses.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jngue
 */
@Entity
@Table(name = "quizz")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Quizz.findAll", query = "SELECT q FROM Quizz q"),
    @NamedQuery(name = "Quizz.findByQuizzId", query = "SELECT q FROM Quizz q WHERE q.quizzId = :quizzId"),
    @NamedQuery(name = "Quizz.findByCreatedDate", query = "SELECT q FROM Quizz q WHERE q.createdDate = :createdDate")})
public class Quizz implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 36)
    @Column(name = "quizz_id")
    private String quizzId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "quizzId")
    private Collection<Question> questionCollection;
    @JoinColumn(name = "module_id", referencedColumnName = "module_id")
    @ManyToOne(optional = false)
    private Module moduleId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "quizzId")
    private Collection<Score> scoreCollection;

    public Quizz() {
    }

    public Quizz(String quizzId) {
        this.quizzId = quizzId;
    }

    public Quizz(String quizzId, Date createdDate) {
        this.quizzId = quizzId;
        this.createdDate = createdDate;
    }

    public String getQuizzId() {
        return quizzId;
    }

    public void setQuizzId(String quizzId) {
        this.quizzId = quizzId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @XmlTransient
    public Collection<Question> getQuestionCollection() {
        return questionCollection;
    }

    public void setQuestionCollection(Collection<Question> questionCollection) {
        this.questionCollection = questionCollection;
    }

    public Module getModuleId() {
        return moduleId;
    }

    public void setModuleId(Module moduleId) {
        this.moduleId = moduleId;
    }

    @XmlTransient
    public Collection<Score> getScoreCollection() {
        return scoreCollection;
    }

    public void setScoreCollection(Collection<Score> scoreCollection) {
        this.scoreCollection = scoreCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (quizzId != null ? quizzId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Quizz)) {
            return false;
        }
        Quizz other = (Quizz) object;
        if ((this.quizzId == null && other.quizzId != null) || (this.quizzId != null && !this.quizzId.equals(other.quizzId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.supcourses.entity.Quizz[ quizzId=" + quizzId + " ]";
    }
    
}
