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

import org.ektorp.AttachmentInputStream;

import uk.ac.brookes.arnaudbos.luscinia.R;
import uk.ac.brookes.arnaudbos.luscinia.utils.ImageThreadLoader;
import uk.ac.brookes.arnaudbos.luscinia.utils.ImageThreadLoader.ImageLoadedListener;
import uk.ac.brookes.arnaudbos.luscinia.utils.Log;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PatientDocumentAdapter extends BaseAdapter
{
	private ViewHolder holder;
	private ImageThreadLoader imageLoader = new ImageThreadLoader();
	private Context mContext;
	private List<AttachmentInputStream> attachments;

	public PatientDocumentAdapter(Context c, List<AttachmentInputStream> attachments)
	{
		this.mContext = c;
		this.attachments = attachments;
	}

	public int getCount()
	{
		return attachments.size();
	}

	public AttachmentInputStream getItem(int position)
	{
		return attachments.get(position);
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
		if (convertView == null)
		{
			holder = new ViewHolder();
			AttachmentInputStream attachment = this.attachments.get(position);
			String contentType = attachment.getContentType();
			
			convertView = LayoutInflater.from(mContext).inflate(R.layout.patient_document_item, null);
			holder.picture = (ImageView) convertView.findViewById(R.id.document_picture);
			if (contentType.startsWith("image/"))
			{
				Bitmap cachedImage = null;
				try
				{
					cachedImage = imageLoader.loadImage(attachment.getId(), attachment, new ImageLoadedListener()
					{
						public void imageLoaded(Bitmap imageBitmap)
						{
							holder.picture.setImageBitmap(imageBitmap);
							notifyDataSetChanged();
						}
					});
				}
				catch (Exception e)
				{
					Log.e("Error while loading image.", e);
					holder.picture.setImageResource(R.drawable.no_folder_picture);
				}
				if( cachedImage != null )
				{
					holder.picture.setImageBitmap(cachedImage);
				}
				else
				{
					holder.picture.setImageResource(R.drawable.no_folder_picture);
				}
			}
			else if (contentType.equals("application/pdf"))
			{
				holder.picture.setImageResource(R.drawable.pdf);
			}
			else
			{
				holder.picture.setImageResource(R.drawable.no_folder_picture);
			}
			holder.text = (TextView) convertView.findViewById(R.id.document_name);
			holder.text.setText(attachment.getId());
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