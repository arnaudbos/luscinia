package uk.ac.brookes.arnaudbos.luscinia.adapters;

import java.util.List;
import java.util.Map;

import uk.ac.brookes.arnaudbos.luscinia.R;
import uk.ac.brookes.arnaudbos.luscinia.data.Folder;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PatientFolderAdapter extends BaseAdapter
{
	private LayoutInflater mInflater;
	private List<Folder> folders;
	private Context context;

	public PatientFolderAdapter(Context c, List<Folder> folders)
	{
		mInflater = LayoutInflater.from(c);
		this.context = c;
		this.folders = folders;
	}

	public int getCount()
	{
		return folders.size();
	}

	public Folder getItem(int position)
	{
		return folders.get(position);
	}

	public long getItemId(int id)
	{
		return id;
	}

	public void Remove(int id)
	{
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder;

		Folder folder = folders.get(position);

		if (convertView == null)
		{
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.patient_folder_item, null);

			holder.text = (TextView) convertView.findViewById(R.id.folder_name);
			holder.picture = (ImageView) convertView.findViewById(R.id.folder_picture);

			holder.text.setText(folder.getTitle());
			switch (folder.getType())
			{
				case Folder.ADMINISTRATIVE_FOLDER_TYPE:
					holder.picture.setImageResource(R.drawable.no_folder_picture);
					break;
				case Folder.NURSING_FOLDER_TYPE:
					holder.picture.setImageResource(R.drawable.no_folder_picture);
					break;
				case Folder.MEDICAL_FOLDER_TYPE:
					holder.picture.setImageResource(R.drawable.no_folder_picture);
					break;
				default:
					holder.picture.setImageResource(R.drawable.no_folder_picture);
					break;
			}
			convertView.setTag(holder);
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
