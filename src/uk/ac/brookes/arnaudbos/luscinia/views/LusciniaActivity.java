/*
 * Copyright (C) 2011 Arnaud Bos <arnaud.tlse@gmail.com>
 * 
 * This file is part of Luscinia.
 * 
 * Luscinia is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Luscinia is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Luscinia.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.ac.brookes.arnaudbos.luscinia.views;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import uk.ac.brookes.arnaudbos.luscinia.R;
import uk.ac.brookes.arnaudbos.luscinia.listeners.LusciniaListener;
import uk.ac.brookes.arnaudbos.luscinia.utils.ICouchDbUtils;
import uk.ac.brookes.arnaudbos.luscinia.utils.Log;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.couchbase.libcouch.CouchbaseMobile;
import com.couchbase.libcouch.ICouchClient;
import com.google.inject.Inject;
import com.markupartist.android.widget.ScrollingTextView;

/**
 * Main Activity purpose is to identify users
 * @author arnaudbos
 */
public class LusciniaActivity extends RoboActivity
{
	public static final int DIALOG_USERNAME_ERROR = 101;
	public static final int DIALOG_PASSWORD_ERROR = 102;
	public static final int DIALOG_LOGIN_ERROR = 103;
	
	private ServiceConnection couchServiceConnection;
	private ProgressDialog installProgress;

    final Handler uiThreadCallback = new Handler();
    private ProgressDialog mProgressDialog;
	
	@Inject private ICouchDbUtils couchDbUtils;
	@Inject private LusciniaListener listener;
	@Inject private SharedPreferences sharedPreferences;

	@InjectView(R.id.edit_login) private EditText editLogin;
	@InjectView(R.id.edit_password) private EditText editPassword;
	@InjectView(R.id.button_validate) private Button buttonValidate;

	@InjectResource(R.string.ok) private String ok;
	@InjectResource(R.string.cancel) private String cancel;
	@InjectResource(R.string.dialog_username_error_title) private String dialogUsernameErrorTitle;
	@InjectResource(R.string.dialog_username_error_message) private String dialogUsernameErrorMessage;
	@InjectResource(R.string.dialog_password_error_title) private String dialogPasswordErrorTitle;
	@InjectResource(R.string.dialog_password_error_message) private String dialogPasswordErrorMessage;
	@InjectResource(R.string.dialog_login_error_title) private String dialogLoginErrorTitle;
	@InjectResource(R.string.dialog_login_error_message) private String dialogLoginErrorMessage;
	@InjectResource(R.string.connect_loading) private String connectLoading;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    	Log.d("LusciniaActivity.onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.luscinia);
        listener.setContext(this);
        
        buttonValidate.setOnClickListener(listener);
        startCouch();
    }
    
    @Override
	public void onRestart() 
    {
		super.onRestart();
		startCouch();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		try
		{
			unbindService(couchServiceConnection);
		}
		catch (IllegalArgumentException e)
		{}
	}

	private final ICouchClient mCallback = new ICouchClient.Stub()
	{
		@Override
		public void couchStarted(String host, int port) 
		{
			if (installProgress != null)
			{
				installProgress.dismiss();
			}

			couchDbUtils.setHost (host, port);
		}

		@Override
		public void installing(int completed, int total)
		{
			ensureProgressDialog();
			installProgress.setTitle("Installing");
			installProgress.setProgress(completed);
			installProgress.setMax(total);
		}

		@Override
		public void exit(String error)
		{
			Log.d("CouchDB install error: " + error);
			couchError();
		}
	};

	private void startCouch() 
	{
		CouchbaseMobile couch = new CouchbaseMobile(getBaseContext(), mCallback);

		try
		{
			couch.copyIniFile("luscinia.ini");
		}
		catch (IOException e)
		{
			Log.e("Error while copying luscinia.ini", e);
			e.printStackTrace();
		}

		couchServiceConnection = couch.startCouchbase();
	}

	private void ensureProgressDialog()
	{
		if (installProgress == null)
		{
			installProgress = new ProgressDialog(LusciniaActivity.this);
			installProgress.setTitle(" ");
			installProgress.setCancelable(false);
			installProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			installProgress.show();
		}
	}

