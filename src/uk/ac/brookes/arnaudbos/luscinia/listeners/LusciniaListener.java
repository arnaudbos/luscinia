package uk.ac.brookes.arnaudbos.luscinia.listeners;

import uk.ac.brookes.arnaudbos.luscinia.LusciniaApplication;
import uk.ac.brookes.arnaudbos.luscinia.utils.Log;
import uk.ac.brookes.arnaudbos.luscinia.views.DashboardActivity;
import uk.ac.brookes.arnaudbos.luscinia.views.LusciniaActivity;
import android.app.Application;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class LusciniaListener implements OnClickListener
{
	private LusciniaActivity context;
	
	@Override
	public void onClick(View view)
	{
		String login = context.getEditLogin();
		String password = context.getEditPassword();
		if (login == null || login.equals(""))
		{
			context.showDialog(LusciniaActivity.DIALOG_USERNAME_ERROR);
		}
		else if (password == null || password.equals(""))
		{
			context.showDialog(LusciniaActivity.DIALOG_PASSWORD_ERROR);
		}
		else
		{
			// TODO: call manager
			boolean loginOk = connect(login, password);
			if (loginOk)
			{
				launchDashboardActivity(login, password);
			}
			else
			{
				context.showDialog(LusciniaActivity.DIALOG_LOGIN_ERROR);
			}
		}
	}
	
	private void launchDashboardActivity(String login, String password)
	{
		Intent intent = new Intent(this.context, DashboardActivity.class);
        this.context.startActivity(intent);
        this.context.finish();
	}

	private boolean connect(String login, String password)
	{
		try
		{
			LusciniaApplication.setDB(this.context.getCouchDBUtils().getDB(login, password));
			return true;
		}
		catch (Exception e)
		{
			Log.e("CATCHED: Login action failed", e);
			return false;
		}
	}

	public void setContext(LusciniaActivity context)
	{
		this.context = context;
	}
}
