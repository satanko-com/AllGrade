/************************************************************
 *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
 *                                                          *
 *  See the file LICENCE.txt for copying permission.        *
 ***********************************************************/
package com.satanko.allgrade.task;

import java.util.ArrayList;

import com.satanko.allgrade.R;
import com.satanko.allgrade.data.C;
import com.satanko.allgrade.data.StudentTable;
import com.satanko.allgrade.view.StudentListFragment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

/**
 * The class loads the student list for the application.
 * 
 * @author Philipp Sommersguter (philipp.sommersguter@satanko.at)<p>
 * ATTENTION: If not otherwise specified, the author(s) of the class is/are also responsible for all methods.
 * @version 1.0
 */
public class LoadStudentListTask extends AllGradeTask<String> {

	private final String tag = this.getClass().getSimpleName();
	
	private final SQLiteDatabase database;
	private final ListFragment listFragment;
	private final Context uiContext;
	
	public LoadStudentListTask(Context context, SQLiteDatabase database,
			StudentListFragment studentListFragment) {
		this.uiContext = context;
		this.database = database;
		this.listFragment = studentListFragment;
	}
	
	/**
	 * This method tries to query the database for students.
	 * If the query is not successful, null is returned.
	 * @param activeClass A String representing the class
	 * @return A {@link ArrayList} with the list of students or null
	 */
	private ArrayList<String> tryDatabaseQuery(final String activeClass)
	{
		ArrayList<String> list = new ArrayList<String>();
		Cursor c = this.database.query(StudentTable.TABLE_NAME,
				StudentTable.PROJECTION, StudentTable.FIELD_CLASS+" = '"+activeClass+"'", 
				null, null, null, StudentTable.FIELD_NAME);
		if(c.getCount()>0)
		{
			int nameIndex = c.getColumnIndex(StudentTable.FIELD_NAME);
			c.moveToFirst();
			while(!c.isAfterLast())
			{
				list.add(c.getString(nameIndex));
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
	 */
	private void saveInDatabase(ArrayList<String> list, String activeClass) {
		for(int i=0; i<list.size();i++)
		{
			Log.d(tag, "item to save in db: "+list.get(i));
			String[] parts = list.get(i).split(C.STRING_SPLIT);
			ContentValues values = new ContentValues();
			values.put(StudentTable.FIELD_TOKEN, parts[0]);
			values.put(StudentTable.FIELD_NAME, parts[1]);
			values.put(StudentTable.FIELD_CLASS, activeClass);
			this.database.insert(StudentTable.TABLE_NAME, null, values);
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
			ArrayList<String> studentList = tryDatabaseQuery(activeClass);
			if(studentList == null)
			{
				new CheckTask(false).checkNetworkConnection(this.uiContext);
				studentList = new GetDataFromServerTask()
					.execute(C.weballgrade.RESOURCE_STUDENTS, activeClass, activeTeacher)
					.get();
				if(studentList!=null)
				{
					this.saveInDatabase(studentList, activeClass);
					for(int i=0; i < studentList.size(); i++)
					{
						String[] parts = studentList.get(i).split(C.STRING_SPLIT);
						studentList.set(i, parts[1]);
					}
				}else{
					throw new TaskException(tag, "could not load studentlist", R.string.error_no_account);
				}
				
			}
			
			ListAdapter adapter = new ArrayAdapter<String>(
					this.listFragment.getActivity().getApplicationContext(),
	        		R.layout.student_list_item, studentList);
			this.listFragment.setListAdapter(adapter);
			this.listFragment.setListShown(false);
		}catch(TaskException te)
		{
			throw te;
		}catch(Exception e)
		{
			Log.e(tag, "Exception occured in doWork()");
			e.printStackTrace();
		};
	}
	
}
