package uk.ac.brookes.arnaudbos.luscinia.listeners;

import uk.ac.brookes.arnaudbos.luscinia.views.NursingDiagramActivity;
import uk.ac.brookes.arnaudbos.luscinia.widget.LusciniaScrollView;
import uk.ac.brookes.arnaudbos.luscinia.widget.ScrollViewListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

public class NursingDiagramListener implements OnClickListener, OnTouchListener, ScrollViewListener
{
	private NursingDiagramActivity context;
	
	@Override
	public void onClick(View view)
	{
	}
	
    public boolean onTouch(View v, MotionEvent event)
    {
        return true; 
    }


	/*
	 * 
	 * Implementation of a basic ScrollView synchronizer, see also LusciniaScollView and ScrollViewListener.
	 * Thanks to @Andy on StackOverflow question:
	 * http://stackoverflow.com/questions/3948934/synchronise-scrollview-scroll-positions-android
	 *
	 */
	@Override
	public void onScrollChanged(LusciniaScrollView scrollView, int x, int y, int oldx, int oldy)
	{
		context.getRecordsScrollView().scrollTo(x, y);
	}

	public void setContext(NursingDiagramActivity context)
	{
		this.context = context;
	}
}
