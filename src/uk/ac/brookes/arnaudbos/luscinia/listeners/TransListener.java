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
import android.widget.TextView;

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
			// Create a new records
			context.createRecord(focus, data, actions, results);
		}
	}

	@Override
	public boolean onLongClick(View view)
	{
		Log.d("TransListener.onLongClick");
		TableRow selectedRow = (TableRow) view;
		Date now = new Date();
		// Calculate the difference between now and the date of creation of the record and pass only if delay < 15 minutes
		if(now.getTime() - ((TransRecord)selectedRow.getTag()).getDate().getTime() > 900000)
		{
			// Display DIALOG_TIME_ELAPSED Alert
			context.showDialog(TransActivity.DIALOG_TIME_ELAPSED);
		}
		// Else
		else
		{
			// Pass the selected row to the context and display the DIALOG_UPDATE_ROW Alert
			context.setSelectedRow(selectedRow);
			context.showDialog(TransActivity.DIALOG_UPDATE_ROW);
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
				//TODO: Update the Record
				TransRecord selectedRecord = (TransRecord)context.getSelectedRow().getTag();

				TableLayout dialogTableView = context.getDialogTableView();
	        	((TextView)context.getSelectedRow().findViewById(R.id.focus_view)).setText(((EditText)dialogTableView.findViewById(R.id.focus_edit)).getText().toString());
	        	((TextView)context.getSelectedRow().findViewById(R.id.data_view)).setText(((EditText)dialogTableView.findViewById(R.id.data_edit)).getText().toString());
	        	((TextView)context.getSelectedRow().findViewById(R.id.actions_view)).setText(((EditText)dialogTableView.findViewById(R.id.actions_edit)).getText().toString());
	        	((TextView)context.getSelectedRow().findViewById(R.id.results_view)).setText(((EditText)dialogTableView.findViewById(R.id.results_edit)).getText().toString());
				break;
			case Dialog.BUTTON_NEGATIVE:
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
