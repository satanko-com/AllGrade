/************************************************************
 *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
 *                                                          *
 *  See the file LICENCE.txt for copying permission.        *
 ***********************************************************/
package com.satanko.allgrade.data;

/**
 * This class is a collection of statements and fields for the "Gradelog" table in the database.
 * <p>
 * The class was created to provide a simple and fast use of the field names and often used statements 
 * for the table which also results in avoiding mistakes such as typos etc.
 * @author Philipp Sommersguter (philipp.sommersguter@satanko.at)<p>
 * ATTENTION: If not otherwise specified, the author(s) of the class is/are also responsible for all methods.
 * @version 1.0
 */
public class GradelogTable {

	
	/**
	 * Represents the table name for the <code>GradelogTable</code> class in the database.
	 * @see GradelogTable
	 */
	public static final String TABLE_NAME = "Gradelog";

	/**
	 * Represents the <code>_id</code> field for the <code>GradelogTable</code> class in the database.
	 * @see GradelogTable
	 */
	public static final String FIELD_ID = "_id";
	
	/**
	 * Represents the <code>timestamp</code> field for the <code>GradelogTable</code> class in the database.
	 * @see GradelogTable
	 */
	public static final String FIELD_TIMESTAMP = "timestamp";
	
	/**
	 * Represents the <code>classCode</code> field for the <code>GradelogTable</code> class in the database.
	 * @see GradelogTable
	 */
	public static final String FIELD_SUBJECTINCLASS = "subjectinclass";
	
	/**
	 * Represents the <code>student</code> field for the <code>GradelogTable</code> class in the database.
	 * @see GradelogTable
	 */
	public static final String FIELD_STUDENT = "student";
	
	/**
	 * Represents the <code>grading</code> field for the <code>GradelogTable</code> class in the database.
	 * @see GradelogTable
	 */
	public static final String FIELD_GRADING = "grading";
	
	/**
	 * Represents the <code>reason</code> field for the <code>GradelogTable</code> class in the database.
	 * @see GradelogTable
	 */
	public static final String FIELD_REASON = "reason";

	/**
	 * Represents a <code>String Array</code> projection of all the fields of the <code>GradelogTable</code> class in the database.
	 * @see GradelogTable
	 */
	public static final String[] PROJECTION = new String[] { FIELD_ID, FIELD_TIMESTAMP, FIELD_SUBJECTINCLASS, FIELD_STUDENT, FIELD_GRADING, FIELD_REASON };

	/**
	 * Represents the <code>SQL-CREATE</code> Statement for the <code>GradelogTable</code> class in the database.
	 * @see GradelogTable
	 */
	public static final String CREATE = 
		"CREATE TABLE " + TABLE_NAME + "("
		+ FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," 
		+ FIELD_TIMESTAMP + " TIMESTAMP NOT NULL," 
		+ FIELD_SUBJECTINCLASS + " INTEGER NOT NULL,"
		+ FIELD_STUDENT + " INTEGER NOT NULL,"
		+ FIELD_GRADING + " INTEGER NOT NULL,"
		+ FIELD_REASON + " VARCHAR(150) NOT NULL"
		+ ")";

	/**
	 * Represents the <code>SQL-DROP</code> Statement for the <code>GradelogTable</code> class in the database.
	 * @see GradelogTable
	 */
	public static final String DROP = 
		"DROP TABLE IF EXISTS " + TABLE_NAME;
	
	/**
	 * Represents a almost finished <code>SELECT</code> statement for selecting an {@linkplain GradelogTable} entry by it's {@linkplain FIELD_ID}.
	 * <p>
	 * <b>Use it as following:</b><br>
	 * <code>String completeStatement = GradelogTable.SELECT_BY_ID + wantedID</code><p>
	 * <b>wantedID</b> represents the {@linkplain FIELD_ID} wanted by the user is an {@linkplain Integer}.
	 * @see GradelogTable
	 */
	public static final String SELECT_BY_ID =
		"SELECT * FROM "+ TABLE_NAME +" WHERE "+ FIELD_ID +" >= ";
}
