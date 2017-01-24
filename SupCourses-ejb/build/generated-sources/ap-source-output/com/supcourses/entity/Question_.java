package com.supcourses.entity;

import com.supcourses.entity.Quizz;
import com.supcourses.entity.Response;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-01-24T10:10:10")
@StaticMetamodel(Question.class)
public class Question_ { 

    public static volatile SingularAttribute<Question, String> questionId;
    public static volatile SingularAttribute<Question, String> questiionContent;
    public static volatile SingularAttribute<Question, Quizz> quizzId;
    public static volatile CollectionAttribute<Question, Response> responseCollection;

}