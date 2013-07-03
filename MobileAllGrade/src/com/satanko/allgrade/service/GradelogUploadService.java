/************************************************************
 *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
 *                                                          *
 *  See the file LICENCE.txt for copying permission.        *
 ***********************************************************/
package com.satanko.allgrade.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.satanko.allgrade.data.C;
import com.satanko.allgrade.data.GradelogEntry;
import com.satanko.allgrade.task.CheckTask;
import com.satanko.allgrade.task.SendDataToServerTask;
import com.satanko.allgrade.task.TaskException;

/**
 * This service class creates an {@link IntentService} for a single {@link GradelogEntry} to upload.
 * @author Philipp Sommersguter (philipp.sommersguter@satanko.at)<p>
 * ATTENTION: If not otherwise specified, the author(s) of the class is/are also responsible for all methods.
 * @version 1.0
 */
public class GradelogUploadService extends IntentService {

	private final String tag = this.getClass().getSimpleName();
	
	/**
	 * The constructor is required and must call the super {@link IntentService#IntentService(java.lang.String)}
	 * constructor with a name for the worker thread.
	 * @param name The name of the worker thread.
	 */
	public GradelogUploadService() {
		super("GradelogUploadService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		//ToDo in this method:
		//1. Check for network connection
		//2. Execute upload task
		//3. Go buy a delicious pudding and eat it like there is no tomorrow!
		try{
			new CheckTask().checkNetworkConnection(this.getApplicationContext());
			new SendDataToServerTask().execute(C.weballgrade.RESOURCE_GRADELOG, intent.getStringExtra(C.intent.EXTRA_JSONSTRING));
		}catch(TaskException te)
		{
			Log.i(tag, "There was no network connection. New attempt will be started in "+C.integer.UPLOAD_ATTEMPT_SECONDS+" seconds");
			android.os.SystemClock.sleep(C.integer.UPLOAD_ATTEMPT_SECONDS*1000);
			this.startService(intent);
		}
	}
	
}
