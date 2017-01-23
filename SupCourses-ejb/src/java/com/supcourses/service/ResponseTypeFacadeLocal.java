/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supcourses.service;

import com.supcourses.entity.ResponseType;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jngue
 */
@Local
public interface ResponseTypeFacadeLocal {

    void create(ResponseType responseType);

    void edit(ResponseType responseType);

    void remove(ResponseType responseType);

    ResponseType find(Object id);

    List<ResponseType> findAll();

    List<ResponseType> findRange(int[] range);

    int count();
    
}
