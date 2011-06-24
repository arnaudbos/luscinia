package uk.ac.brookes.arnaudbos.luscinia.widget;

import java.util.ArrayList;

import org.miscwidgets.widget.Panel;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.Toast;

import roboguice.activity.RoboActivityGroup;

public class FolderActivityGroup extends RoboActivityGroup
{
	private ArrayList<String> activityIdsList;
	private RelativeLayout targetLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if (activityIdsList == null)
		{
			activityIdsList = new ArrayList<String> ();
		}
	}

	@Override
	public void finishFromChild(Activity child)
	{
		LocalActivityManager manager = getLocalActivityManager();
		int index = activityIdsList.size()-1;

		if (index < 1)
		{
			finish();
			return;
		}

		manager.destroyActivity(activityIdsList.get(index), true);
		activityIdsList.remove(index);
		index--;
		String lastId = activityIdsList.get(index);
		Intent lastIntent = manager.getActivity(lastId).getIntent();
		Window newWindow = manager.startActivity(lastId, lastIntent);
		targetLayout.removeViewAt(0);
		targetLayout.addView(newWindow.getDecorView(), 0);
	}

	public void startChildActivity(String Id, Intent intent)
	{
		Window window = getLocalActivityManager().startActivity(Id,intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		if (window != null)
		{
			activityIdsList.add(Id);
			if (!(targetLayout.getChildAt(0) instanceof Panel))
			{
				targetLayout.removeViewAt(0);
			}
			targetLayout.addView(window.getDecorView(), 0);
		}
	}
	
	protected void setTargetLayout(RelativeLayout target)
	{
		this.targetLayout = target;
	}

	@Override
	final public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	final public boolean onKeyUp(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			onBackPressed();
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

	@Override
	final public void onBackPressed ()
	{
		int length = activityIdsList.size();
		Toast.makeText(this, "onBackPressed, length="+length, Toast.LENGTH_SHORT).show();
		if ( length > 1)
		{
			Activity current = getLocalActivityManager().getActivity(activityIdsList.get(length-1));
			current.finish();
		}
		else
		{
			finish();
		}
	}
}
