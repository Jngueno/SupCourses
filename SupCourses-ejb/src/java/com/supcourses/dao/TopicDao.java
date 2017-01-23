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
import com.supcourses.entity.Topic;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jngue
 */
 
@Local
public interface TopicDao {
    public Topic create(Topic topic) throws PreexistingEntityException, RollbackFailureException, Exception;
    
    public Topic edit(Topic topic) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception;
    
    public Topic destroy(String id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception;
    
    public Topic findTopicByTitle(String title);
    
    public List<Topic> findTopicEntities(boolean validate);
    
    public List<Topic> findTopicEntities();
    
    public List<Topic> findTopicEntities(int maxResults, int firstResult);
    
    public Topic findTopic(String id);
    
    public int getTopicCount();
}
