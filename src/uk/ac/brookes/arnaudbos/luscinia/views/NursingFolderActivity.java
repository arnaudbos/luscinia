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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.ektorp.ViewQuery;
import org.miscwidgets.widget.EasingType.Type;
import org.miscwidgets.widget.ExpoInterpolator;
import org.miscwidgets.widget.Panel;

import roboguice.inject.InjectExtra;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import uk.ac.brookes.arnaudbos.luscinia.LusciniaApplication;
import uk.ac.brookes.arnaudbos.luscinia.R;
import uk.ac.brookes.arnaudbos.luscinia.data.Document;
import uk.ac.brookes.arnaudbos.luscinia.data.Folder;
import uk.ac.brookes.arnaudbos.luscinia.data.Patient;
import uk.ac.brookes.arnaudbos.luscinia.listeners.NursingFolderListener;
import uk.ac.brookes.arnaudbos.luscinia.utils.ICouchDbUtils;
import uk.ac.brookes.arnaudbos.luscinia.utils.Log;
import uk.ac.brookes.arnaudbos.luscinia.utils.TemplateActivityMapper;
import uk.ac.brookes.arnaudbos.luscinia.widget.DocumentView;
import uk.ac.brookes.arnaudbos.luscinia.widget.FolderActivityGroup;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.inject.Inject;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;

/**
 * Activity displaying a nursing folder and attached documents
 * @author arnaudbos
 */
public class NursingFolderActivity extends FolderActivityGroup
{
	public static final int DIALOG_LOAD_ERROR = 101;
	public static final int DIALOG_ADD_DOCUMENT = 102;
	public static final int DIALOG_CREATE_DOCUMENT_ERROR = 103;

	public static final int ADD_TRANS_DOCUMENT = 0;
	public static final int ADD_MACROCIBLE_DOCUMENT = 1;

	final Handler uiThreadCallback = new Handler();
    private ProgressDialog mProgressDialog;
    private List<Document> documents = new ArrayList<Document>();

	@Inject private ICouchDbUtils couchDbUtils;
    @Inject private NursingFolderListener listener;

    @InjectExtra("patient") private Patient patient;
	@InjectExtra("folder") private Folder folder;

	@InjectView(R.id.actionbar) private ActionBar actionBar;
	@InjectView(R.id.internal_content) private RelativeLayout internalContentLayout;
	@InjectView(R.id.tracks) private LinearLayout documentsTrack;
	@InjectView(R.id.patient_picture) private ImageView patientPictureView;
	@InjectView(R.id.patient_first_infos) private TextView patientFirstInfosView;
	@InjectView(R.id.patient_rest_infos) private TextView patientRestInfosView;
	@InjectView(R.id.bottomPanel) Panel bottomPanel;
	@InjectView(R.id.bottomPanel) Panel leftPanel;
	@InjectView(R.id.button_add) ImageButton addDocumentButton;
	
	@InjectResource(R.string.ok) private String ok;
	@InjectResource(R.string.folder_loading) private String folderLoading;
	@InjectResource(R.string.load_error_title) private String loadErrorTitle;
	@InjectResource(R.string.load_error_message) private String loadErrorMessage;
	@InjectResource(R.string.trans_document) private String transDocumentTitle;
	@InjectResource(R.string.week) private String week;
	@InjectResource(R.string.create_loading) private String createLoading;
	@InjectResource(R.string.create_error_title) private String createErrorTitle;
	@InjectResource(R.string.create_document_error_message) private String createDocumentErrorMessage;
	@InjectResource(R.string.add_document) private String addDocument;
	@InjectResource(R.drawable.no_folder_picture) private Drawable noFolderPicture;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		Log.d("NursingFolderActivity.onCreate");
		super.onCreate(savedInstanceState);
        setContentView(R.layout.folder);
        listener.setContext(this);
		this.setChildActivityPlaceHolder(internalContentLayout);

        // Initialize the actionBar
        initActionBar();        
        bottomPanel.setInterpolator(new ExpoInterpolator(Type.OUT));
        leftPanel.setInterpolator(new ExpoInterpolator(Type.OUT));

        // Render the patient into the view
		renderPatientInfos();
		// Load the folder's document from the database
		loadDocuments();

