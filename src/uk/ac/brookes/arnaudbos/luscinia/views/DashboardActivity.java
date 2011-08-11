package uk.ac.brookes.arnaudbos.luscinia.views;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.ektorp.ViewQuery;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import uk.ac.brookes.arnaudbos.luscinia.LusciniaApplication;
import uk.ac.brookes.arnaudbos.luscinia.R;
import uk.ac.brookes.arnaudbos.luscinia.adapters.DashboardNotificationAdapter;
import uk.ac.brookes.arnaudbos.luscinia.adapters.DashboardPatientAdapter;
import uk.ac.brookes.arnaudbos.luscinia.data.Document;
import uk.ac.brookes.arnaudbos.luscinia.data.Folder;
import uk.ac.brookes.arnaudbos.luscinia.data.Notification;
import uk.ac.brookes.arnaudbos.luscinia.data.Patient;
import uk.ac.brookes.arnaudbos.luscinia.listeners.DashboardListener;
import uk.ac.brookes.arnaudbos.luscinia.utils.Log;
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
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.inject.Inject;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;

/**
 * User dashboard activity
 * @author arnaudbos
 */
public class DashboardActivity extends RoboActivity
{
	public static final int DIALOG_LOAD_ERROR = 101;
	public static final int DIALOG_DELETE_PATIENT = 102;
	public static final int DIALOG_DELETE_PATIENT_ERROR = 103;

	final Handler uiThreadCallback = new Handler();
    private ProgressDialog mProgressDialog;
    private List<Patient> patients = new ArrayList<Patient>();

    @Inject private DashboardListener listener;
	
	@InjectResource(R.string.dashboard_title) private String dashboardTitle;
	@InjectResource(R.string.ok) private String ok;
	@InjectResource(R.string.cancel) private String cancel;
	@InjectResource(R.string.patients_loading) private String patientsLoading;
	@InjectResource(R.string.load_error_title) private String loadErrorTitle;
	@InjectResource(R.string.load_error_message) private String loadErrorMessage;
	@InjectResource(R.string.delete) private String deletePatientTitle;
	@InjectResource(R.string.delete_patient_message) private String deletePatientMessage;
	@InjectResource(R.string.deleting) private String deleting;
	@InjectResource(R.string.delete_error_title) private String deleteErrorTitle;
	@InjectResource(R.string.delete_patient_error_message) private String deletePatientErrorMessage;

	@InjectView(R.id.actionbar) private ActionBar actionBar;
	@InjectView(R.id.gridView1) private GridView gridview;
	@InjectView(R.id.expandableListView1) private ExpandableListView expandablelistview;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
    	Log.d("DashboardActivity.onCreate");
		super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        listener.setContext(this);

