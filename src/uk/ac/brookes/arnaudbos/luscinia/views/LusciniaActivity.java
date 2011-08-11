package uk.ac.brookes.arnaudbos.luscinia.views;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import uk.ac.brookes.arnaudbos.luscinia.LusciniaApplication;
import uk.ac.brookes.arnaudbos.luscinia.R;
import uk.ac.brookes.arnaudbos.luscinia.listeners.LusciniaListener;
import uk.ac.brookes.arnaudbos.luscinia.utils.ICouchDBUtils;
import uk.ac.brookes.arnaudbos.luscinia.utils.Log;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;

import com.google.inject.Inject;

/**
 * Main Activity purpose is to identify users
 * @author arnaudbos
 */
public class LusciniaActivity extends RoboActivity
{
	public static final int DIALOG_USERNAME_ERROR = 101;
	public static final int DIALOG_PASSWORD_ERROR = 102;
	public static final int DIALOG_LOGIN_ERROR = 103;

    final Handler uiThreadCallback = new Handler();
    private ProgressDialog mProgressDialog;
	
	@Inject private ICouchDBUtils couchDBUtils;
	@Inject private LusciniaListener listener;

	@InjectView(R.id.edit_login) private EditText editLogin;
	@InjectView(R.id.edit_password) private EditText editPassword;
	@InjectView(R.id.button_validate) private Button buttonValidate;

	@InjectResource(R.string.ok) private String ok;
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
						LusciniaApplication.setDB(getCouchDBUtils().getDB(editLogin.getText().toString(), editPassword.getText().toString()));
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

	public ICouchDBUtils getCouchDBUtils()
	{
		return couchDBUtils;
	}
	
	private void launchDashboardActivity()
	{
    	Log.d("LusciniaActivity.launchDashboardActivity");
		Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
        finish();
	}
}