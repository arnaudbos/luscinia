package uk.ac.brookes.arnaudbos.luscinia.listeners;

import uk.ac.brookes.arnaudbos.luscinia.data.Patient;
import uk.ac.brookes.arnaudbos.luscinia.views.DashboardActivity;
import uk.ac.brookes.arnaudbos.luscinia.views.PatientActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;

public class DashboardListener implements OnClickListener, OnItemClickListener, OnGroupClickListener, OnChildClickListener, OnItemLongClickListener, android.content.DialogInterface.OnClickListener
{
	private DashboardActivity context;
	private boolean expanded = false;
	private Patient selectedPatient;
	
	@Override
	public void onClick(View view)
	{
	}

	@Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
    	Patient patient = (Patient) parent.getAdapter().getItem(position);
		launchPatientActivity(patient);
    }

	@Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
    {
		selectedPatient = (Patient) parent.getAdapter().getItem(position);
		context.showDialog(DashboardActivity.DIALOG_DELETE_PATIENT);
		return true;
    }

	@Override
	public boolean onGroupClick(ExpandableListView arg0, View arg1, int arg2, long arg3)
	{
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
    	//TODO: Implement notification click listener
		return false;
	}
	
	private void launchPatientActivity(Patient patient)
	{
		Intent intent = new Intent(this.context, PatientActivity.class);
		intent.putExtra("patient", patient);
        this.context.startActivity(intent);
	}
	
	public void setContext(DashboardActivity context)
	{
		this.context = context;
	}

	@Override
	public void onClick(DialogInterface dialog, int which)
	{
		switch(which)
		{
			case DialogInterface.BUTTON_POSITIVE:
				context.deletePatient(selectedPatient);
				break;
		}
	}
}
