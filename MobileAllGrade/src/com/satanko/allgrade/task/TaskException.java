/************************************************************
 *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
 *                                                          *
 *  See the file LICENCE.txt for copying permission.        *
 ***********************************************************/
package com.satanko.allgrade.task;

import android.util.Log;

/**
 * A special Exception class to handle any errors while loading data.
 * 
 * @author Philipp Sommersguter (philipp.sommersguter@satanko.at)<p>
 * ATTENTION: If not otherwise specified, the author(s) of the class is/are also responsible for all methods.
 * @version 1.0
 */
@SuppressWarnings("serial")
public class TaskException extends Exception {

	private final String tag = this.getClass().getSimpleName();
	private final int toastErrorMessageId;
	
	public TaskException(final String sourceClassName, final String cause , final int toastErrorMessageId)
	{
		Log.w(tag, "IN ".concat(sourceClassName).concat(", CAUSE: ").concat(cause));
		this.toastErrorMessageId = toastErrorMessageId;
	}

	public int getToastErrorStringId() {
		return toastErrorMessageId;
	}
	
}
