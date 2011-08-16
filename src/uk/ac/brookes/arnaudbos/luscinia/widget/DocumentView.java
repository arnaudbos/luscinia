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

package uk.ac.brookes.arnaudbos.luscinia.widget;

import uk.ac.brookes.arnaudbos.luscinia.R;
import uk.ac.brookes.arnaudbos.luscinia.data.Document;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * View representing a Luscinia document, with an icon and a title
 * @author arnaudbos
 */
public class DocumentView extends RelativeLayout
{
    private LayoutInflater inflater;
    private RelativeLayout documentView;
    private ImageView documentPicture;
    private TextView documentText;

    /**
     * Create a new DocumentView
     * @param context The context to create the view
     * @param icon The icon that will be displayed
     * @param text The title of the document
     * @param listener The listener that will be used when the DocumentView is clicked
     */
	public DocumentView(Context context, Drawable icon, String text)
	{
        super(context);

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        documentView = (RelativeLayout) inflater.inflate(R.layout.folder_document_item, null);
        addView(documentView);

        documentPicture = (ImageView) documentView.findViewById(R.id.document_picture);
        documentPicture.setImageDrawable(icon);

        documentText = (TextView) documentView.findViewById(R.id.document_name);
        documentText.setText(text);
    }
	
	@Override
	public void setOnClickListener(OnClickListener listener)
	{
		documentView.setOnClickListener(listener);
	}
	
	@Override
	public void setTag(Object tag)
	{
		documentView.setTag(tag);
	}
	
	@Override
	public Object getTag()
	{
		return documentView.getTag();
	}
}
