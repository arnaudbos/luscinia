package uk.ac.brookes.arnaudbos.luscinia.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * 
 * Implementation of a basic ScrollView synchronizer, see also ScollViewListener and NursingDiagramListener.onScrollChanged().
 * Thanks to @Andy on StackOverflow question:
 * http://stackoverflow.com/questions/3948934/synchronise-scrollview-scroll-positions-android
 *
 */
public class LusciniaScrollView extends ScrollView
{
	private ScrollViewListener scrollViewListener = null;

    public LusciniaScrollView(Context context)
    {
        super(context);
    }

    public LusciniaScrollView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public LusciniaScrollView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener)
    {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy)
    {
        super.onScrollChanged(x, y, oldx, oldy);
        if(scrollViewListener != null)
        {
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }
}
