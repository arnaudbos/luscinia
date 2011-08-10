package uk.ac.brookes.arnaudbos.luscinia.listeners;

import java.io.File;
import java.io.FileOutputStream;

import org.ektorp.AttachmentInputStream;

import uk.ac.brookes.arnaudbos.luscinia.R;
import uk.ac.brookes.arnaudbos.luscinia.data.Folder;
import uk.ac.brookes.arnaudbos.luscinia.utils.Log;
import uk.ac.brookes.arnaudbos.luscinia.views.NursingFolderActivity;
import uk.ac.brookes.arnaudbos.luscinia.views.PatientActivity;
import uk.ac.brookes.arnaudbos.luscinia.views.TransActivity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

public class PatientListener implements OnItemClickListener, OnClickListener, OnItemLongClickListener
{
	private PatientActivity context;
	private Folder selectedFolder;

	@Override
	public void onClick(DialogInterface dialog, int which)
	{
		switch(which)
		{
			case PatientActivity.ADD_ADMIN_FOLDER:
				break;
			case PatientActivity.ADD_NURSING_FOLDER:
				context.createNursingFolder();
				break;
			case PatientActivity.ADD_MEDICAL_FOLDER:
				break;

			case DialogInterface.BUTTON_POSITIVE:
				context.deleteFolder(selectedFolder);
				break;
		}
	}

	@Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		if(parent instanceof GridView)
		{
			// Try to launch the proper application to open this attachment
	    	AttachmentInputStream document = (AttachmentInputStream) parent.getAdapter().getItem(position);
			//byte[] fileBytes = Base64.decode(document.getDataBase64());
			String filePath;
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
			{
					filePath = Environment.getExternalStorageDirectory()+"/temp_file."+document.getContentType().substring(document.getContentType().indexOf("/")+1);
			}
			else
			{
					filePath = "/data/data/" + context.getPackageName() + "/temp_file."+document.getContentType().substring(document.getContentType().indexOf("/")+1);
			}
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
				Intent i = new Intent(Intent.ACTION_VIEW);
				File f = new File(filePath);
				Uri data = Uri.fromFile(f);
				Log.i("filePath="+filePath);
				Log.i("file exists="+f.exists());
				Log.i("uri="+data.toString());
				i.setDataAndType(data, document.getContentType());
				context.startActivity(i);
			}
			catch (Exception e)
			{
				Log.e("Error while opening attached document.", e);
			}
		}
		else if(parent instanceof ListView)
		{
			context.launchFolderActivity((Folder)parent.getAdapter().getItem(position));
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
	{
		if(parent instanceof GridView)
		{
			return true;
		}
		else if(parent instanceof ListView)
		{
			selectedFolder = (Folder)parent.getAdapter().getItem(position);
			context.showDialog(PatientActivity.DIALOG_DELETE_FOLDER);
			return true;
		}
		return false;
	}
	
	public void setContext(PatientActivity context)
	{
		this.context = context;
	}
}
