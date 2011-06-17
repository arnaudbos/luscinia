package uk.ac.brookes.arnaudbos.luscinia.listeners;

import uk.ac.brookes.arnaudbos.luscinia.views.DashboardActivity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class DashboardListener implements OnClickListener
{
	private DashboardActivity context;
	
	@Override
	public void onClick(View view)
	{
	}
	
	private void launchPatientActivity()
	{
		Intent intent = new Intent(this.context, DashboardActivity.class);
        this.context.startActivity(intent);
        this.context.finish();
	}
	
	public void setContext(DashboardActivity context)
	{
		this.context = context;
	}
}
