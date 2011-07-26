package uk.ac.brookes.arnaudbos.luscinia.listeners;

import java.text.SimpleDateFormat;
import java.util.Date;

import uk.ac.brookes.arnaudbos.luscinia.R;
import uk.ac.brookes.arnaudbos.luscinia.utils.Log;
import uk.ac.brookes.arnaudbos.luscinia.views.TransActivity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class TransListener implements OnClickListener, OnLongClickListener, android.content.DialogInterface.OnClickListener
{
	private TransActivity context;
	private String date, focus, data, actions, results;
	private TableLayout tableView;
	private TableRow row;
	
	@Override
	public void onClick(View view)
	{
		// Get TableLayout container and EditTexts' typed content from activity view
		tableView = context.getTableView();
		focus = context.getFocus();
		data = context.getData();
		actions = context.getActions();
		results = context.getResults();
		
		if ((focus == null || focus.equals("")) &&
			(data == null || data.equals("")) &&
			(actions == null || actions.equals("")) &&
			(results == null || results.equals(""))
			)
		{
			context.showDialog(TransActivity.DIALOG_EMPTY_FIELD);
		}
		else
		{
			//TODO: Review all this when real data will be inserted because
			//TODO: an Adapter workaround
			// Create a new TableRow with previous data
			TableRow newRow = (TableRow) LayoutInflater.from(context).inflate(R.layout.trans_item, null);
			newRow.setOnLongClickListener(this);

			TextView dateView = (TextView) newRow.findViewById(R.id.date_view);
			SimpleDateFormat s = new SimpleDateFormat("dd/mm/yyyy\nHH:mm:ss");
			Date date = new Date();
			dateView.setText(s.format(date));
			TextView focusView = (TextView) newRow.findViewById(R.id.focus_view);
			focusView.setText(focus);
			TextView dataView = (TextView) newRow.findViewById(R.id.data_view);
			dataView.setText(data);
			TextView actionsView = (TextView) newRow.findViewById(R.id.actions_view);
			actionsView.setText(actions);
			TextView resultsView = (TextView) newRow.findViewById(R.id.results_view);
			resultsView.setText(results);
			
			//TODO: Insert the data as a Record in the Document and do next two lines only if succeed
	
			// Insert the new TableRow as last TableLayout row before the EditTexts and validation Button
			tableView.addView(newRow, tableView.getChildCount()-2);
	
			// Reset the content of the EditTexts
			context.resetEditTexts();
			InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), 0);
		}
	}

	@Override
	public boolean onLongClick(View view)
	{
		row = (TableRow) view;
		context.setSelectedRow(row);

		context.showDialog(TransActivity.DIALOG_ROW_LONGCLICK);

		return true;
	}

	@Override
	public void onClick(DialogInterface dialog, int which)
	{
		Log.i(""+which);
		switch(which)
		{
			case 0:
				context.showDialog(TransActivity.DIALOG_UPDATE_ROW);
				break;
			case 1:
				//TODO: Delete the Record
				tableView.removeView(row);
				break;
			case Dialog.BUTTON_POSITIVE:
				TableLayout dialogTableView = context.getDialogTableView();
	        	((TextView)row.findViewById(R.id.focus_view)).setText(((EditText)dialogTableView.findViewById(R.id.focus_edit)).getText().toString());
	        	((TextView)row.findViewById(R.id.data_view)).setText(((EditText)dialogTableView.findViewById(R.id.data_edit)).getText().toString());
	        	((TextView)row.findViewById(R.id.actions_view)).setText(((EditText)dialogTableView.findViewById(R.id.actions_edit)).getText().toString());
	        	((TextView)row.findViewById(R.id.results_view)).setText(((EditText)dialogTableView.findViewById(R.id.results_edit)).getText().toString());
				break;
			case Dialog.BUTTON_NEGATIVE:
				break;
		}
	}

	public void setContext(TransActivity context)
	{
		this.context = context;
	}
}
