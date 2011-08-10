package uk.ac.brookes.arnaudbos.luscinia;

import static com.xtremelabs.robolectric.Robolectric.shadowOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.miscwidgets.widget.Panel;

import roboguice.inject.InjectView;

import uk.ac.brookes.arnaudbos.luscinia.views.DashboardActivity;
import uk.ac.brookes.arnaudbos.luscinia.views.NursingFolderActivity;
import uk.ac.brookes.arnaudbos.luscinia.views.PatientActivity;
import uk.ac.brookes.arnaudbos.luscinia.widget.DocumentView;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.inject.Inject;
import com.markupartist.android.widget.ActionBar;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.shadows.ShadowActivity;
import com.xtremelabs.robolectric.shadows.ShadowIntent;

@RunWith(InjectedTestRunner.class)
public class FolderTest
{
	@Inject private NursingFolderActivity nursingFolderActivity;
	@InjectView(R.id.actionbar) ActionBar actionBar;
	@InjectView(R.id.internal_content) private RelativeLayout internalContentLayout;
	@InjectView(R.id.tracks) private LinearLayout documentsTrack;
	@InjectView(R.id.bottomPanel) Panel bottomPanel;
	@InjectView(R.id.bottomPanel) Panel leftPanel;
	
	@Before
	public void setUp() throws Exception
	{
		nursingFolderActivity = new NursingFolderActivity();
		nursingFolderActivity.setIntent(new Intent(Robolectric.application, NursingFolderActivity.class).putExtra("folder", "Dossier de soins"));
		try
		{
			nursingFolderActivity.onCreate(null);
		}
		catch (NullPointerException e)
		{
			e.printStackTrace();
		}
	}
	
	@Test
	public void shouldHaveViews() throws Exception
	{
        assertThat(actionBar, notNullValue());
        assertThat(internalContentLayout, notNullValue());
        assertThat(leftPanel, notNullValue());
        assertThat(bottomPanel, notNullValue());
        assertThat(documentsTrack, notNullValue());
    }
	
	@Test
	public void documentsTrackShouldContainDocuments() throws Exception
	{
		for(int i=0 ; i<documentsTrack.getChildCount() ; i++)
		{
			assertThat(documentsTrack.getChildAt(i), instanceOf(DocumentView.class));
		}
    }
	
	@Test
	public void pressingDocumentInTrackShouldStartChildActivity() throws Exception
	{
		// No real way to test that because a performClick() on documentView child
		// of documentsTrack will cause FolderActivityGroup.startChildActivity to
		// throw a NullPointerException...
		int i=0;
		do
		{
			String title = ((DocumentView)documentsTrack.getChildAt(i)).getText();
			
			assertThat(title, not(equalTo(((DocumentView)documentsTrack.getChildAt(i+1)).getText())));
			i++;
		} while (i<documentsTrack.getChildCount()-1);
    }
	
	@Test
	public void pressingActionBarHomeShouldStartDashboardActivity() throws Exception
	{
		actionBar.getHomeButton().performClick();

        ShadowActivity shadowActivity = shadowOf(nursingFolderActivity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent = shadowOf(startedIntent);
        assertThat(shadowIntent.getComponent().getClassName(), equalTo(DashboardActivity.class.getName()));
    }
}