package com.supcourses.entity;

import com.supcourses.entity.Course;
import com.supcourses.entity.Quizz;
import com.supcourses.entity.Topic;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-01-24T10:04:30")
@StaticMetamodel(Module.class)
public class Module_ { 

    public static volatile SingularAttribute<Module, Date> duration;
    public static volatile CollectionAttribute<Module, Quizz> quizzCollection;
    public static volatile CollectionAttribute<Module, Topic> topicCollection;
    public static volatile SingularAttribute<Module, String> name;
    public static volatile SingularAttribute<Module, String> description;
    public static volatile SingularAttribute<Module, String> moduleId;
    public static volatile SingularAttribute<Module, Course> courseId;

}