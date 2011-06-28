package uk.ac.brookes.arnaudbos.luscinia.views;

import roboguice.activity.RoboActivity;
import uk.ac.brookes.arnaudbos.luscinia.R;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.Toast;

public class MacrocibleActivity extends RoboActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.macrocible);
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
   		MenuItem item = menu.add("Macrocible Menu 1");
	   	
	   	item.setOnMenuItemClickListener(new OnMenuItemClickListener()
		{
			@Override
			public boolean onMenuItemClick(MenuItem item)
			{
				// Do something.
				Toast.makeText(MacrocibleActivity.this, "Macrocible Menu 1", Toast.LENGTH_SHORT).show();
				return true;
			}
		});

	   	return true;
    }
}