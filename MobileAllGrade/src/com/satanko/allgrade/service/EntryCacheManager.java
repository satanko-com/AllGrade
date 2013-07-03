/************************************************************
 *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
 *                                                          *
 *  See the file LICENCE.txt for copying permission.        *
 ***********************************************************/
package com.satanko.allgrade.service;

import java.util.ArrayList;

import com.satanko.allgrade.data.C;
import com.satanko.allgrade.data.GradelogEntry;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * This service class serves as a controller for the cached {@link GradelogEntry} objects.
 * @author Philipp Sommersguter (philipp.sommersguter@satanko.at)<p>
 * ATTENTION: If not otherwise specified, the author(s) of the class is/are also responsible for all methods.
 * @version 1.0
 */
public class EntryCacheManager extends Service{
	//DO NOT MESS WITH THE SERVICE BINDING UNLESS YOU KNOW WHAT YOU ARE DOING!!!
	
	private final String tag = this.getClass().getSimpleName();
	/**
	 * The list where all {@link GradelogEntry}s are saved.
	 */
	private ArrayList<GradelogEntry> entryList;
	
	// Class has to run in its own thread therefore the binding...
	private final IBinder mBinder = new LocalEntryCacheManagerBinder();
	
	public class LocalEntryCacheManagerBinder extends Binder{
		public EntryCacheManager getService()
		{
			// Return this instance of EntryCacheManager so clients can call public methods
			return EntryCacheManager.this;
		}
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
	
	@Override
	public void onCreate() {
		entryList = new ArrayList<GradelogEntry>();
		super.onCreate();
	}
	
	/**
	 * Adds the {@link GradelogEntry} to the list.
	 * @param ge The entry to add
	 */
	public void addEntry(GradelogEntry ge)
	{
		this.entryList.add(ge);
	}
	
	/**
	 * Removes the {@link GradelogEntry} to the list.
	 * @param ge The entry to remove
	 */
	public void removeEntry(GradelogEntry ge)
	{
		this.entryList.remove(ge);
	}
	
	/**
	 * The method runs through the entry list and converts them into {@link GradelogUploadService} objects.
	 */
	public void startUpload()
	{
//		this.printList();
		for(int i=0; i<this.entryList.size(); i++)
		{
			Intent uploadEntryIntent = new Intent(this.getApplicationContext(), GradelogUploadService.class);
			uploadEntryIntent.putExtra(C.intent.EXTRA_JSONSTRING, this.entryList.get(i).getAsJSONObject().toString());
			this.startService(uploadEntryIntent);
		}
		this.entryList.clear();
	}

	/**
	 * Prints the list of GradeLogEntries... obviously.
	 */
	public void printList() {
		for(int i=0; i<this.entryList.size(); i++)
		{
			Log.d(tag, this.entryList.get(i).toString());
		}
	}

	/**
	 * Searches the list for entries with matching names and returns them.
	 * @param token The String token of the student.
	 * @return An {@link ArrayList} of entries that matched the token.
	 */
	public ArrayList<GradelogEntry> getEntriesWhereStudentToken(String token) {
		ArrayList<GradelogEntry> matching = new ArrayList<GradelogEntry>();
		for(int i=0; i<this.entryList.size(); i++)
		{
			GradelogEntry currentEntry = this.entryList.get(i);
			if(currentEntry.getStudentName().toLowerCase().equals(token))
			{
				matching.add(currentEntry);
			}
		}
		return matching;
	}
	
}
