package uk.ac.brookes.arnaudbos.luscinia.listeners;

import uk.ac.brookes.arnaudbos.luscinia.views.FolderActivity;
import uk.ac.brookes.arnaudbos.luscinia.views.PatientActivity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;

public class PatientListener implements OnClickListener, OnItemClickListener
{
	private PatientActivity context;
	
	@Override
	public void onClick(View view)
	{
	}

	@Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		if(parent instanceof GridView)
		{
			//TODO: Try to launch the proper application to open this attachment
	    	//Attachment document = (Attachment) parent.getAdapter().getItem(position);
			//byte[] fileBytes = Base64.decode(document.getDataBase64());
			//File filePath;
			//if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
			//{
			//		filePath = new File(Environment.getExternalStorageDirectory()+"/temp_file."+document.getContentType().subString(document.getContentType().indexOf("/")+1));
			//}
			//else
			//{
			//		filePath = new File("/data/data/" + context.getPackageName() + "/temp_file."+document.getContentType().subString(document.getContentType().indexOf("/")+1));
			//}
			//FileOutputStream outputStream = new FileOutputStream(filePath, true);
			//outputStream.write(fileBytes);
			//outputStream.flush();
			//outputStream.close();
		}
		else if(parent instanceof ListView)
		{
	    	//TODO: Open the selected folder
			//Object folder = parent.getAdapter().getItem(position);
			launchFolderActivity();
		}
	}
	
	private void launchFolderActivity()
	{
		Intent intent = new Intent(this.context, FolderActivity.class);
		intent.putExtra("folder", "Dossier de soins");
        this.context.startActivity(intent);
	}
	
	public void setContext(PatientActivity context)
	{
		this.context = context;
	}
}
