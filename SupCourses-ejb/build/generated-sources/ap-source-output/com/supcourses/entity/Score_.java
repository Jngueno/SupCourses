package com.supcourses.entity;

import com.supcourses.entity.Quizz;
import com.supcourses.entity.Student;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-01-17T09:19:52")
@StaticMetamodel(Score.class)
public class Score_ { 

    public static volatile SingularAttribute<Score, String> scoreId;
    public static volatile SingularAttribute<Score, Student> studentId;
    public static volatile SingularAttribute<Score, Integer> score;
    public static volatile SingularAttribute<Score, Quizz> quizzId;

}