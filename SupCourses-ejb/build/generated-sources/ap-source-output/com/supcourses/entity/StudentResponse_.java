package com.supcourses.entity;

import com.supcourses.entity.Response;
import com.supcourses.entity.Student;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-01-24T10:10:10")
@StaticMetamodel(StudentResponse.class)
public class StudentResponse_ { 

    public static volatile SingularAttribute<StudentResponse, Student> studentId;
    public static volatile SingularAttribute<StudentResponse, String> questionId;
    public static volatile SingularAttribute<StudentResponse, String> quizzId;
    public static volatile SingularAttribute<StudentResponse, String> studentResponseId;
    public static volatile SingularAttribute<StudentResponse, Response> responseId;

}