package uk.ac.brookes.arnaudbos.luscinia.listeners;

import uk.ac.brookes.arnaudbos.luscinia.views.LusciniaActivity;
import android.view.View;
import android.view.View.OnClickListener;

public class LusciniaListener implements OnClickListener
{
	private LusciniaActivity context;
	
	@Override
	public void onClick(View view)
	{
		context.connect();
	}

	public void setContext(LusciniaActivity context)
	{
		this.context = context;
	}
}
