/*
 * Copyright (C) 2011 Arnaud Bos <arnaud.tlse@gmail.com>
 * 
 * This file is part of Luscinia.
 * 
 * Luscinia is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Luscinia is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Luscinia.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.ac.brookes.arnaudbos.luscinia.listeners;

import uk.ac.brookes.arnaudbos.luscinia.R;
import uk.ac.brookes.arnaudbos.luscinia.views.NursingDiagramActivity;
import uk.ac.brookes.arnaudbos.luscinia.widget.LusciniaScrollView;
import uk.ac.brookes.arnaudbos.luscinia.widget.ScrollViewListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class NursingDiagramListener implements OnClickListener, OnTouchListener, ScrollViewListener
{
	private NursingDiagramActivity context;
	
	@Override
	public void onClick(View view)
	{
		TableLayout recordsTableView;
		switch(view.getId())
		{
			case R.id.button_add_row:
				LinearLayout newRow = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.nursing_diagram_row, null);
				((TextView)newRow.findViewById(R.id.title)).setText("New line");

				ScrollView caresList = context.getCaresListScrollView();
				LinearLayout osef = (LinearLayout) caresList.getChildAt(0);
				int childCount = osef.getChildCount();
				osef.addView(newRow, childCount-1);

				recordsTableView = context.getRecordsTableView();
				childCount = recordsTableView.getChildCount();

				TableRow newTableRow = new TableRow(context);
				RelativeLayout monday = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.nursing_diagram_item, null);
				newTableRow.addView(monday);

				RelativeLayout tuesday = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.nursing_diagram_item, null);
				newTableRow.addView(tuesday);

				RelativeLayout wednesday = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.nursing_diagram_item, null);
				newTableRow.addView(wednesday);

				RelativeLayout thursday = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.nursing_diagram_item, null);
				newTableRow.addView(thursday);

				RelativeLayout friday = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.nursing_diagram_item, null);
				newTableRow.addView(friday);

				RelativeLayout saturday = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.nursing_diagram_item, null);
				newTableRow.addView(saturday);

				RelativeLayout sunday = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.nursing_diagram_item, null);
				newTableRow.addView(sunday);
				recordsTableView.addView(newTableRow, childCount-1);
				break;
		}
	}
	
    public boolean onTouch(View v, MotionEvent event)
    {
        return true; 
    }


	/*
	 * 
	 * Implementation of a basic ScrollView synchronizer, see also LusciniaScollView and ScrollViewListener.
	 * Thanks to @Andy on StackOverflow question:
	 * http://stackoverflow.com/questions/3948934/synchronise-scrollview-scroll-positions-android
	 *
	 */
	@Override
	public void onScrollChanged(LusciniaScrollView scrollView, int x, int y, int oldx, int oldy)
	{
		context.getRecordsScrollView().scrollTo(x, y);
	}

	public void setContext(NursingDiagramActivity context)
	{
		this.context = context;
	}
}
