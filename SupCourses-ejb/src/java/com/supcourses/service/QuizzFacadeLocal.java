/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supcourses.service;

import com.supcourses.entity.Quizz;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jngue
 */
@Local
public interface QuizzFacadeLocal {

    void create(Quizz quizz);

    void edit(Quizz quizz);

    void remove(Quizz quizz);

    Quizz find(Object id);

    List<Quizz> findAll();

    List<Quizz> findRange(int[] range);

    int count();
    
}
