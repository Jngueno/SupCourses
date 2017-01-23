/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supcourses.service;

import com.supcourses.entity.Topic;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jngue
 */
@Local
public interface TopicFacadeLocal {

    void create(Topic topic);

    void edit(Topic topic);

    void remove(Topic topic);

    Topic find(Object id);

    List<Topic> findAll();

    List<Topic> findRange(int[] range);

    int count();
    
}
