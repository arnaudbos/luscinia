package uk.ac.brookes.arnaudbos.luscinia.views;

import java.text.SimpleDateFormat;
import java.util.UUID;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import uk.ac.brookes.arnaudbos.luscinia.LusciniaApplication;
import uk.ac.brookes.arnaudbos.luscinia.R;
import uk.ac.brookes.arnaudbos.luscinia.data.Document;
import uk.ac.brookes.arnaudbos.luscinia.data.Record;
import uk.ac.brookes.arnaudbos.luscinia.data.TransRecord;
import uk.ac.brookes.arnaudbos.luscinia.listeners.TransListener;
import uk.ac.brookes.arnaudbos.luscinia.utils.Log;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.inject.Inject;

/**
 * Activity displaying TRANS document
 * @author arnaudbos
 */
public class TransActivity extends RoboActivity
{
	public static final int DIALOG_EMPTY_FIELD = 101;
	public static final int DIALOG_TIME_ELAPSED = 102;
	public static final int DIALOG_UPDATE_ROW = 103;
	public static final int DIALOG_CREATE_RECORD_ERROR = 104;

	final Handler uiThreadCallback = new Handler();
    private ProgressDialog mProgressDialog;
    private TransRecord newRecord;

	@Inject private TransListener listener;

	@InjectExtra("document") private Document document;

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
	@InjectResource(R.string.create_loading) private String createLoading;
	@InjectResource(R.string.create_error_title) private String createErrorTitle;
	@InjectResource(R.string.create_folder_error_message) private String createRecordErrorMessage;

	private TableRow selectedRow;
	private TableLayout dialogTableView;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		Log.d("TransActivity.onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trans);
		listener.setContext(this);

		validateButton.setOnClickListener(listener);
	}

	@Override
	protected Dialog onCreateDialog(int id)
	{
		Log.d("PatientTransActivity.onCreateDialog");
		switch (id)
		{
			case DIALOG_EMPTY_FIELD:
				Log.d("Display DIALOG_EMPTY_FIELD Alert");
				return new AlertDialog.Builder(this)
				.setTitle(dialogEmptyFieldErrorTitle)
				.setMessage(dialogEmptyFieldErrorMessage)
				.setNegativeButton(ok, null)
				.create();
			case DIALOG_TIME_ELAPSED:
				Log.d("Display DIALOG_TIME_ELAPSED Alert");
				return new AlertDialog.Builder(this)
				.setTitle(dialogTimeElapsedErrorTitle)
				.setMessage(dialogTimeElapsedErrorMessage)
				.setNegativeButton(ok, null)
				.create();
			case DIALOG_UPDATE_ROW:
				Log.d("Display DIALOG_UPDATE_ROW Alert");
				// Inflate a new trans item and fill it with the content from the selected row's record
				TransRecord record = (TransRecord) selectedRow.getTag();
	
				ScrollView scroll = (ScrollView)LayoutInflater.from(this).inflate(R.layout.trans_update_item, null);
				dialogTableView = (TableLayout)scroll.findViewById(R.id.table_view);
				((EditText)dialogTableView.findViewById(R.id.focus_edit)).setText(record.getFocus());
				((EditText)dialogTableView.findViewById(R.id.data_edit)).setText(record.getData());
				((EditText)dialogTableView.findViewById(R.id.actions_edit)).setText(record.getActions());
				((EditText)dialogTableView.findViewById(R.id.results_edit)).setText(record.getResults());
	
				// Display the dialog with the trans item view
				return new AlertDialog.Builder(this)
					.setTitle(update)
					.setView(scroll)
					.setPositiveButton(save, listener)
					.setNegativeButton(cancel, listener)
					.create();
			case DIALOG_CREATE_RECORD_ERROR:
				Log.d("Display DIALOG_CREATE_RECORD_ERROR Alert");
				return new AlertDialog.Builder(this)
					.setTitle(createErrorTitle)
					.setMessage(createRecordErrorMessage)
					.setPositiveButton(ok, null)
					.create();
		}
		return null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		Log.d("TransActivity.onCreateOptionsMenu");
		//   	MenuItem item = menu.add("Transmission Menu");
		//	   	item.setOnMenuItemClickListener(new OnMenuItemClickListener()
		//		{
		//			@Override
		//			public boolean onMenuItemClick(MenuItem item)
		//			{
		//				// Do something.
		//				Toast.makeText(TransActivity.this, "Trans Menu 1", Toast.LENGTH_SHORT).show();
		//				return true;
		//			}
		//		});

		return true;
	}