		addDocumentButton.setOnClickListener(listener);
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
		Log.d("NursingFolderActivity.onCreateOptionsMenu");
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.nursing_folder, menu);

		Activity currentActivity = getLocalActivityManager().getCurrentActivity();
	   	if(currentActivity != null)
	   	{
	   		return currentActivity.onCreateOptionsMenu(menu);
	   	}
	   	return true;
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		Log.d("NursingFolderActivity.onOptionsItemSelected");
		switch(item.getItemId())
		{
			case R.id.add:
				Log.d("Add menu pressed");
				addDocumentButton.performClick();
				return true;
			default:
				Log.d("Unknown menu pressed, check child menus");
				Activity currentActivity = getLocalActivityManager().getCurrentActivity();
			   	if(currentActivity != null)
			   	{
			   		return currentActivity.onOptionsItemSelected(item);
			   	}
			   	return true;
		}
	}

	@Override	
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		Log.d("NursingFolderActivity.onPrepareOptionsMenu");
        menu.clear();
        return onCreateOptionsMenu(menu);
    }

	@Override
	protected Dialog onCreateDialog(int id)
	{
		Log.d("NursingFolderActivity.onCreateDialog");
		switch(id)
		{
			case DIALOG_LOAD_ERROR:
				Log.d("Display DIALOG_LOAD_ERROR Alert");
				return new AlertDialog.Builder(this)
					.setTitle(loadErrorTitle)
					.setMessage(loadErrorMessage)
					.setPositiveButton(ok, null)
					.create();
	        case DIALOG_ADD_DOCUMENT:
				Log.d("Display DIALOG_ADD_DOCUMENT Alert");
	            return new AlertDialog.Builder(this)
	                .setItems(R.array.folder_add_document_dialog, listener)
	                .create();
			case DIALOG_CREATE_DOCUMENT_ERROR:
				Log.d("Display DIALOG_CREATE_DOCUMENT_ERROR Alert");
				return new AlertDialog.Builder(this)
					.setTitle(createErrorTitle)
					.setMessage(createDocumentErrorMessage)
					.setPositiveButton(ok, null)
					.create();
		}
		return null;
	}

	/**
	 * Initialize the actionBar
	 */
	private void initActionBar()
	{
        actionBar.setTitle(folder.getTitle());
        actionBar.setHomeAction(new HomeAction());
//        actionBar.addAction(new StubShareAction());
//        actionBar.addAction(new StubSearchAction());
	}

	/**
	 * Render the patient's infos into the patientFirstInfosView and patientRestInfosView
	 */
	private void renderPatientInfos()
	{
		Log.d("NursingFolderActivity.renderPatientInfos");
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
	 * Load the documents from database
	 */
	public void loadDocuments()
	{
		Log.d("NursingFolderActivity.loadDocuments");
		// Launch an indeterminate ProgressBar in the UI while getting the documents in a new thread
    	mProgressDialog = ProgressDialog.show(this, "", folderLoading, true);

    	int count = documentsTrack.getChildCount()-1;
		for(int i = count ; i>0 ; i--)
		{
			documentsTrack.removeViewAt(0);
		}

    	// Create a Runnable that will be executed if the getting succeeds
    	final Runnable threadCallBackSuceeded = new Runnable()
    	{
    		public void run()
    		{
    			Log.d("Documents loaded successfully");
    			// Hide the ProgressBar and render the documentsTrack view
    			mProgressDialog.dismiss();

    			// For each document in the documents list create a DocumentView with an OnClickListener and add it to the documentsTrack view
    	    	for(final Document document : documents)
    	    	{
    	    		Log.d("Adding document "+document.getTitle()+" to the track");
    	    		renderDocumentInTrack(document);
    	    	}
    		}
    	};

    	// Create a Runnable that will be executed if the getting fails
    	final Runnable threadCallBackFailed = new Runnable()
    	{
    		public void run()
    		{
    			Log.d("Documents loading failed");
    			// Hide the ProgressBar and open an Alert with an error message
    			mProgressDialog.dismiss();
    			showDialog(DIALOG_LOAD_ERROR);
    		}
    	};

    	// Create the separate thread that will load the documents and start it
		new Thread()
		{
			@Override public void run()
			{
				try
				{
	    			Log.d("Query "+Document.VIEW_ALL_DOCUMENTS+" view with key="+folder.getId());
					// Execute the view query and retrieve the documents
	    			documents = new ArrayList<Document>();
					documents.addAll(couchDbUtils.queryView(Document.VIEW_ALL_DOCUMENTS, Document.class, folder.getId()));
					uiThreadCallback.post(threadCallBackSuceeded);
				}
				catch (Exception e)
				{
					Log.e("Load documents failed", e);
					uiThreadCallback.post(threadCallBackFailed);
				}
			}
		}.start();
	}

	/**
	 * Render the given document into the documentsTrack view
	 */
    private void renderDocumentInTrack(Document document)
    {
		Log.d("NursingFolderActivity.renderDocumentInTrack");
		// Create a new documentView from current document
    	DocumentView doc = new DocumentView(this, noFolderPicture, document.getTitle());
    	doc.setTag(document);
		switch (TemplateActivityMapper.toActivity(document.getType()))
		{
			case TRANS:
				Log.d("Document is type TRANS");
				// Create a new listener that will start a TransActivity when clicked
				doc.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						Document document = (Document)v.getTag();
						Log.d("NursingFolderActivity document \""+document.getTitle()+"\" onClick");
				        actionBar.setTitle(folder.getTitle()+" - "+document.getTitle());
				        Intent intent = new Intent(NursingFolderActivity.this, TransActivity.class);
				        intent.putExtra("document", document);
				        startChildActivity(document.getId(), intent);
				        // Retract the documentsTrack
				        bottomPanel.getHandle().performClick();
					}
				});
				break;
			case MACROCIBLE:
				Log.d("Document is type MACROCIBLE");
				// Create a new listener that will start a MacrocibleActivity when clicked
				doc.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						Document document = (Document)v.getTag();
						Log.d("NursingFolderActivity document \""+document.getTitle()+"\" onClick");
				        actionBar.setTitle(folder.getTitle()+" - "+document.getTitle());
				        Intent intent = new Intent(NursingFolderActivity.this, MacrocibleActivity.class);
				        intent.putExtra("document", document);
				        intent.putExtra("folder", folder);
				        startChildActivity(document.getId(), intent);
				        // Retract the documentsTrack
				        bottomPanel.getHandle().performClick();
					}
				});
				break;
			case NURSING_DIAGRAM:
				Log.d("Document is type NURSING_DIAGRAM");
				// Create a new listener that will start a NursingDiagramActivity when clicked
				doc.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						Document document = (Document)v.getTag();
						Log.d("NursingFolderActivity document \""+document.getTitle()+"\" onClick");
				        actionBar.setTitle(folder.getTitle()+" - "+document.getTitle());
				        Intent intent = new Intent(NursingFolderActivity.this, NursingDiagramActivity.class);
				        intent.putExtra("document", document);
				        startChildActivity(document.getId(), intent);
				        // Retract the documentsTrack
				        bottomPanel.getHandle().performClick();
					}
				});
				break;
			case GENERIC:
			default:
				break;
		}
		// Add the documentView to the track
		Log.d("Add document \""+document.getTitle()+"\"");
		documentsTrack.addView(doc, documentsTrack.getChildCount()-1);
	}

	// Create a Runnable that will be executed if the document creation succeeds
	final Runnable documentCreationSucceeded = new Runnable()
	{
		public void run()
		{
			Log.d("Document created successfully");
			// Add the document to the track
			renderDocumentInTrack(documents.get(documents.size()-1));
		}
	};

	// Create a Runnable that will be executed if the document creation fails
	final Runnable documentCreationFailed = new Runnable()
	{
		public void run()
		{
			Log.d("Create document failed");
			// Hide the ProgressBar and open an Alert with an error message
			mProgressDialog.dismiss();
			showDialog(DIALOG_CREATE_DOCUMENT_ERROR);
		}
	};

	/**
	 * Create a new Trans document into the database and add it to the documents track
	 */
	public void addNewTransDocument()
	{
		Log.d("NursingFolderActivity.addNewTransDocument");
		// Launch an indeterminate ProgressBar in the UI while creating the document in a new thread
    	mProgressDialog = ProgressDialog.show(this, "", createLoading, true);

    	// Create the separate thread that will create the trans document and start it
		new Thread()
		{
			@Override public void run()
			{
				try
				{
	    			Log.d("Create the new TRANS document");
					Document trans = new Document();
					trans.setId(UUID.randomUUID().toString());
					trans.setType(TemplateActivityMapper.TRANS.toString());
					Calendar cal = Calendar.getInstance();
					cal.setTime(trans.getDate());
					trans.setTitle(transDocumentTitle+" "+week+" "+cal.get(Calendar.WEEK_OF_YEAR));
					trans.setFolderId(folder.getId());
					couchDbUtils.create(trans);

					// Add the new document to the list of documents
					addDocument(trans);
					mProgressDialog.dismiss();
					uiThreadCallback.post(documentCreationSucceeded);
				}
				catch (Exception e)
				{
					Log.e("Create document failed", e);
					uiThreadCallback.post(documentCreationFailed);
				}
			}
		}.start();
	}

	/**
	 * Start a new Macrocible document activity into the child activities placeHolder
	 */
	public void addNewMacrocibleDocument()
	{
		Log.d("NursingFolderActivity.addNewMacrocibleDocument");
        actionBar.setTitle(folder.getTitle()+" - "+addDocument);
        Intent intent = new Intent(this, MacrocibleActivity.class);
        intent.putExtra("folder", folder);
        startChildActivity(addDocument, intent);
	};
	
	public void addDocument(Document document)
	{
		if(documents!=null)
		{
			documents.add(document);
		}
	}
	
	public void deleteDocumentFromTrack(Document document)
	{
		documentsTrack.removeView(documentsTrack.findViewWithTag(document));
	}

	/**
	 * @return the documentsTrack
	 */
	public LinearLayout getDocumentsTrack()
	{
		return documentsTrack;
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
			Intent intent = new Intent(NursingFolderActivity.this, DashboardActivity.class);
			startActivity(intent);
			NursingFolderActivity.this.finish();
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
			Toast.makeText(NursingFolderActivity.this, "ShareAction", Toast.LENGTH_SHORT).show();
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
			Toast.makeText(NursingFolderActivity.this, "otherAction", Toast.LENGTH_SHORT).show();
		}
		
		@Override
		public int getDrawable()
		{
			return R.drawable.ic_title_export_default;
		}
	}
}
