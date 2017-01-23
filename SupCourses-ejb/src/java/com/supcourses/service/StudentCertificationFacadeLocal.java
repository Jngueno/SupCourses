/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supcourses.service;

import com.supcourses.entity.StudentCertification;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jngue
 */
@Local
public interface StudentCertificationFacadeLocal {

    void create(StudentCertification studentCertification);

    void edit(StudentCertification studentCertification);

    void remove(StudentCertification studentCertification);

    StudentCertification find(Object id);

    List<StudentCertification> findAll();

    List<StudentCertification> findRange(int[] range);

    int count();
    
}