        // Initialize the actionBar
        initActionBar();
        //stubGetUser(login, password);
	}
	
	@Override
	protected void onStart()
	{
    	Log.d("DashboardActivity.onStart");
		super.onStart();
		
		// Load the patients from the database
		loadPatients();
	}
	
	/**
	 * Initialize notifications and patients view components from database
	 */
	private void loadPatients()
	{
    	Log.d("DashboardActivity.loadPatients");
    	
    	// Initialize the Notifications ExpandableListView
        initExpandableListView();
    	// Initialize the Patients GridView
        initGridView();
	}
	
	/**
	 * Initialize the Notifications ExpandableListView
	 */
	private void initExpandableListView()
	{
		List<String> groups = new ArrayList<String>();
		groups.add("Notifications");
		List<Notification> notifs = new ArrayList<Notification>();
		//		TODO: Get Notifications from database
		//		notifs.add(new Notification("Accord patient", "Message"));
		List<List<Notification>> list = new ArrayList<List<Notification>> ();
		list.add(notifs);
		
		// Set ExpandableListView adapter
		DashboardNotificationAdapter adapter = new DashboardNotificationAdapter(this, groups, list);
		this.expandablelistview.setAdapter(adapter);
        
		// Add listeners to the ExpandableListView
		this.expandablelistview.setOnGroupClickListener(listener);
		this.expandablelistview.setOnChildClickListener(listener);
	}

	/**
	 * Initialize the Patients GridView
	 */
	private void initGridView()
	{
		Log.d("DashboardActivity.initGridView");
		// Launch an indeterminate ProgressBar in the UI while retrieving the patients list in a new thread
    	mProgressDialog = ProgressDialog.show(this, "", patientsLoading, true);

    	// Create a Runnable that will be executed if the query succeeds
    	final Runnable threadCallBackSuceeded = new Runnable()
    	{
    		public void run()
    		{
    			Log.d("Query executed successfully");
    			// Hide the ProgressBar and render the patients GridView
    			mProgressDialog.dismiss();
    			renderGridView();
    		}
    	};

    	// Create a Runnable that will be executed if the query fails
    	final Runnable threadCallBackFailed = new Runnable()
    	{
    		public void run()
    		{
    			Log.d("View query failed");
    			// Hide the ProgressBar and open an Alert with an error message
    			mProgressDialog.dismiss();
    			showDialog(DIALOG_LOAD_ERROR);
    		}
    	};

    	// Create the separate thread that will retrieve the patients thanks to a view query and start it
		new Thread()
		{
			@Override public void run()
			{
				try
				{
	    			Log.d("Query "+Patient.VIEW_ALL_PATIENTS+" view");
					// Execute the view query and retrieve the patients
					patients = LusciniaApplication.getDB().queryView(new ViewQuery().designDocId("_design/views").viewName(Patient.VIEW_ALL_PATIENTS), Patient.class);
					uiThreadCallback.post(threadCallBackSuceeded);
				}
				catch (Exception e)
				{
					Log.e("Execute view query "+Patient.VIEW_ALL_PATIENTS+" failed", e);
					uiThreadCallback.post(threadCallBackFailed);
				}
			}
		}.start();
	}
	
	/**
	 * Render the patient items into the GridView
	 */
	private void renderGridView()
	{
		Log.d("DashboardActivity.renderGridView");
		// Set GridView adapter
		DashboardPatientAdapter adapter = new DashboardPatientAdapter(DashboardActivity.this, patients);
	    gridview.setAdapter(adapter);
	    
	    // Manually calculate the size of the GridView to display and scroll properly depending on screen resolution
	    setGridViewSize(adapter);

	    // Set listeners on the GridView
	    gridview.setOnItemClickListener(listener);
	    gridview.setOnItemLongClickListener(listener);
	}
	
	/**
	 * Manually calculate the size of the GridView to display and scroll properly depending on screen resolution
	 */
	private void setGridViewSize(DashboardPatientAdapter adapter)
	{
		Log.d("DashboardActivity.setGridViewSize");
	    LayoutParams params = gridview.getLayoutParams();
	    int count = adapter.getCount();
	    
	    final float scale = getResources().getDisplayMetrics().density;
	    int unit = (int) (160 * scale + 0.5f);
	    
	    if(count%4 == 0)
	    {
	    	params.height = count/4*unit;
	    	gridview.setLayoutParams(params);
	    }
	    else
	    {
	    	params.height = (count/4+1)*unit;
	    	gridview.setLayoutParams(params);
	    }
	}

