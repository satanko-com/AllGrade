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
import org.hibernate.annotations.ForeignKey;
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
@Table(name="AG_Lesson")
public class Lesson  implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="lessonId")
    private Long id;
    
    @JoinColumn(name="lessonClassToken_fk", referencedColumnName="classToken", nullable=false)
    @ManyToOne
    @ForeignKey(name="FK_Lesson_Class")
    private Clazz clazz;
    
    @JoinColumn(name="lessonSubjectToken_fk", referencedColumnName="subjectToken", nullable=false)
    @OneToOne
    @ForeignKey(name="FK_Lesson_Subject")
    private Subject subject;
    
    @ManyToMany()
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name="JOIN_Teacher_Lesson", joinColumns={@JoinColumn(name="lessonId_fk",referencedColumnName="lessonId")}, 
        inverseJoinColumns={@JoinColumn(name="teacherToken_fk",referencedColumnName="teacherToken")})
    private List<Teacher> teachers = new ArrayList<>();

    @OneToMany(mappedBy="lesson", cascade=CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<GradeLog> gradelogs = new ArrayList<>();
    
    public Lesson() {
        super();
    }

    public Lesson(Clazz clazz, Subject subject) {
        this.clazz = clazz;
        this.subject = subject;
    }
    
    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Clazz getClazz() {
        return clazz;
    }

    public void setClazz(Clazz clazz) {
        this.clazz = clazz;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }

    public List<GradeLog> getGradelogs() {
        return gradelogs;
    }

    public void setGradelogs(List<GradeLog> gradelogs) {
        this.gradelogs = gradelogs;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Lesson other = (Lesson) obj;
        if (!Objects.equals(this.clazz, other.clazz)) {
            return false;
        }
        if (!Objects.equals(this.subject, other.subject)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.clazz);
        hash = 53 * hash + Objects.hashCode(this.subject);
        return hash;
    }
    
    
}


