/************************************************************
 *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
 *                                                          *
 *  See the file LICENCE.txt for copying permission.        *
 ***********************************************************/
package com.satanko.weballgrade.data.dao;

import com.satanko.weballgrade.data.model.JsonLesson;
import com.satanko.weballgrade.data.model.Lesson;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.WebApplicationException;

/**
 * This is an interface designed after the DAO principle.<br> It represents the <b>"AG_Lesson"</b> table in the database.
 * <p>Reference: <a href=http://en.wikipedia.org/wiki/Data_access_object>http://en.wikipedia.org/wiki/Data_access_object</a>
 * 
 * @author Philipp Sommersguter (philipp.sommersguter@satanko.at)
 * ATTENTION: If not otherwise specified, the author(s) of the class is/are also responsible for all methods.
 * 
 * @version 1.0
 * @see Lesson
 */
public interface LessonDAO {

    /**
     * <b>WRITTEN BY: <br>Julian Tropper (julian.tropper@gmail.com) <br>Philipp Sommersguter (philipp.sommersguter@satanko.at)</b><p>
     * <i>This method selects lessons from the table and returns them as a {@link List}.</i>
     * 
     * @param paramTeacher A String representing a teacher
     * @param paramClass A String representing a class
     * @return A list of {@link Lesson} objects
     * @see LessonDAO
     */
    public List<Lesson> selectLessonList(String paramTeacher, String paramClass);
    
    /**
     * <b>WRITTEN BY: Julian Tropper (julian.tropper@gmail.com)</b><p>
     * <i> This method updates the lessons by inserting new lessons and deleting old lessons.
     * 
     * @param oldLessons  A {@link ArrayList} of {@link JsonLesson}s containing all lessons as they are in the database now
     * @param newLessons A {@link ArrayList} of {@link JsonLesson}s representing all lessons as they should be after updating
     * @throws WebApplicationException DESCRIPTION
     * @see LessonDAO
     */
    public void createOrUpdateLessons(ArrayList<JsonLesson> oldLessons, ArrayList<JsonLesson> newLessons) throws WebApplicationException;
}