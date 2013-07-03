/************************************************************
 *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
 *                                                          *
 *  See the file LICENCE.txt for copying permission.        *
 ***********************************************************/
package com.satanko.allgrade.view.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.satanko.allgrade.R;
import com.satanko.allgrade.data.C;
import com.satanko.allgrade.data.DatabaseOpenHelper;
import com.satanko.allgrade.data.GradelogEntry;
import com.satanko.allgrade.data.StudentTable;
import com.satanko.allgrade.service.EntryCacheManager;
import com.satanko.allgrade.service.EntryCacheManager.LocalEntryCacheManagerBinder;
import com.satanko.allgrade.task.CheckTask;
import com.satanko.allgrade.task.LoadStudentListTask;
import com.satanko.allgrade.task.LoadSubjectsForClassTask;
import com.satanko.allgrade.task.TaskException;
import com.satanko.allgrade.view.StudentListFragment;
import com.satanko.allgrade.view.dialog.DeleteGradelogEntryDialogFragment;
import com.satanko.allgrade.view.dialog.SubjectChooserDialogFragment;

/**
 * This is the main activity for Mobile AllGrade.<p>
 * The user will see this activity after the launch of the app.
 * 
 * @author Philipp Sommersguter (philipp.sommersguter@satanko.at)<p>
 * ATTENTION: If not otherwise specified, the author(s) of the class is/are also responsible for all methods.
 * @version 1.0
 */
public class MainActivity extends FragmentActivity {

	private final String tag = this.getClass().getSimpleName();
	
	//Access to preferences and editor of preferences.
	private SharedPreferences prefs;
	private SharedPreferences.Editor editor;
	
	/**
	 * A field providing access to the {@link EntryCacheManager}.
	 */
	private EntryCacheManager entryCacheManager;
	
	/**
	 * A field providing access to the database of the application.
	 */
	private SQLiteDatabase database;
	
	/**
	 * Indicates whether the {@link EntryCacheManager} is bound to this activity.
	 */
	private boolean ecmBound;
	
	/**
	 * A field holding the current class.
	 */
	private String activeClass;
	
	/**
	 * A field holding the active teacher.
	 */
	private String activeTeacher;
	
	/**
	 * A field holding the current subject.
	 */
	private String activeSubject;
	
	/**
	 * A field holding a instance of the {@link StudentListFragment}.
	 */
	private StudentListFragment studentListFragment;

