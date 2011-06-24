package uk.ac.brookes.arnaudbos.luscinia.views;

import org.miscwidgets.widget.EasingType.Type;
import org.miscwidgets.widget.ExpoInterpolator;
import org.miscwidgets.widget.Panel;

import roboguice.inject.InjectExtra;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import uk.ac.brookes.arnaudbos.luscinia.R;
import uk.ac.brookes.arnaudbos.luscinia.widget.FolderActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;

public class FolderActivity extends FolderActivityGroup
{
	@InjectExtra("folder") private String folder;

	@InjectResource(R.string.patient_title) private String patientTitle;

	@InjectView(R.id.actionbar) private ActionBar actionBar;
	@InjectView(R.id.internal_content) private RelativeLayout internalContentLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.folder);

        prepareActionBar();
        getFolderInfos(folder);
        prepareFolderInfos();
        
        Panel panel = (Panel) findViewById(R.id.bottomPanel);
        panel.setInterpolator(new ExpoInterpolator(Type.OUT));
        
        panel = (Panel) findViewById(R.id.leftPanel);
        panel.setInterpolator(new ExpoInterpolator(Type.OUT));

		this.setTargetLayout(internalContentLayout);
//		Intent intent = new Intent(this, DashboardActivity.class);
//		intent.putExtra("login", "login");
//		intent.putExtra("password", "password");
//        startChildActivity("DashboardActivity", intent);
	}

	private void prepareActionBar()
	{
        actionBar.setTitle(patientTitle+" "+folder);
        actionBar.setHomeAction(new HomeAction());
        actionBar.addAction(new StubShareAction());
        actionBar.addAction(new StubSearchAction());
	}

	private void prepareFolderInfos()
	{
	}

	private void getFolderInfos(String patient2)
	{
		// TODO: Stub method. Implement it.
	}

	private class HomeAction implements Action
    {
		@Override
		public void performAction(View view)
		{
			startActivity(new Intent(FolderActivity.this, DashboardActivity.class));
			FolderActivity.this.finish();
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
			Toast.makeText(FolderActivity.this, "ShareAction", Toast.LENGTH_SHORT).show();
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
			Toast.makeText(FolderActivity.this, "otherAction", Toast.LENGTH_SHORT).show();
		}
		
		@Override
		public int getDrawable()
		{
			return R.drawable.ic_title_export_default;
		}
	};
}
