package uk.ac.brookes.arnaudbos.luscinia.views;

import java.util.ArrayList;
import java.util.List;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import uk.ac.brookes.arnaudbos.luscinia.R;
import uk.ac.brookes.arnaudbos.luscinia.adapters.DashboardNotificationAdapter;
import uk.ac.brookes.arnaudbos.luscinia.adapters.DashboardPatientAdapter;
import uk.ac.brookes.arnaudbos.luscinia.data.Notification;
import uk.ac.brookes.arnaudbos.luscinia.listeners.DashboardListener;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.inject.Inject;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;

public class DashboardActivity extends RoboActivity
{
	@Inject private DashboardListener listener;

	@InjectExtra("login") private String login;
	@InjectExtra("password") private String password;
	
	@InjectResource(R.string.dashboard_title) private String dashboardTitle;

	@InjectView(R.id.actionbar) private ActionBar actionBar;
	@InjectView(R.id.gridView1) private GridView gridview;
	@InjectView(R.id.expandableListView1) private ExpandableListView expandablelistview;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        listener.setContext(this);

        prepareActionBar();
        stubGetUser(login, password);
        prepareExpandableListView();
        prepareGridView();
	}
	
	private void prepareExpandableListView()
	{
		List<String> groups = new ArrayList<String>();
		groups.add("Notifications");
		List<Notification> notifs = new ArrayList<Notification>();
		notifs.add(new Notification("Demande de prescription", "Message"));
		notifs.add(new Notification("Accord patient", "Message"));
		List<List<Notification>> list = new ArrayList<List<Notification>> ();
		list.add(notifs);
		
		DashboardNotificationAdapter adapter = new DashboardNotificationAdapter(this, groups, list);
		this.expandablelistview.setAdapter(adapter);
        
		this.expandablelistview.setOnGroupClickListener(listener);
		this.expandablelistview.setOnChildClickListener(listener);
	}

	private void prepareGridView()
	{
		DashboardPatientAdapter adapter = new DashboardPatientAdapter(this, null);
        gridview.setAdapter(adapter);
        
        LayoutParams params = gridview.getLayoutParams();
        int count = adapter.getCount();
        
        final float scale = getResources().getDisplayMetrics().density;
        int unit = (int) (160 * scale + 0.5f);
        
        if(count%4 == 0)
        {
        	params.height = count/4*unit;
        	gridview.setLayoutParams(params);
        }
        else
        {
        	params.height = (count/4+1)*unit;
        	gridview.setLayoutParams(params);
        }

        gridview.setOnItemClickListener(listener);
	}

	private void stubGetUser(String login, String password)
	{
		// TODO: Stub method. Implement it.
		// Do some stuff to retrieve the user
		String userFirstname = "Tiffany";
		String userName = "REMY";
		actionBar.setTitle(dashboardTitle+" "+userFirstname+" "+userName);
	}

	private void prepareActionBar()
	{
        actionBar.setTitle(dashboardTitle);
        actionBar.addAction(new StubShareAction());
        actionBar.addAction(new StubSearchAction());
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
			Toast.makeText(DashboardActivity.this, "otherAction", Toast.LENGTH_SHORT).show();
		}
		
		@Override
		public int getDrawable()
		{
			return R.drawable.ic_title_export_default;
		}
	};
}
