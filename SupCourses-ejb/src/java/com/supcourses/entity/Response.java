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
@Table(name = "response")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Response.findAll", query = "SELECT r FROM Response r"),
    @NamedQuery(name = "Response.findByResponseId", query = "SELECT r FROM Response r WHERE r.responseId = :responseId"),
    @NamedQuery(name = "Response.findByCorrect", query = "SELECT r FROM Response r WHERE r.correct = :correct")})
public class Response implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 36)
    @Column(name = "response_id")
    private String responseId;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "response_content")
    private String responseContent;
    @Basic(optional = false)
    @NotNull
    @Column(name = "correct")
    private boolean correct;
    @JoinColumn(name = "response_type_id", referencedColumnName = "response_type_id")
    @ManyToOne(optional = false)
    private ResponseType responseTypeId;
    @JoinColumn(name = "question_id", referencedColumnName = "question_id")
    @ManyToOne(optional = false)
    private Question questionId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "responseId")
    private Collection<StudentResponse> studentResponseCollection;

    public Response() {
    }

    public Response(String responseId) {
        this.responseId = responseId;
    }

    public Response(String responseId, String responseContent, boolean correct) {
        this.responseId = responseId;
        this.responseContent = responseContent;
        this.correct = correct;
    }

    public String getResponseId() {
        return responseId;
    }

    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }

    public String getResponseContent() {
        return responseContent;
    }

    public void setResponseContent(String responseContent) {
        this.responseContent = responseContent;
    }

    public boolean getCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public ResponseType getResponseTypeId() {
        return responseTypeId;
    }

    public void setResponseTypeId(ResponseType responseTypeId) {
        this.responseTypeId = responseTypeId;
    }

    public Question getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Question questionId) {
        this.questionId = questionId;
    }

    @XmlTransient
    public Collection<StudentResponse> getStudentResponseCollection() {
        return studentResponseCollection;
    }

    public void setStudentResponseCollection(Collection<StudentResponse> studentResponseCollection) {
        this.studentResponseCollection = studentResponseCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (responseId != null ? responseId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Response)) {
            return false;
        }
        Response other = (Response) object;
        if ((this.responseId == null && other.responseId != null) || (this.responseId != null && !this.responseId.equals(other.responseId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.supcourses.entity.Response[ responseId=" + responseId + " ]";
    }
    
}
