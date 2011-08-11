package uk.ac.brookes.arnaudbos.luscinia.listeners;

import uk.ac.brookes.arnaudbos.luscinia.utils.Log;
import uk.ac.brookes.arnaudbos.luscinia.views.CreatePatientActivity;
import uk.ac.brookes.arnaudbos.luscinia.views.EditPatientActivity;
import android.app.Activity;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * Events listener dedicated to the CreatePatientActivity and EditPatientActivity
 * @author arnaudbos
 */
public class CreatePatientListener implements OnClickListener, android.content.DialogInterface.OnClickListener
{
	private Activity context;

	@Override
	public void onClick(View view)
	{
		Log.d("CreatePatientListener.onClick");
		// Show the DIALOG_NEW_ITEM alert
		context.showDialog(CreatePatientActivity.DIALOG_NEW_ITEM);
	}

	@Override
	public void onClick(DialogInterface dialog, int which)
	{
		Log.d("CreatePatientListener.DialogInterface.onClick");
		switch(which)
		{
			case DialogInterface.BUTTON_POSITIVE:
				Log.d("Button BUTTON_POSITIVE pressed");
				// Save or update the patient depending on the context
				if(context instanceof CreatePatientActivity)
				{
					((CreatePatientActivity)context).savePatient();
				}
				else if(context instanceof EditPatientActivity)
				{
					((EditPatientActivity)context).updatePatient();
				}
				break;
			case DialogInterface.BUTTON_NEUTRAL:
				Log.d("Button BUTTON_NEUTRAL pressed");
				// Set a RESULT_CANCELED result to the context or finish it depending on the context
				if(context instanceof EditPatientActivity)
				{
					context.setResult(Activity.RESULT_CANCELED, null);
				}
				context.finish();
				break;
		}
	}

	/**
	 * Set the listener's context
	 * @param context The context to set
	 */
	public void setContext (Activity context)
	{
		Log.d("CreatePatientListener.setContext");
		this.context = context;
	}
}