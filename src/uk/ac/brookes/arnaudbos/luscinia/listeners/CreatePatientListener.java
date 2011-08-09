package uk.ac.brookes.arnaudbos.luscinia.listeners;

import uk.ac.brookes.arnaudbos.luscinia.views.CreatePatientActivity;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;

public class CreatePatientListener implements OnClickListener, android.content.DialogInterface.OnClickListener
{
	private CreatePatientActivity context;

	@Override
	public void onClick(View view)
	{
		context.showDialog(CreatePatientActivity.DIALOG_NEW_ITEM);
	}
	
	public void setContext (CreatePatientActivity context)
	{
		this.context = context;
	}

	@Override
	public void onClick(DialogInterface dialog, int which)
	{
		switch(which)
		{
			case DialogInterface.BUTTON_POSITIVE:
				context.savePatient();
				break;
			case DialogInterface.BUTTON_NEUTRAL:
				context.finish();
				break;
		}
	}
}