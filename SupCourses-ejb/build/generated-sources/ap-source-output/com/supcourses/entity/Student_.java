package com.supcourses.entity;

import com.supcourses.entity.Score;
import com.supcourses.entity.StudentCertification;
import com.supcourses.entity.StudentCourse;
import com.supcourses.entity.StudentResponse;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-01-24T10:10:10")
@StaticMetamodel(Student.class)
public class Student_ { 

    public static volatile SingularAttribute<Student, String> studentId;
    public static volatile SingularAttribute<Student, String> firstName;
    public static volatile SingularAttribute<Student, String> lastName;
    public static volatile SingularAttribute<Student, String> password;
    public static volatile CollectionAttribute<Student, StudentCourse> studentCourseCollection;
    public static volatile CollectionAttribute<Student, Score> scoreCollection;
    public static volatile CollectionAttribute<Student, StudentCertification> studentCertificationCollection;
    public static volatile CollectionAttribute<Student, StudentResponse> studentResponseCollection;
    public static volatile SingularAttribute<Student, String> eMail;

}