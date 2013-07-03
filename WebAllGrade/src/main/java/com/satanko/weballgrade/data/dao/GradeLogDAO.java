/************************************************************
 *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
 *                                                          *
 *  See the file LICENCE.txt for copying permission.        *
 ***********************************************************/
package com.satanko.weballgrade.data.dao;

import com.satanko.weballgrade.data.model.GradeLog;
import java.util.List;

/**
 * This is an interface designed after the DAO principle.<br> It represents the <b>"AG_Gradelog"</b> table in the database.
 * <p>Reference: <a href=http://en.wikipedia.org/wiki/Data_access_object>http://en.wikipedia.org/wiki/Data_access_object</a>
 * 
 * @author Philipp Sommersguter (philipp.sommersguter@satanko.at)
 * ATTENTION: If not otherwise specified, the author(s) of the class is/are also responsible for all methods.
 * 
 * @version 1.0
 * @see GradeLog
 */
public interface GradeLogDAO {

    /**
     * <i>This method inserts a new entry for a {@link GradeLog} in the table.</i>
     * 
     * @param paramTimestamp A long representing the time of the gradelog
     * @param paramClass A String representing a class
     * @param paramTeacher A String representing a teacher
     * @param paramStudent A String representing a student
     * @param paramSubject A String representing a subject
     * @param paramType A String representing the type of the gradelog
     * @param paramGrading A int with a value for the gradelog
     * @param paramComment A String representing a comment
     * @see GradeLogDAO
     */
    public void insertNewGradeLog(long paramTimestamp, String paramClass, String paramTeacher, 
            String paramStudent, String paramSubject, String paramType, int paramGrading, String paramComment);
    
    /**
     * <i>This method selects gradelogs from the table and returns them as a {@link List}.</i>
     * 
     * @param paramTeacher A String representing a teacher
     * @param paramClass A String representing a class
     * @param paramSubject A String representing a subject
     * @return A list of {@link GradeLog} objects
     * @see GradeLogDAO
     */
    public List<GradeLog> selectGradeLogs(String paramTeacher, String paramClass, String paramSubject);
}