	private void couchError()
	{
		new AlertDialog.Builder(this)
			.setMessage("Unknown Error")
			.setPositiveButton("Try Again?",
				new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int id)
					{
						startCouch();
					}
				})
			.setNegativeButton(cancel,
				new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int id)
					{
						LusciniaActivity.this.finish();
					}
				})
			.create()
			.show();
	}

	public String getLocalIpAddress()
	{
		try
		{
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)
			{
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)
				{
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress())
					{
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		}
		catch (SocketException ex)
		{
			ex.printStackTrace();
		}
		return null;
	}

    @Override
    protected Dialog onCreateDialog(int id)
    {
		Log.d("LusciniaActivity.onCreateDialog");
        switch (id)
        {
	        case DIALOG_USERNAME_ERROR:
	        	Log.d("Display DIALOG_USERNAME_ERROR Arlert");
	            return new AlertDialog.Builder(this)
	                .setTitle(dialogUsernameErrorTitle)
	                .setMessage(dialogUsernameErrorMessage)
	                .setNegativeButton(ok, null)
	                .create();
	        case DIALOG_PASSWORD_ERROR:
	        	Log.d("Display DIALOG_PASSWORD_ERROR Arlert");
	            return new AlertDialog.Builder(this)
	                .setTitle(dialogPasswordErrorTitle)
	                .setMessage(dialogPasswordErrorMessage)
	                .setNegativeButton(ok, null)
	                .create();
	        case DIALOG_LOGIN_ERROR:
	        	Log.d("Display DIALOG_LOGIN_ERROR Arlert");
		        	return new AlertDialog.Builder(this)
		        	.setTitle(dialogLoginErrorTitle)
		        	.setMessage(dialogLoginErrorMessage)
		        	.setNegativeButton(ok, null)
		        	.create();
        }
        return null;
    }
    
    /**
     * Try to etablish a connection with the database instance.
     */
	public void connect()
	{
    	Log.d("LusciniaActivity.connect");
		// Check validation fields
		String login = editLogin.getText().toString();
		String password = editPassword.getText().toString();
		// The login is required
		if (login == null || login.equals(""))
		{
			Log.d("Login is required");
			showDialog(LusciniaActivity.DIALOG_USERNAME_ERROR);
		}
		// The password is required
		else if (password == null || password.equals(""))
		{
			Log.d("Password is required");
			showDialog(LusciniaActivity.DIALOG_PASSWORD_ERROR);
		}
		// If the login AND the password are filled try to establish the connection
		else
		{
			Log.d("Login and password correctly filled");
			// Launch an indeterminate ProgressBar in the UI while establishing connection in a new thread
	    	mProgressDialog = ProgressDialog.show(this, "", connectLoading, true);
	    	
	    	// Create a Runnable that will be executed if the connection succeeds
	    	final Runnable threadCallBackSuceeded = new Runnable()
	    	{
	    		public void run()
	    		{
	    			Log.d("Connection successfully established");
	    			// Hide the ProgressBar and launch the DashboardActivity
	    			mProgressDialog.dismiss();
	    			launchDashboardActivity();
	    		}
	    	};

	    	// Create a Runnable that will be executed if the connection fails
	    	final Runnable threadCallBackFailed = new Runnable()
	    	{
	    		public void run()
	    		{
	    			Log.d("Connection not established");
	    			// Hide the ProgressBar and open an Alert with an error message
	    			mProgressDialog.dismiss();
	    			showDialog(LusciniaActivity.DIALOG_LOGIN_ERROR);
	    		}
	    	};
	    	
	    	// Create the separate thread that will establish the connection and start it
			new Thread()
			{
				@Override public void run()
				{
					try
					{
		    			Log.d("Try to establish connection");
						// Establish the connection and retrieve the dabatase object that will be used as a session
						couchDbUtils.connect(editLogin.getText().toString(), editPassword.getText().toString());
						uiThreadCallback.post(threadCallBackSuceeded);
					}
					catch (Exception e)
					{
						Log.e("Establishing connection failed", e);
						uiThreadCallback.post(threadCallBackFailed);
					}
				}
			}.start();
		}
	}

	private void launchDashboardActivity()
	{
    	Log.d("LusciniaActivity.launchDashboardActivity");
		Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
	}
}
