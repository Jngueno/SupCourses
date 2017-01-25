package com.supcourses.entity;

import com.supcourses.entity.Course;
import com.supcourses.entity.StudentCertification;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-01-25T21:22:52")
@StaticMetamodel(Certification.class)
public class Certification_ { 

    public static volatile SingularAttribute<Certification, Integer> score;
    public static volatile SingularAttribute<Certification, String> certificationId;
    public static volatile CollectionAttribute<Certification, StudentCertification> studentCertificationCollection;
    public static volatile SingularAttribute<Certification, Course> courseId;

}