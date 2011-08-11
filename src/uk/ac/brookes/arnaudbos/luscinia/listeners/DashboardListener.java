package uk.ac.brookes.arnaudbos.luscinia.listeners;

import uk.ac.brookes.arnaudbos.luscinia.data.Patient;
import uk.ac.brookes.arnaudbos.luscinia.utils.Log;
import uk.ac.brookes.arnaudbos.luscinia.views.DashboardActivity;
import uk.ac.brookes.arnaudbos.luscinia.views.PatientActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;

/**
 * Events listener dedicaded to the DashboardListener
 * @author arnaudbos
 */
public class DashboardListener implements OnItemClickListener, OnGroupClickListener, OnChildClickListener, OnItemLongClickListener, DialogInterface.OnClickListener
{
	private DashboardActivity context;
	private boolean expanded = false;
	private Patient selectedPatient;
	
	@Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
		Log.d("DashboardListener.onItemClick");
		// Retrieve patient from pressed item
    	Patient patient = (Patient) parent.getAdapter().getItem(position);
    	// Start PatientActivity
		launchPatientActivity(patient);
    }

	@Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
    {
		Log.d("DashboardListener.onItemLongClick");
		// Retrieve patient from pressed item
		selectedPatient = (Patient) parent.getAdapter().getItem(position);
		// Show delete dialog confirmation
		context.showDialog(DashboardActivity.DIALOG_DELETE_PATIENT);
		return true;
    }

	@Override
	public boolean onGroupClick(ExpandableListView arg0, View arg1, int arg2, long arg3)
	{
		Log.d("DashboardListener.onGroupClick");
		// Recalculate the height of the ExpandableListView regarding if the clicked group is expanded or not
    	if(!expanded)
    	{
            LayoutParams params = arg0.getLayoutParams();
            int count = arg0.getExpandableListAdapter().getChildrenCount(0);
        	params.height = (count+1)*arg1.getLayoutParams().height;
        	arg0.setLayoutParams(params);
        	expanded = true;
    	}
    	else
    	{
            LayoutParams params = arg0.getLayoutParams();
        	params.height = arg1.getLayoutParams().height;
        	arg0.setLayoutParams(params);
        	expanded = false;
    	}
    	return false;
	}

	@Override
	public boolean onChildClick(ExpandableListView arg0, View arg1, int arg2, int arg3, long arg4)
	{
		Log.d("DashboardListener.onChildClick");
    	//TODO: Implement notification click listener
		return false;
	}
	
	/**
	 * Start PatientActivity with a patient extra
	 * @param patient The patient that will be passed to the PatientActivity as an Intent Extra
	 */
	private void launchPatientActivity(Patient patient)
	{
		Log.d("DashboardListener.launchPatientActivity");
		Intent intent = new Intent(this.context, PatientActivity.class);
		intent.putExtra("patient", patient);
        this.context.startActivity(intent);
	}

	/**
	 * Set the listener's context
	 * @param context The context to set
	 */
	public void setContext(DashboardActivity context)
	{
		Log.d("DashboardListener.setContext");
		this.context = context;
	}

	@Override
	public void onClick(DialogInterface dialog, int which)
	{
		Log.d("DashboardListener.DialogInterface.onClick");
		switch(which)
		{
			case DialogInterface.BUTTON_POSITIVE:
				Log.d("Button BUTTON_POSITIVE pressed");
				// Delete the selected patient
				context.deletePatient(selectedPatient);
				break;
		}
	}
}
