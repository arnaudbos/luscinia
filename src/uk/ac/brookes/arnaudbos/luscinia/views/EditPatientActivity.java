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
import java.util.Map;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import uk.ac.brookes.arnaudbos.luscinia.LusciniaApplication;
import uk.ac.brookes.arnaudbos.luscinia.R;
import uk.ac.brookes.arnaudbos.luscinia.data.Patient;
import uk.ac.brookes.arnaudbos.luscinia.listeners.CreatePatientListener;
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
 * Activity updating a patient
 * @author arnaudbos
 */
public class EditPatientActivity extends RoboActivity
{
    final Handler uiThreadCallback = new Handler();
    private ProgressDialog mProgressDialog;

	@Inject private CreatePatientListener listener;

	@InjectExtra("patient") private Patient patient;

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
	
	@InjectResource(R.string.edit_patient) private String title;
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
		Log.d("EditPatientActivity.onCreate");
		super.onCreate(savedInstanceState);
        setContentView(R.layout.create_patient);
        listener.setContext(this);

        // Initialize the actionBar
        initActionBar();
        // Load the patient into the view
        renderPatient();
        addButton.setOnClickListener(listener);
	}

	/**
	 * Initialize the actionBar
	 */
	private void initActionBar()
	{
		Log.d("EditPatientActivity.initActionBar");
		actionBar.setTitle(title);
//        actionBar.addAction(new StubShareAction());
//        actionBar.addAction(new StubSearchAction());
	}

	/**
	 * Render the patient from intent extra into the views
	 */
	private void renderPatient()
	{
		Log.d("EditPatientActivity.renderPatient");
		// Retrieve known fields from the patient
		String firstname = patient.getFirstname();
		String lastname = patient.getLastname();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String dob = dateFormat.format(patient.getDateOfBirth());
		String insee = patient.getInsee();
		String telephone = patient.getTelephone();
		String weight = Double.toString(patient.getWeight());
		String size = Double.toString(patient.getSize());

		// If not null, put them into the views
		if(firstname!=null)
		{
			firstnameView.setText(firstname);
		}
		if(lastname!=null)
		{
			lastnameView.setText(lastname);
		}
		if(dob!=null)
		{
			dobView.setText(dob);
		}
		if(insee!=null)
		{
			inseeView.setText(insee);
		}
		if(telephone!=null)
		{
			telephoneView.setText(telephone);
		}
		if(weight!=null)
		{
			weightView.setText(weight);
		}
		if(size!=null)
		{
			sizeView.setText(size);
		}

		// For each unknown field add a new patient item and fill it
		for (Map.Entry<String, Object> entry : patient.getUnknownFields().entrySet())
		{
			// Inflate a new patient item
			final LinearLayout newChild = (LinearLayout) LayoutInflater.from(EditPatientActivity.this).inflate(R.layout.create_patient_item, null);
			// If the key exists in duplicate, keep only the trunc key and put in into the patient item's key view
			String key = entry.getKey();
			if(key.contains("#"))
			{
				((ScrollingTextView)newChild.findViewById(R.id.key)).setText(key.substring(0, key.lastIndexOf("#")));
			}
			else
			{
				((ScrollingTextView)newChild.findViewById(R.id.key)).setText(key);
			}
			// Get the value and put it into the patient item's value view
			((EditText)newChild.findViewById(R.id.value)).setText(""+entry.getValue());
			// Set the kay as the patient item's tag
			newChild.setTag(key);
			
			// Add a listener to the patient item's remove button
			((ImageButton)newChild.findViewById(R.id.remove)).setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					Log.d("Patient item's remove button onClick");
					// Remove this patient item (the view and the unknown field from the Patient)
					linearLayout.removeView(newChild);
					patient.getUnknownFields().remove(newChild.getTag());
				}
			});
			// Add the new patient item into the activity's view
			linearLayout.addView(newChild, linearLayout.getChildCount()-1);
		}
	}

	@Override
	protected Dialog onCreateDialog(int id)
	{
		Log.d("EditPatientActivity.onCreateDialog");
		switch(id)
		{
			case CreatePatientActivity.DIALOG_NEW_ITEM:
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
							final LinearLayout newChild = (LinearLayout) LayoutInflater.from(EditPatientActivity.this).inflate(R.layout.create_patient_item, null);
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
			case CreatePatientActivity.DIALOG_SAVE:
				Log.d("Display DIALOG_SAVE Alert");
				return new AlertDialog.Builder(this)
					.setTitle(quit)
					.setMessage(returnMessage)
					.setPositiveButton(save, listener)
					.setNeutralButton(discard, listener)
					.setNegativeButton(cancel, null)
					.create();
			case CreatePatientActivity.DIALOG_SAVE_ERROR:
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
		Log.d("EditPatientActivity.onCreateOptionsMenu");
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.create_patient, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		Log.d("EditPatientActivity.onOptionsItemSelected");
		switch(item.getItemId())
		{
			case R.id.save:
				Log.d("Save menu pressed");
				// Update the patient
				updatePatient();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onBackPressed()
	{
		Log.d("EditPatientActivity.onBackPressed");
		// Show the DIALOG_SAVE alert to confirm the update of discard of the patient
		showDialog(CreatePatientActivity.DIALOG_SAVE);
	}
	
	/**
	 * Update the patient into the database
	 */
	public void updatePatient()
	{
		Log.d("EditPatientActivity.updatePatient");
		// Launch an indeterminate ProgressBar in the UI while updating the patient in a new thread
    	mProgressDialog = ProgressDialog.show(this, "", saveLoading, true);

    	// Create a Runnable that will be executed if the update succeeds
    	final Runnable threadCallBackSuceeded = new Runnable()
    	{
    		public void run()
    		{
    			Log.d("Patient updated successfully");
    			// Hide the ProgressBar and return to DashboardActivity with the Patient as result
    			mProgressDialog.dismiss();
    			Toast.makeText(EditPatientActivity.this, patientSaved, Toast.LENGTH_SHORT).show();
    			setResult(RESULT_OK, getIntent().putExtra("patient", patient));
    			finish();
    		}
    	};

    	// Create a Runnable that will be executed if the update fails
    	final Runnable threadCallBackFailed = new Runnable()
    	{
    		public void run()
    		{
    			Log.d("Update patient failed");
    			// Hide the ProgressBar and open an Alert with an error message
    			mProgressDialog.dismiss();
    			showDialog(CreatePatientActivity.DIALOG_SAVE_ERROR);
    		}
    	};

    	// Create the separate thread that will update the patient and start it
		new Thread()
		{
			@Override public void run()
			{
				try
				{
	    			Log.d("Update the patient");
					Patient p = getPatient();
					LusciniaApplication.getDB().update(p);
					patient = p;
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
		// Create a new Patient from the existing one
		Patient patient = new Patient(this.patient.getId(), this.patient.getRevision(), this.patient.getDateOfCreation());
		
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
//				String key = (String) child.findViewById(R.id.key).getTag();
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
			Toast.makeText(EditPatientActivity.this, "ShareAction", Toast.LENGTH_SHORT).show();
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
			Toast.makeText(EditPatientActivity.this, "otherAction", Toast.LENGTH_SHORT).show();
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
