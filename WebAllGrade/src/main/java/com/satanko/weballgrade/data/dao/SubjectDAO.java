/************************************************************
 *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
 *                                                          *
 *  See the file LICENCE.txt for copying permission.        *
 ***********************************************************/
package com.satanko.weballgrade.data.dao;

import com.satanko.weballgrade.data.model.Subject;
import java.util.List;

/**
 * This is an interface designed after the DAO principle.<br> It represents the <b>"AG_Subject"</b> table in the database.
 * <p>Reference: <a href=http://en.wikipedia.org/wiki/Data_access_object>http://en.wikipedia.org/wiki/Data_access_object</a>
 * 
 * @author Julian Tropper (julian.tropper@gmail.com)
 * ATTENTION: If not otherwise specified, the author(s) of the class is/are also responsible for all methods.
 * 
 * @version 1.0
 * @see Subject
 */
public interface SubjectDAO {

    /**
     * <i>This method selects <b>all</b> subjects from the table and returns them as a {@link List}.</i>
     * 
     * @return A list of {@link Subject} objects
     * @see SubjectDAO
     */
    public List<Subject> getSubjectList();
    
}
