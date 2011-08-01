package uk.ac.brookes.arnaudbos.luscinia.widget;

/**
 * 
 * Implementation of a basic ScrollView synchronizer, see also LusciniaScollView and NursingDiagramListener.onScrollChanged().
 * Thanks to @Andy on StackOverflow question:
 * http://stackoverflow.com/questions/3948934/synchronise-scrollview-scroll-positions-android
 *
 */
public interface ScrollViewListener
{
	void onScrollChanged(LusciniaScrollView scrollView, int x, int y, int oldx, int oldy);
}
