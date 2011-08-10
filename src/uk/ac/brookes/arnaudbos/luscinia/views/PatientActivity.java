package uk.ac.brookes.arnaudbos.luscinia.views;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
	@InjectResource(R.string.create_folder_error_title) private String createFolderErrorTitle;
	@InjectResource(R.string.create_folder_error_message) private String createFolderErrorMessage;
	@InjectResource(R.string.create_loading) private String createLoading;
	@InjectResource(R.string.delete) private String deleteFolderTitle;
	@InjectResource(R.string.delete_folder_message) private String deleteFolderMessage;
	@InjectResource(R.string.deleting) private String deleting;
	@InjectResource(R.string.delete_folder_error_title) private String deleteFolderErrorTitle;
	@InjectResource(R.string.delete_folder_error_message) private String deleteFolderErrorMessage;

	@InjectResource(R.string.administrative_folder) private String administrativeFolder;
	@InjectResource(R.string.nursing_cares_folder) private String nursingCaresFolder;
	@InjectResource(R.string.medical_folder) private String medicalFolder;

	final Runnable threadCallBackSuceeded = new Runnable()
	{
		public void run()
		{
			mProgressDialog.dismiss();
	        preparePatientInfos();
	        prepareAttachedDocumentsView();
	        prepareFoldersListView();
		}
	};

	final Runnable threadCallBackFailed = new Runnable()
	{
		public void run()
		{
			mProgressDialog.dismiss();
			showDialog(DIALOG_LOAD_ERROR);
		}
	};

	final Runnable folderCreationSucceeded = new Runnable()
	{
		public void run()
		{
			mProgressDialog.dismiss();
			((PatientFolderAdapter)foldersListView.getAdapter()).notifyDataSetChanged();
		}
	};

	final Runnable folderCreationFailed = new Runnable()
	{
		public void run()
		{
			mProgressDialog.dismiss();
			showDialog(DIALOG_CREATE_FOLDER_ERROR);
		}
	};

	final Runnable folderDeleteFailed = new Runnable()
	{
		public void run()
		{
			mProgressDialog.dismiss();
			showDialog(DIALOG_DELETE_FOLDER_ERROR);
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.patient);
        listener.setContext(this);

        prepareActionBar();
        loadPatient();
	}
	
	private void loadPatient()
	{
    	mProgressDialog = ProgressDialog.show(this, "", patientLoading, true);
		new Thread()
		{
			@Override public void run()
			{
				try
				{
					patient = LusciniaApplication.getDB().get(Patient.class, patient.getId(), patient.getRevision());
					if(patient.getAttachments() != null)
					{
						for (Iterator<String> it = patient.getAttachments().keySet().iterator() ; it.hasNext() ;)
						{
							String attachment_id = it.next();
							AttachmentInputStream attachment = LusciniaApplication.getDB().getAttachment(patient.getId(), attachment_id);
							attachments.add(attachment);
						}
					}
					folders.addAll(LusciniaApplication.getDB().queryView(new ViewQuery().designDocId("_design/views").viewName(Folder.VIEW_ALL_FOLDERS).key(patient.getId()), Folder.class));
					uiThreadCallback.post(threadCallBackSuceeded);
				}
				catch (Exception e)
				{
					Log.e("Load patient's attachments failed", e);
					uiThreadCallback.post(threadCallBackFailed);
				}
			}
		}.start();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch (resultCode)
		{
			case RESULT_OK:
				patient = (Patient)data.getSerializableExtra("patient");
		        preparePatientInfos();
				break;
			default:
				break;
		}
	}

	private void prepareActionBar()
	{
        actionBar.setTitle(patientTitle+" "+patient.getFirstname()+" "+patient.getLastname());
        actionBar.setHomeAction(new HomeAction());
        actionBar.addAction(new StubShareAction());
        actionBar.addAction(new StubSearchAction());
	}
	
	private void preparePatientInfos()
	{
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

		String restInfos = "";
		for (Map.Entry<String, Object> entry : patient.getProperties().entrySet())
		{
			restInfos += entry.getKey() + " " + entry.getValue() + "\n";
		}
		patientRestInfosView.setText(restInfos);
	}
	
	private void prepareAttachedDocumentsView()
	{
		PatientDocumentAdapter adapter = new PatientDocumentAdapter(PatientActivity.this, attachments);
        attachedDocumentsView.setAdapter(adapter);
        
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

        attachedDocumentsView.setOnItemClickListener(listener);
	}

	private void prepareFoldersListView()
	{
		PatientFolderAdapter adapter = new PatientFolderAdapter(this, folders);
        foldersListView.setAdapter(adapter);

        foldersListView.setOnItemClickListener(listener);
        foldersListView.setOnItemLongClickListener(listener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.patient, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
			case R.id.add_folder:
				showDialog(DIALOG_ADD_FOLDER);
				return true;
			case R.id.add_document:
				Toast.makeText(this, "Not implemented yet.", Toast.LENGTH_SHORT).show();
				return true;
			case R.id.edit:
				Intent intent = new Intent(this, EditPatientActivity.class);
				intent.putExtra("patient", patient);
				startActivityForResult(intent, 0);
				return true;
			case R.id.refresh:
				loadPatient();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected Dialog onCreateDialog(int id)
	{
		switch(id)
		{
			case DIALOG_LOAD_ERROR:
				return new AlertDialog.Builder(this)
					.setTitle(loadErrorTitle)
					.setMessage(loadErrorMessage)
					.setPositiveButton(ok, null)
					.create();
	        case DIALOG_ADD_FOLDER:
	            return new AlertDialog.Builder(this)
	                .setItems(R.array.patient_add_folder_dialog, listener)
	                .create();
			case DIALOG_CREATE_FOLDER_ERROR:
				return new AlertDialog.Builder(this)
					.setTitle(createFolderErrorTitle)
					.setMessage(createFolderErrorMessage)
					.setPositiveButton(ok, null)
					.create();
			case DIALOG_DELETE_FOLDER:
				return new AlertDialog.Builder(this)
					.setTitle(deleteFolderTitle)
					.setMessage(deleteFolderMessage)
					.setPositiveButton(ok, listener)
					.setNegativeButton(cancel, null)
					.create();
			case DIALOG_DELETE_FOLDER_ERROR:
				return new AlertDialog.Builder(this)
					.setTitle(deleteFolderErrorTitle)
					.setMessage(deleteFolderErrorMessage)
					.setPositiveButton(ok, null)
					.create();
		}
		return null;
	}
	
	public void createNursingFolder()
	{
    	mProgressDialog = ProgressDialog.show(this, "", createLoading, true);
		new Thread()
		{
			@Override public void run()
			{
				try
				{
					Folder newFolder = new Folder();
					newFolder.setId(UUID.randomUUID().toString());
					newFolder.setPatientId(patient.getId());
					newFolder.setDocType(Folder.FOLDER_TYPE);
					newFolder.setType(Folder.NURSING_FOLDER_TYPE);
					newFolder.setTitle(nursingCaresFolder);
					LusciniaApplication.getDB().create(newFolder);

					Document trans = new Document();
					trans.setDocType(Document.DOCUMENT_TYPE);
					trans.setType(TemplateActivityMapper.TRANS.toString());
					trans.setTitle("Transmissions ciblées week X");
					trans.setFolderId(newFolder.getId());
					LusciniaApplication.getDB().create(trans);

					Document macro = new Document();
					macro.setDocType(Document.DOCUMENT_TYPE);
					macro.setType(TemplateActivityMapper.MACROCIBLE.toString());
					macro.setTitle("Macrocible xyz");
					macro.setFolderId(newFolder.getId());
					LusciniaApplication.getDB().create(macro);

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
	
	public void deleteFolder(final Folder folder)
	{
    	mProgressDialog = ProgressDialog.show(this, "", deleting, true);
		new Thread()
		{
			@Override public void run()
			{
				try
				{
					Date now = new Date();
					if(now.getTime() - folder.getDate().getTime() > 900000)
					{
						throw new Exception ("The 15 minutes delay allowed to delete a folder has elapsed.");
					}

					for(Document document : LusciniaApplication.getDB().queryView(new ViewQuery().designDocId("_design/views").viewName(Document.VIEW_ALL_DOCUMENTS).key(folder.getId()), Document.class))
					{
						LusciniaApplication.getDB().delete(document);
					}
					LusciniaApplication.getDB().delete(folder);

					folders.remove(folder);
					uiThreadCallback.post(folderCreationSucceeded);
				}
				catch (Exception e)
				{
					Log.e("Delete folder failed", e);
					uiThreadCallback.post(folderDeleteFailed);
				}
			}
		}.start();
	}
	
	public void launchFolderActivity(Folder folder)
	{
		Intent intent = new Intent();
		intent.putExtra("patient", patient);
		intent.putExtra("folder", folder);
		switch (folder.getType())
		{
			case Folder.ADMINISTRATIVE_FOLDER_TYPE:
				//intent.setClass(this, AdministrativeFolderActivity.class);
				break;
			case Folder.NURSING_FOLDER_TYPE:
				intent.setClass(this, NursingFolderActivity.class);
				break;
			case Folder.MEDICAL_FOLDER_TYPE:
				//intent.setClass(this, MedicalFolderActivity.class);
				break;
		}
        startActivity(intent);
	}

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
