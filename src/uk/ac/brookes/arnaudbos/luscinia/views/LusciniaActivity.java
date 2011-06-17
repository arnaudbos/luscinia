package uk.ac.brookes.arnaudbos.luscinia.views;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import uk.ac.brookes.arnaudbos.luscinia.R;
import uk.ac.brookes.arnaudbos.luscinia.listeners.LusciniaListener;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.inject.Inject;

public class LusciniaActivity extends RoboActivity
{
	public static final int DIALOG_USERNAME_ERROR = 101;
	public static final int DIALOG_PASSWORD_ERROR = 102;
	public static final int DIALOG_LOGIN_ERROR = 103;

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
    
    public String getEditLogin ()
    {
    	return editLogin.getText().toString();
    }
    
    public String getEditPassword ()
    {
    	return editPassword.getText().toString();
    }
}