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
import com.supcourses.entity.Course;
import com.supcourses.entity.Module;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jngue
 */

@Local
public interface ModuleDao {
    public Module create(Module module) throws PreexistingEntityException, RollbackFailureException, Exception;
    
    public Module edit(Module module) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception;
    
    public Module destroy(String id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception;
    
    public Module findModuleByName (String name);
    
    public List<Module> findModulesCourse (Course course);
    
    public List<Module> findModuleEntities();
    
    public List<Module> findModuleEntities(int maxResults, int firstResult);
    
    public Module findModule(String id);
    
    public int getModuleCount();
}
