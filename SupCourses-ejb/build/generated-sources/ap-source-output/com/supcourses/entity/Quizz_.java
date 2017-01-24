package com.supcourses.entity;

import com.supcourses.entity.Module;
import com.supcourses.entity.Question;
import com.supcourses.entity.Score;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-01-24T10:10:10")
@StaticMetamodel(Quizz.class)
public class Quizz_ { 

    public static volatile CollectionAttribute<Quizz, Question> questionCollection;
    public static volatile SingularAttribute<Quizz, Date> createdDate;
    public static volatile CollectionAttribute<Quizz, Score> scoreCollection;
    public static volatile SingularAttribute<Quizz, String> quizzId;
    public static volatile SingularAttribute<Quizz, Module> moduleId;

}