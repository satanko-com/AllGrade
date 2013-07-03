/************************************************************
 *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
 *                                                          *
 *  See the file LICENCE.txt for copying permission.        *
 ***********************************************************/
package com.satanko.allgrade.view.dialog;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.satanko.allgrade.R;
import com.satanko.allgrade.data.C;
import com.satanko.allgrade.data.GradelogEntry;
import com.satanko.allgrade.view.activity.MainActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.ArrayAdapter;

/**
 * The implementation of a {@link android.app.DialogFragment} to delete 
 * {@link GradelogEntry} objects associated with a student.
 * 
 * @author Philipp Sommersguter (philipp.sommersguter@satanko.at)<p>
 * ATTENTION: If not otherwise specified, the author(s) of the class is/are also responsible for all methods.
 * @version 1.0
 */
@SuppressLint("ValidFragment")
public class DeleteGradelogEntryDialogFragment extends DialogFragment{

	private String title;
	private ArrayList<GradelogEntry> arrayList;
	
	/**
	 * Creates a new instance of this class.
	 * @return A {@link DeleteGradelogEntryDialogFragment} instance.
	 */
	public static DeleteGradelogEntryDialogFragment newInstance()
	{
		DeleteGradelogEntryDialogFragment frag = new DeleteGradelogEntryDialogFragment();
		return frag;
	}

	/**
	 * The method responsible for building the actual dialog displayed to the user.
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		ArrayList<String> stringList = new ArrayList<String>();
		try {
			for(int i=0; i<this.arrayList.size(); i++)
			{
				JSONObject jo = this.arrayList.get(i).getAsJSONObject();
				Resources res = this.getActivity().getApplicationContext().getResources();
				String type = res.getString(R.string.dialog_deleteLogEntry_detailType, jo.getString(C.weballgrade.json.TYPE));
				String rating = res.getString(R.string.dialog_deleteLogEntry_detailRating, jo.getString(C.weballgrade.json.GRADING));
				String comment = res.getString(R.string.dialog_deleteLogEntry_detailComment, jo.getString(C.weballgrade.json.COMMENT));
				stringList.add(type+"\n\n"+rating+"\n\n"+comment);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getActivity(), R.layout.logentry_list_item, stringList);
		
		return new AlertDialog.Builder(getActivity())
		.setTitle(this.title)
		.setAdapter(adapter, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				((MainActivity)getActivity()).deleteLogFromEntryList(getArrayList().get(which));
			}
		}).create();
	}

	public void setTitle(String text)
	{
		this.title = text;
	}
	
	public void setArrayList(ArrayList<GradelogEntry> list)
	{
		this.arrayList = list;
	}
	
	public ArrayList<GradelogEntry> getArrayList()
	{
		return this.arrayList;
	}
	
}
