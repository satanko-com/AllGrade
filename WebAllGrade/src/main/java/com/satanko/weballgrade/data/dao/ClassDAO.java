/************************************************************
 *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
 *                                                          *
 *  See the file LICENCE.txt for copying permission.        *
 ***********************************************************/
package com.satanko.weballgrade.data.dao;

import com.satanko.weballgrade.data.model.Clazz;
import java.util.ArrayList;
import java.util.List;

/**
 * This is an interface designed after the DAO principle.<br> It represents the <b>"AG_Class"</b> table in the database.
 * <p>Reference: <a href=http://en.wikipedia.org/wiki/Data_access_object>http://en.wikipedia.org/wiki/Data_access_object</a>
 * 
 * @author Philipp Sommersguter (philipp.sommersguter@satanko.at)
 * ATTENTION: If not otherwise specified, the author(s) of the class is/are also responsible for all methods.
 * 
 * @version 1.0
 * @see Clazz
 */
public interface ClassDAO {

    /**
     * <b>WRITTEN BY: <br>Julian Tropper (julian.tropper@gmail.com) <br>Philipp Sommersguter (philipp.sommersguter@satanko.at)</b><p>
     * <i>This method takes the given list of classes and inserts them into the table.</i>
     * 
     * @param classes A list of Strings representing classes
     * @see ClassDAO
     */
    public void updateAllClasses(ArrayList<String> classes);
    
    /**
     * <i>This method selects <b>all</b> classes from the table and returns them as a {@link List}.</i>
     * 
     * @return A list of {@link Clazz} objects
     * @see ClassDAO
     */
    public List<Clazz> selectClassList();
    
}
