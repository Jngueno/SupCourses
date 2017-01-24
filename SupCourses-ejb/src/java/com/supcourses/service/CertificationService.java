/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supcourses.service;

import com.supcourses.dao.CertificationDao;
import com.supcourses.dao_jpa.exceptions.NonexistentEntityException;
import com.supcourses.dao_jpa.exceptions.RollbackFailureException;
import com.supcourses.entity.Certification;
import com.supcourses.entity.Student;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Asynchronous;
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

    public Certification create(Certification certification) {
        try {
            return certificationDao.create(certification);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(CertificationService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(CertificationService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Certification edit(Certification certification) {
        try {
            return certificationDao.edit(certification);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(CertificationService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (RollbackFailureException ex) {
            Logger.getLogger(CertificationService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(CertificationService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Certification remove(Certification certification) {
        try {
            return certificationDao.destroy(certification.getCertificationId());
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(CertificationService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (RollbackFailureException ex) {
            Logger.getLogger(CertificationService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(CertificationService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Certification find(String id) {
        return certificationDao.findCertification(id);
        //return null;
    }

    public List<Certification> findAll() {
        return certificationDao.findCertificationEntities();
    }

    public List<Certification> findRange(int[] range) {
        return certificationDao.findCertificationEntities(range[0], range[1]);
    }

    public int count() {
        return certificationDao.getCertificationCount();
    }
    
}
