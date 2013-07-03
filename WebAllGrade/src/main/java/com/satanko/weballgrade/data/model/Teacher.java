/************************************************************
 *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
 *                                                          *
 *  See the file LICENCE.txt for copying permission.        *
 ***********************************************************/
package com.satanko.weballgrade.data.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 * This class acts as a model class for holding data in the application. Additionally it provides the information required by Hibernate
 * for the generation of the database tables.
 * <p>
 * For more information about the implementation of the data layer, please see the 
 * <a href=http://docs.jboss.org/hibernate/annotations/3.5/reference/en/html_single/>official documentation of annotation usage</a>
 * , the <a href=http://docs.oracle.com/javaee/6/api/javax/persistence/package-summary.html>official java persistence documentation</a> or
 * (for a more general overview) the <a href=http://en.wikipedia.org/wiki/Java_Persistence_API>"Java Persistence API" article</a> on wikipedia.
 * 
 * @author Philipp Sommersguter (philipp.sommersguter@satanko.at)
 * ATTENTION: If not otherwise specified, the author(s) of the class is/are also responsible for all methods.
 * 
 * @version 1.0
 */
@Entity
@Table(name="AG_Teacher")
public class Teacher  implements java.io.Serializable {

    @Id
    @Column(name="teacherToken", length=3)
    private String token;

    @ManyToMany(cascade=CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.TRUE)
    @JoinTable(name="JOIN_Teacher_Lesson", joinColumns={@JoinColumn(name="teacherToken_fk",referencedColumnName="teacherToken")}, 
            inverseJoinColumns={@JoinColumn(name="lessonId_fk",referencedColumnName="lessonId")})
    private List<Lesson> lessons = new ArrayList<>();
    
    public Teacher() {
        super();
    }

    public Teacher(String token) {
       this.token = token;
    }
   
    public String getToken() {
        return this.token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Teacher other = (Teacher) obj;
        if (!Objects.equals(this.token, other.token)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.token);
        return hash;
    }

}


