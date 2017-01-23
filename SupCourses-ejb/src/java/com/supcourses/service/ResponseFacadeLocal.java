/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supcourses.service;

import com.supcourses.entity.Response;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jngue
 */
@Local
public interface ResponseFacadeLocal {

    void create(Response response);

    void edit(Response response);

    void remove(Response response);

    Response find(Object id);

    List<Response> findAll();

    List<Response> findRange(int[] range);

    int count();
    
}
