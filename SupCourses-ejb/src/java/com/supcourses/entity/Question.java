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
import javax.persistence.Lob;
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
@Table(name = "question")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Question.findAll", query = "SELECT q FROM Question q"),
    @NamedQuery(name = "Question.findByQuestionId", query = "SELECT q FROM Question q WHERE q.questionId = :questionId")})
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 36)
    @Column(name = "question_id")
    private String questionId;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "questiion_content")
    private String questiionContent;
    @JoinColumn(name = "quizz_id", referencedColumnName = "quizz_id")
    @ManyToOne(optional = false)
    private Quizz quizzId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "questionId")
    private Collection<Response> responseCollection;

    public Question() {
    }

    public Question(String questionId) {
        this.questionId = questionId;
    }

    public Question(String questionId, String questiionContent) {
        this.questionId = questionId;
        this.questiionContent = questiionContent;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestiionContent() {
        return questiionContent;
    }

    public void setQuestiionContent(String questiionContent) {
        this.questiionContent = questiionContent;
    }

    public Quizz getQuizzId() {
        return quizzId;
    }

    public void setQuizzId(Quizz quizzId) {
        this.quizzId = quizzId;
    }

    @XmlTransient
    public Collection<Response> getResponseCollection() {
        return responseCollection;
    }

    public void setResponseCollection(Collection<Response> responseCollection) {
        this.responseCollection = responseCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (questionId != null ? questionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Question)) {
            return false;
        }
        Question other = (Question) object;
        if ((this.questionId == null && other.questionId != null) || (this.questionId != null && !this.questionId.equals(other.questionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.supcourses.entity.Question[ questionId=" + questionId + " ]";
    }
    
}
