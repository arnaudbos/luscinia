package uk.ac.brookes.arnaudbos.luscinia.views;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import uk.ac.brookes.arnaudbos.luscinia.R;
import uk.ac.brookes.arnaudbos.luscinia.listeners.PatientListener;
import uk.ac.brookes.arnaudbos.luscinia.utils.Log;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.inject.Inject;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;

public class PatientActivity extends RoboActivity
{
	@Inject private PatientListener listener;
	
	@InjectExtra("patient") private String patient;

	@InjectResource(R.string.patient_title) private String patientTitle;
	
	@InjectView(R.id.actionbar) private ActionBar actionBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.patient);
        listener.setContext(this);

        prepareActionBar();
        getPatientInfos(patient);
	}

	private void prepareActionBar()
	{
        actionBar.setTitle(patientTitle+" "+patient);
        actionBar.setHomeAction(new HomeAction());
        actionBar.addAction(new StubShareAction());
        actionBar.addAction(new StubSearchAction());
	}
	
	private void getPatientInfos(String patient2)
	{
		// TODO: Stub method. Implement it.
	}

	private class HomeAction implements Action
    {
		@Override
		public void performAction(View view)
		{
			Toast.makeText(PatientActivity.this, "HomeAction", Toast.LENGTH_SHORT).show();
		}
		
		@Override
		public int getDrawable()
		{
			return R.drawable.ic_title_home_default;
		}
	}

	/**
	 * TODO: Stub class. Implement it.
	 */
	private class StubShareAction implements Action
    {
		@Override
		public void performAction(View view)
		{
			Toast.makeText(PatientActivity.this, "ShareAction", Toast.LENGTH_SHORT).show();
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
			Log.i(view.getId()+"");
			Toast.makeText(PatientActivity.this, "otherAction", Toast.LENGTH_SHORT).show();
		}
		
		@Override
		public int getDrawable()
		{
			return R.drawable.ic_title_export_default;
		}
	};
}
