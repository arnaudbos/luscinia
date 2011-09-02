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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import uk.ac.brookes.arnaudbos.luscinia.LusciniaApplication;
import uk.ac.brookes.arnaudbos.luscinia.R;
import uk.ac.brookes.arnaudbos.luscinia.data.Document;
import uk.ac.brookes.arnaudbos.luscinia.data.Folder;
import uk.ac.brookes.arnaudbos.luscinia.listeners.MacrocibleListener;
import uk.ac.brookes.arnaudbos.luscinia.utils.ICouchDbUtils;
import uk.ac.brookes.arnaudbos.luscinia.utils.Log;
import uk.ac.brookes.arnaudbos.luscinia.utils.TemplateActivityMapper;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
	public static final int DIALOG_UPDATE_DOCUMENT_TIME_ELAPSED = 102;
	public static final int DIALOG_SAVE_DOCUMENT_ERROR = 103;
	public static final int DIALOG_DELETE_DOCUMENT_TIME_ELAPSED = 104;
	public static final int DIALOG_DELETE_DOCUMENT_ERROR = 105;

	final Handler uiThreadCallback = new Handler();
    private ProgressDialog mProgressDialog;

	@Inject private ICouchDbUtils couchDbUtils;
	@Inject private MacrocibleListener listener;

	@InjectExtra(value="document", optional=true) private Document document = null;
	@InjectExtra("folder") private Folder folder;

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
	@InjectResource(R.string.dialog_update_document_time_elapsed_error_title) private String dialogUpdateDocumentTimeElapsedErrorTitle;
	@InjectResource(R.string.dialog_update_document_time_elapsed_error_message) private String dialogUpdateDocumentTimeElapsedErrorMessage;
	@InjectResource(R.string.ok) private String ok;
	@InjectResource(R.string.cancel) private String cancel;
	@InjectResource(R.string.save_loading) private String saveLoading;
	@InjectResource(R.string.save_error_title) private String saveErrorTitle;
	@InjectResource(R.string.save_error_message) private String saveErrorMessage;
	@InjectResource(R.string.macrocible_document) private String macrocibleDocumentTitle;
	@InjectResource(R.string.dialog_delete_document_time_elapsed_error_title) private String dialogDeleteDocumentTimeElapsedErrorTitle;
	@InjectResource(R.string.dialog_delete_document_time_elapsed_error_message) private String dialogDeleteDocumentTimeElapsedErrorMessage;
	@InjectResource(R.string.delete) private String deleteTitle;
	@InjectResource(R.string.delete_document_message) private String deleteDocumentMessage;
	@InjectResource(R.string.deleting) private String deleting;
	@InjectResource(R.string.delete_error_title) private String deleteErrorTitle;
	@InjectResource(R.string.delete_document_error_message) private String deleteDocumentErrorMessage;

	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		Log.d("MacrocibleActivity.onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.macrocible);
		listener.setContext(this);
		
		if(document!=null)
		{
			setFieldsDisabled();
		}

		updateButton.setOnClickListener(listener);
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
	        case DIALOG_UPDATE_DOCUMENT_TIME_ELAPSED:
				Log.d("Display DIALOG_UPDATE_DOCUMENT_TIME_ELAPSED Alert");
	            return new AlertDialog.Builder(this)
	                .setTitle(dialogUpdateDocumentTimeElapsedErrorTitle)
	                .setMessage(dialogUpdateDocumentTimeElapsedErrorMessage)
	                .setNegativeButton(ok, null)
	                .create();
	        case DIALOG_DELETE_DOCUMENT_TIME_ELAPSED:
				Log.d("Display DIALOG_DELETE_DOCUMENT_TIME_ELAPSED Alert");
	            return new AlertDialog.Builder(this)
	                .setTitle(dialogDeleteDocumentTimeElapsedErrorTitle)
	                .setMessage(dialogDeleteDocumentTimeElapsedErrorMessage)
	                .setNegativeButton(ok, null)
	                .create();
			case DIALOG_SAVE_DOCUMENT_ERROR:
				Log.d("Display DIALOG_SAVE_DOCUMENT_ERROR Alert");
				return new AlertDialog.Builder(this)
					.setTitle(saveErrorTitle)
					.setMessage(saveErrorMessage)
					.setPositiveButton(ok, null)
					.create();
			case DIALOG_DELETE_DOCUMENT_ERROR:
				Log.d("Display DIALOG_DELETE_DOCUMENT_ERROR Alert");
				return new AlertDialog.Builder(this)
					.setTitle(deleteErrorTitle)
					.setMessage(deleteDocumentErrorMessage)
					.setPositiveButton(ok, null)
					.create();
        }
        return null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		Log.d("MacrocibleActivity.onCreateOptionsMenu");
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.macrocible_document, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		Log.d("MacrocibleActivity.onOptionsItemSelected");
		switch(item.getItemId())
		{
			case R.id.delete:
				Log.d("Delete menu pressed");
				if(document != null)
				{
					Date now = new Date();
					// Calculate the difference between now and the date of creation of the document and pass only if delay < 15 minutes
					if(now.getTime() - document.getDate().getTime() > 900000)
					{
						// Display DIALOG_DELETE_DOCUMENT_TIME_ELAPSED Alert
						showDialog(DIALOG_DELETE_DOCUMENT_TIME_ELAPSED);
					}
					// Else
					else
					{
						Log.d("Display delete confirm alert");
						new AlertDialog.Builder(this)
							.setTitle(deleteTitle)
							.setMessage(deleteDocumentMessage)
							.setPositiveButton(ok, new DialogInterface.OnClickListener()
							{
								@Override
								public void onClick(DialogInterface dialog, int which)
								{
									// Launch an indeterminate ProgressBar in the UI while deleting the document in a new thread
							    	mProgressDialog = ProgressDialog.show(MacrocibleActivity.this, "", deleting, true);

							    	// Create a Runnable that will be executed if the delete fails
							    	final Runnable deleteDocumentFailed = new Runnable()
							    	{
							    		public void run()
							    		{
							    			Log.d("Delete document failed");
							    			// Hide the ProgressBar and open an Alert with an error message
							    			mProgressDialog.dismiss();
							    			showDialog(DIALOG_DELETE_DOCUMENT_ERROR);
							    		}
							    	};

							    	// Create the separate thread that will delete the document and start it
									new Thread()
									{
										@Override public void run()
										{
											try
											{
								    			Log.d("Delete the document");
												couchDbUtils.delete(document);

												((NursingFolderActivity)getParent()).deleteDocumentFromTrack(document);
												mProgressDialog.dismiss();
												finish();
											}
											catch (Exception e)
											{
												Log.e("Delete document failed", e);
												uiThreadCallback.post(deleteDocumentFailed);
											}
										}
									}.start();
								}
							})
							.setNegativeButton(cancel, null)
							.create()
							.show();
					}
				}
				else
				{
					finish();
				}
				return true;
			default:
				Log.d("Unknown menu pressed");
				return super.onOptionsItemSelected(item);
		}
	}

	// Create a Runnable that will be executed if the creation or update fails
	final Runnable documentSaveOrUpdateFailed = new Runnable()
	{
		public void run()
		{
			// Hide the ProgressBar and open an Alert with an error message
			mProgressDialog.dismiss();
			showDialog(DIALOG_SAVE_DOCUMENT_ERROR);
		}
	};

	/**
	 * Save a new document into the database
	 */
	public void createDocument()
	{
		Log.d("TransActivity.createDocument");
		// Launch an indeterminate ProgressBar in the UI while creating the document in a new thread
    	mProgressDialog = ProgressDialog.show(this, "", saveLoading, true);

    	// Create the separate thread that will create the document and start it
		new Thread()
		{
			@Override public void run()
			{
				try
				{
	    			Log.d("Create the document");
	    			document = new Document();
	    			document.setId(UUID.randomUUID().toString());
	    			document.setType(TemplateActivityMapper.MACROCIBLE.toString());
	    			SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	    			document.setTitle(macrocibleDocumentTitle+" "+s.format(document.getDate()));
					couchDbUtils.create(fillDocument());

					((NursingFolderActivity)getParent()).addDocument(document);
					mProgressDialog.dismiss();
					uiThreadCallback.post(((NursingFolderActivity)getParent()).documentCreationSucceeded);
				}
				catch (Exception e)
				{
					Log.e("Create record failed", e);
					uiThreadCallback.post(documentSaveOrUpdateFailed);
				}
			}
		}.start();
	}

	/**
	 * Update the document
	 */
	public void updateDocument()
	{
		Log.d("TransActivity.updateDocument");
		// Launch an indeterminate ProgressBar in the UI while updating the document in a new thread
    	mProgressDialog = ProgressDialog.show(this, "", saveLoading, true);

    	// Create a Runnable that will be executed if the creation succeeds
    	final Runnable documentUpdateSucceeded = new Runnable()
    	{
    		public void run()
    		{
    			Log.d("Document updated successfully");
    			// Hide the ProgressBar and render the document
    			mProgressDialog.dismiss();
    			setFieldsDisabled();
    		}
    	};

    	// Create the separate thread that will create the document and start it
		new Thread()
		{
			@Override public void run()
			{
				try
				{
	    			Log.d("Create the document");
	    			document = new Document(document.getId(), document.getRevision(), document.getDate());
	    			document.setType(TemplateActivityMapper.MACROCIBLE.toString());
	    			SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	    			document.setTitle(macrocibleDocumentTitle+" "+s.format(document.getDate()));
					couchDbUtils.update(fillDocument());

					uiThreadCallback.post(documentUpdateSucceeded);
				}
				catch (Exception e)
				{
					Log.e("Update record failed", e);
					uiThreadCallback.post(documentSaveOrUpdateFailed);
				}
			}
		}.start();
	}
	
	/**
	 * Return the document filled
	 * @return a Document object
	 */
	private Document fillDocument()
	{
		Log.d("MacrocibleActivity.fillDocument");
		document.setFolderId(folder.getId());
		document.add("initial_evaluation", getInitialEvaluationCheckBox());
		document.add("remote_evaluation", getRemoteEvaluationCheckBox());
		document.add("liaison", getLiaisonCheckBox());
		if(!getM().equals(""))
		{
			document.add("m", getM());
		}
		if(!getT().equals(""))
		{
			document.add("t", getT());
		}
		if(!getV().equals(""))
		{
			document.add("v", getV());
		}
		if(!getE().equals(""))
		{
			document.add("e", getE());
		}
		if(!getD().equals(""))
		{
			document.add("d", getD());
		}
		
		return document;
	}
    
    /**
     * Enable all the fields to accept user input, hide updateButton and show validateButton
     */
    public void setFieldsEnabled()
    {
		Log.d("MacrocibleActivity.setFieldsEnabled");
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
		Log.d("MacrocibleActivity.setFieldsDisabled");
		// Show the creation date of the document
		dateTextView.setVisibility(View.VISIBLE);
		SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		dateTextView.setText(s.format(document.getDate()));
		// Disable all the fields
		Map<String, Object> unknownFields = document.getUnknownFields();
		initialEvaluationCheckBox.setEnabled(false);
		if(unknownFields.get("initial_evaluation")!=null)
		{
			initialEvaluationCheckBox.setChecked((Boolean) unknownFields.get("initial_evaluation"));
		}
		remoteEvaluationCheckBox.setEnabled(false);
		if(unknownFields.get("remote_evaluation")!=null)
		{
			remoteEvaluationCheckBox.setChecked((Boolean) unknownFields.get("remote_evaluation"));
		}
		liaisonCheckBox.setEnabled(false);
		if(unknownFields.get("liaison")!=null)
		{
			liaisonCheckBox.setChecked((Boolean) unknownFields.get("liaison"));
		}
		mEditText.setEnabled(false);
		mEditText.setHint("");
		if(unknownFields.get("m")!=null)
		{
			mEditText.setText((String) unknownFields.get("m"));
		}
		tEditText.setEnabled(false);
		tEditText.setHint("");
		if(unknownFields.get("t")!=null)
		{
			tEditText.setText((String) unknownFields.get("t"));
		}
		vEditText.setEnabled(false);
		vEditText.setHint("");
		if(unknownFields.get("v")!=null)
		{
			vEditText.setText((String) unknownFields.get("v"));
		}
		eEditText.setEnabled(false);
		eEditText.setHint("");
		if(unknownFields.get("e")!=null)
		{
			eEditText.setText((String) unknownFields.get("e"));
		}
		dEditText.setEnabled(false);
		dEditText.setHint("");
		if(unknownFields.get("d")!=null)
		{
			dEditText.setText((String) unknownFields.get("d"));
		}
		// Inverse buttons visibilities
		validateButton.setVisibility(View.GONE);
		updateButton.setVisibility(View.VISIBLE);
    }

	/**
	 * @return the document
	 */
	public Document getDocument() {
		return document;
	}

	/**
	 * @return the initialEvaluationCheckBox value
	 */
	private boolean getInitialEvaluationCheckBox() {
		return initialEvaluationCheckBox.isChecked();
	}

	/**
	 * @return the remoteEvaluationCheckBox value
	 */
	private boolean getRemoteEvaluationCheckBox() {
		return remoteEvaluationCheckBox.isChecked();
	}

	/**
	 * @return the liaisonCheckBox value
	 */
	private boolean getLiaisonCheckBox() {
		return liaisonCheckBox.isChecked();
	}

	/**
	 * @return the mEditText value
	 */
	public String getM() {
		return mEditText.getText().toString();
	}

	/**
	 * @return the tEditText value
	 */
	public String getT() {
		return tEditText.getText().toString();
	}

	/**
	 * @return the vEditText value
	 */
	public String getV() {
		return vEditText.getText().toString();
	}

	/**
	 * @return the eEditText value
	 */
	public String getE() {
		return eEditText.getText().toString();
	}

	/**
	 * @return the dEditText value
	 */
	public String getD() {
		return dEditText.getText().toString();
	}
}