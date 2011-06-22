package uk.ac.brookes.arnaudbos.luscinia.listeners;

import uk.ac.brookes.arnaudbos.luscinia.views.PatientActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class PatientListener implements OnClickListener, OnItemClickListener
{
	private PatientActivity context;
	
	@Override
	public void onClick(View view)
	{
	}
	
	public void setContext(PatientActivity context)
	{
		this.context = context;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
	}
}
