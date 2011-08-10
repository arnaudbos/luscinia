package uk.ac.brookes.arnaudbos.luscinia.views;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.ektorp.AttachmentInputStream;
import org.ektorp.ViewQuery;
import org.miscwidgets.widget.EasingType.Type;
import org.miscwidgets.widget.ExpoInterpolator;
import org.miscwidgets.widget.Panel;

import roboguice.inject.InjectExtra;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import uk.ac.brookes.arnaudbos.luscinia.LusciniaApplication;
import uk.ac.brookes.arnaudbos.luscinia.R;
import uk.ac.brookes.arnaudbos.luscinia.data.Document;
import uk.ac.brookes.arnaudbos.luscinia.data.Folder;
import uk.ac.brookes.arnaudbos.luscinia.data.Patient;
import uk.ac.brookes.arnaudbos.luscinia.utils.Log;
import uk.ac.brookes.arnaudbos.luscinia.utils.TemplateActivityMapper;
import uk.ac.brookes.arnaudbos.luscinia.widget.DocumentView;
import uk.ac.brookes.arnaudbos.luscinia.widget.FolderActivityGroup;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;

public class NursingFolderActivity extends FolderActivityGroup
{
	public static final int DIALOG_LOAD_ERROR = 101;

	final Handler uiThreadCallback = new Handler();
    private ProgressDialog mProgressDialog;
    private List<Document> documents = new ArrayList<Document>();

    @InjectExtra("patient") private Patient patient;
	@InjectExtra("folder") private Folder folder;

	@InjectView(R.id.actionbar) private ActionBar actionBar;
	@InjectView(R.id.internal_content) private RelativeLayout internalContentLayout;
	@InjectView(R.id.tracks) private LinearLayout documentsTrack;
	@InjectView(R.id.patient_picture) private ImageView patientPictureView;
	@InjectView(R.id.patient_first_infos) private TextView patientFirstInfosView;
	@InjectView(R.id.patient_rest_infos) private TextView patientRestInfosView;
	@InjectView(R.id.bottomPanel) Panel bottomPanel;
	@InjectView(R.id.bottomPanel) Panel leftPanel;
	
	@InjectResource(R.string.ok) private String ok;
	@InjectResource(R.string.folder_loading) private String folderLoading;
	@InjectResource(R.string.load_error_title) private String loadErrorTitle;
	@InjectResource(R.string.load_error_message) private String loadErrorMessage;

	final Runnable threadCallBackSuceeded = new Runnable()
	{
		public void run()
		{
			mProgressDialog.dismiss();
			prepareDocumentsTrack();
		}
	};

	final Runnable threadCallBackFailed = new Runnable()
	{
		public void run()
		{
			mProgressDialog.dismiss();
			showDialog(DIALOG_LOAD_ERROR);
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.folder);
		this.setTargetLayout(internalContentLayout);

        prepareActionBar();        
        bottomPanel.setInterpolator(new ExpoInterpolator(Type.OUT));
        leftPanel.setInterpolator(new ExpoInterpolator(Type.OUT));

		preparePatientInfos();
		loadDocuments();
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
        actionBar.setTitle(folder.getTitle());
        actionBar.setHomeAction(new HomeAction());
        actionBar.addAction(new StubShareAction());
        actionBar.addAction(new StubSearchAction());
	}
	
	private void preparePatientInfos()
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String firstInfos = "";
		if(patient.getDateOfBirth()!=null)
		{
			firstInfos += "\n" + dateFormat.format(patient.getDateOfBirth());
		}
		if(patient.getInsee()!=null)
		{
			firstInfos += "\n" + patient.getInsee();
		}
		if(patient.getTelephone()!=null)
		{
			firstInfos += "\n" + patient.getTelephone();
		}
		if(patient.getWeight()!=null)
		{
			firstInfos += "\n" + Double.toString(patient.getWeight());
		}
		if(patient.getSize()!=null)
		{
			firstInfos += "\n" + Double.toString(patient.getSize());
		}
		patientFirstInfosView.setText(firstInfos);

		String restInfos = "";
		for (Map.Entry<String, Object> entry : patient.getProperties().entrySet())
		{
			restInfos += entry.getKey() + " " + entry.getValue() + "\n";
		}
		patientRestInfosView.setText(restInfos);
	}
	
	private void loadDocuments()
	{
    	mProgressDialog = ProgressDialog.show(this, "", folderLoading, true);
		new Thread()
		{
			@Override public void run()
			{
				try
				{
					documents = LusciniaApplication.getDB().queryView(new ViewQuery().designDocId("_design/views").viewName(Document.VIEW_ALL_DOCUMENTS).key(folder.getId()), Document.class);
					uiThreadCallback.post(threadCallBackSuceeded);
				}
				catch (Exception e)
				{
					Log.e("Load documents failed", e);
					uiThreadCallback.post(threadCallBackFailed);
				}
			}
		}.start();
	}

    private void prepareDocumentsTrack()
    {
    	for(final Document document : documents)
    	{
	    	DocumentView doc = new DocumentView(this, getResources().getDrawable(R.drawable.no_folder_picture), document.getTitle(), null);
	    	OnClickListener listener;
    		switch (TemplateActivityMapper.toActivity(document.getType()))
    		{
				case TRANS:
					listener = new OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
					        actionBar.setTitle(folder.getTitle()+" - "+document.getTitle());
					        startChildActivity(document.getId(), new Intent(NursingFolderActivity.this, TransActivity.class));
					        bottomPanel.getHandle().performClick();
						}
					};
					doc.setOnClickListener(listener);
					doc.setText(document.getTitle());
					break;
				case MACROCIBLE:
					listener = new OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
					        actionBar.setTitle(folder.getTitle()+" - "+document.getTitle());
					        startChildActivity(document.getId(), new Intent(NursingFolderActivity.this, MacrocibleActivity.class));
					        bottomPanel.getHandle().performClick();
						}
					};
					doc.setOnClickListener(listener);
					doc.setText(document.getTitle());
					break;
				case NURSING_DIAGRAM:
					listener = new OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
					        actionBar.setTitle(folder.getTitle()+" - "+document.getTitle());
					        startChildActivity(document.getId(), new Intent(NursingFolderActivity.this, NursingDiagramActivity.class));
					        bottomPanel.getHandle().performClick();
						}
					};
					doc.setOnClickListener(listener);
					doc.setText(document.getTitle());
					break;
				case GENERIC:
				default:
					break;
			}
    		documentsTrack.addView(doc, documentsTrack.getChildCount()-1);
    	}

    	startChildActivity("template-trans", new Intent(NursingFolderActivity.this, TransActivity.class));
	}

	@Override
	protected Dialog onCreateDialog(int id)
	{
		switch(id)
		{
			case DIALOG_LOAD_ERROR:
				return new AlertDialog.Builder(this)
					.setTitle(loadErrorTitle)
					.setMessage(loadErrorMessage)
					.setPositiveButton(ok, null)
					.create();
		}
		return null;
	}

	private class HomeAction implements Action
    {
		@Override
		public void performAction(View view)
		{
			Intent intent = new Intent(NursingFolderActivity.this, DashboardActivity.class);
			intent.putExtra("login", "osef");
			intent.putExtra("password", "osef");
			startActivity(intent);
			NursingFolderActivity.this.finish();
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
			Toast.makeText(NursingFolderActivity.this, "ShareAction", Toast.LENGTH_SHORT).show();
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
			Toast.makeText(NursingFolderActivity.this, "otherAction", Toast.LENGTH_SHORT).show();
		}
		
		@Override
		public int getDrawable()
		{
			return R.drawable.ic_title_export_default;
		}
	};
}
