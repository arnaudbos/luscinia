package uk.ac.brookes.arnaudbos.luscinia.views;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.ektorp.ViewQuery;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import uk.ac.brookes.arnaudbos.luscinia.LusciniaApplication;
import uk.ac.brookes.arnaudbos.luscinia.R;
import uk.ac.brookes.arnaudbos.luscinia.adapters.DashboardNotificationAdapter;
import uk.ac.brookes.arnaudbos.luscinia.adapters.DashboardPatientAdapter;
import uk.ac.brookes.arnaudbos.luscinia.adapters.PatientFolderAdapter;
import uk.ac.brookes.arnaudbos.luscinia.data.Document;
import uk.ac.brookes.arnaudbos.luscinia.data.Folder;
import uk.ac.brookes.arnaudbos.luscinia.data.Notification;
import uk.ac.brookes.arnaudbos.luscinia.data.Patient;
import uk.ac.brookes.arnaudbos.luscinia.listeners.DashboardListener;
import uk.ac.brookes.arnaudbos.luscinia.utils.Log;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.inject.Inject;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ScrollingTextView;
import com.markupartist.android.widget.ActionBar.Action;

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
	@InjectResource(R.string.delete_patient_error_title) private String deletePatientErrorTitle;
	@InjectResource(R.string.delete_patient_error_message) private String deletePatientErrorMessage;

	@InjectView(R.id.actionbar) private ActionBar actionBar;
	@InjectView(R.id.gridView1) private GridView gridview;
	@InjectView(R.id.expandableListView1) private ExpandableListView expandablelistview;

	final Runnable threadCallBackSuceeded = new Runnable()
	{
		public void run()
		{
			mProgressDialog.dismiss();
			prepareGridView();
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

	final Runnable patientDeleteSucceeded = new Runnable()
	{
		public void run()
		{
			mProgressDialog.dismiss();
			prepareGridView();
		}
	};

	final Runnable patientDeleteFailed = new Runnable()
	{
		public void run()
		{
			mProgressDialog.dismiss();
			showDialog(DIALOG_DELETE_PATIENT_ERROR);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        listener.setContext(this);

        prepareActionBar();
        //stubGetUser(login, password);
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		loadPatients();
	}
	
	private void loadPatients()
	{
        prepareExpandableListView();
        loadGridView();
	}
	
	private void prepareExpandableListView()
	{
		List<String> groups = new ArrayList<String>();
		groups.add("Notifications");
		List<Notification> notifs = new ArrayList<Notification>();
//		notifs.add(new Notification("Demande de prescription", "Message"));
//		notifs.add(new Notification("Accord patient", "Message"));
		List<List<Notification>> list = new ArrayList<List<Notification>> ();
		list.add(notifs);
		
		DashboardNotificationAdapter adapter = new DashboardNotificationAdapter(this, groups, list);
		this.expandablelistview.setAdapter(adapter);
        
		this.expandablelistview.setOnGroupClickListener(listener);
		this.expandablelistview.setOnChildClickListener(listener);
	}

	private void loadGridView()
	{
    	mProgressDialog = ProgressDialog.show(this, "", patientsLoading, true);
		new Thread()
		{
			@Override public void run()
			{
				try
				{
					patients = LusciniaApplication.getDB().queryView(new ViewQuery().designDocId("_design/views").viewName(Patient.VIEW_ALL_PATIENTS), Patient.class);
					uiThreadCallback.post(threadCallBackSuceeded);
				}
				catch (Exception e)
				{
					Log.e("Load view _all_patients failed", e);
					uiThreadCallback.post(threadCallBackFailed);
				}
			}
		}.start();
	}
	
	private void prepareGridView()
	{
		DashboardPatientAdapter adapter = new DashboardPatientAdapter(DashboardActivity.this, patients);
	    gridview.setAdapter(adapter);
	    
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

	    gridview.setOnItemClickListener(listener);
	    gridview.setOnItemLongClickListener(listener);
	}

	private void stubGetUser(String login, String password)
	{
		// TODO: Stub method. Implement it.
		// Do some stuff to retrieve the user
		String userFirstname = "Tiffany";
		String userName = "REMY";
		actionBar.setTitle(dashboardTitle+" "+userFirstname+" "+userName);
	}

	private void prepareActionBar()
	{
        actionBar.setTitle(dashboardTitle);
        actionBar.addAction(new StubShareAction());
        actionBar.addAction(new StubSearchAction());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.dashboard, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		Intent i = null;
		switch(item.getItemId())
		{
			case R.id.add:
				i = new Intent(this, CreatePatientActivity.class);
				startActivity(i);
				return true;
			case R.id.refresh:
				loadPatients();
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
			case DIALOG_DELETE_PATIENT:
				return new AlertDialog.Builder(this)
					.setTitle(deletePatientTitle)
					.setMessage(deletePatientMessage)
					.setPositiveButton(ok, listener)
					.setNegativeButton(cancel, null)
					.create();
			case DIALOG_DELETE_PATIENT_ERROR:
				return new AlertDialog.Builder(this)
					.setTitle(deletePatientErrorTitle)
					.setMessage(deletePatientErrorMessage)
					.setPositiveButton(ok, null)
					.create();
		}
		return null;
	}
	
	public void deletePatient(final Patient patient)
	{
    	mProgressDialog = ProgressDialog.show(this, "", deleting, true);
		new Thread()
		{
			@Override public void run()
			{
				try
				{
					Date now = new Date();
					if(now.getTime() - patient.getDateOfCreation().getTime() > 900000)
					{
						throw new Exception ("The 15 minutes delay allowed to delete a folder has elapsed.");
					}

					for(Folder folder : LusciniaApplication.getDB().queryView(new ViewQuery().designDocId("_design/views").viewName(Folder.VIEW_ALL_FOLDERS).key(patient.getId()), Folder.class))
					{
						for(Document document : LusciniaApplication.getDB().queryView(new ViewQuery().designDocId("_design/views").viewName(Document.VIEW_ALL_DOCUMENTS).key(folder.getId()), Document.class))
						{
							LusciniaApplication.getDB().delete(document);
						}
						LusciniaApplication.getDB().delete(folder);
					}
					LusciniaApplication.getDB().delete(patient);

					patients.remove(patient);
					uiThreadCallback.post(patientDeleteSucceeded);
				}
				catch (Exception e)
				{
					Log.e("Delete folder failed", e);
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
