package com.supcourses.entity;

import com.supcourses.entity.Certification;
import com.supcourses.entity.Student;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-01-24T10:04:30")
@StaticMetamodel(StudentCertification.class)
public class StudentCertification_ { 

    public static volatile SingularAttribute<StudentCertification, Student> studentId;
    public static volatile SingularAttribute<StudentCertification, Certification> certificationId;
    public static volatile SingularAttribute<StudentCertification, String> studentCertificationId;
    public static volatile SingularAttribute<StudentCertification, Date> dateDelivered;

}