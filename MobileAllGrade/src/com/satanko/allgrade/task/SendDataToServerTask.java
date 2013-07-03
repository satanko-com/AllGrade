/************************************************************
 *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
 *                                                          *
 *  See the file LICENCE.txt for copying permission.        *
 ***********************************************************/
package com.satanko.allgrade.task;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.client.ClientProtocolException;

import android.os.AsyncTask;
import android.util.Log;

import com.satanko.allgrade.data.C;

/**
 * The class responsible for sending data to the server.<p>
 * The {@link #doInBackground(String...)} method opens a HTTP connection to the server and sends the data.
 * 
 * @author Philipp Sommersguter (philipp.sommersguter@satanko.at)<p>
 * ATTENTION: If not otherwise specified, the author(s) of the class is/are also responsible for all methods.
 * @version 1.0
 */
public class SendDataToServerTask extends AsyncTask<String, Void, Void> {

	private final String tag = this.getClass().getSimpleName();
	
	@Override
	protected Void doInBackground(String... params) {
		// param[0] = C.weballgrade.RESOURCE_GRADELOG
		// param[1] = JSONString
		try {
			String postString = C.weballgrade.SERVER_URL
			.concat(C.weballgrade.TRANSFER_QUERY)
			.replaceAll(C.weballgrade.PARAM_PATH_RESOURCE, params[0]);
			HttpURLConnection httpConnection = (HttpURLConnection) new URL(
					postString).openConnection();
			httpConnection.setDoOutput(true);
			httpConnection.setRequestMethod("POST");
			httpConnection.setRequestProperty("Content-Type", "application/json");
			Log.i(tag, "json->"+params[1]);
			byte[] outbytes = params[1].getBytes();
			
			Log.d(tag, "Content-Length: "+Integer.toString(outbytes.length));
			OutputStream os = httpConnection.getOutputStream();
			
			os.write(outbytes);
			os.flush();
			if(httpConnection.getResponseCode()==HttpURLConnection.HTTP_OK)
			{
				Log.d(tag, "Server Response Code: "+httpConnection.getResponseCode());
			}else
			{
				Log.e(tag, "Server Response to upload: "+httpConnection.getResponseCode());
				Log.e(tag, "Server Response to upload: "+httpConnection.getResponseMessage());
			}
			
			os.close();
			httpConnection.disconnect();
			Log.i(tag, "Upload should be successfull");
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
