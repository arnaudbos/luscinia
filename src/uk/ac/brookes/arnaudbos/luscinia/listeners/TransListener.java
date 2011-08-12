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

package uk.ac.brookes.arnaudbos.luscinia.listeners;

import java.util.Date;

import uk.ac.brookes.arnaudbos.luscinia.R;
import uk.ac.brookes.arnaudbos.luscinia.data.TransRecord;
import uk.ac.brookes.arnaudbos.luscinia.utils.Log;
import uk.ac.brookes.arnaudbos.luscinia.views.TransActivity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;

/**
 * Events listener dedicated to the TransActivity
 * @author arnaudbos
 */
public class TransListener implements OnClickListener, OnLongClickListener, android.content.DialogInterface.OnClickListener
{
	private TransActivity context;
	private String focus, data, actions, results;
	
	@Override
	public void onClick(View view)
	{
		Log.d("TransListener.onClick");
		// Get TableLayout container and EditTexts' typed content from activity view
		focus = context.getFocus();
		data = context.getData();
		actions = context.getActions();
		results = context.getResults();
		
		// One field at least must be filled
		if ((focus == null || focus.equals("")) &&
			(data == null || data.equals("")) &&
			(actions == null || actions.equals("")) &&
			(results == null || results.equals(""))
			)
		{
			Log.d("No field is filled");
			// Show the DIALOG_EMPTY_FIELD Alert
			context.showDialog(TransActivity.DIALOG_EMPTY_FIELD);
		}
		else
		{
			Log.d("Fields ready");
			// Create a new record
			context.createRecord(focus, data, actions, results);
		}
	}

	@Override
	public boolean onLongClick(View view)
	{
		Log.d("TransListener.onLongClick");
		TableRow selectedRow = (TableRow) view;
		TransRecord record = (TransRecord)selectedRow.getTag();
		Date now = new Date();
		// Calculate the difference between now and the date of creation of the record and pass only if delay < 15 minutes
		if(now.getTime() - record.getDate().getTime() > 900000)
		{
			// Display DIALOG_TIME_ELAPSED Alert
			context.showDialog(TransActivity.DIALOG_TIME_ELAPSED);
		}
		// Else
		else
		{
			// Pass the selected row to the context and display the DIALOG_UPDATE_ROW Alert
			context.setSelectedRow(selectedRow);
			context.showRowUpdateDialog();
		}

		return true;
	}

	@Override
	public void onClick(DialogInterface dialog, int which)
	{
		Log.d("TransListener.DialogInterface.onClick");
		switch(which)
		{
			case Dialog.BUTTON_POSITIVE:
				Log.d("BUTTON_POSITIVE pressed");

				TableLayout dialogTableView = context.getDialogTableView();
	        	focus = ((EditText)dialogTableView.findViewById(R.id.focus_edit)).getText().toString();
	        	data = ((EditText)dialogTableView.findViewById(R.id.data_edit)).getText().toString();
	        	actions = ((EditText)dialogTableView.findViewById(R.id.actions_edit)).getText().toString();
	        	results = ((EditText)dialogTableView.findViewById(R.id.results_edit)).getText().toString();
	    		
	    		// One field at least must be filled
	    		if ((focus == null || focus.equals("")) &&
	    			(data == null || data.equals("")) &&
	    			(actions == null || actions.equals("")) &&
	    			(results == null || results.equals(""))
	    			)
	    		{
	    			Log.d("No field is filled");
	    			// Show the DIALOG_EMPTY_FIELD Alert
	    			context.showDialog(TransActivity.DIALOG_EMPTY_FIELD);
	    		}
	    		else
	    		{
	    			Log.d("Fields ready");
	    			// Update the selected record
	    			context.updateRecord(focus, data, actions, results);
	    		}
	        	break;
			case Dialog.BUTTON_NEGATIVE:
				context.setSelectedRow(null);
				break;
		}
	}

	/**
	 * Set the listener's context
	 * @param context The context to set
	 */
	public void setContext(TransActivity context)
	{
		this.context = context;
	}
}
