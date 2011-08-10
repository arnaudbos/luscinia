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

	final Runnable threadCallBackSuceeded = new Runnable()
	{
		public void run()
		{
			launchDashboardActivity();
		}
	};

	final Runnable threadCallBackFailed = new Runnable()
	{
		public void run()
		{
			mProgressDialog.dismiss();
			showDialog(LusciniaActivity.DIALOG_LOGIN_ERROR);
		}
	};

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.luscinia);
        listener.setContext(this);
        
        buttonValidate.setOnClickListener(listener);
    }
    
    @Override
    protected Dialog onCreateDialog(int id)
    {
        switch (id)
        {
	        case DIALOG_USERNAME_ERROR:
	            return new AlertDialog.Builder(this)
//	                .setIcon(R.drawable.)
	                .setTitle(dialogUsernameErrorTitle)
	                .setMessage(dialogUsernameErrorMessage)
	                .setNegativeButton(ok, null)
	                .create();
	        case DIALOG_PASSWORD_ERROR:
	            return new AlertDialog.Builder(this)
//	                .setIcon(R.drawable.)
	                .setTitle(dialogPasswordErrorTitle)
	                .setMessage(dialogPasswordErrorMessage)
	                .setNegativeButton(ok, null)
	                .create();
	        case DIALOG_LOGIN_ERROR:
		        	return new AlertDialog.Builder(this)
//		        	.setIcon(R.drawable.)
		        	.setTitle(dialogLoginErrorTitle)
		        	.setMessage(dialogLoginErrorMessage)
		        	.setNegativeButton(ok, null)
		        	.create();
        }
        return null;
    }

	public void connect()
	{
		String login = editLogin.getText().toString();
		String password = editPassword.getText().toString();
		if (login == null || login.equals(""))
		{
			showDialog(LusciniaActivity.DIALOG_USERNAME_ERROR);
		}
		else if (password == null || password.equals(""))
		{
			showDialog(LusciniaActivity.DIALOG_PASSWORD_ERROR);
		}
		else
		{
	    	mProgressDialog = ProgressDialog.show(this, "", connectLoading, true);
			new Thread()
			{
				@Override public void run()
				{
					try
					{
						Log.i("Try to connect");
						LusciniaApplication.setDB(getCouchDBUtils().getDB(editLogin.getText().toString(), editPassword.getText().toString()));

						Log.i("Connection ok return to UI");
						uiThreadCallback.post(threadCallBackSuceeded);
					}
					catch (Exception e)
					{
						Log.e("CATCHED: Login action failed", e);
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
		Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
        finish();
	}
}