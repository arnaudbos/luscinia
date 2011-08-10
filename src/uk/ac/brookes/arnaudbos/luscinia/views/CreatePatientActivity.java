package uk.ac.brookes.arnaudbos.luscinia.views;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import uk.ac.brookes.arnaudbos.luscinia.LusciniaApplication;
import uk.ac.brookes.arnaudbos.luscinia.R;
import uk.ac.brookes.arnaudbos.luscinia.data.Patient;
import uk.ac.brookes.arnaudbos.luscinia.listeners.CreatePatientListener;
import uk.ac.brookes.arnaudbos.luscinia.utils.Log;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.inject.Inject;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
import com.markupartist.android.widget.ScrollingTextView;

public class CreatePatientActivity extends RoboActivity
{
	public static final int DIALOG_NEW_ITEM = 101;
	public static final int DIALOG_SAVE = 102;
	public static final int DIALOG_SAVE_ERROR = 103;

    final Handler uiThreadCallback = new Handler();
    private ProgressDialog mProgressDialog;

	@Inject private CreatePatientListener listener;

	@InjectView(R.id.actionbar) private ActionBar actionBar;
	@InjectView(R.id.linearLayout) private LinearLayout linearLayout;
	@InjectView(R.id.firstname) private EditText firstnameView;
	@InjectView(R.id.lastname) private EditText lastnameView;
	@InjectView(R.id.dob) private EditText dobView;
	@InjectView(R.id.insee) private EditText inseeView;
	@InjectView(R.id.telephone) private EditText telephoneView;
	@InjectView(R.id.weight) private EditText weightView;
	@InjectView(R.id.size) private EditText sizeView;
	@InjectView(R.id.add) private ImageButton addButton;
	
	@InjectResource(R.string.new_patient) private String title;
	@InjectResource(R.string.ok) private String ok;
	@InjectResource(R.string.cancel) private String cancel;
	@InjectResource(R.string.new_value) private String newValueTitle;
	@InjectResource(R.string.save) private String save;
	@InjectResource(R.string.quit) private String quit;
	@InjectResource(R.string.return_message) private String returnMessage;
	@InjectResource(R.string.discard) private String discard;
	@InjectResource(R.string.save_loading) private String saveLoading;
	@InjectResource(R.string.save_error_title) private String saveErrorTitle;
	@InjectResource(R.string.save_error_message) private String saveErrorMessage;
	@InjectResource(R.string.patient_saved) private String patientSaved;

	final Runnable threadCallBackSuceeded = new Runnable()
	{
		public void run()
		{
			mProgressDialog.dismiss();
			Toast.makeText(CreatePatientActivity.this, patientSaved, Toast.LENGTH_SHORT).show();
			finish();
		}
	};

	final Runnable threadCallBackFailed = new Runnable()
	{
		public void run()
		{
			mProgressDialog.dismiss();
			showDialog(DIALOG_SAVE_ERROR);
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.create_patient);
        listener.setContext(this);

        prepareActionBar();
        addButton.setOnClickListener(listener);
	}
	
	private void prepareActionBar()
	{
		actionBar.setTitle(title);
        actionBar.addAction(new StubShareAction());
        actionBar.addAction(new StubSearchAction());
	}

