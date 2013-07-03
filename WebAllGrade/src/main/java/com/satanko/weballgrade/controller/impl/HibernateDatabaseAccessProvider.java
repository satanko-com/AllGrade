/************************************************************
 *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
 *                                                          *
 *  See the file LICENCE.txt for copying permission.        *
 ***********************************************************/
package com.satanko.weballgrade.controller.impl;

import com.satanko.weballgrade.controller.DatabaseAccessProvider;
import com.satanko.weballgrade.data.dao.*;
import com.satanko.weballgrade.data.dao.impl.hibernate.*;
import com.satanko.weballgrade.data.model.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides an implementation of the {@link DatabaseAccessProvider} 
 * interface using the Hibernate Framework to access the database.
 * 
 * @author Philipp Sommersguter (philipp.sommersguter@satanko.at) 
 * ATTENTION: If not otherwise specified, the author(s) of the class is/are also responsible for all methods.
 * 
 * @version 1.0
 */
public class HibernateDatabaseAccessProvider implements DatabaseAccessProvider{

    /**
     *  A field providing access to the <b>"AG_Student"</b> table in the database.
     * @see StudentDAO
     * @see HibernateStudentDAO
     * @see Student
     */
    private StudentDAO studentDao = new HibernateStudentDAO();
    
    /**
     *  A field providing access to the <b>"AG_Lesson"</b> table in the database.
     * @see LessonDAO
     * @see HibernateLessonDAO
     * @see Lesson
     */
    private LessonDAO lessonDAO = new HibernateLessonDAO();
    
    /**
     *  A field providing access to the <b>"AG_Gradelog"</b> table in the database.
     * @see GradeLogDAO
     * @see HibernateGradeLogDAO
     * @see GradeLog
     */
    private GradeLogDAO gradelogDAO = new HibernateGradeLogDAO();
    
    /**
     *  A field providing access to the <b>"AG_Subject"</b> table in the database.
     * @see SubjectDAO
     * @see HibernateSubjectDAO
     * @see Subject
     */
    private SubjectDAO subjectDAO = new HibernateSubjectDAO();
    
    /**
     *  A field providing access to the <b>"AG_Class"</b> table in the database.
     * @see ClassDAO
     * @see HibernateClassDAO
     * @see Clazz
     */
    private ClassDAO classDAO = new HibernateClassDAO();
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<Student> getStudentList(String paramClass) {
        return this.studentDao.selectStudentList(paramClass);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Queries for a list of all lessons a teacher has in a class. 
     * If the list is not empty (lessons exist in database) the method returns <b>true</b>.
     * When there is no lesson in the database for the combination of teacher and class the method will return <b>false</b>.
     * @see Lesson
     */
    @Override
    public boolean checkTeacherInClass(String paramTeacher, String paramClass) {
        List<Lesson> lessonsInClass = this.lessonDAO.selectLessonList(paramTeacher, paramClass);
        return !lessonsInClass.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Lesson> getLessonList(String paramTeacher, String paramClass) {
        return this.lessonDAO.selectLessonList(paramTeacher, paramClass);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createGradeLog(long paramTimestamp, String paramClass, String paramTeacher, String paramStudent, 
                            String paramSubject, String paramType, int paramGrading, String paramComment) {
        this.gradelogDAO.insertNewGradeLog(paramTimestamp, paramClass, paramTeacher, paramStudent, 
                paramSubject, paramType, paramGrading, paramComment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Subject> getSubjectList() {
        return this.subjectDAO.getSubjectList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Clazz> getClassList() {
        return this.classDAO.selectClassList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createOrUpdateLessons(ArrayList<JsonLesson> oldLessons, ArrayList<JsonLesson> newLessons) {
        this.lessonDAO.createOrUpdateLessons(oldLessons, newLessons);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GradeLog> getGradeLogs(String paramTeacher, String paramClass, String paramSubject) {
        return this.gradelogDAO.selectGradeLogs(paramTeacher, paramClass, paramSubject);
    }
}