	/**
	 * Save a new record into the database
	 */
	public void createRecord(final String focus, final String data, final String actions, final String results)
	{
		Log.d("TransActivity.createRecord");
		// Launch an indeterminate ProgressBar in the UI while creating the records in a new thread
    	mProgressDialog = ProgressDialog.show(this, "", createLoading, true);

    	// Create a Runnable that will be executed if the creation succeeds
    	final Runnable recordCreationSucceeded = new Runnable()
    	{
    		public void run()
    		{
    			Log.d("Record created successfully");
    			// Hide the ProgressBar and notify dataset changed to the folders ListView adapter
    			mProgressDialog.dismiss();
    			renderNewRecord();
    		}
    	};

    	// Create a Runnable that will be executed if the creation fails
    	final Runnable recordCreationFailed = new Runnable()
    	{
    		public void run()
    		{
    			Log.d("Create record failed");
    			// Hide the ProgressBar and open an Alert with an error message
    			newRecord = null;
    			mProgressDialog.dismiss();
    			showDialog(DIALOG_CREATE_RECORD_ERROR);
    		}
    	};

    	// Create the separate thread that will create the record and start it
		new Thread()
		{
			@Override public void run()
			{
				try
				{
	    			Log.d("Create the record");
					LusciniaApplication.getDB().create(getRecord(focus, data, actions, results));

					uiThreadCallback.post(recordCreationSucceeded);
				}
				catch (Exception e)
				{
					Log.e("Create record failed", e);
					uiThreadCallback.post(recordCreationFailed);
				}
			}
		}.start();
	}
	
	/**
	 * Return a new Record object
	 * @return a Record object
	 */
	private Record getRecord(String focus, String data, String actions, String results)
	{
		Log.d("TransActivity.getRecord");
		// Create a new Record
		newRecord = new TransRecord(); 
		newRecord.setId(UUID.randomUUID().toString());
		newRecord.setDocumentId(document.getId());
		newRecord.setFocus(focus);
		newRecord.setData(data);
		newRecord.setActions(actions);
		newRecord.setResults(results);
		
		return newRecord;
	}

	/**
	 * Render the record into the a new row and add it to the table
	 */
	private void renderNewRecord()
	{
		Log.d("TransActivity.renderNewRecord");
		// Inflate a new tableRow
		TableRow newRow = (TableRow) LayoutInflater.from(this).inflate(R.layout.trans_item, null);
		// Set the new record as tag for this row
		newRow.setTag(newRecord);
		newRow.setOnLongClickListener(listener);

		// Fill in the trans item row with the fields from the new record
		TextView dateView = (TextView) newRow.findViewById(R.id.date_view);
		SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy\nHH:mm:ss");
		dateView.setText(s.format(newRecord.getDate()));
		TextView focusView = (TextView) newRow.findViewById(R.id.focus_view);
		focusView.setText(newRecord.getFocus());
		TextView dataView = (TextView) newRow.findViewById(R.id.data_view);
		dataView.setText(newRecord.getData());
		TextView actionsView = (TextView) newRow.findViewById(R.id.actions_view);
		actionsView.setText(newRecord.getActions());
		TextView resultsView = (TextView) newRow.findViewById(R.id.results_view);
		resultsView.setText(newRecord.getResults());
		
		// Insert the new TableRow as last TableLayout row before the EditTexts and validation Button
		tableView.addView(newRow, tableView.getChildCount()-2);

		// Reset the content of the EditTexts
		resetEditTexts();
		if(getCurrentFocus()!=null)
		{
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		}
	}

	/**
	 * @return the tableView
	 */
	public TableLayout getTableView()
	{
		return tableView;
	}

	/**
	 * @return the focusEdit
	 */
	public String getFocus()
	{
		return focusEdit.getText().toString();
	}

	/**
	 * @return the dataEdit
	 */
	public String getData()
	{
		return dataEdit.getText().toString();
	}

	/**
	 * @return the actionsEdit
	 */
	public String getActions()
	{
		return actionsEdit.getText().toString();
	}

	/**
	 * @return the resultsEdit
	 */
	public String getResults()
	{
		return resultsEdit.getText().toString();
	}

	/**
	 * Reset the EditTexts of the activity view
	 */
	public void resetEditTexts()
	{
		focusEdit.setText("");
		dataEdit.setText("");
		actionsEdit.setText("");
		resultsEdit.setText("");
	}

	/**
	 * @return the dialogTableView
	 */
	public TableLayout getDialogTableView()
	{
		return dialogTableView;
	}

	/**
	 * @param the selectedRow
	 */
	public void setSelectedRow(TableRow selectedRow)
	{
		this.selectedRow = selectedRow;
	}

	/**
	 * @return the selectedRow
	 */
	public TableRow getSelectedRow()
	{
		return selectedRow;
	}
}