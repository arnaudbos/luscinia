package uk.ac.brookes.arnaudbos.luscinia.views;

import roboguice.activity.RoboActivity;
import uk.ac.brookes.arnaudbos.luscinia.R;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.Toast;

public class TransActivity extends RoboActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trans);
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
   		MenuItem item = menu.add("Trans Menu 1");
	   	
	   	item.setOnMenuItemClickListener(new OnMenuItemClickListener()
		{
			@Override
			public boolean onMenuItemClick(MenuItem item)
			{
				// Do something.
				Toast.makeText(TransActivity.this, "Trans Menu 1", Toast.LENGTH_SHORT).show();
				return true;
			}
		});

	   	return true;
    }
}