package uk.ac.brookes.arnaudbos.luscinia.views;

import com.google.inject.Inject;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import uk.ac.brookes.arnaudbos.luscinia.R;
import uk.ac.brookes.arnaudbos.luscinia.listeners.MacrocibleListener;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

public class MacrocibleActivity extends RoboActivity
{
	public static final int DIALOG_EMPTY_FIELD = 101;
	public static final int DIALOG_TIME_ELAPSED = 102;

	@Inject private MacrocibleListener listener;

	@InjectView(R.id.date_text_view) private TextView dateTextView;
	@InjectView(R.id.initial_evaluation_checkBox) private CheckBox initialEvaluationCheckBox;
	@InjectView(R.id.remote_evaluation_checkBox) private CheckBox remoteEvaluationCheckBox;
	@InjectView(R.id.liaison_checkBox) private CheckBox liaisonCheckBox;
	@InjectView(R.id.mEditText) private EditText mEditText;
	@InjectView(R.id.tEditText) private EditText tEditText;
	@InjectView(R.id.vEditText) private EditText vEditText;
	@InjectView(R.id.eEditText) private EditText eEditText;
	@InjectView(R.id.dEditText) private EditText dEditText;
	@InjectView(R.id.button_validate) private Button validateButton;
	@InjectView(R.id.button_update) private Button updateButton;

	@InjectResource(R.string.dialog_empty_field_error_title) private String dialogEmptyFieldErrorTitle;
	@InjectResource(R.string.dialog_empty_field_error_message) private String dialogEmptyFieldErrorMessage;
	@InjectResource(R.string.dialog_time_elapsed_error_title) private String dialogTimeElapsedErrorTitle;
	@InjectResource(R.string.dialog_time_elapsed_error_message) private String dialogTimeElapsedErrorMessage;
	@InjectResource(R.string.ok) private String ok;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.macrocible);
		listener.setContext(this);

		updateButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				//TODO: Check the date and open DIALOG_TIME_ELAPSED if needed
				setFieldsEnabled();
			}
		});
		validateButton.setOnClickListener(listener);
	}
	
	@Override
	protected Dialog onCreateDialog(int id)
	{
        switch (id)
        {
	        case DIALOG_EMPTY_FIELD:
	            return new AlertDialog.Builder(this)
//	                .setIcon(R.drawable.)
	                .setTitle(dialogEmptyFieldErrorTitle)
	                .setMessage(dialogEmptyFieldErrorMessage)
	                .setNegativeButton(ok, null)
	                .create();
	        case DIALOG_TIME_ELAPSED:
	            return new AlertDialog.Builder(this)
	                .setTitle(dialogTimeElapsedErrorTitle)
	                .setMessage(dialogTimeElapsedErrorMessage)
	                .setNegativeButton(ok, null)
	                .create();
        }
        return null;
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
   		MenuItem item = menu.add("Macrocible Menu");
	   	
	   	item.setOnMenuItemClickListener(new OnMenuItemClickListener()
		{
			@Override
			public boolean onMenuItemClick(MenuItem item)
			{
				// Do something.
				return true;
			}
		});

	   	return true;
    }
    
    public void setFieldsEnabled()
    {
		initialEvaluationCheckBox.setEnabled(true);
		remoteEvaluationCheckBox.setEnabled(true);
		liaisonCheckBox.setEnabled(true);
		mEditText.setEnabled(true);
		tEditText.setEnabled(true);
		vEditText.setEnabled(true);
		eEditText.setEnabled(true);
		dEditText.setEnabled(true);
		validateButton.setVisibility(View.VISIBLE);
		updateButton.setVisibility(View.GONE);
    }
    
    public void setFieldsDisabled()
    {
		initialEvaluationCheckBox.setEnabled(false);
		remoteEvaluationCheckBox.setEnabled(false);
		liaisonCheckBox.setEnabled(false);
		mEditText.setEnabled(false);
		tEditText.setEnabled(false);
		vEditText.setEnabled(false);
		eEditText.setEnabled(false);
		dEditText.setEnabled(false);
		validateButton.setVisibility(View.GONE);
		updateButton.setVisibility(View.VISIBLE);
    }

	/**
	 * @return the mEditText
	 */
	public String getM() {
		return mEditText.getText().toString();
	}

	/**
	 * @return the tEditText
	 */
	public String getT() {
		return tEditText.getText().toString();
	}

	/**
	 * @return the vEditText
	 */
	public String getV() {
		return vEditText.getText().toString();
	}

	/**
	 * @return the eEditText
	 */
	public String getE() {
		return eEditText.getText().toString();
	}

	/**
	 * @return the dEditText
	 */
	public String getD() {
		return dEditText.getText().toString();
	}
}