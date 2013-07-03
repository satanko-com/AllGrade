/************************************************************
 *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
 *                                                          *
 *  See the file LICENCE.txt for copying permission.        *
 ***********************************************************/
package com.satanko.allgrade.data;

/**
 * This class is a collection of statements and fields for the "Student" table in the database.
 * <p>
 * The class was created to provide a simple and fast use of the field names and often used statements 
 * for the table which also results in avoiding mistakes such as typos etc.
 * @author Philipp Sommersguter (philipp.sommersguter@satanko.at)<p>
 * ATTENTION: If not otherwise specified, the author(s) of the class is/are also responsible for all methods.
 * @version 1.0
 */
public class StudentTable {
	
	/**
	 * Represents the table name for the <code>StudentTable</code> class in the database.
	 * @see StudentTable
	 */
	public static final String TABLE_NAME = "Student";
	
	/**
	 * Represents the <code>_id</code> field for the <code>StudentTable</code> class in the database.
	 * @see StudentTable
	 */
	public static final String FIELD_ID = "_id";
	
	/**
	 * Represents the <code>name</code> field for the <code>StudentTable</code> class in the database.
	 * @see StudentTable
	 */
	public static final String FIELD_NAME = "name";
	
	/**
	 * Represents the <code>classToken</code> field for the <code>StudentTable</code> class in the database.
	 * @see StudentTable
	 */
	public static final String FIELD_CLASS = "classToken";
	
	/**
	 * Represents the <code>studentToken</code> field for the <code>StudentTable</code> class in the database.
	 * @see StudentTable
	 */
	public static final String FIELD_TOKEN = "studentToken";
	
	/**
	 * Represents a <code>String Array</code> projection of all the fields of the <code>StudentTable</code> class in the database.
	 * @see StudentTable
	 */
	public static final String[] PROJECTION = new String[] { FIELD_ID, FIELD_NAME, FIELD_CLASS, FIELD_TOKEN};
	
	/**
	 * Represents the <code>SQL-CREATE</code> Statement for the <code>StudentTable</code> class in the database.
	 * @see StudentTable
	 */
	public static final String CREATE = 
		"CREATE TABLE " + TABLE_NAME + " ("
		+ FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
		+ FIELD_NAME + " VARCHAR(50) NOT NULL, "
		+ FIELD_CLASS + " VARCHAR(7) NOT NULL, "
		+ FIELD_TOKEN + " VARCHAR(8) NOT NULL "
		+ ")";
	
	/**
	 * Represents the <code>SQL-DROP</code> Statement for the <code>StudentTable</code> class in the database.
	 * @see StudentTable
	 */
	public static final String DROP = 
		"DROP TABLE IF EXISTS " + TABLE_NAME;
}
