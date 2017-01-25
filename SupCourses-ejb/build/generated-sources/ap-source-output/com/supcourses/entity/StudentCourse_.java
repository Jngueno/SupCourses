package com.supcourses.entity;

import com.supcourses.entity.Course;
import com.supcourses.entity.Student;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-01-25T21:22:52")
@StaticMetamodel(StudentCourse.class)
public class StudentCourse_ { 

    public static volatile SingularAttribute<StudentCourse, Student> studentId;
    public static volatile SingularAttribute<StudentCourse, Date> subscriptionDate;
    public static volatile SingularAttribute<StudentCourse, String> studentCourseId;
    public static volatile SingularAttribute<StudentCourse, Course> courseId;

}