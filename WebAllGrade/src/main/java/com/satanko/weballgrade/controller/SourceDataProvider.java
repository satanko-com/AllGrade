/************************************************************
 *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
 *                                                          *
 *  See the file LICENCE.txt for copying permission.        *
 ***********************************************************/
package com.satanko.weballgrade.controller;

import com.satanko.weballgrade.data.model.Student;
import com.satanko.weballgrade.data.model.Teacher;
import java.util.ArrayList;
import javax.naming.NamingException;

/**
 * This is an interface providing access to any underlayling implementation to get all
 * necessary data needed to fill the database.
 * 
 * @author Philipp Sommersguter (philipp.sommersguter@satanko.at)
 * ATTENTION: If not otherwise specified, the author(s) of the class is/are also responsible for all methods.
 * 
 * @version 1.0
 */
public interface SourceDataProvider {
    
    /**
     * This method returns an {@code ArrayList} of {@link Class}es from the datasource.
     * 
     * @return An {@code ArrayList<String>} of {@link Class}es or null if an Exception occured
     * @throws NamingException 
     */
    public ArrayList<String> getAllClasses();
    
    /**
     * This method returns an {@code ArrayList} of {@link Student}s from the specified {@link Class} from the datasource .
     * 
     * @param paramClass The class to search in
     * @return An {@code ArrayList<String>} of {@link Student}s or null if an Exception occured
     * @throws NamingException 
     */
    public ArrayList<Student> getAllStudents(String paramClass);
    
    /**
     * This method returns an {@code ArrayList} of {@link Student}s from the specified {@link Class} from the datasource .
     * <br><i>WARNING</i>: Only use this method if {@link #getAllStudents(java.lang.String) } returned an empty list since it can be inaccurate!
     * 
     * @param paramClass The class to search in
     * @return An {@code ArrayList<String>} of {@link Student}s or null if an Exception occured
     * @throws NamingException 
     */
    public ArrayList<Student> getAllStudentsV2(String paramClass);
       
    /**
     * This method returns an {@code ArrayList} of {@link Teacher}s from the datasource .
     *
     * @return An {@code ArrayList<String>} of {@link Teacher}s or null if an Exception occured
     * @throws NamingException 
     */
    public ArrayList<String> getAllTeachers();
}
