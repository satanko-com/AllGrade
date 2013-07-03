/************************************************************
 *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
 *                                                          *
 *  See the file LICENCE.txt for copying permission.        *
 ***********************************************************/
package com.satanko.allgrade.view.activity;

import java.util.LinkedList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.satanko.allgrade.R;
import com.satanko.allgrade.data.C;
import com.satanko.allgrade.view.dialog.SpinnerInputDialogFragment;

/**
 * In this Activity, a teacher is able to create a new mark.
 * 
 * @author Philipp Sommersguter (philipp.sommersguter@satanko.at)<p>
 * ATTENTION: If not otherwise specified, the author(s) of the class is/are also responsible for all methods.
 * @version 1.0
 */
public class GradeActivity extends FragmentActivity {

	/**
	 * This boolean holds the value whether the info Toast message for the 
	 * "Mark Comment Spinner" should be displayed or not.
	 */
	public static boolean SHOW_COMMENT_SPINNER_INFO = true;
	
	/**
	 * This field represents the spinner for the "Mark Type"
	 */
	private Spinner spinner_markType;
	
	/**
	 * This field represents the spinner for the "Mark Comment"
	 */
	private Spinner spinner_markComment;
	
	/**
	 * This field represents the rating bar on the screen
	 */
	private RatingBar ratingBar_markRating;
	
	/**
	 * This field represents the text view for the student name on the screen
	 */
	private TextView textView_studentName;
	
	/**
	 * This field represents the text view for the bottom info on the screen
	 */
	private TextView textView_bottomInfo;
	
	/**
	 * This field holds the comments for the <code>spinner_markComment</code>
	 */
	private LinkedList<String> commentList;
	
	/**
	 * This field represents the {@link SharedPreferences} in the activity
	 */
	private SharedPreferences prefs;
	
	/**
	 * This String holds the identifier for the class in this activity
	 */
	private String activeClass;
	
	/**
	 * This String holds the identifier for the subject in this activity
	 */
	private String activeSubject;
	
	/**
	 * This {@link OnLongClickListener} is responsible for registering long clicks on the 
	 * <code>spinner_markComment</code> field and opening the {@link SpinnerInputDialogFragment}
	 * so that the user can add new data to the <code>commentList</code> field.
	 */
	private final OnLongClickListener spinnerLongClick = new OnLongClickListener() {

		/**
		 * This is the method which is called by the {@link OnLongClickListener}.
		 * It shows the dialog to add a new comment.
		 */
		@Override
		public boolean onLongClick(View v) {
			DialogFragment spinnerInputDialog = SpinnerInputDialogFragment
					.newInstance(v.getId());
			spinnerInputDialog.show(getSupportFragmentManager(),
					"SpinnerInputDialog");
			return false;
		}
	};
	
