package uk.ac.brookes.arnaudbos.luscinia.listeners;

import uk.ac.brookes.arnaudbos.luscinia.views.MacrocibleActivity;
import uk.ac.brookes.arnaudbos.luscinia.views.TransActivity;
import android.view.View;
import android.view.View.OnClickListener;

public class MacrocibleListener implements OnClickListener
{
	private MacrocibleActivity context;
	
	@Override
	public void onClick(View view)
	{
		String m = context.getM();
		String t = context.getT();
		String v = context.getV();
		String e = context.getE();
		String d = context.getD();
		
		if ((m == null || m.equals("")) &&
			(t == null || t.equals("")) &&
			(v == null || v.equals("")) &&
			(e == null || e.equals("")) &&
			(d == null || d.equals(""))
			)
		{
			context.showDialog(TransActivity.DIALOG_EMPTY_FIELD);
		}
		else
		{
			context.setFieldsDisabled();
		}
	}

	public void setContext(MacrocibleActivity context)
	{
		this.context = context;
	}
}
