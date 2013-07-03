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
@Table(name="AG_Student")
public class Student  implements java.io.Serializable {

    @Id
    @Column(name="studentToken", length=8, nullable=false)
    private String token;
    @Column(name="studentFirstName")
    private String firstName;
    @Column(name="studentLastName")
    private String lastName;
    

    @JoinColumn(name="studentClassToken_fk", referencedColumnName="classToken")
    @ManyToOne(fetch=FetchType.EAGER)
    @ForeignKey(name="FK_Student_Class")
    private Clazz clazz;

    @OneToMany(mappedBy="student", cascade=CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<GradeLog> gradelogs = new ArrayList<>();
    
    public Student() {
        super();
    }

    public Student(String token, Clazz clazz) {
        this.token = token;
        this.clazz = clazz;
    }

    public Student(String token, String firstName, String lastName) {
        this.token = token;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    public Student(String token, String firstName, String lastName, Clazz clazz) {
        this.token = token;
        this.firstName = firstName;
        this.lastName = lastName;
        this.clazz = clazz;
    }
    
    
   
    public String getToken() {
        return this.token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    public String getFirstName() {
        return this.firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return this.lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Clazz getClazz() {
        return clazz;
    }

    public void setClazz(Clazz clazz) {
        this.clazz = clazz;
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
        final Student other = (Student) obj;
        if (!Objects.equals(this.token, other.token)) {
            return false;
        }
        if (!Objects.equals(this.firstName, other.firstName)) {
            return false;
        }
        if (!Objects.equals(this.lastName, other.lastName)) {
            return false;
        }
        if (!Objects.equals(this.clazz, other.clazz)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.token);
        hash = 59 * hash + Objects.hashCode(this.firstName);
        hash = 59 * hash + Objects.hashCode(this.lastName);
        hash = 59 * hash + Objects.hashCode(this.clazz);
        return hash;
    }

    




}


