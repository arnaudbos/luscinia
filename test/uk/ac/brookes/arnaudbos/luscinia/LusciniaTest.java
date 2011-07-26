package uk.ac.brookes.arnaudbos.luscinia;

import static com.xtremelabs.robolectric.Robolectric.shadowOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import uk.ac.brookes.arnaudbos.luscinia.views.DashboardActivity;
import uk.ac.brookes.arnaudbos.luscinia.views.LusciniaActivity;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;

import com.google.inject.Inject;
import com.xtremelabs.robolectric.shadows.ShadowActivity;
import com.xtremelabs.robolectric.shadows.ShadowIntent;

@RunWith(InjectedTestRunner.class)
public class LusciniaTest
{
	@Inject private LusciniaActivity luscinia;
	@InjectView(R.id.edit_login) private EditText editLogin;
	@InjectView(R.id.edit_password) private EditText editPassword;
	@InjectView(R.id.button_validate) private Button buttonValidate;
	@InjectResource(R.string.app_name) String appName;
	
	@Before
	public void setUp() throws Exception
	{
		luscinia.onCreate(null);
	}
	
	@Test
	public void shouldHaveApplicationName() throws Exception
	{
        assertThat(appName, equalTo("Luscinia"));
    }
	
	@Test
	public void shouldHaveButtons() throws Exception
	{
        assertThat(editLogin, notNullValue());
        assertThat(editPassword, notNullValue());
        assertThat(buttonValidate, notNullValue());
    }

    @Test
    public void pressingValidateButtonShouldStartSetupActivity() throws Exception
    {
    	editLogin.setText("tremy");
    	editPassword.setText("sushi");
    	buttonValidate.performClick();

        ShadowActivity shadowActivity = shadowOf(luscinia);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent = shadowOf(startedIntent);
        assertThat(shadowIntent.getComponent().getClassName(), equalTo(DashboardActivity.class.getName()));
    }
}