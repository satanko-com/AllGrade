/************************************************************
 *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
 *                                                          *
 *  See the file LICENCE.txt for copying permission.        *
 ***********************************************************/
package com.satanko.weballgrade.data.model;

import java.sql.Timestamp;
import java.util.Objects;
import javax.persistence.*;
import org.hibernate.annotations.ForeignKey;

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
@Table(name="AG_Gradelog")
public class GradeLog implements java.io.Serializable {
    
    @Id
    @Column(name="gradelogTimestamp", nullable=false)
    private Timestamp timestamp;
    
    @Id
    @JoinColumn(name="gradelogLessonFk", referencedColumnName="lessonId", nullable=false)
    @ManyToOne(fetch=FetchType.EAGER)
    @ForeignKey(name="FK_Gradelog_Lesson")
    private Lesson lesson;
    
    @Id
    @JoinColumn(name="gradelogStudentFk", referencedColumnName="studentToken", nullable=false)
    @ManyToOne(fetch=FetchType.EAGER)
    @ForeignKey(name="FK_Gradelog_Student")
    private Student student;
    
    @Column(name="gradelogType", nullable=false)
    private String type;
    
    @Column(name="gradelogComment", nullable=false)
    private String comment;
    
    @Column(name="gradelogGrading", nullable=false)
    private int grading;
    
    public GradeLog(){
        super();
    }

    public GradeLog(Timestamp timestamp, Lesson lesson, Student student, String comment, String type, int grading) {
        this.timestamp = timestamp;
        this.lesson = lesson;
        this.student = student;
        this.comment = comment;
        this.type = type;
        this.grading = grading;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getGrading() {
        return grading;
    }

    public void setGrading(int grading) {
        this.grading = grading;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GradeLog other = (GradeLog) obj;
        if (!Objects.equals(this.timestamp, other.timestamp)) {
            return false;
        }
        if (!Objects.equals(this.lesson, other.lesson)) {
            return false;
        }
        if (!Objects.equals(this.student, other.student)) {
            return false;
        }
        if (!Objects.equals(this.comment, other.comment)) {
            return false;
        }
        if (this.grading != other.grading) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.timestamp);
        hash = 89 * hash + Objects.hashCode(this.lesson);
        hash = 89 * hash + Objects.hashCode(this.student);
        hash = 89 * hash + Objects.hashCode(this.comment);
        hash = 89 * hash + this.grading;
        return hash;
    }
    
    
}
