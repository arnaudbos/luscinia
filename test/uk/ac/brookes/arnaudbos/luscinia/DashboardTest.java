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
import uk.ac.brookes.arnaudbos.luscinia.views.PatientActivity;
import android.content.Intent;
import android.widget.ExpandableListView;
import android.widget.GridView;

import com.google.inject.Inject;
import com.markupartist.android.widget.ActionBar;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.shadows.ShadowActivity;
import com.xtremelabs.robolectric.shadows.ShadowIntent;

@RunWith(InjectedTestRunner.class)
public class DashboardTest
{
	@Inject private DashboardActivity dashboard;
	@InjectView(R.id.actionbar) ActionBar actionBar;
	@InjectView(R.id.gridView1) private GridView gridview;
	@InjectView(R.id.expandableListView1) private ExpandableListView expandablelistview;
	
	@Before
	public void setUp() throws Exception
	{
	    dashboard = new DashboardActivity();
	    dashboard.setIntent(new Intent(Robolectric.application, DashboardActivity.class).putExtra("login", "goober").putExtra("password", "pass"));
		dashboard.onCreate(null);
	}
	
	@Test
	public void shouldHaveViews() throws Exception
	{
        assertThat(actionBar, notNullValue());
        assertThat(expandablelistview, notNullValue());
        assertThat(gridview, notNullValue());
    }
	
	@Test
	public void pressingGridViewShouldStartPatientActivity() throws Exception
	{
		gridview.performItemClick(null, 0, 0);

        ShadowActivity shadowActivity = shadowOf(dashboard);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent = shadowOf(startedIntent);
        assertThat(shadowIntent.getComponent().getClassName(), equalTo(PatientActivity.class.getName()));
    }
}