/************************************************************
 *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
 *                                                          *
 *  See the file LICENCE.txt for copying permission.        *
 ***********************************************************/
package com.satanko.allgrade.view.dialog;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.ArrayAdapter;

import com.satanko.allgrade.R;
import com.satanko.allgrade.view.activity.MainActivity;

/**
 * The implementation of a {@link android.app.DialogFragment} to 
 * choose a subject.
 * 
 * @author Philipp Sommersguter (philipp.sommersguter@satanko.at)<p>
 * ATTENTION: If not otherwise specified, the author(s) of the class is/are also responsible for all methods.
 * @version 1.0
 */
@SuppressLint("ValidFragment")
public class SubjectChooserDialogFragment extends DialogFragment {
	
	private ArrayList<String> arrayList;
	
	public SubjectChooserDialogFragment(){
		
	}
	
	public SubjectChooserDialogFragment(ArrayList<String> al) {
		this.arrayList = al;
	}

	/**
	 * Creates a new instance of this class.
	 * @return A {@link DeleteGradelogEntryDialogFragment} instance.
	 */
	public static SubjectChooserDialogFragment newInstance(ArrayList<String> al)
	{
		SubjectChooserDialogFragment frag = new SubjectChooserDialogFragment(al);
		return frag;
	}
	
	/**
	 * The method responsible for building the actual dialog displayed to the user.
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getActivity(), R.layout.subject_list_item, this.arrayList);
		return new AlertDialog.Builder(getActivity())
		.setTitle(R.string.dialog_subjectChooser_heading)
		.setAdapter(adapter, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				((MainActivity)getActivity()).onSubjectChooserDialogItemClick(
						adapter.getItem(which));
			}
		})
		.create();
	}
	
}
