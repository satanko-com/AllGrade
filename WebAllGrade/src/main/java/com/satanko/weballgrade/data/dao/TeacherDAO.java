/************************************************************
 *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
 *                                                          *
 *  See the file LICENCE.txt for copying permission.        *
 ***********************************************************/
package com.satanko.weballgrade.data.dao;

import java.util.ArrayList;

/**
 * This is an interface designed after the DAO principle.<br> It represents the <b>"AG_Teacher"</b> table in the database.
 * <p>Reference: <a href=http://en.wikipedia.org/wiki/Data_access_object>http://en.wikipedia.org/wiki/Data_access_object</a>
 * 
 * @author Philipp Sommersguter (philipp.sommersguter@satanko.at)
 * ATTENTION: If not otherwise specified, the author(s) of the class is/are also responsible for all methods.
 * 
 * @version 1.0
 * @see Teacher
 */
public interface TeacherDAO {

    /**
     * <b>WRITTEN BY: <br>Julian Tropper (julian.tropper@gmail.com) <br>Philipp Sommersguter (philipp.sommersguter@satanko.at)</b><p>
     * <i>This method takes the given list of teachers and inserts them into the table.</i>
     * 
     * @param teachers A list of Strings representing teachers
     * @see TeacherDAO
     */
    public void updateTeachers(ArrayList<String> teachers);
    
}
