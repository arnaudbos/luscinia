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

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import uk.ac.brookes.arnaudbos.luscinia.R;
import uk.ac.brookes.arnaudbos.luscinia.listeners.MacrocibleListener;
import uk.ac.brookes.arnaudbos.luscinia.utils.Log;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.inject.Inject;

/**
 * Activity displaying MACROCIBLE document
 * @author arnaudbos
 */
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
		Log.d("MacrocibleActivity.onCreate");
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
		Log.d("MacrocibleActivity.onCreateDialog");
        switch (id)
        {
	        case DIALOG_EMPTY_FIELD:
				Log.d("Display DIALOG_EMPTY_FIELD Alert");
	            return new AlertDialog.Builder(this)
//	                .setIcon(R.drawable.)
	                .setTitle(dialogEmptyFieldErrorTitle)
	                .setMessage(dialogEmptyFieldErrorMessage)
	                .setNegativeButton(ok, null)
	                .create();
	        case DIALOG_TIME_ELAPSED:
				Log.d("Display DIALOG_TIME_ELAPSED Alert");
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
		Log.d("MacrocibleActivity.onCreateOptionsMenu");
//   	MenuItem item = menu.add("Macrocible Menu");
//	   	
//	   	item.setOnMenuItemClickListener(new OnMenuItemClickListener()
//		{
//			@Override
//			public boolean onMenuItemClick(MenuItem item)
//			{
//				// Do something.
//				return true;
//			}
//		});

	   	return true;
    }
    
    /**
     * Enable all the fields to accept user input, hide updateButton and show validateButton
     */
    public void setFieldsEnabled()
    {
		Log.d("PatientTransActivity.setFieldsEnabled");
		// Enable all the fields
		initialEvaluationCheckBox.setEnabled(true);
		remoteEvaluationCheckBox.setEnabled(true);
		liaisonCheckBox.setEnabled(true);
		mEditText.setEnabled(true);
		mEditText.setHint(R.string.mtved_m_hint);
		tEditText.setEnabled(true);
		tEditText.setHint(R.string.mtved_t_hint);
		vEditText.setEnabled(true);
		vEditText.setHint(R.string.mtved_v_hint);
		eEditText.setEnabled(true);
		eEditText.setHint(R.string.mtved_e_hint);
		dEditText.setEnabled(true);
		dEditText.setHint(R.string.mtved_d_hint);
		// Inverse buttons visibilities
		validateButton.setVisibility(View.VISIBLE);
		updateButton.setVisibility(View.GONE);
    }

    /**
     * Disable all the fields, show updateButton and hide validateButton
     */
    public void setFieldsDisabled()
    {
		Log.d("PatientTransActivity.setFieldsDisabled");
		// Disable all the fields
		initialEvaluationCheckBox.setEnabled(false);
		remoteEvaluationCheckBox.setEnabled(false);
		liaisonCheckBox.setEnabled(false);
		mEditText.setEnabled(false);
		mEditText.setHint("");
		tEditText.setEnabled(false);
		tEditText.setHint("");
		vEditText.setEnabled(false);
		vEditText.setHint("");
		eEditText.setEnabled(false);
		eEditText.setHint("");
		dEditText.setEnabled(false);
		dEditText.setHint("");
		// Inverse buttons visibilities
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