//	private void stubGetUser(String login, String password)
//	{
//		// TODO: Stub method. Implement it.
//		// Do some stuff to retrieve the user
//		String userFirstname = "Tiffany";
//		String userName = "REMY";
//		actionBar.setTitle(dashboardTitle+" "+userFirstname+" "+userName);
//	}

	/**
	 * Initialize the actionBar
	 */
	private void initActionBar()
	{
		Log.d("DashboardActivity.initActionBar");
        actionBar.setTitle(dashboardTitle);
//        actionBar.addAction(new StubShareAction());
//        actionBar.addAction(new StubSearchAction());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		Log.d("DashboardActivity.onCreateOptionsMenu");
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.dashboard, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		Log.d("DashboardActivity.onOptionsItemSelected");
		Intent i = null;
		switch(item.getItemId())
		{
			case R.id.add:
				Log.d("Add menu pressed");
				i = new Intent(this, CreatePatientActivity.class);
				Log.d("Start CreatePatientActivity");
				startActivity(i);
				return true;
			case R.id.refresh:
				Log.d("Refresh menu pressed");
				loadPatients();
				return true;
			default:
				Log.d("Unknown menu pressed");
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected Dialog onCreateDialog(int id)
	{
		Log.d("LusciniaActivity.onCreateDialog");
		switch(id)
		{
			case DIALOG_LOAD_ERROR:
				Log.d("Display DIALOG_LOAD_ERROR Alert");
				return new AlertDialog.Builder(this)
					.setTitle(loadErrorTitle)
					.setMessage(loadErrorMessage)
					.setPositiveButton(ok, null)
					.create();
			case DIALOG_DELETE_PATIENT:
				Log.d("Display DIALOG_DELETE_PATIENT Alert");
				return new AlertDialog.Builder(this)
					.setTitle(deletePatientTitle)
					.setMessage(deletePatientMessage)
					.setPositiveButton(ok, listener)
					.setNegativeButton(cancel, null)
					.create();
			case DIALOG_DELETE_PATIENT_ERROR:
				Log.d("Display DIALOG_DELETE_PATIENT_ERROR Alert");
				return new AlertDialog.Builder(this)
					.setTitle(deleteErrorTitle)
					.setMessage(deletePatientErrorMessage)
					.setPositiveButton(ok, null)
					.create();
		}
		return null;
	}
	
	/**
	 * Delete a Patient and all related data (folders, documents, etc.)
	 * @param patient The Patient to delete
	 */
	public void deletePatient(final Patient patient)
	{
		Log.d("DashboardActivity.deletePatient");
		// Launch an indeterminate ProgressBar in the UI while deleting the patient in a new thread
    	mProgressDialog = ProgressDialog.show(this, "", deleting, true);

    	// Create a Runnable that will be executed if the delete succeeds
    	final Runnable patientDeleteSucceeded = new Runnable()
    	{
    		public void run()
    		{
    			Log.d("Patient deleted successfully");
    			// Hide the ProgressBar and re-render the GridView
    			mProgressDialog.dismiss();
    			renderGridView();
    		}
    	};

    	// Create a Runnable that will be executed if the delete fails
    	final Runnable patientDeleteFailed = new Runnable()
    	{
    		public void run()
    		{
    			Log.d("Delete patient failed");
    			// Hide the ProgressBar and open an Alert with an error message
    			mProgressDialog.dismiss();
    			showDialog(DIALOG_DELETE_PATIENT_ERROR);
    		}
    	};

    	// Create the separate thread that will delete the patient and start it
		new Thread()
		{
			@Override public void run()
			{
				try
				{
	    			Log.d("Delete the patient and all associated documents");
					// Calculate the difference between now and the date of creation of the patient and pass only if delay < 15 minutes 
					Date now = new Date();
					if(now.getTime() - patient.getDateOfCreation().getTime() > 900000)
					{
						throw new Exception ("The 15 minutes delay allowed to delete a folder has elapsed.");
					}

					// For each folder associated with the patient, delete this folder
					for(Folder folder : LusciniaApplication.getDB().queryView(new ViewQuery().designDocId("_design/views").viewName(Folder.VIEW_ALL_FOLDERS).key(patient.getId()), Folder.class))
					{
						// For each document associated with the current folder, delete this document
						for(Document document : LusciniaApplication.getDB().queryView(new ViewQuery().designDocId("_design/views").viewName(Document.VIEW_ALL_DOCUMENTS).key(folder.getId()), Document.class))
						{
			    			Log.d("Delete the document: "+document.getId());
							LusciniaApplication.getDB().delete(document);
						}
		    			Log.d("Delete the folder: "+folder.getId());
						LusciniaApplication.getDB().delete(folder);
					}
	    			Log.d("Delete the patient: "+patient.getId());
					LusciniaApplication.getDB().delete(patient);

					// Remove patient from the list of patients
					patients.remove(patient);
					uiThreadCallback.post(patientDeleteSucceeded);
				}
				catch (Exception e)
				{
					Log.e("Delete patient failed", e);
					uiThreadCallback.post(patientDeleteFailed);
				}
			}
		}.start();
	}

	/**
	 * TODO: Stub class. Implement it.
	 */
	private class StubShareAction implements Action
    {
		@Override
		public void performAction(View view)
		{
			Toast.makeText(DashboardActivity.this, "ShareAction", Toast.LENGTH_SHORT).show();
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
			Toast.makeText(DashboardActivity.this, "otherAction", Toast.LENGTH_SHORT).show();
		}
		
		@Override
		public int getDrawable()
		{
			return R.drawable.ic_title_export_default;
		}
	};
}
