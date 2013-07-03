/************************************************************
 *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
 *                                                          *
 *  See the file LICENCE.txt for copying permission.        *
 ***********************************************************/
package com.satanko.weballgrade.data.model;

import com.satanko.weballgrade.data.dao.impl.hibernate.HibernateLessonDAO;
import java.util.ArrayList;

/**
 * This class acts as a model class for holding data in the application.
 * It represents a variation of {@link Lesson} as posted from ExcelAllGrade in {@link HibernateLessonDAO#createOrUpdateLessons(java.util.ArrayList, java.util.ArrayList)}.
 * 
 * @author Julian Tropper (julian.tropper@gmail.com)
 * ATTENTION: If not otherwise specified, the author(s) of the class is/are also responsible for all methods.
 * 
 * @version 1.0
 */
public class JsonLesson {
    private String classToken;
    private String subjectToken;
    private ArrayList<String> teacherList = new ArrayList<>();
    
    public JsonLesson (String classToken, String subjectToken)
    {
        this.classToken = classToken;
        this.subjectToken = subjectToken;
    }
    
    public void addTeacher(String teacherToken)
    {
        teacherList.add(teacherToken);
    }

    public String getClassToken() {
        return classToken;
    }

    public void setClassToken(String classToken) {
        this.classToken = classToken;
    }

    public String getSubjectToken() {
        return subjectToken;
    }

    public void setSubjectToken(String subjectToken) {
        this.subjectToken = subjectToken;
    }

    public ArrayList<String> getTeacherList() {
        return teacherList;
    }

    public void setTeacherList(ArrayList<String> teacherList) {
        this.teacherList = teacherList;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        JsonLesson lesson = (JsonLesson) obj;
        
        String classToken = lesson.classToken;
        String subjectToken = lesson.subjectToken;
        ArrayList<String> teacherTokens = lesson.getTeacherList();
        
        if(this.classToken.equals(classToken) && this.subjectToken.equals(subjectToken))
        {
            for (String teacherToken : teacherTokens)
            {
                if (!this.teacherList.contains(teacherToken))
                {
                    return false;
                }
            }
        }
        else 
        {
            return false;
        }
        
        return true;
    }
}
