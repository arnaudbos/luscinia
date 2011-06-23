package uk.ac.brookes.arnaudbos.luscinia.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import uk.ac.brookes.arnaudbos.luscinia.R;
import uk.ac.brookes.arnaudbos.luscinia.adapters.PatientDocumentAdapter;
import uk.ac.brookes.arnaudbos.luscinia.adapters.PatientFolderAdapter;
import uk.ac.brookes.arnaudbos.luscinia.listeners.PatientListener;
import uk.ac.brookes.arnaudbos.luscinia.utils.Log;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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
	@InjectView(R.id.folders_listview) private ListView foldersListView;
	@InjectView(R.id.patient_picture) private ImageView patientPictureView;
	@InjectView(R.id.patient_first_infos) private TextView patientFirstInfosView;
	@InjectView(R.id.patient_rest_infos) private TextView patientRestInfosView;
	@InjectView(R.id.attached_documents) private GridView attachedDocumentsView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.patient);
        listener.setContext(this);

        prepareActionBar();
        getPatientInfos(patient);
        preparePatientInfos();
        prepareAttachedDocumentsView();
        prepareFoldersListView();
        
//        Panel panel = (Panel) findViewById(R.id.bottomPanel);
//        panel.setInterpolator(new ExpoInterpolator(Type.OUT));
//        
//        panel = (Panel) findViewById(R.id.leftPanel);
//        panel.setInterpolator(new ExpoInterpolator(Type.OUT));
	}

	private void prepareActionBar()
	{
        actionBar.setTitle(patientTitle+" "+patient);
        actionBar.setHomeAction(new HomeAction());
        actionBar.addAction(new StubShareAction());
        actionBar.addAction(new StubSearchAction());
	}

	private void prepareFoldersListView()
	{
		Map<String, Object> temp_folder = new HashMap<String, Object>();
		temp_folder.put("name", "Temp Folder");
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		list.add(temp_folder);
		list.add(temp_folder);
		list.add(temp_folder);
		list.add(temp_folder);
		list.add(temp_folder);
		list.add(temp_folder);
		list.add(temp_folder);
        foldersListView.setAdapter(new PatientFolderAdapter(this, list));

        foldersListView.setOnItemClickListener(listener);
	}
	
	private void preparePatientInfos()
	{
	}

	private void prepareAttachedDocumentsView()
	{
        attachedDocumentsView.setAdapter(new PatientDocumentAdapter(this, null));
        
        LayoutParams params = attachedDocumentsView.getLayoutParams();
        int count = attachedDocumentsView.getAdapter().getCount();
        
        final float scale = getResources().getDisplayMetrics().density;
        int unit = (int) (160 * scale + 0.5f);
        
        if(count%3 == 0)
        {
        	params.height = count/3*unit;
        	attachedDocumentsView.setLayoutParams(params);
        }
        else
        {
        	params.height = (count/3+1)*unit;
        	attachedDocumentsView.setLayoutParams(params);
        }

        attachedDocumentsView.setOnItemClickListener(listener);
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
			startActivity(new Intent(PatientActivity.this, DashboardActivity.class));
			PatientActivity.this.finish();
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
