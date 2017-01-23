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
import com.supcourses.entity.Tag;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jngue
 */

@Local
public interface TagDao {
    public Tag create(Tag tag) throws PreexistingEntityException, RollbackFailureException, Exception;
    
    public Tag edit(Tag tag) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception;
    
    public Tag destroy(String id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception;
    
    public Tag findTagByName(String name);
    
    public List<Tag> findTagEntities();
    
    public List<Tag> findTagEntities(int maxResults, int firstResult);
    
    public Tag findTag(String id);
    
    public int getTagCount();
}
