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
import com.supcourses.entity.Course;
import com.supcourses.entity.Tag;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jngue
 */

@Local
public interface CourseDao {
    public Course create(Course course) throws PreexistingEntityException, RollbackFailureException, Exception;
    
    public Course edit(Course course) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception;
    
    public Course destroy(String id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception;
    
    public Course findCourseByName(String name);
    
    public List<Course> findCoursesByTag(Tag tag);
    
    public List<Course> findCourseEntities();
    
    public List<Course> findCourseEntities(int maxResults, int firstResult);
    
    public Course findCourse(String id);
    
    public int getCourseCount();
}
