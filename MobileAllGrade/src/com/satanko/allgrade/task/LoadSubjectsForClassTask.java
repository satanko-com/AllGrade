/************************************************************
 *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
 *                                                          *
 *  See the file LICENCE.txt for copying permission.        *
 ***********************************************************/
package com.satanko.allgrade.task;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.satanko.allgrade.R;
import com.satanko.allgrade.data.C;
import com.satanko.allgrade.data.SubjectInClassTable;
import com.satanko.allgrade.view.dialog.SubjectChooserDialogFragment;

/**
 * The class loads the subject list for the application.
 * 
 * @author Philipp Sommersguter (philipp.sommersguter@satanko.at)<p>
 * ATTENTION: If not otherwise specified, the author(s) of the class is/are also responsible for all methods.
 * @version 1.0
 */
public class LoadSubjectsForClassTask extends AllGradeTask<String>{

	private final String tag = this.getClass().getSimpleName();
	
	private final SQLiteDatabase database;
	private final FragmentManager uiFragmentManager;
	private final Context uiContext;
	
	private boolean showDialog;
	
	public LoadSubjectsForClassTask(Context context, SQLiteDatabase database, 
			FragmentManager fragmentManager, boolean showDialog) {
		this.uiContext = context;
		this.database = database;
		this.uiFragmentManager = fragmentManager;
		this.showDialog = showDialog;
	}
	
	/**
	 * This method tries to query the database for subjects.
	 * If the query is not successful, null is returned.
	 * @param activeClass A String representing the class
	 * @param activeTeacher A String representing the teacher
	 * @return A {@link ArrayList} with the list of objects or null
	 */
	private ArrayList<String> tryDatabaseQuery(final String activeClass, final String activeTeacher)
	{
		ArrayList<String> list = new ArrayList<String>();
		Cursor c = this.database.query(SubjectInClassTable.TABLE_NAME,
				new String[] {SubjectInClassTable.FIELD_SUBJECT},
				SubjectInClassTable.FIELD_CLASS.concat("= '"+activeClass+"' AND ").concat(
				SubjectInClassTable.FIELD_TEACHER).concat("= '"+activeTeacher+"'"), null, 
				null, null, SubjectInClassTable.FIELD_SUBJECT );
		if(c.getCount()>0)
		{
			int subjectColumnIndex = c.getColumnIndex(SubjectInClassTable.FIELD_SUBJECT);
			c.moveToFirst();
			while(!c.isAfterLast())
			{
				list.add(c.getString(subjectColumnIndex));
				c.moveToNext();
			}
			c.close();
			Log.d(tag, "successfully selected from database");
			return list;
		}else
		{
			c.close();
			return null;
		}
	}
	
	/**
	 * Saves the list to the database.
	 * @param list An {@link ArrayList} holding the subjects.
	 * @param activeClass A String representing the class
	 * @param activeTeacher A String representing the teacher
	 */
	private void saveInDatabase(ArrayList<String> list,
			String activeClass, String activeTeacher) {
		for(int i=0; i<list.size(); i++)
		{
			ContentValues values = new ContentValues();
			values.put(SubjectInClassTable.FIELD_SUBJECT, list.get(i));
			values.put(SubjectInClassTable.FIELD_CLASS, activeClass);
			values.put(SubjectInClassTable.FIELD_TEACHER, activeTeacher);
			this.database.insert(SubjectInClassTable.TABLE_NAME, null, values);
		}
		
	}

	/**
	 * 1. Tries database query for the data.<br>
	 * 2. When the query is not successful, the data is requested from the server
	 * and saves it into the database<br>
	 * 3. Displays the data in the list fragment.
	 */
	@Override
	void doWork(String... strings) throws TaskException {
		try{
			String activeClass = strings[0];
			String activeTeacher = strings[1];
			ArrayList<String> subjectList = tryDatabaseQuery(activeClass, activeTeacher);
			if(subjectList == null)
			{
				new CheckTask(false).checkNetworkConnection(this.uiContext);
				subjectList = new GetDataFromServerTask()
					.execute(C.weballgrade.RESOURCE_LESSONS, activeClass, activeTeacher)
					.get();
				if(subjectList!=null)
				{
					if(subjectList.isEmpty())
					{
						throw new TaskException(tag, 
								"Received empty Subject List", R.string.toast_emptySubjectList);
					}else if(subjectList.get(0).equals("conflict409"))
					{
						
						throw new TaskException(tag, "Conflict 409: Teacher is not teaching class", R.string.toast_teacherNotTeachingClass);
					}
					this.saveInDatabase(subjectList, activeClass, activeTeacher);
					
				}else{
					throw new TaskException(tag, "could not load subjectlist", R.string.error_no_account);
				}
				
			}
			if(this.showDialog)
			{
				this.uiContext.getSharedPreferences(C.prefs.FILE_NAME, Activity.MODE_PRIVATE).edit()
				.putBoolean(C.prefs.SHOW_DIALOG_SUBJECT, false).commit();
				DialogFragment subjectChooser = SubjectChooserDialogFragment.newInstance(subjectList);
				subjectChooser.setCancelable(false);
				subjectChooser.show(this.uiFragmentManager, "SubjectChooserDialog");
			}
		}catch(TaskException te)
		{
			throw te;
		}catch(Exception e)
		{
			Log.e(tag, "Exception occured in doWork()");
			e.printStackTrace();
		}
	}

}
