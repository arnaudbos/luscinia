package uk.ac.brookes.arnaudbos.luscinia;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import roboguice.inject.InjectView;
import uk.ac.brookes.arnaudbos.luscinia.views.NursingDiagramActivity;
import uk.ac.brookes.arnaudbos.luscinia.widget.LusciniaScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.inject.Inject;

@RunWith(InjectedTestRunner.class)
public class NursingDiagramTest
{
	@Inject private NursingDiagramActivity nursingActivity;

	@InjectView(R.id.cares_title) private TextView caresTitleView;
	@InjectView(R.id.cares_list) private LusciniaScrollView caresListScrollView;
	@InjectView(R.id.titles_row) private LinearLayout titlesRow;
	@InjectView(R.id.records_scrollview) private ScrollView recordsScrollView;
	@InjectView(R.id.records_tableview) private TableLayout recordsTableView; 

	@Before
	public void setUp() throws Exception
	{
		nursingActivity = new NursingDiagramActivity();
		nursingActivity.onCreate(null);
	}
	
	@Test
	public void shouldHaveViews() throws Exception
	{
		assertThat(caresTitleView, notNullValue());
		assertThat(caresListScrollView, notNullValue());
		assertThat(titlesRow, notNullValue());
		assertThat(recordsScrollView, notNullValue());
		assertThat(recordsTableView, notNullValue());
    }
}