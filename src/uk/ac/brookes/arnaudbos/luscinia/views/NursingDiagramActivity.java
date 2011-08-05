package uk.ac.brookes.arnaudbos.luscinia.views;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import uk.ac.brookes.arnaudbos.luscinia.R;
import uk.ac.brookes.arnaudbos.luscinia.listeners.NursingDiagramListener;
import uk.ac.brookes.arnaudbos.luscinia.widget.LusciniaScrollView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.inject.Inject;

public class NursingDiagramActivity extends RoboActivity
{
	@Inject private NursingDiagramListener listener;

	@InjectView(R.id.cares_title) private TextView caresTitleView;
	@InjectView(R.id.cares_list) private LusciniaScrollView caresListScrollView;
	@InjectView(R.id.titles_row) private LinearLayout titlesRow;
	@InjectView(R.id.records_scrollview) private ScrollView recordsScrollView;
	@InjectView(R.id.records_tableview) private TableLayout recordsTableView;
	@InjectView(R.id.button_add_row) private ImageButton addRowButton;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nursing_diagram);
		listener.setContext(this);

		caresListScrollView.setScrollViewListener(listener);
		recordsScrollView.setOnTouchListener(listener);
		recordsScrollView.setVerticalScrollBarEnabled(false);
		addRowButton.setOnClickListener(listener);
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
   		MenuItem item = menu.add("NursingDiagram Menu");
	   	
	   	item.setOnMenuItemClickListener(new OnMenuItemClickListener()
		{
			@Override
			public boolean onMenuItemClick(MenuItem item)
			{
				// Do something.
				return true;
			}
		});

	   	return true;
    }

	/**
	 * @return the caresListScrollView
	 */
	public LusciniaScrollView getCaresListScrollView() {
		return caresListScrollView;
	}

	/**
	 * @return the titlesRow
	 */
	public LinearLayout getTitlesRow() {
		return titlesRow;
	}

	/**
	 * @return the recordsScrollView
	 */
	public ScrollView getRecordsScrollView() {
		return recordsScrollView;
	}

	/**
	 * @return the recordsTableView
	 */
	public TableLayout getRecordsTableView() {
		return recordsTableView;
	}
}