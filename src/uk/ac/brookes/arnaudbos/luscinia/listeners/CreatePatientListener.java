package uk.ac.brookes.arnaudbos.luscinia.listeners;

import uk.ac.brookes.arnaudbos.luscinia.views.CreatePatientActivity;
import uk.ac.brookes.arnaudbos.luscinia.views.EditPatientActivity;
import android.app.Activity;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;

public class CreatePatientListener implements OnClickListener, android.content.DialogInterface.OnClickListener
{
	private Activity context;

	@Override
	public void onClick(View view)
	{
		context.showDialog(CreatePatientActivity.DIALOG_NEW_ITEM);
	}
	
	public void setContext (Activity context)
	{
		this.context = context;
	}

	@Override
	public void onClick(DialogInterface dialog, int which)
	{
		switch(which)
		{
			case DialogInterface.BUTTON_POSITIVE:
				if(context instanceof CreatePatientActivity)
				{
					((CreatePatientActivity)context).savePatient();
				}
				else if(context instanceof EditPatientActivity)
				{
					((EditPatientActivity)context).savePatient();
				}
				break;
			case DialogInterface.BUTTON_NEUTRAL:
				if(context instanceof EditPatientActivity)
				{
					context.setResult(Activity.RESULT_CANCELED, null);
				}
				context.finish();
				break;
		}
	}
}