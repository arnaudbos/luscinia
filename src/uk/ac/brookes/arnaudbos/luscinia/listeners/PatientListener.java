package uk.ac.brookes.arnaudbos.luscinia.listeners;

import uk.ac.brookes.arnaudbos.luscinia.views.PatientActivity;
import android.view.View;
import android.view.View.OnClickListener;

public class PatientListener implements OnClickListener
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
}
