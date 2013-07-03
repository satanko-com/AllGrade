/************************************************************
 *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
 *                                                          *
 *  See the file LICENCE.txt for copying permission.        *
 ***********************************************************/
package com.satanko.allgrade.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.EditText;

import com.satanko.allgrade.R;
import com.satanko.allgrade.view.activity.GradeActivity;

/**
 * This class is used to build a new dialog so that the user is able to
 * add a new comment.
 * 
 * @author Philipp Sommersguter (philipp.sommersguter@satanko.at)<p>
 * ATTENTION: If not otherwise specified, the author(s) of the class is/are also responsible for all methods.
 * @version 1.0
 */
public class SpinnerInputDialogFragment extends DialogFragment {
	/**
	 * An {@link EditText} object which is used for the input of a new comment.
	 */
	private EditText inputText;

	/**
	 * An instance builder which uses the given <code>int</code> to build/identify
	 * several instances of the Dialog.
	 */
	public static SpinnerInputDialogFragment newInstance(int num) {
		SpinnerInputDialogFragment frag = new SpinnerInputDialogFragment();
		Bundle args = new Bundle();
		args.putInt("num", num);
		frag.setArguments(args);
		return frag;
	}

	/**
	 * In this method a custom Dialog is built using an {@link AlertDialog.Builder}
	 * and returned to the Activity for handling.
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		inputText = new EditText(getActivity());
		inputText.setHeight(75);
		final int num = getArguments().getInt("num");
		return new AlertDialog.Builder(getActivity())
				.setTitle(R.string.dialog_spinnerInput_heading)
				.setView(inputText)
				.setPositiveButton(R.string.dialog_spinnerInput_btn_insert,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int whichButton) {
								((GradeActivity)getActivity()).doPositiveSpinnerInputDialogClick(
										num, inputText.getText().toString());
							}
						})
				.setNegativeButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int whichButton) {
								return;
							}
						}).create();
	}

	
	
}
