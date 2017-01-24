package com.supcourses.entity;

import com.supcourses.entity.Question;
import com.supcourses.entity.ResponseType;
import com.supcourses.entity.StudentResponse;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-01-24T10:10:10")
@StaticMetamodel(Response.class)
public class Response_ { 

    public static volatile SingularAttribute<Response, Question> questionId;
    public static volatile SingularAttribute<Response, Boolean> correct;
    public static volatile SingularAttribute<Response, ResponseType> responseTypeId;
    public static volatile CollectionAttribute<Response, StudentResponse> studentResponseCollection;
    public static volatile SingularAttribute<Response, String> responseContent;
    public static volatile SingularAttribute<Response, String> responseId;

}