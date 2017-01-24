/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supcourses.service;

import com.supcourses.dao.ResponseTypeDao;
import com.supcourses.dao_jpa.exceptions.NonexistentEntityException;
import com.supcourses.dao_jpa.exceptions.RollbackFailureException;
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
public class ResponseTypeService {
    
    @EJB
    private ResponseTypeDao responseTypeDao;

    public ResponseType create(ResponseType responseType) {
        try {
            return responseTypeDao.create(responseType);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(ResponseTypeService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(ResponseTypeService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public ResponseType edit(ResponseType responseType) {
        try {
            return responseTypeDao.edit(responseType);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ResponseTypeService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (RollbackFailureException ex) {
            Logger.getLogger(ResponseTypeService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(ResponseTypeService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public ResponseType remove(ResponseType responseType) {
        try {
            return responseTypeDao.destroy(responseType.getResponseTypeId());
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ResponseTypeService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (RollbackFailureException ex) {
            Logger.getLogger(ResponseTypeService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(ResponseTypeService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public ResponseType findResponseTypeByResponseTypeContent (String responseTypeContent) {
        return responseTypeDao.findResponseTypeByResponseTypeContent(responseTypeContent);
    }
    
    public ResponseType find(String id) {
        return responseTypeDao.findResponseType(id);
    }

    public List<ResponseType> findAll() {
        return responseTypeDao.findResponseTypeEntities();
    }

    public List<ResponseType> findRange(int[] range) {
        return responseTypeDao.findResponseTypeEntities(range[0], range[1]);
    }

    public int count() {
        return responseTypeDao.getResponseTypeCount();
    }
    
}
