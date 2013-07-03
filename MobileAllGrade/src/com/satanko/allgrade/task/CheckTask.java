/************************************************************
 *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
 *                                                          *
 *  See the file LICENCE.txt for copying permission.        *
 ***********************************************************/
package com.satanko.allgrade.task;

import com.satanko.allgrade.R;
import com.satanko.allgrade.data.C;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * This class checks many things for availability and/or correctness.<p>
 * Please note that checks are only for validation and not data manipulation. 
 * The only exception to this rule is the {@link #checkGAEAccountAvailable(Context)} method.<p>
 * Every method is a single check. The methods can be put together in a chain and this way
 * form a convenient way to check multiple cases at the same time.
 * 
 * @author Philipp Sommersguter (philipp.sommersguter@satanko.at)<p>
 * ATTENTION: If not otherwise specified, the author(s) of the class is/are also responsible for all methods.
 * @version 1.0
 */
public class CheckTask{

	private final String tag = this.getClass().getSimpleName();
	private boolean logOutputEnabled;
	private boolean passedAllChecks;
	
	public CheckTask(){
		this.passedAllChecks = true;
	}
	
	/**
	 * Use this constructor to enable log messages when checks are positive.
	 * @param positiveCheckLogOutput true if you want to enable messages.
	 */
	public CheckTask(boolean positiveCheckLogOutput)
	{
		this.passedAllChecks = true;
		this.logOutputEnabled = positiveCheckLogOutput;
	}

	/**
	 * Checks if the class is set (not empty).
	 * @param activeClass A String representing the class
	 * @return Itself for further testing
	 * @throws TaskException
	 */
	public final CheckTask checkActiveClass(String activeClass) throws TaskException
	{
		if(activeClass.equals(C.STRING_EMPTY))
		{
			this.passedAllChecks = false;
			throw new TaskException(tag, "activeClass is empty", C.integer.NO_TOAST);
		}else if(this.logOutputEnabled)
		{
			Log.i(tag, "checkActiveClass() passed");
		}
		return this;
	}
	
	/**
	 * Checks if the teacher is set (not empty).
	 * @param activeTeacher A String representing the teacher
	 * @return Itself for further testing
	 * @throws TaskException
	 */
	public final CheckTask checkActiveTeacher(String activeTeacher) throws TaskException
	{
		if(activeTeacher.equals(C.STRING_EMPTY))
		{
			this.passedAllChecks = false;
			throw new TaskException(tag, "activeTeacher is empty", C.integer.NO_TOAST);
		}else if(this.logOutputEnabled)
		{
			Log.i(tag, "checkActiveTeacher() passed");
		}
		return this;
	}

	/**
	 * Checks if the subject is set (not empty).
	 * @param activeSubject A String representing the subject
	 * @return Itself for further testing
	 * @throws TaskException
	 */
	public final CheckTask checkActiveSubject(String activeSubject)
	{
		if(activeSubject.equals(C.STRING_EMPTY))
		{
			this.passedAllChecks = false;
		}else if(this.logOutputEnabled)
		{
			Log.i(tag, "checkActiveSubject() passed");
		}
		return this;
	}
	
	/**
	 * Checks if the network connection is ready for use.
	 * @param applicationContext The context of the application
	 * @return Itself for further testing
	 * @throws TaskException
	 */
	public final CheckTask checkNetworkConnection(Context applicationContext) throws TaskException {
		NetworkInfo networkInfo  = ((ConnectivityManager)applicationContext
    			.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
		if(networkInfo == null || !networkInfo.isConnected())
		{
			this.passedAllChecks = false;
			throw new TaskException(tag, "Network Connection is not ready!",
					R.string.toast_noNetworkConnection);
		}else if(networkInfo.isRoaming())
		{
			this.passedAllChecks = false;
			throw new TaskException(tag, "Data Roaming enabled",
					R.string.toast_dataRoamingEnabled);
		}else if(this.logOutputEnabled)
		{
			Log.i(tag, "checkNetworkConnection() passed");
		}
		return this;
	}

	/**
	 * This test case checks if a correct "Google Apps for Education" account is available on the device.<p>
	 * This test is the ONLY exception to the rule that test cases do not change data.
	 * This test also also writes the correct account to the shared preferences.
	 * @param applicationContext The context of the application
	 * @return Itself for further testing
	 * @throws TaskException
	 */
	public final CheckTask checkGAEAccountAvailable(Context applicationContext) throws TaskException {
		AccountManager am = AccountManager.get(applicationContext);
		Account[] accountArray = am.getAccountsByType("com.google");
		boolean nomatch = true;
		for(Account acc : accountArray)
		{
			String[] accParts = acc.name.split("@");
			if(accParts[1].equals(C.STRING_ACCOUNTDOMAIN))
			{
				nomatch = false;
				applicationContext.getSharedPreferences(C.prefs.FILE_NAME, Activity.MODE_PRIVATE)
					.edit()
					.putString(C.prefs.ACTIVE_TEACHER, accParts[0])
					.commit();
			}
		}
		if(nomatch)
		{
			this.passedAllChecks = false;
			throw new TaskException(tag, "Could not find Google account", R.string.error_no_account);
		}else if(this.logOutputEnabled)
		{
			Log.i(tag, "checkGAEAccountAvailable passed");
		}
		return this;
	}
	
	/**
	 * Returns whether all checks passed or not
	 * @return true or false
	 */
	public boolean getCheckVariable()
	{
		return this.passedAllChecks;
	}
}
