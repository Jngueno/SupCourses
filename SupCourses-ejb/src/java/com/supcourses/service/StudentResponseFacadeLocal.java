/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supcourses.service;

import com.supcourses.entity.StudentResponse;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jngue
 */
@Local
public interface StudentResponseFacadeLocal {

    void create(StudentResponse studentResponse);

    void edit(StudentResponse studentResponse);

    void remove(StudentResponse studentResponse);

    StudentResponse find(Object id);

    List<StudentResponse> findAll();

    List<StudentResponse> findRange(int[] range);

    int count();
    
}
