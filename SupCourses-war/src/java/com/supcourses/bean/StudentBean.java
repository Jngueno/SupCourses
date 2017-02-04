/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supcourses.bean;

import com.supcourses.entity.Student;
import com.supcourses.helper.helper;
import com.supcourses.service.StudentService;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.UUID;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

/**
 *
 * @author jngue
 */
@ManagedBean
@SessionScoped
public class StudentBean implements Serializable {

    @EJB
    private com.supcourses.service.StudentService studentService;
    
    private String firstname;
    private String lastname;
    private String eMail;
    private String password;
    
    private com.supcourses.entity.Student student;
    
    
    public Student register () {
        student.setStudentId(UUID.randomUUID().toString());
        student.setFirstName(firstname);
        student.setLastName(lastname);
        student.setEMail(eMail);
        student.setPassword(helper.encode(password));
        
        return studentService.create(student);
    }
    
    public Student connection() {
        return null;
    }

    /**
     * Creates a new instance of StudentBean
     */
    public StudentBean() {
    }

    public StudentService getStudentService() {
        return studentService;
    }

    public void setStudentService(StudentService studentService) {
        this.studentService = studentService;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
    
    
    
}
