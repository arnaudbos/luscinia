package uk.ac.brookes.arnaudbos.luscinia;

import static com.xtremelabs.robolectric.Robolectric.shadowOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import roboguice.inject.InjectView;

import uk.ac.brookes.arnaudbos.luscinia.views.DashboardActivity;
import uk.ac.brookes.arnaudbos.luscinia.views.FolderActivity;
import uk.ac.brookes.arnaudbos.luscinia.views.PatientActivity;
import android.content.Intent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.inject.Inject;
import com.markupartist.android.widget.ActionBar;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.shadows.ShadowActivity;
import com.xtremelabs.robolectric.shadows.ShadowIntent;

@RunWith(InjectedTestRunner.class)
public class PatientTest
{
	@Inject private PatientActivity patientActivity;
	@InjectView(R.id.actionbar) ActionBar actionBar;
	@InjectView(R.id.folders_listview) private ListView foldersListView;
	@InjectView(R.id.patient_picture) private ImageView patientPictureView;
	@InjectView(R.id.patient_first_infos) private TextView patientFirstInfosView;
	@InjectView(R.id.patient_rest_infos) private TextView patientRestInfosView;
	@InjectView(R.id.attached_documents) private GridView attachedDocumentsView;
	
	@Before
	public void setUp() throws Exception
	{
		patientActivity = new PatientActivity();
		patientActivity.setIntent(new Intent(Robolectric.application, PatientActivity.class).putExtra("patient", "goober"));
		patientActivity.onCreate(null);
	}
	
	@Test
	public void shouldHaveViews() throws Exception
	{
        assertThat(actionBar, notNullValue());
        assertThat(foldersListView, notNullValue());
        assertThat(patientPictureView, notNullValue());
        assertThat(patientFirstInfosView, notNullValue());
        assertThat(patientRestInfosView, notNullValue());
        assertThat(attachedDocumentsView, notNullValue());
    }
	
	@Test
	public void pressingActionBarHomeShouldStartDashboardActivity() throws Exception
	{
		actionBar.getHomeButton().performClick();

        ShadowActivity shadowActivity = shadowOf(patientActivity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent = shadowOf(startedIntent);
        assertThat(shadowIntent.getComponent().getClassName(), equalTo(DashboardActivity.class.getName()));
    }
	
	@Test
	public void pressingFoldersListViewShouldStartFolderActivity() throws Exception
	{
		foldersListView.performItemClick(null, 0, 0);

        ShadowActivity shadowActivity = shadowOf(patientActivity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent = shadowOf(startedIntent);
        assertThat(shadowIntent.getComponent().getClassName(), equalTo(FolderActivity.class.getName()));
    }
}