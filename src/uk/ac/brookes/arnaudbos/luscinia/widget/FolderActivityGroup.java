/*
 * Copyright (C) 2011 Arnaud Bos <arnaud.tlse@gmail.com>
 * 
 * This file is part of Luscinia.
 * 
 * Luscinia is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Luscinia is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Luscinia.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.ac.brookes.arnaudbos.luscinia.widget;

import java.util.ArrayList;

import org.miscwidgets.widget.Panel;

import roboguice.activity.RoboActivityGroup;
import uk.ac.brookes.arnaudbos.luscinia.utils.Log;
import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;

/**
 * Implementation of an ActivityGroup representing a folder that will contain documents as child activities.
 * Folders activities implementations should inherit from this object in order to provide navigation history facilities between child documents activities.
 * @author arnaudbos
 */
public class FolderActivityGroup extends RoboActivityGroup
{
	private ArrayList<String> history;
	private ViewGroup childActivityPlaceHolder;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		Log.d("FolderActivityGroup.onCreate");
		super.onCreate(savedInstanceState);
		// Create a new list of child activities Ids
		if (history == null)
		{
			history = new ArrayList<String> ();
		}
	}

	@Override
	public void finishFromChild(Activity child)
	{
		Log.d("FolderActivityGroup.finishFromChild");
		// Get the localActivityManager and the last started child activity
		LocalActivityManager manager = getLocalActivityManager();
		int index = history.size()-1;

		// If the child activity is the last one running
		if (index < 1)
		{
			Log.d("Finish the 'parent' activity");
			finish();
			return;
		}

		// Remove the child activity from the localActivityManager and from the list of child activities
		manager.destroyActivity(history.get(index), true);
		history.remove(index);

		// Get the last started child activity and start it
		index--;
		String lastId = history.get(index);
		Intent lastIntent = manager.getActivity(lastId).getIntent();
		Window newWindow = manager.startActivity(lastId, lastIntent);

		// Refrech the childActivityPlaceHodler
		childActivityPlaceHolder.removeViewAt(0);
		childActivityPlaceHolder.addView(newWindow.getDecorView(), 0);
	}

	/**
	 * Start a new child Activity or resume a previously started child Activity into the childActivityPlaceHolder
	 * @param id The id of the child Activity to start or resume
	 * @param intent The intent to start or resume the child activity
	 */
	protected void startChildActivity(String id, Intent intent)
	{
		Log.d("FolderActivityGroup.startChildActivity");
		// Try to start the child Activity from intent
		Window window = getLocalActivityManager().startActivity(id,intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		// If succeeded
		if (window != null)
		{
			// If child activity has already been started (id is already in the history)
			if(history.contains(id))
			{
				// Remove it from the history
				history.remove(id);
			}
			// Add the child activity started at the end of the history
			history.add(id);
			// Remove the previous child activity view from the placeHolder
			if (!(childActivityPlaceHolder.getChildAt(0) instanceof Panel))
			{
				childActivityPlaceHolder.removeViewAt(0);
			}
			// Add the started child activity view into the placeHolder
			childActivityPlaceHolder.addView(window.getDecorView(), 0);
		}
	}
	
	/**
	 * 
	 * @param childActivityPlaceHolder
	 */
	protected void setChildActivityPlaceHolder(ViewGroup childActivityPlaceHolder)
	{
		Log.d("FolderActivityGroup.setTargetLayout");
		this.childActivityPlaceHolder = childActivityPlaceHolder;
	}

	@Override
	final public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		Log.d("FolderActivityGroup.onKeyDown");
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	final public boolean onKeyUp(int keyCode, KeyEvent event)
	{
		Log.d("FolderActivityGroup.onKeyUp");
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			onBackPressed();
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

	@Override
	final public void onBackPressed()
	{
		Log.d("FolderActivityGroup.onBackPressed");
		// If the Back button has been pressed, finish the current (last in history) child activity
		int length = history.size();
		if ( length > 1)
		{
			Activity current = getLocalActivityManager().getActivity(history.get(length-1));
			// See FolderActivityGroup.finishFromChild
			current.finish();
		}
		else
		{
			finish();
		}
	}
}
