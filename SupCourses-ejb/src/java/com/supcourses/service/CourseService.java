/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supcourses.service;

import com.supcourses.dao.CourseDao;
import com.supcourses.dao_jpa.exceptions.NonexistentEntityException;
import com.supcourses.dao_jpa.exceptions.RollbackFailureException;
import com.supcourses.entity.Course;
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
public class CourseService {
    
    @EJB
    private CourseDao courseDao;

    public Course create(Course course) {
        try {
            return courseDao.create(course);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(CourseService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(CourseService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Course edit(Course course) {
        try {
            return courseDao.edit(course);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(CourseService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (RollbackFailureException ex) {
            Logger.getLogger(CourseService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(CourseService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Course remove(Course course) {
        try {
            return courseDao.destroy(course.getCourseId());
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(CourseService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (RollbackFailureException ex) {
            Logger.getLogger(CourseService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(CourseService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Course find(String id) {
        return courseDao.findCourse(id);
    }
    
    public Course findCourseByName(String name) {
        return courseDao.findCourseByName(name);
    }
    
    public List<Course> findCoursesByTag(Tag tag) {
        return courseDao.findCoursesByTag(tag);
    }

    public List<Course> findAll() {
        return courseDao.findCourseEntities();
    }

    public List<Course> findRange(int[] range) {
        return courseDao.findCourseEntities(range[0], range[1]);
    }

    public int count() {
        return courseDao.getCourseCount();
    }
    
}
