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

import java.text.ParseException;
import java.text.SimpleDateFormat;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import uk.ac.brookes.arnaudbos.luscinia.LusciniaApplication;
import uk.ac.brookes.arnaudbos.luscinia.R;
import uk.ac.brookes.arnaudbos.luscinia.data.Patient;
import uk.ac.brookes.arnaudbos.luscinia.listeners.CreatePatientListener;
import uk.ac.brookes.arnaudbos.luscinia.utils.ICouchDbUtils;
import uk.ac.brookes.arnaudbos.luscinia.utils.Log;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.inject.Inject;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
import com.markupartist.android.widget.ScrollingTextView;

/**
 * Activity creating a new patient
 * @author arnaudbos
 */
public class CreatePatientActivity extends RoboActivity
{
	public static final int DIALOG_NEW_ITEM = 101;
	public static final int DIALOG_SAVE = 102;
	public static final int DIALOG_SAVE_ERROR = 103;

    final Handler uiThreadCallback = new Handler();
    private ProgressDialog mProgressDialog;

	@Inject private ICouchDbUtils couchDbUtils;
	@Inject private CreatePatientListener listener;

	@InjectView(R.id.actionbar) private ActionBar actionBar;
	@InjectView(R.id.linearLayout) private LinearLayout linearLayout;
	@InjectView(R.id.firstname) private EditText firstnameView;
	@InjectView(R.id.lastname) private EditText lastnameView;
	@InjectView(R.id.dob) private EditText dobView;
	@InjectView(R.id.insee) private EditText inseeView;
	@InjectView(R.id.telephone) private EditText telephoneView;
	@InjectView(R.id.weight) private EditText weightView;
	@InjectView(R.id.size) private EditText sizeView;
	@InjectView(R.id.add) private ImageButton addButton;
	
	@InjectResource(R.string.new_patient) private String title;
	@InjectResource(R.string.ok) private String ok;
	@InjectResource(R.string.cancel) private String cancel;
	@InjectResource(R.string.new_value) private String newValueTitle;
	@InjectResource(R.string.save) private String save;
	@InjectResource(R.string.quit) private String quit;
	@InjectResource(R.string.return_message) private String returnMessage;
	@InjectResource(R.string.discard) private String discard;
	@InjectResource(R.string.save_loading) private String saveLoading;
	@InjectResource(R.string.save_error_title) private String saveErrorTitle;
	@InjectResource(R.string.save_error_message) private String saveErrorMessage;
	@InjectResource(R.string.patient_saved) private String patientSaved;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		Log.d("CreatePatientActivity.onCreate");
		super.onCreate(savedInstanceState);
        setContentView(R.layout.create_patient);
        listener.setContext(this);

