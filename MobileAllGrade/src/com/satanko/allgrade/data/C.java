/************************************************************
 *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
 *                                                          *
 *  See the file LICENCE.txt for copying permission.        *
 ***********************************************************/
package com.satanko.allgrade.data;

import android.content.Intent;

/**
 * This class is used as a holder for constants. This way typos are avoided.
 * 
 * @author Philipp Sommersguter (philipp.sommersguter@satanko.at)
 *         <p>
 *         ATTENTION: If not otherwise specified, the author(s) of the class
 *         is/are also responsible for all methods.
 * @version 1.0
 */
public class C {

	public static final String STRING_EMPTY = "";
	public static final String SCANNER_MARKET_URI = "market://details?id=com.google.zxing.client.android";
	public static final String STRING_SPLIT = "?";

	public static final class prefs {
		public static final String FILE_NAME = "MyPrefsFile";
		public static final String ACTIVE_CLASS = "ACTIVECLASS";
		public static final String ACTIVE_TEACHER = "ACTIVETEACHER";
		public static final String ACTIVE_SUBJECT = "ACTIVESUBJECT";
		public static final String SAVED_COMMENT = "SAVEDCOMMENT";
		public static final String SHOW_DIALOG_SUBJECT = "SHOWSUBJECTDIALOG";
	}

	public static final class integer {
		// Count +1 when you specify the number because of the empty comment entry
		public static final int SAVE_COMMENT_NUM = 5;
		public static final int NO_TOAST = -1;
		public static final int UPLOAD_ATTEMPT_SECONDS = 30;
		
		/**
		 * Used to identify {@link Intent}s related to QR scanning.
		 * <p>
		 * Constant Value: 0
		 */
		public static final int CODE_SCAN_REQUEST = 0;

		/**
		 * Used to identify {@link Intent}s for a grade request.
		 * <p>
		 * Constant Value: 1
		 */
		public static final int CODE_GRADE_REQUEST = 1;

		/**
		 * The number of comments at which the list is displayed above the
		 * spinner.
		 */
		// May be different on smaller or larger screens
		// on SGS2 -> 1
		public static final int SPINNER_COMMENT_TURNING_POINT = 1;
	}

	public static final class intent {

		public static final class bundle {
			public static final String STUDENTNAME = "INTENTEXTRASTUDENTNAME";
			public static final String TYPE = "INTENTEXTRATYPE";
			public static final String RATING = "INTENTEXTRARATING";
			public static final String COMMENT = "INTENTEXTRACOMMENT";
			public static final String CLASS = "INTENTEXTRACLASS";
			public static final String SUBJECT = "INTENTEXTRASUBJECT";
			public static final String TIMESTAMP = "INTENTEXTRATIMESTAMP";
			public static final String TEACHER = "INTENTEXTRATEACHER";
		}

		public static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
		public static final String MODE_SCAN = "SCAN_MODE";
		public static final String MODE_QR_CODE = "QR_CODE_MODE";
		public static final String EXTRA_SCAN_RESULT = "SCAN_RESULT";
		public static final String EXTRA_JSONSTRING = "EXTRALOGENTRYJSONSTRING";

	}

	// WebAllGrade constants
	public static final class weballgrade {

		public static final class json {

			public static final String TIMESTAMP = "timestamp";
			public static final String CLASS = "class";
			public static final String TEACHER = "teacher";
			public static final String STUDENT = "student";
			public static final String SUBJECT = "subject";
			public static final String TYPE = "type";
			public static final String GRADING = "grading";
			public static final String COMMENT = "comment";

		}

		public static final String PARAM_PATH_RESOURCE = ":resource:";
		public static final String PARAM_QUERY_TEACHER = ":teacher:";
		public static final String PARAM_QUERY_CLASS = ":class:";
		/**
		 * <b><i>public static final String ALLGRADE_HOSTNAME</i></b>
		 * <p>
		 * The URL of the AllGrade subdomain on the Satanko server.
		 * <p>
		 */
		public static final String ALLGRADE_HOSTNAME = "allgrade.satanko.at";
		public static final String SERVER_URL = "http://INSERT_SERVER_IP_OR_URL:8080/weballgrade/";
		public static final String TRANSFER_QUERY = "database/"
				+ PARAM_PATH_RESOURCE + ".json?teacher=" + PARAM_QUERY_TEACHER
				+ "&class=" + PARAM_QUERY_CLASS;

		public static final String RESOURCE_LESSONS = "lessons";
		public static final String RESOURCE_STUDENTS = "students";
		public static final String RESOURCE_GRADELOG = "gradelogs";
	}

	// CONSTANTS USED FOR TESTING
	public static final String TEST_teacher = "noi";
	public static final String TEST_class = "cdh08";
	public static final String STRING_ACCOUNTDOMAIN = "new.edvhtl.at";

}
