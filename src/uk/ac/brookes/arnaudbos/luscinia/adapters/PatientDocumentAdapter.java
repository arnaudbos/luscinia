package uk.ac.brookes.arnaudbos.luscinia.adapters;

import java.util.Map;

import uk.ac.brookes.arnaudbos.luscinia.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class PatientDocumentAdapter extends BaseAdapter
{
	private Context mContext;
//	private List<Object> documents;

	private Integer[] mThumbIds = {
			R.drawable.no_folder_picture, R.drawable.no_folder_picture,
			R.drawable.no_folder_picture, R.drawable.no_folder_picture,
			R.drawable.no_folder_picture
	};

	public PatientDocumentAdapter(Context c, Map<String, Object> documents)
	{
		this.mContext = c;
//		this.documents = new ArrayList<Object>();
//		for (Map.Entry<String, Object> entry : documents.entrySet())
//		{
//			this.documents.add(entry.getValue());
//		}
	}

	public int getCount()
	{
		//return documents.size();
		return mThumbIds.length;
	}

	public Object getItem(int position)
	{
		//return documents.get(position);
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
			//Object document = this.documents.get(position);
			
			convertView = LayoutInflater.from(mContext).inflate(R.layout.patient_document_item, null);
			holder.picture = (ImageView) convertView.findViewById(R.id.document_picture);
			/*TODO: 
			 * if (document.getContentType().startWith("image/"))
			 * {
			 * 		byte[] decodedString = Base64.decode(document.getDataBase64());
			 * 		Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
			 * 		holder.picture.setImageBitmap(decodedByte);
			 * }
			 */
			holder.picture.setImageResource(mThumbIds[position]);
			
			holder.text = (TextView) convertView.findViewById(R.id.document_name);
			//TODO: holder.text.setText(patients.get(position).get("firstname") + " " + patients.get(position).get("lastname"));
			holder.text.setText(""+position);
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