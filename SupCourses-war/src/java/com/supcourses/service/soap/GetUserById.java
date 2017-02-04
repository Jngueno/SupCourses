/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supcourses.service.soap;

import com.supcourses.entity.Student;
import com.supcourses.service.StudentService;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 *
 * @author jngue
 */
@WebService(serviceName = "GetUserById")
public class GetUserById {

    @EJB
    private StudentService ejbRef;// Add business logic below. (Right-click in editor and choose
    // "Web Service > Add Operation"

    @WebMethod(operationName = "create")
    public Student create(@WebParam(name = "student") Student student) {
        return ejbRef.create(student);
    }

    @WebMethod(operationName = "edit")
    public Student edit(@WebParam(name = "student") Student student) {
        return ejbRef.edit(student);
    }

    @WebMethod(operationName = "remove")
    public Student remove(@WebParam(name = "student") Student student) {
        return ejbRef.remove(student);
    }

    @WebMethod(operationName = "findStudentByEmailAndPassword")
    public Student findStudentByEmailAndPassword(@WebParam(name = "eMail") String eMail, @WebParam(name = "password") String password) {
        return ejbRef.findStudentByEmailAndPassword(eMail, password);
    }

    @WebMethod(operationName = "find")
    public Student find(@WebParam(name = "id") String id) {
        return ejbRef.find(id);
    }

    @WebMethod(operationName = "findAll")
    public List<Student> findAll() {
        return ejbRef.findAll();
    }

    @WebMethod(operationName = "findRange")
    public List<Student> findRange(@WebParam(name = "range") int[] range) {
        return ejbRef.findRange(range);
    }

    @WebMethod(operationName = "count")
    public int count() {
        return ejbRef.count();
    }
    
}