	/**
	 * This method is called when the {@link Activity} is started. It is responsible for the 
	 * initialization of the fields. It also sets an {@link OnRatingBarChangeListener} for the
	 * {@link RatingBar} and the <code>spinnerLongClick</code> on the field <code>spinner_markComment</code>.
	 * 
	 * The method also shows the information {@link Toast} for the user.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_grade);
		prefs = getSharedPreferences(C.prefs.FILE_NAME, MODE_PRIVATE);
		
		textView_studentName = (TextView) findViewById(R.id.textView_studentName);
		spinner_markType = (Spinner) findViewById(R.id.spinner_markType);
		ratingBar_markRating = (RatingBar) findViewById(R.id.ratingBar_markRating);
		spinner_markComment = (Spinner) findViewById(R.id.spinner_markComment);
		textView_bottomInfo = (TextView) findViewById(R.id.textView_bottomInfo);

		ratingBar_markRating.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			
			/**
			 * This method makes sure that the RatingBar never has a rating lower than 1 and bigger than 5.
			 */
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				if(rating<1)
				{
					ratingBar.setRating(1);
				}else if(rating>5)
				{
					ratingBar.setRating(5);
				}
			}
		});
		
		spinner_markComment.setOnLongClickListener(spinnerLongClick);
		
		textView_studentName.setText(getIntent().getExtras().getString(
				C.intent.bundle.STUDENTNAME));
		
		activeClass = prefs.getString(C.prefs.ACTIVE_CLASS, getString(R.string.nodata)).toUpperCase();
		activeSubject = prefs.getString(C.prefs.ACTIVE_SUBJECT, getString(R.string.nodata)).toUpperCase(); 
		textView_bottomInfo.setText(activeSubject+"/"+activeClass);
		if(SHOW_COMMENT_SPINNER_INFO)
		{
			Toast.makeText(getApplicationContext(), R.string.toast_addCommentInfo, Toast.LENGTH_SHORT).show();
			SHOW_COMMENT_SPINNER_INFO = false;
		}
	}

	/**
	 * Lifecycle method
	 */
	@Override
	protected void onStart() {
		commentList = new LinkedList<String>();
		ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(
				getApplicationContext(), R.layout.spinner_item_view, getResources()
						.getStringArray(R.array.spinner_markType_standart));
		typeAdapter.setDropDownViewResource(R.layout.spinner_item_dropdown);
		spinner_markType.setAdapter(typeAdapter);

		for (int i = 0; i < C.integer.SAVE_COMMENT_NUM; i++) {
			String str = prefs.getString(C.prefs.SAVED_COMMENT+i, C.STRING_EMPTY);
			if(!str.equals(C.STRING_EMPTY))
			{
				commentList.addFirst(str);
			}
		}
		spinner_markComment.setAdapter(this.buildCommentAdapter(commentList));
		if(spinner_markComment.getAdapter().getCount()>C.integer.SPINNER_COMMENT_TURNING_POINT+1)
		{
			spinner_markComment.setSelection(spinner_markComment.getAdapter().getCount()-1);
		}
		super.onStart();
	}

	/**
	 * Lifecycle method
	 */
	@Override
	protected void onStop() {
		SharedPreferences.Editor editor = prefs.edit();
		@SuppressWarnings("unchecked")
		ArrayAdapter<String> comments = (ArrayAdapter<String>) spinner_markComment.getAdapter();
		for (int i = 0; i < comments.getCount(); i++) {
			String str = comments.getItem(i);
			if(!str.equals(C.STRING_EMPTY))
			{
				editor.putString(C.prefs.SAVED_COMMENT+i, str);
			}
		}
		editor.apply();
		super.onStop();
	}

	/**
	 * This method inflates ("sets") the menu for the {@link GradeActivity}.
	 * @param menu
	 * @return
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_mark_menu, menu);
		return true;
	}
	
	/**
	 * This method responds to the item selection of the user.
	 * It prepares an {@link Intent} with all the data for the 
	 * {@link MainActivity} and then it finishes the Activity.
	 * 
	 * @param item
	 * @return
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) 
		{
		case R.id.menu_mark_complete:
			Bundle data = new Bundle();
			data.putString(C.intent.bundle.STUDENTNAME, textView_studentName.getText().toString());
			data.putString(C.intent.bundle.TYPE, spinner_markType.getSelectedItem().toString());
			data.putString(C.intent.bundle.RATING, String.valueOf((int)ratingBar_markRating.getRating()));
			data.putString(C.intent.bundle.COMMENT, spinner_markComment.getSelectedItem().toString());
			data.putString(C.intent.bundle.SUBJECT, activeSubject);
			data.putString(C.intent.bundle.CLASS, activeClass);
			Intent resultIntent = this.getIntent();
			resultIntent.putExtras(data);
			this.setResult(Activity.RESULT_OK, resultIntent);
			this.finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * This method is called on the positive Button Click on the {@link SpinnerInputDialogFragment}.
	 * It adds a new comment to the list and builds a new Adapter for <code>spinner_markComment</code>
	 * so that the new comments are displayed correctly.
	 * It also sets the last added comment to be selected in the <code>spinner_markComment</code>.
	 * 
	 * @param id
	 * @param input
	 */
	public void doPositiveSpinnerInputDialogClick(int id, String input) {
		if (id == spinner_markType.getId()) {
			Log.d(GradeActivity.class.getName(), "spinner_markType");
		} else if (id == spinner_markComment.getId()) {
			if(!input.trim().equals(""))
			{
				this.addToCommentList(input);
				spinner_markComment.setAdapter(this.buildCommentAdapter(commentList));
				@SuppressWarnings("unchecked")
				ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner_markComment.getAdapter();
				spinner_markComment.setSelection(adapter.getPosition(input));
//				Log.i("spinner", "count = "+spinner_markComment.getAdapter().getCount());
//				if(spinner_markComment.getAdapter().getCount()<=C.SPINNER_COMMENT_TURNING_POINT+1)
//				{
//					Log.i("spinner", "selection = "+adapter.getPosition(input));
//					spinner_markComment.setSelection(adapter.getPosition(input));
//				}else
//				{
//					Log.i("spinner", "selection = "+(spinner_markComment.getAdapter().getCount()-1));
//					spinner_markComment.setSelection(spinner_markComment.getAdapter().getCount()-1);
//				}
				
			}
			Log.d(GradeActivity.class.getName(), "spinner_markComment");
		} else {
			Toast.makeText(getApplicationContext(), R.string.impossible,
					Toast.LENGTH_LONG).show();
		}
	}
	
	/**
	 * This method is responsible for building a new Adapter for the display of the comments.
	 * 
	 * @param linkedList
	 * @return
	 */
	private ArrayAdapter<String> buildCommentAdapter(LinkedList<String> linkedList)
	{
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
				getApplicationContext(), R.layout.spinner_item_view);
		arrayAdapter.setDropDownViewResource(R.layout.spinner_item_dropdown);
		if(linkedList==null)
		{
			return arrayAdapter;
		}
		if(linkedList.size()<=C.integer.SPINNER_COMMENT_TURNING_POINT)
		{
			arrayAdapter.add(C.STRING_EMPTY);
			for(int i =0; i < linkedList.size(); i++)
			{
				arrayAdapter.add(linkedList.get(i));
			}
		}else
		{
			for(int i = linkedList.size()-1; i>=0; i--)
			{
				arrayAdapter.add(linkedList.get(i));
			}
			arrayAdapter.add(C.STRING_EMPTY);
		}
		return arrayAdapter;
	}
	
	/**
	 * This method adds a new comment to the first place of the list.
	 * If the list gets bigger than {@link C}<code>.SAVE_COMMENT_NUM</code>
	 * the last item is deleted.
	 * 
	 * @param str
	 */
	private void addToCommentList(String str)
	{
		commentList.addFirst(str);
		if(commentList.size()>C.integer.SAVE_COMMENT_NUM)
		{
			commentList.removeLast();
		}
	}

}