	@Override
	protected Dialog onCreateDialog(int id)
	{
		switch(id)
		{
			case DIALOG_NEW_ITEM:
				final EditText prompt = new EditText(this);
				return new AlertDialog.Builder(this)
					.setTitle(newValueTitle)
					.setView(prompt)
					.setPositiveButton(ok, new OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which) 
						{
							final LinearLayout newChild = (LinearLayout) LayoutInflater.from(CreatePatientActivity.this).inflate(R.layout.create_patient_item, null);
							String key = prompt.getText().toString();
							((ScrollingTextView)newChild.findViewById(R.id.key)).setText(key);
							newChild.setTag(key);
							
							((ImageButton)newChild.findViewById(R.id.remove)).setOnClickListener(new View.OnClickListener()
							{
								@Override
								public void onClick(View v)
								{
									linearLayout.removeView(newChild);
								}
							});
							linearLayout.addView(newChild, linearLayout.getChildCount()-1);
							prompt.setText("");
						}
					})
					.setNegativeButton(cancel, null)
					.create();
			case DIALOG_SAVE:
				return new AlertDialog.Builder(this)
					.setTitle(quit)
					.setMessage(returnMessage)
					.setPositiveButton(save, listener)
					.setNeutralButton(discard, listener)
					.setNegativeButton(cancel, null)
					.create();
			case DIALOG_SAVE_ERROR:
				return new AlertDialog.Builder(this)
					.setTitle(saveErrorTitle)
					.setMessage(saveErrorMessage)
					.setPositiveButton(ok, null)
					.create();
		}
		return null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.create_patient, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
			case R.id.save:
				savePatient();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onBackPressed()
	{
		showDialog(DIALOG_SAVE);
	}

	/**
	 * TODO: Stub class. Implement it.
	 */
	private class StubShareAction implements Action
    {
		@Override
		public void performAction(View view)
		{
			Toast.makeText(CreatePatientActivity.this, "ShareAction", Toast.LENGTH_SHORT).show();
		}
		
		@Override
		public int getDrawable()
		{
			return R.drawable.ic_title_share_default;
		}
	};
	
	/**
	 * TODO: Stub class. Implement it.
	 */
	private class StubSearchAction implements Action
    {
		@Override
		public void performAction(View view)
		{
			Toast.makeText(CreatePatientActivity.this, "otherAction", Toast.LENGTH_SHORT).show();
		}
		
		@Override
		public int getDrawable()
		{
			return R.drawable.ic_title_export_default;
		}
	}

	/**
	 * @return the linearLayout
	 */
	public LinearLayout getLinearLayout() {
		return linearLayout;
	};
	
	public void savePatient()
	{
    	mProgressDialog = ProgressDialog.show(this, "", saveLoading, true);
		new Thread()
		{
			@Override public void run()
			{
				try
				{
					LusciniaApplication.getDB().create(getPatient());
					uiThreadCallback.post(threadCallBackSuceeded);
				}
				catch (Exception e)
				{
					Log.e("Create patient failed", e);
					uiThreadCallback.post(threadCallBackFailed);
				}
			}
		}.start();
	}
	
	public Patient getPatient() throws ParseException
	{
		Patient patient = new Patient();
		patient.setDocType(Patient.PATIENT_TYPE);
		String firstname = firstnameView.getText().toString();
		String lastname = lastnameView.getText().toString();
		String dob = dobView.getText().toString();
		String insee = inseeView.getText().toString();
		String telephone = telephoneView.getText().toString();
		String weightString = weightView.getText().toString();
		String sizeString = sizeView.getText().toString();

		if (firstname !=null && !firstname.equals(""))
		{
			patient.setFirstname(firstname);
		}

		if (lastname !=null && !lastname.equals(""))
		{
			patient.setLastname(lastname);
		}

		if (dob !=null && !dob.equals(""))
		{
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			patient.setDateOfBirth(dateFormat.parse(dob));
		}

		if (insee !=null && !insee.equals(""))
		{
			patient.setInsee(insee);
		}

		if (telephone !=null && !telephone.equals(""))
		{
			patient.setTelephone(telephone);
		}

		if (weightString !=null && !weightString.equals(""))
		{
			patient.setWeight(Double.valueOf(weightString));
		}

		if (sizeString !=null && !sizeString.equals(""))
		{
			patient.setSize(Double.valueOf(sizeString));
		}
		
		int i=7;
		while (i<linearLayout.getChildCount()-1)
		{
			LinearLayout child = (LinearLayout) linearLayout.getChildAt(i);
//			String key = (String) child.findViewById(R.id.key).getTag();
			String key = (String) child.getTag();
			String value = ((EditText)child.findViewById(R.id.value)).getText().toString();
			patient.add(key, value);
			i++;
		}
		return patient;
	}
}