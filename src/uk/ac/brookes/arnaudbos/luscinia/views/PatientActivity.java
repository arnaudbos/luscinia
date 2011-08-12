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
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.ektorp.AttachmentInputStream;
import org.ektorp.ViewQuery;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import uk.ac.brookes.arnaudbos.luscinia.LusciniaApplication;
import uk.ac.brookes.arnaudbos.luscinia.R;
import uk.ac.brookes.arnaudbos.luscinia.adapters.PatientDocumentAdapter;
import uk.ac.brookes.arnaudbos.luscinia.adapters.PatientFolderAdapter;
import uk.ac.brookes.arnaudbos.luscinia.data.Document;
import uk.ac.brookes.arnaudbos.luscinia.data.Folder;
import uk.ac.brookes.arnaudbos.luscinia.data.Patient;
import uk.ac.brookes.arnaudbos.luscinia.listeners.PatientListener;
import uk.ac.brookes.arnaudbos.luscinia.utils.Log;
import uk.ac.brookes.arnaudbos.luscinia.utils.TemplateActivityMapper;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.inject.Inject;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;

/**
 * Activity displaying patient's informations, attached documents and associated folders
 * @author arnaudbos
 */
public class PatientActivity extends RoboActivity
{
	public static final int DIALOG_LOAD_ERROR = 101;
	public static final int DIALOG_ADD_FOLDER = 102;
	public static final int DIALOG_CREATE_FOLDER_ERROR = 103;
	public static final int DIALOG_DELETE_FOLDER = 104;
	public static final int DIALOG_DELETE_FOLDER_ERROR = 105;

	public static final int ADD_ADMIN_FOLDER = 0;
	public static final int ADD_NURSING_FOLDER = 1;
	public static final int ADD_MEDICAL_FOLDER = 2;

	final Handler uiThreadCallback = new Handler();
    private ProgressDialog mProgressDialog;
    private List<AttachmentInputStream> attachments = new ArrayList<AttachmentInputStream>();
    private List<Folder> folders = new ArrayList<Folder>();

    @Inject private PatientListener listener;
	
	@InjectExtra("patient") private Patient patient;

	@InjectResource(R.string.patient_title) private String patientTitle;
	
	@InjectView(R.id.actionbar) private ActionBar actionBar;
	@InjectView(R.id.folders_listview) private ListView foldersListView;
	@InjectView(R.id.patient_picture) private ImageView patientPictureView;
	@InjectView(R.id.patient_first_infos) private TextView patientFirstInfosView;
	@InjectView(R.id.patient_rest_infos) private TextView patientRestInfosView;
	@InjectView(R.id.attached_documents) private GridView attachedDocumentsView;

	@InjectResource(R.string.ok) private String ok;
	@InjectResource(R.string.cancel) private String cancel;
	@InjectResource(R.string.patient_loading) private String patientLoading;
	@InjectResource(R.string.load_error_title) private String loadErrorTitle;
	@InjectResource(R.string.load_error_message) private String loadErrorMessage;
	@InjectResource(R.string.create_error_title) private String createErrorTitle;
	@InjectResource(R.string.create_folder_error_message) private String createFolderErrorMessage;
	@InjectResource(R.string.create_loading) private String createLoading;
	@InjectResource(R.string.delete) private String deleteFolderTitle;
	@InjectResource(R.string.delete_folder_message) private String deleteFolderMessage;
	@InjectResource(R.string.deleting) private String deleting;
	@InjectResource(R.string.delete_error_title) private String deleteErrorTitle;
	@InjectResource(R.string.delete_folder_error_message) private String deleteFolderErrorMessage;

	@InjectResource(R.string.administrative_folder) private String administrativeFolder;
	@InjectResource(R.string.nursing_cares_folder) private String nursingCaresFolder;
	@InjectResource(R.string.medical_folder) private String medicalFolder;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		Log.d("PatientActivity.onCreate");
		super.onCreate(savedInstanceState);
        setContentView(R.layout.patient);
        listener.setContext(this);

