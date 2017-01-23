/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supcourses.dao;

import com.supcourses.dao_jpa.exceptions.IllegalOrphanException;
import com.supcourses.dao_jpa.exceptions.NonexistentEntityException;
import com.supcourses.dao_jpa.exceptions.PreexistingEntityException;
import com.supcourses.dao_jpa.exceptions.RollbackFailureException;
import com.supcourses.entity.Certification;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jngue
 */

@Local
public interface CertificationDao {
    public Certification create(Certification certification) throws PreexistingEntityException, RollbackFailureException, Exception;
    
    public Certification edit(Certification certification) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception;
    
    public Certification destroy(String id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception;
    
    public List<Certification> findCertificationEntities();
    
    public List<Certification> findCertificationEntities(int maxResults, int firstResult);
    
    public Certification findCertification(String id);
    
    public int getCertificationCount();
}
