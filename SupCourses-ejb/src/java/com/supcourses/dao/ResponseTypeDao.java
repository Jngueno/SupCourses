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
import com.supcourses.entity.ResponseType;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jngue
 */

@Local
public interface ResponseTypeDao {
    public ResponseType create(ResponseType responseType) throws PreexistingEntityException, RollbackFailureException, Exception;
    
    public ResponseType edit(ResponseType responseType) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception;
    
    public ResponseType destroy(String id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception;
    
    public ResponseType findResponseTypeByResponseTypeContent (String responseTypeContent);
    
    public List<ResponseType> findResponseTypeEntities();
    
    public List<ResponseType> findResponseTypeEntities(int maxResults, int firstResult);
    
    public ResponseType findResponseType(String id);
    
    public int getResponseTypeCount();
}
