package uk.ac.brookes.arnaudbos.luscinia.views;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import uk.ac.brookes.arnaudbos.luscinia.R;
import uk.ac.brookes.arnaudbos.luscinia.listeners.TransListener;
import uk.ac.brookes.arnaudbos.luscinia.utils.Log;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.inject.Inject;

public class TransActivity extends RoboActivity
{
	public static final int DIALOG_EMPTY_FIELD = 101;
	public static final int DIALOG_ROW_LONGCLICK = 102;
	public static final int DIALOG_TIME_ELAPSED = 103;
	public static final int DIALOG_UPDATE_ROW = 104;

	@Inject private TransListener listener;

	@InjectView(R.id.table_view) private TableLayout tableView;
	@InjectView(R.id.focus_edit) private EditText focusEdit;
	@InjectView(R.id.data_edit) private EditText dataEdit;
	@InjectView(R.id.actions_edit) private EditText actionsEdit;
	@InjectView(R.id.results_edit) private EditText resultsEdit;
	@InjectView(R.id.button_validate) private Button validateButton;

	@InjectResource(R.string.dialog_empty_field_error_title) private String dialogEmptyFieldErrorTitle;
	@InjectResource(R.string.dialog_empty_field_error_message) private String dialogEmptyFieldErrorMessage;
	@InjectResource(R.string.dialog_time_elapsed_error_title) private String dialogTimeElapsedErrorTitle;
	@InjectResource(R.string.dialog_time_elapsed_error_message) private String dialogTimeElapsedErrorMessage;
	@InjectResource(R.string.ok) private String ok;
	@InjectResource(R.string.save) private String save;
	@InjectResource(R.string.update) private String update;
	@InjectResource(R.string.cancel) private String cancel;
	
	private TableRow selectedRow;
	private TableLayout dialogTableView;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trans);
		listener.setContext(this);
		
		validateButton.setOnClickListener(listener);
	}
	
	@Override
	protected Dialog onCreateDialog(int id)
	{
        switch (id)
        {
	        case DIALOG_EMPTY_FIELD:
	            return new AlertDialog.Builder(this)
//	                .setIcon(R.drawable.)
	                .setTitle(dialogEmptyFieldErrorTitle)
	                .setMessage(dialogEmptyFieldErrorMessage)
	                .setNegativeButton(ok, null)
	                .create();
	        case DIALOG_ROW_LONGCLICK:
	            return new AlertDialog.Builder(this)
	                .setItems(R.array.trans_longclick, listener)
	                .create();
	        case DIALOG_TIME_ELAPSED:
	            return new AlertDialog.Builder(this)
	                .setTitle(dialogTimeElapsedErrorTitle)
	                .setMessage(dialogTimeElapsedErrorMessage)
	                .setNegativeButton(ok, null)
	                .create();
	        case DIALOG_UPDATE_ROW:
	        	ScrollView scroll = (ScrollView)LayoutInflater.from(this).inflate(R.layout.trans_update_item, null);
	        	dialogTableView = (TableLayout)scroll.findViewById(R.id.table_view);
	        	((EditText)dialogTableView.findViewById(R.id.focus_edit)).setText(((TextView)selectedRow.findViewById(R.id.focus_view)).getText().toString());
	        	((EditText)dialogTableView.findViewById(R.id.data_edit)).setText(((TextView)selectedRow.findViewById(R.id.data_view)).getText().toString());
	        	((EditText)dialogTableView.findViewById(R.id.actions_edit)).setText(((TextView)selectedRow.findViewById(R.id.actions_view)).getText().toString());
	        	((EditText)dialogTableView.findViewById(R.id.results_edit)).setText(((TextView)selectedRow.findViewById(R.id.results_view)).getText().toString());
	        	return new AlertDialog.Builder(this)
	        		.setTitle(update)
	        		.setView(scroll)
	        		.setPositiveButton(save, listener)
	        		.setNegativeButton(cancel, listener)
	        		.create();
        }
        return null;
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
   		MenuItem item = menu.add("Transmission Menu");
	   	
	   	item.setOnMenuItemClickListener(new OnMenuItemClickListener()
		{
			@Override
			public boolean onMenuItemClick(MenuItem item)
			{
				// Do something.
				Toast.makeText(TransActivity.this, "Trans Menu 1", Toast.LENGTH_SHORT).show();
				return true;
			}
		});

	   	return true;
    }

	/**
	 * @return the tableView
	 */
	public TableLayout getTableView() {
		return tableView;
	}

	/**
	 * @return the focusEdit
	 */
	public String getFocus() {
		return focusEdit.getText().toString();
	}

	/**
	 * @return the dataEdit
	 */
	public String getData() {
		return dataEdit.getText().toString();
	}

	/**
	 * @return the actionsEdit
	 */
	public String getActions() {
		return actionsEdit.getText().toString();
	}

	/**
	 * @return the resultsEdit
	 */
	public String getResults() {
		return resultsEdit.getText().toString();
	}

	/**
	 * Reset the EditTexts of the activity view
	 */
	public void resetEditTexts() {
		focusEdit.setText("");
		dataEdit.setText("");
		actionsEdit.setText("");
		resultsEdit.setText("");
	}

	/**
	 * @return the dialogTableView
	 */
	public TableLayout getDialogTableView() {
		return dialogTableView;
	}

	/**
	 * @param selectedRow the selectedRow to set
	 */
	public void setSelectedRow(TableRow selectedRow) {
		this.selectedRow = selectedRow;
	}
}