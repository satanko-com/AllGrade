/************************************************************
 *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
 *                                                          *
 *  See the file LICENCE.txt for copying permission.        *
 ***********************************************************/
package com.satanko.allgrade.data;

/**
 * This class is a collection of statements and fields for the "SubjectInClass" table in the database.
 * <p>
 * The class was created to provide a simple and fast use of the field names and often used statements 
 * for the table which also results in avoiding mistakes such as typos etc.
 * @author Philipp Sommersguter (philipp.sommersguter@satanko.at)<p>
 * ATTENTION: If not otherwise specified, the author(s) of the class is/are also responsible for all methods.
 * @version 1.0
 */
public class SubjectInClassTable {

	
	public static final String TABLE_NAME = "SubjectInClass";
	
	/**
	 * Represents the <code>_id</code> field for the <code>SubjectInClassTable</code> class in the database.
	 * @see SubjectInClassTable
	 */
	public static final String FIELD_ID = "_id";
	
	/**
	 * Represents the <code>name</code> field for the <code>SubjectInClassTable</code> class in the database.
	 * @see SubjectInClassTable
	 */
	public static final String FIELD_TEACHER = "teacherToken";
	
	/**
	 * Represents the <code>classCode</code> field for the <code>SubjectInClassTable</code> class in the database.
	 * @see SubjectInClassTable
	 */
	public static final String FIELD_CLASS = "classToken";
	
	/**
	 * Represents the <code>subjectToken</code> field for the <code>SubjectInClassTable</code> class in the database.
	 * @see SubjectInClassTable
	 */
	public static final String FIELD_SUBJECT = "subjectToken";
	
	/**
	 * Represents a <code>String Array</code> projection of all the fields of the <code>SubjectInClassTable</code> class in the database.
	 * @see SubjectInClassTable
	 */
	public static final String[] PROJECTION = new String[] { FIELD_ID, FIELD_TEACHER, FIELD_CLASS, FIELD_SUBJECT};
	
	/**
	 * Represents the <code>SQL-CREATE</code> Statement for the <code>SubjectInClassTable</code> class in the database.
	 * @see SubjectInClassTable
	 */
	public static final String CREATE = 
		"CREATE TABLE " + TABLE_NAME + "("
		+ FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," 
		+ FIELD_TEACHER + " VARCHAR(3) NOT NULL,"
		+ FIELD_CLASS + " VARCHAR(8) NOT NULL,"
		+ FIELD_SUBJECT +" VARCHAR(5) NOT NULL"
		+ ")";
	
	/**
	 * Represents the <code>SQL-DROP</code> Statement for the <code>SubjectInClassTable</code> class in the database.
	 * @see SubjectInClassTable
	 */
	public static final String DROP = 
		"DROP TABLE IF EXISTS " + TABLE_NAME;
	
}
