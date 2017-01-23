package com.supcourses.entity;

import com.supcourses.entity.Certification;
import com.supcourses.entity.Module;
import com.supcourses.entity.StudentCourse;
import com.supcourses.entity.Tag;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-01-17T09:19:52")
@StaticMetamodel(Course.class)
public class Course_ { 

    public static volatile SingularAttribute<Course, Date> duration;
    public static volatile CollectionAttribute<Course, StudentCourse> studentCourseCollection;
    public static volatile CollectionAttribute<Course, Certification> certificationCollection;
    public static volatile SingularAttribute<Course, Tag> tagId;
    public static volatile SingularAttribute<Course, String> name;
    public static volatile SingularAttribute<Course, String> courseId;
    public static volatile CollectionAttribute<Course, Module> moduleCollection;

}