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

import java.io.File;
import java.io.FileOutputStream;

import org.ektorp.AttachmentInputStream;

import uk.ac.brookes.arnaudbos.luscinia.data.Folder;
import uk.ac.brookes.arnaudbos.luscinia.utils.Log;
import uk.ac.brookes.arnaudbos.luscinia.views.PatientActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.ListView;

/**
 * Events listener dedicated to the PatientActivity
 * @author arnaudbos
 */
public class PatientListener implements OnItemClickListener, OnClickListener, OnItemLongClickListener
{
	private PatientActivity context;
	private Folder selectedFolder;

	@Override
	public void onClick(DialogInterface dialog, int which)
	{
		Log.d("PatientListener.DialogInterface.onClick");
		switch(which)
		{
			case PatientActivity.ADD_ADMIN_FOLDER:
				Log.d("ADD_ADMIN_FOLDER pressed");
				// TODO: Create a new administrative folder
				break;
			case PatientActivity.ADD_NURSING_FOLDER:
				Log.d("ADD_NURSING_FOLDER pressed");
				// Create a new nursing folder
				context.createNursingFolder();
				break;
			case PatientActivity.ADD_MEDICAL_FOLDER:
				Log.d("ADD_MEDICAL_FOLDER pressed");
				// TODO: Create a new medical folder
				break;

			case DialogInterface.BUTTON_POSITIVE:
				Log.d("Button BUTTON_POSITIVE pressed");
				// Delete the selected folder
				context.deleteFolder(selectedFolder);
				break;
		}
	}

	@Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		if(parent instanceof GridView)
		{
			Log.d("PatientListener: AttachmentDocumentsGridView.onItemClick");
			// Try to launch the proper application to open this attachment
	    	AttachmentInputStream document = (AttachmentInputStream) parent.getAdapter().getItem(position);
			//byte[] fileBytes = Base64.decode(document.getDataBase64());
	    	
	    	// Create a path where the document could be written
			String filePath;
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
			{
					filePath = Environment.getExternalStorageDirectory()+"/temp_file."+document.getContentType().substring(document.getContentType().indexOf("/")+1);
			}
			else
			{
					filePath = "/data/data/" + context.getPackageName() + "/temp_file."+document.getContentType().substring(document.getContentType().indexOf("/")+1);
			}
			Log.d("File path is "+filePath);
			
	    	// Try to write the document on disk
			try
			{
				FileOutputStream outputStream = new FileOutputStream(filePath, true);
				byte buf[]=new byte[1024];
				int len;
				while((len=document.read(buf))>0)
				{
					outputStream.write(buf,0,len);
				}
				outputStream.close();
				document.close();
				Log.d("File written successfully");

				// Start a new ACTION_VIEW intent to find an application able to open this kind of document
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setDataAndType(Uri.fromFile(new File(filePath)), document.getContentType());
				Log.d("Start activity for MIME TYPE="+document.getContentType());
				context.startActivity(i);
			}
			catch (Exception e)
			{
				Log.e("Error while opening attached document.", e);
			}
		}
		else if(parent instanceof ListView)
		{
			Log.d("PatientListener: FoldersListView.onItemClick");
			// Start the appropriate folder activity for the selected folder
			context.launchFolderActivity((Folder)parent.getAdapter().getItem(position));
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
	{
		if(parent instanceof GridView)
		{
			Log.d("PatientListener: AttachmentDocumentsGridView.onItemLongClick");
			// TODO: Show dialog asking confirmation to delete the document
			return true;
		}
		else if(parent instanceof ListView)
		{
			Log.d("PatientListener: FoldersListView.onItemLongClick");
			// Save the selected forlder and display dialog to confirm deletion
			selectedFolder = (Folder)parent.getAdapter().getItem(position);
			context.showDialog(PatientActivity.DIALOG_DELETE_FOLDER);
			return true;
		}
		return false;
	}

	/**
	 * Set the listener's context
	 * @param context The context to set
	 */
	public void setContext(PatientActivity context)
	{
		this.context = context;
	}
}
