package uk.ac.brookes.arnaudbos.luscinia.listeners;

import uk.ac.brookes.arnaudbos.luscinia.views.DashboardActivity;
import uk.ac.brookes.arnaudbos.luscinia.views.LusciniaActivity;
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
			boolean loginOk = stubCallLoginManagerMethod();
			if (loginOk)
			{
				launchLusciniaActivity(login);
			}
			else
			{
				context.showDialog(LusciniaActivity.DIALOG_LOGIN_ERROR);
			}
		}
	}
	
	private void launchLusciniaActivity(String login)
	{
		Intent intent = new Intent(this.context, DashboardActivity.class);
        intent.putExtra("login", login);
        this.context.startActivity(intent);
        this.context.finish();
	}

	private boolean stubCallLoginManagerMethod()
	{
		// TODO: Stub method. Implement it.
		return true;
	}

	public void setContext(LusciniaActivity context)
	{
		this.context = context;
	}
}