        // Initialize the actionBar
        initActionBar();
        addButton.setOnClickListener(listener);
	}
	
	/**
	 * Initialize the actionBar
	 */
	private void initActionBar()
	{
		Log.d("CreatePatientActivity.initActionBar");
		actionBar.setTitle(title);
//        actionBar.addAction(new StubShareAction());
//        actionBar.addAction(new StubSearchAction());
	}

	@Override
	protected Dialog onCreateDialog(int id)
	{
		Log.d("CreatePatientActivity.onCreateDialog");
		switch(id)
		{
			case DIALOG_NEW_ITEM:
				Log.d("Display DIALOG_NEW_ITEM Alert");
				// Create an EditText that will get the user input
				final EditText prompt = new EditText(this);
				// Create and show the dialog
				return new AlertDialog.Builder(this)
					.setTitle(newValueTitle)
					.setView(prompt)
					.setPositiveButton(ok, new OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which) 
						{
							Log.d("DIALOG_NEW_ITEM onClick");
							// Inflate a new patient item
							final LinearLayout newChild = (LinearLayout) LayoutInflater.from(CreatePatientActivity.this).inflate(R.layout.create_patient_item, null);
							// Retrieve the key from the user input
							String key = prompt.getText().toString();
							// Put the key input the patient item key view
							((ScrollingTextView)newChild.findViewById(R.id.key)).setText(key);
							// Also set the key in the patient item's tag
							newChild.setTag(key);
							
							// Add a listener to the patient item's delete button
							((ImageButton)newChild.findViewById(R.id.remove)).setOnClickListener(new View.OnClickListener()
							{
								@Override
								public void onClick(View v)
								{
									Log.d("Patient item's remove button onClick");
									// Remove this patient item (the view)
									linearLayout.removeView(newChild);
								}
							});
							// Add the new patient item into the activity's view
							linearLayout.addView(newChild, linearLayout.getChildCount()-1);
							// Reset the edit text
							prompt.setText("");
						}
					})
					.setNegativeButton(cancel, null)
					.create();
			case DIALOG_SAVE:
				Log.d("Display DIALOG_SAVE Alert");
				return new AlertDialog.Builder(this)
					.setTitle(quit)
					.setMessage(returnMessage)
					.setPositiveButton(save, listener)
					.setNeutralButton(discard, listener)
					.setNegativeButton(cancel, null)
					.create();
			case DIALOG_SAVE_ERROR:
				Log.d("Display DIALOG_SAVE_ERROR Alert");
				return new AlertDialog.Builder(this)
					.setTitle(saveErrorTitle)
					.setMessage(saveErrorMessage)
					.setPositiveButton(ok, null)
					.create();
		}
		return null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		Log.d("CreatePatientActivity.onCreateOptionsMenu");
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.create_patient, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		Log.d("CreatePatientActivity.onOptionsItemSelected");
		switch(item.getItemId())
		{
			case R.id.save:
				Log.d("Save menu pressed");
				// Save the new patient
				savePatient();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onBackPressed()
	{
		Log.d("CreatePatientActivity.onBackPressed");
		// Show the DIALOG_SAVE alert to confirm the save of discard of the new patient
		showDialog(DIALOG_SAVE);
	}
	
	/**
	 * Save the patient into the database
	 */
	public void savePatient()
	{
		Log.d("CreatePatientActivity.savePatient");
		// Launch an indeterminate ProgressBar in the UI while saving the patient in a new thread
    	mProgressDialog = ProgressDialog.show(this, "", saveLoading, true);

    	// Create a Runnable that will be executed if the creation succeeds
    	final Runnable threadCallBackSuceeded = new Runnable()
    	{
    		public void run()
    		{
    			Log.d("Patient saved successfully");
    			// Hide the ProgressBar and return to DashboardActivity
    			mProgressDialog.dismiss();
    			Toast.makeText(CreatePatientActivity.this, patientSaved, Toast.LENGTH_SHORT).show();
    			finish();
    		}
    	};

    	// Create a Runnable that will be executed if the creation fails
    	final Runnable threadCallBackFailed = new Runnable()
    	{
    		public void run()
    		{
    			Log.d("Create patient failed");
    			// Hide the ProgressBar and open an Alert with an error message
    			mProgressDialog.dismiss();
    			showDialog(DIALOG_SAVE_ERROR);
    		}
    	};

    	// Create the separate thread that will create the patient and start it
		new Thread()
		{
			@Override public void run()
			{
				try
				{
	    			Log.d("Create the patient");
					couchDbUtils.create(getPatient());
					uiThreadCallback.post(threadCallBackSuceeded);
				}
				catch (Exception e)
				{
					Log.e("Create patient failed", e);
					uiThreadCallback.post(threadCallBackFailed);
				}
			}
		}.start();
	}
	
	/**
	 * Return a new Patient object created from the views
	 * @return a Patient object
	 * @throws ParseException if the date is not in the right format
	 */
	private Patient getPatient() throws ParseException
	{
		Log.d("CreatePatientActivity.getPatient");
		// Create a new Patient
		Patient patient = new Patient();
		
		// Retrieve the fields from the views
		String firstname = firstnameView.getText().toString();
		String lastname = lastnameView.getText().toString();
		String dob = dobView.getText().toString();
		String insee = inseeView.getText().toString();
		String telephone = telephoneView.getText().toString();
		String weightString = weightView.getText().toString();
		String sizeString = sizeView.getText().toString();

		// For each field, add it to the Patient only if filled
		if (firstname !=null && !firstname.equals(""))
		{
			patient.setFirstname(firstname);
		}
		if (lastname !=null && !lastname.equals(""))
		{
			patient.setLastname(lastname);
		}
		if (dob !=null && !dob.equals(""))
		{
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			patient.setDateOfBirth(dateFormat.parse(dob));
		}
		if (insee !=null && !insee.equals(""))
		{
			patient.setInsee(insee);
		}
		if (telephone !=null && !telephone.equals(""))
		{
			patient.setTelephone(telephone);
		}
		if (weightString !=null && !weightString.equals(""))
		{
			patient.setWeight(Double.valueOf(weightString));
		}
		if (sizeString !=null && !sizeString.equals(""))
		{
			patient.setSize(Double.valueOf(sizeString));
		}

		// Run through all unknown fields and add them to the Patient
		int i=7;
		while (i<linearLayout.getChildCount()-1)
		{
			LinearLayout child = (LinearLayout) linearLayout.getChildAt(i);
//			String key = (String) child.findViewById(R.id.key).getTag();
			String key = (String) child.getTag();
			String value = ((EditText)child.findViewById(R.id.value)).getText().toString();
			patient.add(key, value);
			i++;
		}
		return patient;
	}

	/**
	 * TODO: Stub class. Implement it.
	 */
	private class StubShareAction implements Action
    {
		@Override
		public void performAction(View view)
		{
			Toast.makeText(CreatePatientActivity.this, "ShareAction", Toast.LENGTH_SHORT).show();
		}
		
		@Override
		public int getDrawable()
		{
			return R.drawable.ic_title_share_default;
		}
	};
	
	/**
	 * TODO: Stub class. Implement it.
	 */
	private class StubSearchAction implements Action
    {
		@Override
		public void performAction(View view)
		{
			Toast.makeText(CreatePatientActivity.this, "otherAction", Toast.LENGTH_SHORT).show();
		}
		
		@Override
		public int getDrawable()
		{
			return R.drawable.ic_title_export_default;
		}
	}

	/**
	 * @return the linearLayout
	 */
	public LinearLayout getLinearLayout()
	{
		return linearLayout;
	};
}
