package uk.ac.brookes.arnaudbos.luscinia.views;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import uk.ac.brookes.arnaudbos.luscinia.R;
import uk.ac.brookes.arnaudbos.luscinia.listeners.DashboardListener;
import uk.ac.brookes.arnaudbos.luscinia.utils.Log;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.inject.Inject;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;

public class DashboardActivity extends RoboActivity
{
	@Inject private DashboardListener listener;

	@InjectExtra("login") private String login;
	
	@InjectResource(R.string.app_name) private String appName;
	
	@InjectView(R.id.actionbar) private ActionBar actionBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        listener.setContext(this);

        prepareActionBar();
	}
	
	private void prepareActionBar()
	{
        actionBar.setTitle(appName);
        actionBar.setHomeAction(new HomeAction());
        actionBar.addAction(new StubShareAction());
        actionBar.addAction(new StubSearchAction());
	}

	private class HomeAction implements Action
    {
		@Override
		public void performAction(View view)
		{
			Toast.makeText(DashboardActivity.this, "HomeAction", Toast.LENGTH_SHORT).show();
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
			Toast.makeText(DashboardActivity.this, "ShareAction", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(DashboardActivity.this, PatientActivity.class);
			intent.putExtra("patient", "Arnaud BOS");
			DashboardActivity.this.startActivity(intent);
			DashboardActivity.this.finish();
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
			Toast.makeText(DashboardActivity.this, "otherAction", Toast.LENGTH_SHORT).show();
		}
		
		@Override
		public int getDrawable()
		{
			return R.drawable.ic_title_export_default;
		}
	};
}
