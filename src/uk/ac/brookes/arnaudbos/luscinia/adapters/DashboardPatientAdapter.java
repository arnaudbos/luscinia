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

package uk.ac.brookes.arnaudbos.luscinia.adapters;

import java.util.List;
import java.util.Map;

import uk.ac.brookes.arnaudbos.luscinia.R;
import uk.ac.brookes.arnaudbos.luscinia.data.Patient;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class DashboardPatientAdapter extends BaseAdapter
{
	private Context mContext;
	private List<Patient> patients;

	public DashboardPatientAdapter(Context c, List<Patient> patients)
	{
		this.mContext = c;
		this.patients = patients;
	}

	public int getCount()
	{
		return patients.size();
	}

	public Object getItem(int position)
	{
		return patients.get(position);
	}

	public long getItemId(int position)
	{
		return position;
	}

	public void Remove(int id)
	{
		notifyDataSetChanged();
	}

	// create a new ImageView for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder;
		
		if (convertView == null)
		{
			holder = new ViewHolder();
			Patient current = patients.get(position);
			convertView = LayoutInflater.from(mContext).inflate(R.layout.dashboard_patient_item, null);
			holder.picture = (ImageView) convertView.findViewById(R.id.patient_picture);
			//TODO: holder.picture.setImageDrawable(Drawable.createFromStream((InputStream)patients.get(position).get("picture"), "src"));
			holder.picture.setImageResource(R.drawable.no_contact_picture);
			
			holder.text = (TextView) convertView.findViewById(R.id.patient_name);
			holder.text.setText(current.getFirstname() + " " + current.getLastname());
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}

		return convertView;
	}

	static class ViewHolder
	{
		ImageView picture;
		TextView text;
	}
}