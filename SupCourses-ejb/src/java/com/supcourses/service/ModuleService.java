/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supcourses.service;

import com.supcourses.dao.ModuleDao;
import com.supcourses.dao_jpa.exceptions.NonexistentEntityException;
import com.supcourses.dao_jpa.exceptions.RollbackFailureException;
import com.supcourses.entity.Course;
import com.supcourses.entity.Module;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;

/**
 *
 * @author jngue
 */
@Stateless
public class ModuleService {
    
    @EJB
    private ModuleDao moduleDao;

    public Module create(Module module) {
        try {
            return moduleDao.create(module);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(ModuleService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(ModuleService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Module edit(Module module) {
        try {
            return moduleDao.edit(module);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ModuleService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (RollbackFailureException ex) {
            Logger.getLogger(ModuleService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(ModuleService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Module remove(Module module) {
        try {
            return moduleDao.destroy(module.getModuleId());
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ModuleService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (RollbackFailureException ex) {
            Logger.getLogger(ModuleService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(ModuleService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Module find(String id) {
        return moduleDao.findModule(id);
    }
    
    public Module findModuleByName (String name) {
        return moduleDao.findModuleByName(name);
    }
    
    public List<Module> findModulesCourse (Course course) {
        return moduleDao.findModulesCourse(course);
    }

    public List<Module> findAll() {
        return moduleDao.findModuleEntities();
    }

    public List<Module> findRange(int[] range) {
        return moduleDao.findModuleEntities(range[0], range[1]);
    }

    public int count() {
        return moduleDao.getModuleCount();
    }
    
}
