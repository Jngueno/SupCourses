/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supcourses.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jngue
 */
@Entity
@Table(name = "student_response")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "StudentResponse.findAll", query = "SELECT s FROM StudentResponse s"),
    @NamedQuery(name = "StudentResponse.findByStudentResponseId", query = "SELECT s FROM StudentResponse s WHERE s.studentResponseId = :studentResponseId"),
    @NamedQuery(name = "StudentResponse.findByQuizzId", query = "SELECT s FROM StudentResponse s WHERE s.quizzId = :quizzId"),
    @NamedQuery(name = "StudentResponse.findByQuestionId", query = "SELECT s FROM StudentResponse s WHERE s.questionId = :questionId")})
public class StudentResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 36)
    @Column(name = "student_response_id")
    private String studentResponseId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 36)
    @Column(name = "quizz_id")
    private String quizzId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 36)
    @Column(name = "question_id")
    private String questionId;
    @JoinColumn(name = "student_id", referencedColumnName = "student_id")
    @ManyToOne(optional = false)
    private Student studentId;
    @JoinColumn(name = "response_id", referencedColumnName = "response_id")
    @ManyToOne(optional = false)
    private Response responseId;

    public StudentResponse() {
    }

    public StudentResponse(String studentResponseId) {
        this.studentResponseId = studentResponseId;
    }

    public StudentResponse(String studentResponseId, String quizzId, String questionId) {
        this.studentResponseId = studentResponseId;
        this.quizzId = quizzId;
        this.questionId = questionId;
    }

    public String getStudentResponseId() {
        return studentResponseId;
    }

    public void setStudentResponseId(String studentResponseId) {
        this.studentResponseId = studentResponseId;
    }

    public String getQuizzId() {
        return quizzId;
    }

    public void setQuizzId(String quizzId) {
        this.quizzId = quizzId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public Student getStudentId() {
        return studentId;
    }

    public void setStudentId(Student studentId) {
        this.studentId = studentId;
    }

    public Response getResponseId() {
        return responseId;
    }

    public void setResponseId(Response responseId) {
        this.responseId = responseId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (studentResponseId != null ? studentResponseId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StudentResponse)) {
            return false;
        }
        StudentResponse other = (StudentResponse) object;
        if ((this.studentResponseId == null && other.studentResponseId != null) || (this.studentResponseId != null && !this.studentResponseId.equals(other.studentResponseId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.supcourses.entity.StudentResponse[ studentResponseId=" + studentResponseId + " ]";
    }
    
}