        // Initialize the actionBar
        initActionBar();
        // Load the patient from the database
        loadPatient();
	}

	/**
	 * Initialize the actionBar
	 */
	private void initActionBar()
	{
		Log.d("PatientActivity.initActionBar");
        actionBar.setTitle(patientTitle+" "+patient.getFirstname()+" "+patient.getLastname());
        actionBar.setHomeAction(new HomeAction());
//        actionBar.addAction(new StubShareAction());
//        actionBar.addAction(new StubSearchAction());
	}

	/**
	 * Load the patient from database
	 */
	private void loadPatient()
	{
		Log.d("PatientActivity.loadPatient");
		// Launch an indeterminate ProgressBar in the UI while getting the patient in a new thread
    	mProgressDialog = ProgressDialog.show(this, "", patientLoading, true);

    	// Create a Runnable that will be executed if the getting succeeds
    	final Runnable threadCallBackSuceeded = new Runnable()
    	{
    		public void run()
    		{
    			Log.d("Patient loaded successfully");
    			// Hide the ProgressBar and render the views
    			mProgressDialog.dismiss();
    	        renderPatientInfos();
    	        renderAttachedDocumentsView();
    	        renderFoldersListView();
    		}
    	};

    	// Create a Runnable that will be executed if the getting fails
    	final Runnable threadCallBackFailed = new Runnable()
    	{
    		public void run()
    		{
    			Log.d("Patient loading failed");
    			// Hide the ProgressBar and open an Alert with an error message
    			mProgressDialog.dismiss();
    			showDialog(DIALOG_LOAD_ERROR);
    		}
    	};

    	// Create the separate thread that will load the patient and start it
		new Thread()
		{
			@Override public void run()
			{
				try
				{
	    			Log.d("Load the patient id="+patient.getId());
	    			// Get the patient
					patient = LusciniaApplication.getDB().get(Patient.class, patient.getId(), patient.getRevision());

					// Get all its attachments
					if(patient.getAttachments() != null)
					{
						attachments = new ArrayList<AttachmentInputStream>();
						for (Iterator<String> it = patient.getAttachments().keySet().iterator() ; it.hasNext() ;)
						{
							String attachment_id = it.next();
			    			Log.d("Load the attachment id="+attachment_id);
							AttachmentInputStream attachment = LusciniaApplication.getDB().getAttachment(patient.getId(), attachment_id);
							attachments.add(attachment);
						}
					}

	    			Log.d("Query "+Folder.VIEW_ALL_FOLDERS+" view with key="+patient.getId());
					// Execute the view query and retrieve the folders
	    			folders = new ArrayList<Folder>();
					folders.addAll(LusciniaApplication.getDB().queryView(new ViewQuery().designDocId("_design/views").viewName(Folder.VIEW_ALL_FOLDERS).key(patient.getId()), Folder.class));
					uiThreadCallback.post(threadCallBackSuceeded);
				}
				catch (Exception e)
				{
					Log.e("Load patient failed", e);
					uiThreadCallback.post(threadCallBackFailed);
				}
			}
		}.start();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		Log.d("PatientActivity.onActivityResult");
		switch (resultCode)
		{
			case RESULT_OK:
				Log.d("Result code OK");
				// Deserialize the updated patient and render its infos
				patient = (Patient)data.getSerializableExtra("patient");
		        renderPatientInfos();
				break;
			default:
				break;
		}
	}

	/**
	 * Render the patient's infos into the patientFirstInfosView and patientRestInfosView
	 */
	private void renderPatientInfos()
	{
		Log.d("PatientActivity.renderPatientInfos");
		// Fill the patientFirstInfosView with the known fields of the patient
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String firstInfos = "";
		if(patient.getDateOfBirth()!=null)
		{
			firstInfos += "\n" + dateFormat.format(patient.getDateOfBirth());
		}
		if(patient.getInsee()!=null)
		{
			firstInfos += "\n" + patient.getInsee();
		}
		if(patient.getTelephone()!=null)
		{
			firstInfos += "\n" + patient.getTelephone();
		}
		if(patient.getWeight()!=null)
		{
			firstInfos += "\n" + Double.toString(patient.getWeight());
		}
		if(patient.getSize()!=null)
		{
			firstInfos += "\n" + Double.toString(patient.getSize());
		}
		patientFirstInfosView.setText(firstInfos);

		// Fill the patientRestInfosView with the unknown fields of the patient
		String restInfos = "";
		for (Map.Entry<String, Object> entry : patient.getUnknownFields().entrySet())
		{
			restInfos += entry.getKey() + " " + entry.getValue() + "\n";
		}
		patientRestInfosView.setText(restInfos);
	}

	/**
	 * Render the attached documents into the GridView
	 */
	private void renderAttachedDocumentsView()
	{
		Log.d("PatientActivity.renderAttachedDocumentsView");
		// Set GridView adapter
		PatientDocumentAdapter adapter = new PatientDocumentAdapter(PatientActivity.this, attachments);
        attachedDocumentsView.setAdapter(adapter);
	    
	    // Manually calculate the size of the GridView to display and scroll properly depending on screen resolution
	    setGridViewSize(adapter);

	    // Set listeners on the GridView
        attachedDocumentsView.setOnItemClickListener(listener);
	}
	
	/**
	 * Manually calculate the size of the GridView to display and scroll properly depending on screen resolution
	 */
	private void setGridViewSize(PatientDocumentAdapter adapter)
	{
		Log.d("PatientActivity.setGridViewSize");
        
        LayoutParams params = attachedDocumentsView.getLayoutParams();
        int count = adapter.getCount();
        
        final float scale = getResources().getDisplayMetrics().density;
        int unit = (int) (160 * scale + 0.5f);
        
        if(count%3 == 0)
        {
        	params.height = count/3*unit;
        	attachedDocumentsView.setLayoutParams(params);
        }
        else
        {
        	params.height = (count/3+1)*unit;
        	attachedDocumentsView.setLayoutParams(params);
        }
	}

	/**
	 * Render the patient folders into the ListView
	 */
	private void renderFoldersListView()
	{
		Log.d("PatientActivity.renderFoldersListView");
		// Set ListView adapter
		PatientFolderAdapter adapter = new PatientFolderAdapter(this, folders);
        foldersListView.setAdapter(adapter);

	    // Set listeners on the ListView
        foldersListView.setOnItemClickListener(listener);
        foldersListView.setOnItemLongClickListener(listener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		Log.d("PatientActivity.onCreateOptionsMenu");
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.patient, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		Log.d("PatientActivity.onOptionsItemSelected");
		switch(item.getItemId())
		{
			case R.id.add_folder:
				Log.d("Add folder menu pressed");
				// Display DIALOG_ADD_FOLDER Alert
				showDialog(DIALOG_ADD_FOLDER);
				return true;
			case R.id.add_document:
				Log.d("Add document menu pressed");
				Toast.makeText(this, "Not implemented yet.", Toast.LENGTH_SHORT).show();
				return true;
			case R.id.edit:
				Log.d("Edit menu pressed");
				// Start EditPatientActivity for result with patient as Intent Extra
				Intent intent = new Intent(this, EditPatientActivity.class);
				intent.putExtra("patient", patient);
				Log.d("Start EditPatientActivity for result");
				startActivityForResult(intent, 0);
				return true;
			case R.id.refresh:
				Log.d("Refresh menu pressed");
				// Reload the patient from database
				loadPatient();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected Dialog onCreateDialog(int id)
	{
		Log.d("PatientActivity.onCreateDialog");
		switch(id)
		{
			case DIALOG_LOAD_ERROR:
				Log.d("Display DIALOG_LOAD_ERROR Alert");
				return new AlertDialog.Builder(this)
					.setTitle(loadErrorTitle)
					.setMessage(loadErrorMessage)
					.setPositiveButton(ok, null)
					.create();
	        case DIALOG_ADD_FOLDER:
				Log.d("Display DIALOG_ADD_FOLDER Alert");
	            return new AlertDialog.Builder(this)
	                .setItems(R.array.patient_add_folder_dialog, listener)
	                .create();
			case DIALOG_CREATE_FOLDER_ERROR:
				Log.d("Display DIALOG_CREATE_FOLDER_ERROR Alert");
				return new AlertDialog.Builder(this)
					.setTitle(createErrorTitle)
					.setMessage(createFolderErrorMessage)
					.setPositiveButton(ok, null)
					.create();
			case DIALOG_DELETE_FOLDER:
				Log.d("Display DIALOG_DELETE_FOLDER Alert");
				return new AlertDialog.Builder(this)
					.setTitle(deleteFolderTitle)
					.setMessage(deleteFolderMessage)
					.setPositiveButton(ok, listener)
					.setNegativeButton(cancel, null)
					.create();
			case DIALOG_DELETE_FOLDER_ERROR:
				Log.d("Display DIALOG_DELETE_FOLDER_ERROR Alert");
				return new AlertDialog.Builder(this)
					.setTitle(deleteErrorTitle)
					.setMessage(deleteFolderErrorMessage)
					.setPositiveButton(ok, null)
					.create();
		}
		return null;
	}
	
	/**
	 * Create a new nursing folder
	 */
	public void createNursingFolder()
	{
		Log.d("PatientActivity.createNursingFolder");
		// Launch an indeterminate ProgressBar in the UI while creating the nursing folder in a new thread
    	mProgressDialog = ProgressDialog.show(this, "", createLoading, true);

    	// Create a Runnable that will be executed if the creation succeeds
    	final Runnable folderCreationSucceeded = new Runnable()
    	{
    		public void run()
    		{
    			Log.d("Folder created successfully");
    			// Hide the ProgressBar and notify dataset changed to the folders ListView adapter
    			mProgressDialog.dismiss();
    			((PatientFolderAdapter)foldersListView.getAdapter()).notifyDataSetChanged();
    		}
    	};

    	// Create a Runnable that will be executed if the creation fails
    	final Runnable folderCreationFailed = new Runnable()
    	{
    		public void run()
    		{
    			Log.d("Create folder failed");
    			// Hide the ProgressBar and open an Alert with an error message
    			mProgressDialog.dismiss();
    			showDialog(DIALOG_CREATE_FOLDER_ERROR);
    		}
    	};

    	// Create the separate thread that will create the folder and start it
		new Thread()
		{
			@Override public void run()
			{
				try
				{
	    			Log.d("Create the folder");
					Folder newFolder = new Folder();
//					newFolder.setId(UUID.randomUUID().toString());
					newFolder.setPatientId(patient.getId());
					newFolder.setType(Folder.NURSING_FOLDER_TYPE);
					newFolder.setTitle(nursingCaresFolder);
					LusciniaApplication.getDB().create(newFolder);

//	    			Log.d("Create the default MACROCIBLE document associated");
//					Document macro = new Document();
//					macro.setType(TemplateActivityMapper.MACROCIBLE.toString());
//					macro.setTitle("Macrocible xyz");
//					macro.setFolderId(newFolder.getId());
//					LusciniaApplication.getDB().create(macro);
//
//	    			Log.d("Create the default TRANS document associated");
//					Document trans = new Document();
//					trans.setType(TemplateActivityMapper.TRANS.toString());
//					trans.setTitle("Transmissions ciblées week X");
//					trans.setFolderId(newFolder.getId());
//					LusciniaApplication.getDB().create(trans);

					// Add the folder to the list of folders
					folders.add(newFolder);
					uiThreadCallback.post(folderCreationSucceeded);
				}
				catch (Exception e)
				{
					Log.e("Create folder failed", e);
					uiThreadCallback.post(folderCreationFailed);
				}
			}
		}.start();
	}

	/**
	 * Delete the patient from the database
	 */
	public void deleteFolder(final Folder folder)
	{
		Log.d("PatientActivity.deleteFolder");
		// Launch an indeterminate ProgressBar in the UI while deleting the folder in a new thread
    	mProgressDialog = ProgressDialog.show(this, "", deleting, true);

    	// Create a Runnable that will be executed if the delete succeeds
    	final Runnable folderDeleteSucceeded = new Runnable()
    	{
    		public void run()
    		{
    			Log.d("Folder deleted successfully");
    			// Hide the ProgressBar and notify dataset changed to the folders ListView adapter
    			mProgressDialog.dismiss();
    			((PatientFolderAdapter)foldersListView.getAdapter()).notifyDataSetChanged();
    		}
    	};

    	// Create a Runnable that will be executed if the delete fails
    	final Runnable folderDeleteFailed = new Runnable()
    	{
    		public void run()
    		{
    			Log.d("Delete folder failed");
    			// Hide the ProgressBar and open an Alert with an error message
    			mProgressDialog.dismiss();
    			showDialog(DIALOG_DELETE_FOLDER_ERROR);
    		}
    	};

    	// Create the separate thread that will delete the folder and start it
		new Thread()
		{
			@Override public void run()
			{
				try
				{
	    			Log.d("Delete the folder and all associated documents");
					// Calculate the difference between now and the date of creation of the folder and pass only if delay < 15 minutes
					Date now = new Date();
					if(now.getTime() - folder.getDate().getTime() > 900000)
					{
						throw new Exception ("The 15 minutes delay allowed to delete a folder has elapsed.");
					}

					// For each document associated with the current folder, delete this document
					for(Document document : LusciniaApplication.getDB().queryView(new ViewQuery().designDocId("_design/views").viewName(Document.VIEW_ALL_DOCUMENTS).key(folder.getId()), Document.class))
					{
		    			Log.d("Delete the document: "+document.getId());
						LusciniaApplication.getDB().delete(document);
					}
	    			Log.d("Delete the folder: "+folder.getId());
					LusciniaApplication.getDB().delete(folder);

					// Remove folder from the list of folders
					folders.remove(folder);
					uiThreadCallback.post(folderDeleteSucceeded);
				}
				catch (Exception e)
				{
					Log.e("Delete folder failed", e);
					uiThreadCallback.post(folderDeleteFailed);
				}
			}
		}.start();
	}
	
	/**
	 * Depending on the folder type, launch the appropriate folder activity
	 * @param folder The folder selected
	 */
	public void launchFolderActivity(Folder folder)
	{
		Log.d("PatientActivity.launchFolderActivity");
		// Create a new intent witht the current patient and selected folder as extras
		Intent intent = new Intent();
		intent.putExtra("patient", patient);
		intent.putExtra("folder", folder);
		// Depending on the folder type, launch the appropriate folder activity
		switch (folder.getType())
		{
			case Folder.ADMINISTRATIVE_FOLDER_TYPE:
				Log.d("Folder type is ADMINISTRATIVE_FOLDER_TYPE");
				//intent.setClass(this, AdministrativeFolderActivity.class);
				break;
			case Folder.NURSING_FOLDER_TYPE:
				Log.d("Folder type is NURSING_FOLDER_TYPE");
				intent.setClass(this, NursingFolderActivity.class);
				break;
			case Folder.MEDICAL_FOLDER_TYPE:
				Log.d("Folder type is MEDICAL_FOLDER_TYPE");
				//intent.setClass(this, MedicalFolderActivity.class);
				break;
		}
		Log.d("Start the folder activity");
        startActivity(intent);
	}

	/**
	 * Action implemented to return to DahsboardActivity when associated ActionBar item is pressed
	 * @author arnaudbos
	 */
	private class HomeAction implements Action
    {
		@Override
		public void performAction(View view)
		{
			Intent intent = new Intent(PatientActivity.this, DashboardActivity.class);
			startActivity(intent);
			PatientActivity.this.finish();
		}
		
		@Override
		public int getDrawable()
		{
			return R.drawable.ic_title_home_default;
		}
	}

	/**
	 * TODO: Stub class. Implement it.
	 */
	private class StubShareAction implements Action
    {
		@Override
		public void performAction(View view)
		{
			Toast.makeText(PatientActivity.this, "ShareAction", Toast.LENGTH_SHORT).show();
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
			Log.i(view.getId()+"");
			Toast.makeText(PatientActivity.this, "otherAction", Toast.LENGTH_SHORT).show();
		}
		
		@Override
		public int getDrawable()
		{
			return R.drawable.ic_title_export_default;
		}
	};
}