	/** Defines callbacks for service binding, passed to bindService(). */
	private ServiceConnection entryCacheManagerConnection = new ServiceConnection() {
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			LocalEntryCacheManagerBinder binder = (LocalEntryCacheManagerBinder) service;
			entryCacheManager = binder.getService();
			ecmBound = true;
			Log.d(tag, "EntryCacheManager connected");
		}
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			ecmBound = false;
			Log.d(tag, "EntryCacheManager disconnected");
		}
	};
	
	/**
	 * This method is called when the {@link Activity} is started. It is responsible for the 
	 * initialization of the fields and all the content and views in order for the activtiy to
	 * work properly.<p>
	 * What exactly is done:<br>
	 * 1. Check if a proper Google account is on the device.
	 * 2. Initialize access to the preferences and the database.
	 * 3. Adds the {@link StudentListFragment} to the activity.
	 * 4. Binds the {@link EntryCacheManager} to the activity.
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	try{
    		new CheckTask(true)
				.checkGAEAccountAvailable(this.getApplicationContext());
    	}catch (TaskException te) {
			if(te.getToastErrorStringId()==R.string.error_no_account)
			{
	    		Toast.makeText( this.getApplicationContext(), 
						this.getResources().getString(te.getToastErrorStringId()),
						Toast.LENGTH_LONG)
					 .show();
	    		this.finish();
			}
		}
    	
    	setContentView(R.layout.activity_student_list);
    	
    	prefs = getSharedPreferences(C.prefs.FILE_NAME, MODE_PRIVATE);
    	editor = prefs.edit();
    	
		this.database = new DatabaseOpenHelper(
				this.getApplicationContext()).getWritableDatabase();
    	Uri data = getIntent().getData();
        if(data != null){
            this.processQrCode(data.toString());
        }
        
        studentListFragment = new StudentListFragment();
        getSupportFragmentManager().beginTransaction()
        	.add(R.id.activity_student_list_layout, studentListFragment, "StudentListFragment")
        .commit();
        
        Intent entryCacheManagerStartIntent = new Intent(this, EntryCacheManager.class);
        this.bindService(entryCacheManagerStartIntent, this.entryCacheManagerConnection, Context.BIND_AUTO_CREATE);
    }
    
    /**
	 * Lifecycle method
	 */
    @Override
	protected void onStart() {
    	try{
    		super.onStart();
    		this.activeClass   = prefs.getString(C.prefs.ACTIVE_CLASS, C.STRING_EMPTY);
    		this.activeTeacher = prefs.getString(C.prefs.ACTIVE_TEACHER, C.STRING_EMPTY);
    		this.activeSubject = prefs.getString(C.prefs.ACTIVE_SUBJECT, C.STRING_EMPTY);
    		
    		if(this.activeTeacher.equals("somphc08")||this.activeTeacher.equals("trojuc08"))
    		{
    			this.activeTeacher = C.TEST_teacher;
    			Log.i(tag, "Teacher was changed to test teacher -> "+C.TEST_teacher);
    		}
    		
    		new CheckTask(false)
    			.checkActiveClass(this.activeClass)
    			.checkActiveTeacher(this.activeTeacher);
    		boolean subjectsLoaded = new CheckTask(false)
    			.checkActiveSubject(activeSubject).getCheckVariable();
    		if(!subjectsLoaded){
	    		new LoadSubjectsForClassTask(this.getApplicationContext(), 
	    				this.database, this.getSupportFragmentManager(), 
	    				this.prefs.getBoolean(C.prefs.SHOW_DIALOG_SUBJECT, true))
	    			.execute(this.activeClass, this.activeTeacher);
	    		new LoadStudentListTask(this.getApplicationContext(), this.database, this.studentListFragment)
    				.execute(this.activeClass, this.activeTeacher);
    		}
    	}catch (TaskException te) {
    		if(te.getToastErrorStringId()!=C.integer.NO_TOAST)
    		{
    			Toast.makeText( this.getApplicationContext(), 
						this.getResources().getString(te.getToastErrorStringId()),
						Toast.LENGTH_SHORT)
						.show();
    		}
    		this.studentListFragment.setListAdapter(null);
    		this.studentListFragment.setEmptyText(this.getApplicationContext().getResources().getText(R.string.scan_code));
		}
	}
	
    /**
	 * Lifecycle method
	 */
    @Override
	protected void onDestroy() {
		this.entryCacheManager.startUpload();
    	GradeActivity.SHOW_COMMENT_SPINNER_INFO = true;
		this.editor.remove(C.prefs.ACTIVE_CLASS)
				.commit();
		if (this.ecmBound) {
            this.unbindService(this.entryCacheManagerConnection);
            this.ecmBound = false;
        }
		super.onDestroy();
	}

    /**
     * Creates the options menu... obviously.
     */
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return true;
    }

	/**
	 * Implements actions for the different selections of option items.
	 */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        //Called when menu 'Scan Code' is clicked.
        case R.id.menu_scan:
        	this.startScanIntent();
            return true;
        case R.id.menu_sync:
        	this.entryCacheManager.startUpload();
            Toast.makeText(this.getApplicationContext(), 
            		this.getResources().getString(R.string.toast_uploadStarted), Toast.LENGTH_SHORT).show();
            return true;
