package uk.ac.brookes.arnaudbos.luscinia.views;

import java.util.HashMap;
import java.util.Map;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import uk.ac.brookes.arnaudbos.luscinia.LusciniaApplication;
import uk.ac.brookes.arnaudbos.luscinia.R;
import uk.ac.brookes.arnaudbos.luscinia.listeners.CreatePatientListener;
import uk.ac.brookes.arnaudbos.luscinia.utils.Log;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
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
		Map<String, Object> patient = getPatient();
		LusciniaApplication.getDB().create(patient);
	}
	
	public Map<String, Object> getPatient()
	{
		Map<String, Object> patient = new HashMap<String, Object>();
		String firstname = firstnameView.getText().toString();
		String lastname = lastnameView.getText().toString();
		String dob = dobView.getText().toString();
		String inseeString = inseeView.getText().toString();
		String telephone = telephoneView.getText().toString();
		String weightString = weightView.getText().toString();
		String sizeString = sizeView.getText().toString();

		if (firstname !=null && !firstname.equals(""))
		{
			patient.put("firstname", firstname);
		}

		if (lastname !=null && !lastname.equals(""))
		{
			patient.put("lastname", lastname);
		}

		if (dob !=null && !dob.equals(""))
		{
			patient.put("date_of_birth", dob);
		}

		if (inseeString !=null && !inseeString.equals(""))
		{
			patient.put("INSEE", Double.valueOf(inseeString));
		}

		if (telephone !=null && !telephone.equals(""))
		{
			patient.put("telephone", telephone);
		}

		if (weightString !=null && !weightString.equals(""))
		{
			patient.put("weight", Double.valueOf(weightString));
		}

		if (sizeString !=null && !sizeString.equals(""))
		{
			patient.put("size", Double.valueOf(sizeString));
		}
		
		int i=7;
		while (i<linearLayout.getChildCount()-1)
		{
			LinearLayout child = (LinearLayout) linearLayout.getChildAt(i);
//			String key = (String) child.findViewById(R.id.key).getTag();
			String key = (String) child.getTag();
			String value = ((EditText)child.findViewById(R.id.value)).getText().toString();
			findAndPutGoddamnKey(patient, key, value);
			i++;
		}
		return patient;
	}
	
	public void findAndPutGoddamnKey (Map<String, Object> patient, String key, Object value)
	{
		Log.d("key: "+key+", value: "+value);
		if(patient.containsKey(key))
		{
			int count;
			if(key.contains("#"))
			{
				count = Integer.valueOf(key.substring(key.lastIndexOf("#")+1))+1;
				String newKey = key.substring(0, key.lastIndexOf("#")+1) + count;
				findAndPutGoddamnKey(patient, newKey, value);
			}
			else
			{
				count = 2;
				String newKey = key + "#" + count;
				findAndPutGoddamnKey(patient, newKey, value);
			}
		}
		else
		{
			patient.put(key, value);
		}
	}
}
