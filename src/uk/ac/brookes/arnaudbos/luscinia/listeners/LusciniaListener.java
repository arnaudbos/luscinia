package uk.ac.brookes.arnaudbos.luscinia.listeners;

import uk.ac.brookes.arnaudbos.luscinia.utils.Log;
import uk.ac.brookes.arnaudbos.luscinia.views.LusciniaActivity;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * Events listener dedicated to the LusciniaActivity
 * @author arnaudbos
 */
public class LusciniaListener implements OnClickListener
{
	private LusciniaActivity context;
	
	@Override
	public void onClick(View view)
	{
    	Log.d("LusciniaListener.onClick");
		context.connect();
	}

	/**
	 * Set the listener's context
	 * @param context The context to set
	 */
	public void setContext(LusciniaActivity context)
	{
    	Log.d("LusciniaListener.setContext");
		this.context = context;
	}
}
