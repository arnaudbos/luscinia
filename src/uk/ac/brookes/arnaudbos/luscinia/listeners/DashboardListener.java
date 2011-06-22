package uk.ac.brookes.arnaudbos.luscinia.listeners;

import uk.ac.brookes.arnaudbos.luscinia.views.DashboardActivity;
import uk.ac.brookes.arnaudbos.luscinia.views.PatientActivity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;

public class DashboardListener implements OnClickListener, OnItemClickListener, OnGroupClickListener, OnChildClickListener
{
	private DashboardActivity context;
	private boolean expanded = false;
	
	@Override
	public void onClick(View view)
	{
	}

	@Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
    	//Map<String, Object> user = (Map<String, Object>) parent.getAdapter().getItem(position);
		launchPatientActivity();
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
	
	private void launchPatientActivity()
	{
		Intent intent = new Intent(this.context, PatientActivity.class);
		intent.putExtra("patient", "Arnaud BOS");
        this.context.startActivity(intent);
	}
	
	public void setContext(DashboardActivity context)
	{
		this.context = context;
	}
}
