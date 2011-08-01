package uk.ac.brookes.arnaudbos.luscinia;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import uk.ac.brookes.arnaudbos.luscinia.views.TransActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.google.inject.Inject;
import com.xtremelabs.robolectric.shadows.ShadowAlertDialog;

@RunWith(InjectedTestRunner.class)
public class TransTest
{
	@Inject private TransActivity transActivity;
	@InjectView(R.id.table_view) private TableLayout tableView;
	@InjectView(R.id.titles_row) private TableRow titlesRow;
	@InjectView(R.id.focus_edit) private EditText focusEdit;
	@InjectView(R.id.data_edit) private EditText dataEdit;
	@InjectView(R.id.actions_edit) private EditText actionsEdit;
	@InjectView(R.id.results_edit) private EditText resultsEdit;
	@InjectView(R.id.button_validate) private Button validateButton;
	@InjectResource(R.string.dialog_empty_field_error_title) private String dialogEmptyFieldErrorTitle; 

	@Before
	public void setUp() throws Exception
	{
		transActivity = new TransActivity();
		transActivity.onCreate(null);
	}
	
	@Test
	public void shouldHaveViews() throws Exception
	{
        assertThat(tableView, notNullValue());
        assertThat(titlesRow, notNullValue());
        assertThat(focusEdit, notNullValue());
        assertThat(dataEdit, notNullValue());
        assertThat(actionsEdit, notNullValue());
        assertThat(resultsEdit, notNullValue());
        assertThat(validateButton, notNullValue());
    }
	
//	@Test
//	public void pressingValidateButtonWithEmptyFieldsShouldOpenErrorDialog() throws Exception
//	{
//		assertThat(validateButton.performClick(), equalTo(true));
//
//		ShadowAlertDialog shadowDialog = ShadowAlertDialog.getLatestAlertDialog();
//		assertThat(shadowDialog.getTitle(), equalTo(dialogEmptyFieldErrorTitle));
//    }
	
	@Test
	public void pressingValidateButtonShouldAddTableRow() throws Exception
	{
		int count = tableView.getChildCount();

		focusEdit.setText("Tralala");
		dataEdit.setText("Tralala");
		actionsEdit.setText("Tralala");
		resultsEdit.setText("Tralala");

		assertThat(validateButton.performClick(), equalTo(true));
		assertThat(count, not((equalTo(tableView.getChildCount()))));
    }
}