//        case R.id.menu_settings:
//            Toast.makeText(getApplicationContext(), R.string.notImplemented, Toast.LENGTH_SHORT).show();
//            return true;
//        case R.id.menu_undo:
//            Toast.makeText(getApplicationContext(), R.string.notImplemented, Toast.LENGTH_SHORT).show();
//            return true;
//        case R.id.menu_server:
//        	Toast.makeText(getApplicationContext(), R.string.notImplemented, Toast.LENGTH_SHORT).show();
//        	return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Fires the intent to scan a new QR-Code.<p>
     * If the intent fails, a dialog asking the user if he wants to install 
     * the <i>QR Code Scanner</i> by <i>ZXing Team</i> appears.
     * <br>
     * If accepted, the user will directly be redirected to the <b>Google Play Store</b> page.
     */
	private void startScanIntent() {
		this.editor.putString(C.prefs.ACTIVE_SUBJECT, C.STRING_EMPTY)
				.putString(C.prefs.ACTIVE_CLASS, C.STRING_EMPTY)
				.commit();
		Intent intent = new Intent(C.intent.ACTION_SCAN);
		intent.putExtra(C.intent.MODE_SCAN, C.intent.MODE_QR_CODE);
		try {
		    startActivityForResult(intent, C.integer.CODE_SCAN_REQUEST);
		} catch (ActivityNotFoundException e) {
			Dialog confirmScannerInstall = new AlertDialog.Builder(this)
	        .setTitle(getString(R.string.app_name))
	        .setMessage(R.string.dialog_barcodeNotInstalled)
	        .setPositiveButton(android.R.string.yes,
	                new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog,
	                    int whichButton) {
	                Uri uri = Uri.parse(C.SCANNER_MARKET_URI);
	                Intent installIntent = new Intent(
	                        Intent.ACTION_VIEW, uri);
	                startActivity(installIntent);
	            }
	        })
	        .setNeutralButton(android.R.string.no, new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int whichButton) {
	                return;
	            }
	        }).create();
	        confirmScannerInstall.show();
		}
	}

    /**
     * Processes the QR-Code passed to the method and reacts accordingly. The QR-Code has to follow a certain scheme in order
     * to get progressed: <i>http://allgrade.fanngrim.net/PARAM1/[PARAM2]/...</i>
     * @param qrCode The QR-Code which is processed by the method.
     */
    private void processQrCode(String qrCode)
    {
        String classIdentifier;
        Uri codeUri = Uri.parse(qrCode);
        
        try{
            if(codeUri.getHost().equals(C.weballgrade.ALLGRADE_HOSTNAME) && codeUri.getPathSegments().size() >= 1){
                List<String> params = codeUri.getPathSegments();
                //in case we need more params, this needs to be edited!
                classIdentifier = params.get(0).toLowerCase();
                this.editor.putString(C.prefs.ACTIVE_CLASS, classIdentifier).commit();
            }
            else
            {
                Toast.makeText(getApplicationContext(), R.string.toast_invalidQrCode, Toast.LENGTH_LONG).show();
                this.editor.putString(C.prefs.ACTIVE_CLASS, C.STRING_EMPTY).commit();
            }
        } catch(NullPointerException e) {
            Toast.makeText(getApplicationContext(), R.string.toast_invalidQrCode, Toast.LENGTH_LONG).show();
            this.editor.putString(C.prefs.ACTIVE_CLASS, C.STRING_EMPTY).commit();
        }
    }

    /**
     * If <code>requestCode</code> is set to <code>CODE_SCAN_REQUEST</code>, the method {@link #handleQrCode} is called.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	switch(requestCode)
        {
        case C.integer.CODE_SCAN_REQUEST:
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra(C.intent.EXTRA_SCAN_RESULT);
                this.processQrCode(contents);
            } else if (resultCode == RESULT_CANCELED) {
            	// Handle cancel = do nothing
            }
            break;
        case C.integer.CODE_GRADE_REQUEST:
            if(resultCode == RESULT_OK) {
            	//HANDLE CODE WHEN GRADE ACTIVITY IS COMPLETED
            	Bundle data = intent.getExtras();
            	data.putString(C.intent.bundle.TEACHER, activeTeacher);
            	//change student name to student token
            	Cursor cursor = database.query(StudentTable.TABLE_NAME, StudentTable.PROJECTION, 
            			StudentTable.FIELD_NAME+" = '"+data.getString(C.intent.bundle.STUDENTNAME)+"'", null, null, null, null);
            	cursor.moveToFirst();
            	String studentToken = cursor.getString(cursor.getColumnIndex(StudentTable.FIELD_TOKEN));
            	cursor.close();
            	data.putString(C.intent.bundle.STUDENTNAME, studentToken);
            	//end of change
            	GradelogEntry le = new GradelogEntry(data);
            	this.entryCacheManager.addEntry(le);
            }else if (resultCode == RESULT_CANCELED){
            	// Handle cancel = do nothing
            }
            break;
        }

    }

    /**
     * Handles the item click on the {@link SubjectChooserDialogFragment}.
     * @param item The item that has been clicked
     */
	public void onSubjectChooserDialogItemClick(String item) {
		this.editor.putString(C.prefs.ACTIVE_SUBJECT, item)
				.putBoolean(C.prefs.SHOW_DIALOG_SUBJECT, true)
				.commit();
		this.studentListFragment.setListShown(true);
	}

	/**
	 * Fires a {@link Intent} to start the {@link GradeActivity}
	 * @param studentListItem
	 */
	public void startGradeActivity(String studentListItem) {
		Intent intent = new Intent(this.getApplicationContext(), GradeActivity.class);
        intent.putExtra(C.intent.bundle.STUDENTNAME, studentListItem);
        startActivityForResult(intent, C.integer.CODE_GRADE_REQUEST);
	}
	
	/**
	 * Displays a dialog to delete {@link GradelogEntry} objects associated with a student.
	 * @param studentName The name of the student
	 */
	public void showDeleteDialog(String studentName) {
		Cursor c = this.database.query(StudentTable.TABLE_NAME, 
				StudentTable.PROJECTION, StudentTable.FIELD_NAME + " = '"+studentName+"'",
				null, null, null, null);
		c.moveToFirst();
		ArrayList<GradelogEntry> deletePossibilities = this.entryCacheManager.getEntriesWhereStudentToken(
				c.getString(c.getColumnIndex(StudentTable.FIELD_TOKEN)).toLowerCase());
		c.close();
		DeleteGradelogEntryDialogFragment deleteLogEntryDialog = DeleteGradelogEntryDialogFragment.newInstance();
		deleteLogEntryDialog.setArrayList(deletePossibilities);
		String rawHeading = this.getApplicationContext().getResources().getString(R.string.dialog_deleteLogEntry_heading);
		String heading = String.format(rawHeading, studentName);
		deleteLogEntryDialog.setTitle(heading);
		deleteLogEntryDialog.show(getSupportFragmentManager(), "DeleteLogEntryDialog");
	}

	/**
	 * Forwards the item to delete to the {@link EntryCacheManager}.
	 * @param selectedLogEntry The {@link GradelogEntry} to remove
	 */
	public void deleteLogFromEntryList(GradelogEntry selectedLogEntry) {
		this.entryCacheManager.removeEntry(selectedLogEntry);
	}

}
