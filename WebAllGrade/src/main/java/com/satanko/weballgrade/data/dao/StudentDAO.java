/************************************************************
 *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
 *                                                          *
 *  See the file LICENCE.txt for copying permission.        *
 ***********************************************************/
package com.satanko.weballgrade.data.dao;

import com.satanko.weballgrade.data.model.Student;
import java.util.ArrayList;
import java.util.List;

/**
 * This is an interface designed after the DAO principle.<br> It represents the <b>"AG_Student"</b> table in the database.
 * <p>Reference: <a href=http://en.wikipedia.org/wiki/Data_access_object>http://en.wikipedia.org/wiki/Data_access_object</a>
 * 
 * @author Philipp Sommersguter (philipp.sommersguter@satanko.at)
 * ATTENTION: If not otherwise specified, the author(s) of the class is/are also responsible for all methods.
 * 
 * @version 1.0
 * @see Student
 */
public interface StudentDAO {

    /**
     * <i>This method selects students from the table and returns them as a {@link List}.</i><p>
     * Only students that are in the given class are returned. 
     * 
     * @param paramClass A String representing a class
     * @return A list of {@link Student} objects
     * @see StudentDAO
     */
    public List<Student> selectStudentList(String paramClass);

    /**
     * <b>WRITTEN BY: <br>Julian Tropper (julian.tropper@gmail.com) <br>Philipp Sommersguter (philipp.sommersguter@satanko.at)</b><p>
     * <i>This method takes the given list of students and inserts them into the table.</i><p>
     * All students are also asociated with the given class.
     * 
     * @param students A list of Strings representing students
     * @param paramClass A String representing a class
     * @see StudentDAO
     */
    public void updateStudents(ArrayList<Student> students, String paramClass);
    
}
