/************************************************************
 *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
 *                                                          *
 *  See the file LICENCE.txt for copying permission.        *
 ***********************************************************/
package com.satanko.weballgrade.data.model;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
@Table(name = "AG_Subject")
public class Subject implements java.io.Serializable {

    @Id
    @Column(name = "subjectToken", length = 5)
    private String token;
    
    @Column(name = "subjectName", unique = true)
    private String name;

    public Subject() {
        super();
    }

    public Subject(String token) {
        this.token = token;
    }

    public Subject(String token, String name) {
        this.token = token;
        this.name = name;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Subject other = (Subject) obj;
        if (!Objects.equals(this.token, other.token)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.token);
        hash = 89 * hash + Objects.hashCode(this.name);
        return hash;
    }
    
}
