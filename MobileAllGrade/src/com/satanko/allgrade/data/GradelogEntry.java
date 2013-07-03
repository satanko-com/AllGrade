/************************************************************
 *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
 *                                                          *
 *  See the file LICENCE.txt for copying permission.        *
 ***********************************************************/
package com.satanko.allgrade.data;

import java.io.Serializable;
import java.sql.Time;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

/**
 * A model class which represents a single entry in the {@link GradelogTable} of the {@link DatabaseOpenHelper}.
 * @author Philipp Sommersguter (philipp.sommersguter@satanko.at)<p>
 * ATTENTION: If not otherwise specified, the author(s) of the class is/are also responsible for all methods.
 * @version 1.0
 */
@SuppressWarnings("serial")
public class GradelogEntry implements Serializable {
 
	private Time timestamp;
	private String activeClass;
	private String activeTeacher;
	private String studentName;
	private String activeSubject;
	private String gradingType;
	private String rating;
	private String comment;
	
	public GradelogEntry(Bundle b)
	{
		this.timestamp = new Time(Calendar.getInstance().getTimeInMillis());
		this.activeClass = b.getString(C.intent.bundle.CLASS);
		this.activeTeacher = b.getString(C.intent.bundle.TEACHER);
		this.studentName = b.getString(C.intent.bundle.STUDENTNAME);
		this.activeSubject = b.getString(C.intent.bundle.SUBJECT);
		this.gradingType = b.getString(C.intent.bundle.TYPE);
		this.rating = b.getString(C.intent.bundle.RATING);
		this.comment = b.getString(C.intent.bundle.COMMENT);
	}

	public JSONObject getAsJSONObject()
	{
		JSONObject object = null;
		try {
			object = new JSONObject()
				.put(C.weballgrade.json.TIMESTAMP, this.timestamp.getTime())
				.put(C.weballgrade.json.CLASS, this.activeClass)
				.put(C.weballgrade.json.TEACHER, this.activeTeacher)
				.put(C.weballgrade.json.STUDENT, this.studentName)
				.put(C.weballgrade.json.SUBJECT, this.activeSubject)
				.put(C.weballgrade.json.TYPE, this.gradingType)
				.put(C.weballgrade.json.GRADING, this.rating)
				.put(C.weballgrade.json.COMMENT, this.comment);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object;
	}

	@Override
	public String toString() {
		return getAsJSONObject().toString().substring(0, 100);
	}

	@Override
	public boolean equals(Object o) {
		GradelogEntry le = (GradelogEntry)o;
		return 
			this.activeClass.equals(le.getActiveClass())&&
			this.activeSubject.equals(le.getActiveSubject())&&
			this.activeTeacher.equals(le.getActiveTeacher())&&
			this.comment.equals(le.getComment())&&
			this.gradingType.equals(le.getGradingType())&&
			this.rating.equals(le.getRating())&&
			this.studentName.equals(le.getStudentName())&&
			this.timestamp.compareTo(le.getTimestamp())==0;
	}

	public Time getTimestamp() {
		return timestamp;
	}

	public String getActiveClass() {
		return activeClass;
	}

	public String getActiveTeacher() {
		return activeTeacher;
	}

	public String getStudentName() {
		return studentName;
	}

	public String getActiveSubject() {
		return activeSubject;
	}

	public String getGradingType() {
		return gradingType;
	}

	public String getRating() {
		return rating;
	}

	public String getComment() {
		return comment;
	}

}
