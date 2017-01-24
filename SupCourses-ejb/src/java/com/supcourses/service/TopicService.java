/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supcourses.service;

import com.supcourses.dao.TopicDao;
import com.supcourses.dao_jpa.exceptions.NonexistentEntityException;
import com.supcourses.dao_jpa.exceptions.RollbackFailureException;
import com.supcourses.entity.Topic;
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
public class TopicService {
    @EJB
    private TopicDao topicDao;

    public Topic create(Topic topic) throws RollbackFailureException {
        try {
            return topicDao.create(topic);
        } catch (Exception ex) {
            Logger.getLogger(TopicService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Topic edit(Topic topic) {
        try {
            return topicDao.edit(topic);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(TopicService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (RollbackFailureException ex) {
            Logger.getLogger(TopicService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(TopicService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Topic remove(Topic topic) {
        try {
            return topicDao.destroy(topic.getTopicId());
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(TopicService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (RollbackFailureException ex) {
            Logger.getLogger(TopicService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(TopicService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Topic findTopicByTitle(String title) {
        return topicDao.findTopicByTitle(title);
    }
    
    public Topic find(String id) {
        return topicDao.findTopic(id);
    }
    
    public List<Topic> findTopicEntities(boolean validate) {
        return topicDao.findTopicEntities(validate);
    }
    
    public List<Topic> findAll() {
        return topicDao.findTopicEntities();
    }

    public List<Topic> findRange(int[] range) {
        return topicDao.findTopicEntities(range[0], range[1]);
    }

    public int count() {
        return topicDao.getTopicCount();
    }
    
}
