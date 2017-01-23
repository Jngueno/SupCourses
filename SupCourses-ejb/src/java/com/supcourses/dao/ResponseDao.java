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
import com.supcourses.entity.Question;
import com.supcourses.entity.Response;
import com.supcourses.entity.ResponseType;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jngue
 */

@Local
public interface ResponseDao {
    public Response create(Response response) throws PreexistingEntityException, RollbackFailureException, Exception;
    
    public Response edit(Response response) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception;
    
    public Response destroy(String id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception;
    
    public List<Response> findResponsesByCorrect(boolean correct);
    
    public List<Response> findResponsesByResponseType(ResponseType responseType);
    
    public List<Response> findResponsesByQuestion(Question question);
    
    public List<Response> findResponseEntities();
    
    public List<Response> findResponseEntities(int maxResults, int firstResult);
    
    public Response findResponse(String id);
    
    public int getResponseCount();
}
