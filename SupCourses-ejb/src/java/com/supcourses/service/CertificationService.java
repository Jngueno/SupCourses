/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supcourses.service;

import com.supcourses.dao.CertificationDao;
import com.supcourses.entity.Certification;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jngue
 */
@Stateless
public class CertificationService {
    
    @EJB
    private CertificationDao certificationDao;

    public void create(Certification certification) {
        
    }

    public void edit(Certification certification) {
        
    }

    public void remove(Certification certification) {
        
    }

    public Certification find(Object id) {
        return null;
    }

    public List<Certification> findAll() {
        return null;
    }

    public List<Certification> findRange(int[] range) {
        return null;
    }

    public int count() {
        return 0;
    }
    
}
