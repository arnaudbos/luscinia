package uk.ac.brookes.arnaudbos.luscinia.adapters;

import java.util.List;
import java.util.Map;

import uk.ac.brookes.arnaudbos.luscinia.R;
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
//	private List<Map<String, Object>> patients;

	private Integer[] mThumbIds = {
			R.drawable.no_contact_picture, R.drawable.no_contact_picture,
			R.drawable.no_contact_picture, R.drawable.no_contact_picture,
			R.drawable.no_contact_picture, R.drawable.no_contact_picture,
			R.drawable.no_contact_picture, R.drawable.no_contact_picture,
			R.drawable.no_contact_picture, R.drawable.no_contact_picture,
			R.drawable.no_contact_picture, R.drawable.no_contact_picture,
			R.drawable.no_contact_picture, R.drawable.no_contact_picture
	};

	public DashboardPatientAdapter(Context c, List<Map<String, Object>> patients)
	{
		this.mContext = c;
//		this.patients = patients;
	}

	public int getCount()
	{
		//return patients.size();
		return mThumbIds.length;
	}

	public Object getItem(int position)
	{
		//return patients.get(position);
		return mThumbIds[position];
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.dashboard_patient_item, null);
			holder.picture = (ImageView) convertView.findViewById(R.id.patient_picture);
			//TODO: holder.picture.setImageDrawable(Drawable.createFromStream((InputStream)patients.get(position).get("picture"), "src"));
			holder.picture.setImageResource(mThumbIds[position]);
			
			holder.text = (TextView) convertView.findViewById(R.id.patient_name);
			//TODO: holder.text.setText(patients.get(position).get("firstname") + " " + patients.get(position).get("lastname"));
			holder.text.setText("Monsieur "+position);
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