/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supcourses.service;

import com.supcourses.dao.TagDao;
import com.supcourses.dao_jpa.exceptions.NonexistentEntityException;
import com.supcourses.dao_jpa.exceptions.RollbackFailureException;
import com.supcourses.entity.Tag;
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
public class TagService {
    
    @EJB
    private TagDao tagDao;

    public Tag create(Tag tag) {
        try {
            return tagDao.create(tag);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(TagService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(TagService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Tag edit(Tag tag) {
        try {
            return tagDao.edit(tag);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(TagService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (RollbackFailureException ex) {
            Logger.getLogger(TagService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(TagService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Tag remove(Tag tag) {
        try {
            return tagDao.destroy(tag.getTagId());
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(TagService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (RollbackFailureException ex) {
            Logger.getLogger(TagService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(TagService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Tag findTagByName(String name) {
        return tagDao.findTagByName(name);
    }
    
    public Tag find(String id) {
        return tagDao.findTag(id);
    }

    public List<Tag> findAll() {
        return tagDao.findTagEntities();
    }

    public List<Tag> findRange(int[] range) {
        return tagDao.findTagEntities(range[0], range[1]);
    }

    public int count() {
        return tagDao.getTagCount();
    }
    
}
