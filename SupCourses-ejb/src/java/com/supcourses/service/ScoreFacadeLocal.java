/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supcourses.service;

import com.supcourses.entity.Score;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jngue
 */
@Local
public interface ScoreFacadeLocal {

    void create(Score score);

    void edit(Score score);

    void remove(Score score);

    Score find(Object id);

    List<Score> findAll();

    List<Score> findRange(int[] range);

    int count();
    
}
