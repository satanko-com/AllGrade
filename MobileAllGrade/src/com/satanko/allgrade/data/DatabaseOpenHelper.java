/************************************************************
 *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
 *                                                          *
 *  See the file LICENCE.txt for copying permission.        *
 ***********************************************************/
package com.satanko.allgrade.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This class is responsible for creating the database and returning a functioning database object.
 * <p>
 * To get a {@linkplain SQLiteDatabase} object for interaction with the database please use the {@link #getWritableDatabase()} method.
 * 
 * @see {@link #onCreate(SQLiteDatabase)}
 * @see {@link #onUpgrade(SQLiteDatabase)}
 * @author Philipp Sommersguter (philipp.sommersguter@satanko.at)<p>
 * ATTENTION: If not otherwise specified, the author(s) of the class is/are also responsible for all methods.
 * @version 1.0
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper {

	/**
	 * Represents the name of the SQLite file in which the database will be saved on the Android device.
	 * @see DatabaseOpenHelper
	 */
	private static final String DB_NAME = "database.db";
	
	/**
	 * Represents the version of the {@link DatabaseOpenHelper} on the Android device.
	 * @see DatabaseOpenHelper
	 */
	private static final int DB_VERSION = 1;
	
	/**
	 * This is the constructor of the {@linkplain DatabaseOpenHelper} class.<br>
	 * It uses the <code>super</code> method to create a database by using the given
	 *  <code>context, DB_NAME</code> and <code>DB_VERSION</code> variables of {@linkplain DatabaseOpenHelper}
	 * @param {@linkplain Context}
	 * @see DatabaseOpenHelper
	 */
	public DatabaseOpenHelper(Context context){
		super(context, DB_NAME, null, DB_VERSION);
	}
	
	/**
	 * Creates tables within the database by using their given <code>CREATE</code> statements.<p>
	 * <b>The created Tables are:<b><br>
	 * {@link GradelogTable}
	 * <p>
	 * @see DatabaseOpenHelper
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(GradelogTable.CREATE);
		db.execSQL(StudentTable.CREATE);
		db.execSQL(SubjectInClassTable.CREATE);
	}

	/**
	 * <b>(Basically a reset function for the database)</b> <p>
	 * Works opposingly to the {@link #onCreate(SQLiteDatabase)} method.<br>
	 * First it drops all the databases tables and then calls the {@link #onCreate(SQLiteDatabase)} method to create them again.
	 * 
	 * @see DatabaseOpenHelper
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(GradelogTable.DROP);
		db.execSQL(StudentTable.DROP);
		db.execSQL(SubjectInClassTable.DROP);
		this.onCreate(db);
	}

}
