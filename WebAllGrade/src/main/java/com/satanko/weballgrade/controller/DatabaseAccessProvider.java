/************************************************************
 *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
 *                                                          *
 *  See the file LICENCE.txt for copying permission.        *
 ***********************************************************/
package com.satanko.weballgrade.controller;

import com.satanko.weballgrade.data.model.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This is an interface providing access to any underlayling database implementation.
 * 
 * @author Philipp Sommersguter (philipp.sommersguter@satanko.at)
 * ATTENTION: If not otherwise specified, the author(s) of the class is/are also responsible for all methods.
 * 
 * @version 1.0
 */
public interface DatabaseAccessProvider {

    /**
     * <i>This method queries the database for all students in a specific class.</i>
     * 
     * @param paramClass A String representing a class
     * @return A list of {@link Student} objects.
     */
    public List<Student> getStudentList(String paramClass);

    /**
     * <i>This method queries the database for all lessons a teacher is teaching in a class.</i>
     * 
     * @param paramTeacher A String representing a teacher
     * @param paramClass A String representing a class
     * @return A list of {@link Lesson} objects.
     */
    public List<Lesson> getLessonList(String paramTeacher, String paramClass);
    
    /**
     * <b>WRITTEN BY: Julian Tropper (julian.tropper@gmail.com)</b><p>
     * 
     * <i>This method queries the database for all subjects.</i>
     * 
     * @return A list of {@link Subject} objects.
     */
    public List<Subject> getSubjectList();
    
    /**
     * <i>This method queries the database for all classes.</i>
     * 
     * @return A list of {@link Clazz} objects.
     */
    public List<Clazz> getClassList();
    
    /**
     * <b>WRITTEN BY: Julian Tropper (julian.tropper@gmail.com)</b><p>
     * <i>This method queries the database for all gradelogs matching the given parameters.</i>
     * 
     * @param paramTeacher A String representing a teacher
     * @param paramClass A String representing a class
     * @param paramSubject A String representing a subject
     * @return A list of {@link GradeLog} objects.
     */
    public List<GradeLog> getGradeLogs(String paramTeacher, String paramClass, String paramSubject);

    /**
     * <i>This method queries the database and checks if the given teacher is actually teaching the given class.</i>
     * 
     * @param paramTeacher A String representing a teacher
     * @param paramClass A String representing a class
     * @return true or false
     */
    public boolean checkTeacherInClass(String paramTeacher, String paramClass);

    /**
     * <i>This method creates a new entry for a {@link GradeLog} in the database.</i>
     * 
     * @param paramTimestamp A long representing the time of the gradelog
     * @param paramClass A String representing a class
     * @param paramTeacher A String representing a teacher
     * @param paramStudent A String representing a student
     * @param paramSubject A String representing a subject
     * @param paramType A String representing the type of the gradelog
     * @param paramGrading A int with a value for the gradelog
     * @param paramComment A String representing a comment
     */
    public void createGradeLog(long paramTimestamp, String paramClass, String paramTeacher, String paramStudent, 
            String paramSubject, String paramType, int paramGrading, String paramComment);
    
    /**
     * <b>WRITTEN BY: Julian Tropper (julian.tropper@gmail.com)</b><p>
     * <i>ONE LINE DESCRIPTION OF THE CODE</i><p>
     * NEEDS DOCUMENTATION!!!!
     * 
     * @param oldLessons A ArrayList representing the old lessons
     * @param newLessons A ArrayList representing the new lessons
     */
    public void createOrUpdateLessons(ArrayList<JsonLesson> oldLessons, ArrayList<JsonLesson> newLessons);

}