package uk.ac.brookes.arnaudbos.luscinia;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import roboguice.inject.InjectView;
import uk.ac.brookes.arnaudbos.luscinia.views.MacrocibleActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.inject.Inject;

@RunWith(InjectedTestRunner.class)
public class MacrocibleTest
{
	@Inject private MacrocibleActivity macroActivity;

	@InjectView(R.id.initial_evaluation_checkBox) private CheckBox initialEvaluationCheckBox;
	@InjectView(R.id.remote_evaluation_checkBox) private CheckBox remoteEvaluationCheckBox;
	@InjectView(R.id.liaison_checkBox) private CheckBox liaisonCheckBox;
	@InjectView(R.id.mTextView) private TextView mTextView;
	@InjectView(R.id.tTextView) private TextView tTextView;
	@InjectView(R.id.vTextView) private TextView vTextView;
	@InjectView(R.id.eTextView) private TextView eTextView;
	@InjectView(R.id.dTextView) private TextView dTextView;
	@InjectView(R.id.mEditText) private EditText mEditText;
	@InjectView(R.id.tEditText) private EditText tEditText;
	@InjectView(R.id.vEditText) private EditText vEditText;
	@InjectView(R.id.eEditText) private EditText eEditText;
	@InjectView(R.id.dEditText) private EditText dEditText;
	@InjectView(R.id.button_validate) private Button validateButton;
	@InjectView(R.id.button_update) private Button updateButton;

	@Before
	public void setUp() throws Exception
	{
		macroActivity = new MacrocibleActivity();
		macroActivity.onCreate(null);
	}
	
	@Test
	public void shouldHaveViews() throws Exception
	{
    	assertThat(initialEvaluationCheckBox, notNullValue());
        assertThat(remoteEvaluationCheckBox, notNullValue());
        assertThat(liaisonCheckBox, notNullValue());
        assertThat(mTextView, notNullValue());
        assertThat(tTextView, notNullValue());
        assertThat(vTextView, notNullValue());
        assertThat(eTextView, notNullValue());
        assertThat(dTextView, notNullValue());
        assertThat(mEditText, notNullValue());
        assertThat(tEditText, notNullValue());
        assertThat(vEditText, notNullValue());
        assertThat(eEditText, notNullValue());
        assertThat(dEditText, notNullValue());
        assertThat(validateButton, notNullValue());
        assertThat(updateButton, notNullValue());
    }
	
	@Test
	public void pressingUpdateButtonShouldAllowEdition() throws Exception
	{
        assertThat(initialEvaluationCheckBox.isEnabled(), equalTo(false));
        assertThat(remoteEvaluationCheckBox.isEnabled(), equalTo(false));
        assertThat(liaisonCheckBox.isEnabled(), equalTo(false));
        assertThat(mEditText.isEnabled(), equalTo(false));
        assertThat(tEditText.isEnabled(), equalTo(false));
        assertThat(vEditText.isEnabled(), equalTo(false));
        assertThat(eEditText.isEnabled(), equalTo(false));
        assertThat(dEditText.isEnabled(), equalTo(false));
        assertThat(validateButton.getVisibility(), equalTo(View.GONE));
        assertThat(updateButton.getVisibility(), equalTo(View.VISIBLE));

		assertThat(updateButton.performClick(), equalTo(true));

        assertThat(initialEvaluationCheckBox.isEnabled(), equalTo(true));
        assertThat(remoteEvaluationCheckBox.isEnabled(), equalTo(true));
        assertThat(liaisonCheckBox.isEnabled(), equalTo(true));
        assertThat(mEditText.isEnabled(), equalTo(true));
        assertThat(tEditText.isEnabled(), equalTo(true));
        assertThat(vEditText.isEnabled(), equalTo(true));
        assertThat(eEditText.isEnabled(), equalTo(true));
        assertThat(dEditText.isEnabled(), equalTo(true));
        assertThat(validateButton.getVisibility(), equalTo(View.VISIBLE));
        assertThat(updateButton.getVisibility(), equalTo(View.GONE));
    }
	
	@Test
	public void pressingValidateButtonShouldSaveRecord() throws Exception
	{
//		String m = mEditText.getText().toString();
//		String t = tEditText.getText().toString();
//		String v = vEditText.getText().toString();
//		String e = eEditText.getText().toString();
//		String d = dEditText.getText().toString();
		updateButton.performClick();
		mEditText.setText("M");
		tEditText.setText("T");
		vEditText.setText("V");
		eEditText.setText("E");
		dEditText.setText("D");

		assertThat(validateButton.performClick(), equalTo(true));

        assertThat(initialEvaluationCheckBox.isEnabled(), equalTo(false));
        assertThat(remoteEvaluationCheckBox.isEnabled(), equalTo(false));
        assertThat(liaisonCheckBox.isEnabled(), equalTo(false));
        assertThat(mEditText.isEnabled(), equalTo(false));
        assertThat(tEditText.isEnabled(), equalTo(false));
        assertThat(vEditText.isEnabled(), equalTo(false));
        assertThat(eEditText.isEnabled(), equalTo(false));
        assertThat(dEditText.isEnabled(), equalTo(false));
        assertThat(validateButton.getVisibility(), equalTo(View.GONE));
        assertThat(updateButton.getVisibility(), equalTo(View.VISIBLE));
    }
}