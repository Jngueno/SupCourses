package com.supcourses.entity;

import com.supcourses.entity.Module;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-01-24T10:10:10")
@StaticMetamodel(Topic.class)
public class Topic_ { 

    public static volatile SingularAttribute<Topic, Date> duration;
    public static volatile SingularAttribute<Topic, String> topicId;
    public static volatile SingularAttribute<Topic, String> title;
    public static volatile SingularAttribute<Topic, Module> moduleId;
    public static volatile SingularAttribute<Topic, String> content;
    public static volatile SingularAttribute<Topic, Boolean> validate;

}