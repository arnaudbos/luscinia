package uk.ac.brookes.arnaudbos.luscinia.views;

import java.util.ArrayList;
import java.util.List;

import org.miscwidgets.widget.EasingType.Type;
import org.miscwidgets.widget.ExpoInterpolator;
import org.miscwidgets.widget.Panel;

import roboguice.inject.InjectExtra;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import uk.ac.brookes.arnaudbos.luscinia.R;
import uk.ac.brookes.arnaudbos.luscinia.utils.TemplateActivityMapper;
import uk.ac.brookes.arnaudbos.luscinia.widget.DocumentView;
import uk.ac.brookes.arnaudbos.luscinia.widget.FolderActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;

public class FolderActivity extends FolderActivityGroup
{
	@InjectExtra("folder") private String folder;

	@InjectView(R.id.actionbar) private ActionBar actionBar;
	@InjectView(R.id.internal_content) private RelativeLayout internalContentLayout;
	@InjectView(R.id.tracks) private LinearLayout documentsTrack;
	@InjectView(R.id.bottomPanel) Panel bottomPanel;
	@InjectView(R.id.bottomPanel) Panel leftPanel;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.folder);
		this.setTargetLayout(internalContentLayout);

        prepareActionBar();
        getFolderInfos(folder);
        prepareFolderInfos();
        prepareDocumentsTrack();
        
        bottomPanel.setInterpolator(new ExpoInterpolator(Type.OUT));
        leftPanel.setInterpolator(new ExpoInterpolator(Type.OUT));
        
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
   		MenuItem item = menu.add("Dossier Médical Menu");
	   	
	   	item.setOnMenuItemClickListener(new OnMenuItemClickListener()
		{
			@Override
			public boolean onMenuItemClick(MenuItem item)
			{
				return true;
			}
		});

	   	return getLocalActivityManager().getCurrentActivity().onCreateOptionsMenu(menu);
    }
	
	public boolean onPrepareOptionsMenu(Menu menu)
	{
        menu.clear();
        return onCreateOptionsMenu(menu);
    }

	private void prepareActionBar()
	{
        actionBar.setTitle(folder);
        actionBar.setHomeAction(new HomeAction());
        actionBar.addAction(new StubShareAction());
        actionBar.addAction(new StubSearchAction());
	}

	private void prepareFolderInfos()
	{
	}
	
    private void prepareDocumentsTrack()
    {
    	//TODO
    	List<String> documents = new ArrayList<String>();
    	documents.add("template-trans");
    	documents.add("template-macrocible");
    	documents.add("template-nursing_diagram");
    	for(final String template : documents)
    	{
	    	DocumentView doc = new DocumentView(this, getResources().getDrawable(R.drawable.no_folder_picture), template, null);
	    	OnClickListener listener;
    		switch (TemplateActivityMapper.toActivity(template))
    		{
				case TRANS:
					listener = new OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
					        actionBar.setTitle(folder+" - Transmission ciblée");
					        startChildActivity(template, new Intent(FolderActivity.this, TransActivity.class));
					        bottomPanel.getHandle().performClick();
						}
					};
					doc.setOnClickListener(listener);
					doc.setText("Transmission ciblée");
					break;
				case MACROCIBLE:
					listener = new OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
					        actionBar.setTitle(folder+" - Fiche Macrocible");
					        startChildActivity(template, new Intent(FolderActivity.this, MacrocibleActivity.class));
					        bottomPanel.getHandle().performClick();
						}
					};
					doc.setOnClickListener(listener);
					doc.setText("Fiche Macrocible");
					break;
				case NURSING_DIAGRAM:
					listener = new OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
					        actionBar.setTitle(folder+" - Diagramme de soins");
					        startChildActivity(template, new Intent(FolderActivity.this, NursingDiagramActivity.class));
					        bottomPanel.getHandle().performClick();
						}
					};
					doc.setOnClickListener(listener);
					doc.setText("Diagramme de soins");
					break;
				case GENERIC:
				default:
					break;
			}
    		documentsTrack.addView(doc, documentsTrack.getChildCount()-1);
    	}

    	startChildActivity("template-trans", new Intent(FolderActivity.this, TransActivity.class));
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
			Intent intent = new Intent(FolderActivity.this, DashboardActivity.class);
			intent.putExtra("login", "osef");
			intent.putExtra("password", "osef");
			startActivity(intent);
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
