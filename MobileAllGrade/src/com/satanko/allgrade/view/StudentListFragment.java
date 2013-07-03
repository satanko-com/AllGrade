/************************************************************
 *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
 *                                                          *
 *  See the file LICENCE.txt for copying permission.        *
 ***********************************************************/
package com.satanko.allgrade.view;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.satanko.allgrade.R;
import com.satanko.allgrade.view.activity.MainActivity;

/**
 * The implementation of a {@link android.app.ListFragment} to display students.
 * 
 * @author Philipp Sommersguter (philipp.sommersguter@satanko.at)<p>
 * ATTENTION: If not otherwise specified, the author(s) of the class is/are also responsible for all methods.
 * @version 1.0
 */
public class StudentListFragment extends ListFragment {

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				 ((MainActivity)getActivity()).showDeleteDialog(getListAdapter().getItem(position).toString());
		         return true;
			}
		});
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		((MainActivity)getActivity()).startGradeActivity(l.getItemAtPosition(position).toString());
        super.onListItemClick(l, v, position, id);
	}

	/**
	 * This methods sets the text to show the user that he should scan a new code.
	 */
	public void show_scanNewCode() {
		ArrayList<String> al = new ArrayList<String>();
		al.add(getActivity().getApplicationContext().getResources().getString(R.string.list_scanNewCode));
		ListAdapter adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
        		R.layout.student_list_item, al);
		setListAdapter(adapter);
	}

	
}
