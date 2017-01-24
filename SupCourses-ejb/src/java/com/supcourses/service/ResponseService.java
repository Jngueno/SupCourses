/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supcourses.service;

import com.supcourses.dao.ResponseDao;
import com.supcourses.dao_jpa.exceptions.NonexistentEntityException;
import com.supcourses.dao_jpa.exceptions.RollbackFailureException;
import com.supcourses.entity.Question;
import com.supcourses.entity.Response;
import com.supcourses.entity.ResponseType;
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
public class ResponseService {
    
    @EJB
    private ResponseDao responseDao;

    public Response create(Response response) {
        try {
            return responseDao.create(response);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(ResponseService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(ResponseService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Response edit(Response response) {
        try {
            return responseDao.edit(response);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ResponseService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (RollbackFailureException ex) {
            Logger.getLogger(ResponseService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(ResponseService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Response remove(Response response) {
        try {
            return responseDao.destroy(response.getResponseId());
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ResponseService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (RollbackFailureException ex) {
            Logger.getLogger(ResponseService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(ResponseService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public List<Response> findResponsesByCorrect(boolean correct) {
        return responseDao.findResponsesByCorrect(correct);
    }
    
    public List<Response> findResponsesByResponseType(ResponseType responseType) {
        return responseDao.findResponsesByResponseType(responseType);
    }
    
    public List<Response> findResponsesByQuestion(Question question) {
        return responseDao.findResponsesByQuestion(question);
    }

    public  Response find(String id) {
        return responseDao.findResponse(id);
    }

    public List<Response> findAll() {
        return responseDao.findResponseEntities();
    }

    public List<Response> findRange(int[] range) {
        return responseDao.findResponseEntities(range[0], range[1]);
    }

    public int count() {
        return responseDao.getResponseCount();
    }
    
}
