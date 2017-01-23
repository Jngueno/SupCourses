/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supcourses.service;

import com.supcourses.entity.Module;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jngue
 */
@Local
public interface ModuleFacadeLocal {

    void create(Module module);

    void edit(Module module);

    void remove(Module module);

    Module find(Object id);

    List<Module> findAll();

    List<Module> findRange(int[] range);

    int count();
    
}
