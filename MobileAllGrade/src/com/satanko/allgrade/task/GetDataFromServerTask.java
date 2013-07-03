/************************************************************
 *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
 *                                                          *
 *  See the file LICENCE.txt for copying permission.        *
 ***********************************************************/
package com.satanko.allgrade.task;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

import com.satanko.allgrade.data.C;

/**
 * The class responsible for retrieving data from the server.<p>
 * The {@link #doInBackground(String...)} method opens a HTTP connection to the server and 
 * converts the response data in a usable format.
 * 
 * @author Philipp Sommersguter (philipp.sommersguter@satanko.at)<p>
 * ATTENTION: If not otherwise specified, the author(s) of the class is/are also responsible for all methods.
 * @version 1.0
 */
public class GetDataFromServerTask extends
		AsyncTask<String, Void, ArrayList<String>> {

	private final String tag = this.getClass().getSimpleName();

	@Override
	protected ArrayList<String> doInBackground(String... params) {
		// params[0] = C.weballgrade.PARAM_PATH_RESOURCE
		// params[1] = C.weballgrade.PARAM_QUERY_CLASS
		// params[2] = C.weballgrade.PARAM_QUERY_TEACHER
		try {
			String queryString = C.weballgrade.SERVER_URL
					.concat(C.weballgrade.TRANSFER_QUERY)
					.replaceAll(C.weballgrade.PARAM_PATH_RESOURCE, params[0])
					.replaceAll(C.weballgrade.PARAM_QUERY_CLASS, params[1])
					.replaceAll(C.weballgrade.PARAM_QUERY_TEACHER, params[2]);

			Log.i(tag, "send to server: " + queryString);
			HttpURLConnection httpConnection = (HttpURLConnection) new URL(
					queryString).openConnection();
			httpConnection.setRequestMethod("GET");
			httpConnection.connect();
			int responseCode = httpConnection.getResponseCode();

			StringBuilder jsonData = new StringBuilder();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				InputStreamReader in = new InputStreamReader(
						(InputStream) httpConnection.getContent());
				BufferedReader buffReader = new BufferedReader(in);

				String line = buffReader.readLine();
				while (line != null) {
					jsonData.append(line);
					line = buffReader.readLine();
				}
				buffReader.close();
				in.close();
				httpConnection.disconnect();
				
				JSONArray jsonArray = new JSONArray(jsonData.toString());
				ArrayList<String> data = new ArrayList<String>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonElement = (JSONObject) jsonArray.get(i);
					String listElement = C.STRING_EMPTY;
					if (params[0].equals(C.weballgrade.RESOURCE_STUDENTS)) {
						listElement = jsonElement.getString("StudentToken")+C.STRING_SPLIT+jsonElement.getString("StudentLastName")
								+ " " + jsonElement.getString("StudentFirstName");
					} else if (params[0].equals(C.weballgrade.RESOURCE_LESSONS)) {
						listElement = jsonElement.getString("LessonSubjectToken");
					}
					data.add(listElement);
				}
				Log.d(tag, "Successfully loaded data from server");
				return data;
			} else {
				Log.e(tag, "error code: "+httpConnection.getResponseCode()
						+"     error message: "+httpConnection.getResponseMessage());
				ArrayList<String> data = new ArrayList<String>();
				data.add("conflict409");
				return data;
			}
		} catch (Exception e) {
			Log.e(tag, e.getLocalizedMessage());
			e.printStackTrace();
			return null;
		}
	}

}
