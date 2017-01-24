package com.supcourses.entity;

import com.supcourses.entity.Course;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-01-24T10:04:30")
@StaticMetamodel(Tag.class)
public class Tag_ { 

    public static volatile SingularAttribute<Tag, String> tagId;
    public static volatile CollectionAttribute<Tag, Course> courseCollection;
    public static volatile SingularAttribute<Tag, String> tagName;

}