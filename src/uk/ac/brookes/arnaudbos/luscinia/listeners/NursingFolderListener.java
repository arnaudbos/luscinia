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

import uk.ac.brookes.arnaudbos.luscinia.utils.Log;
import uk.ac.brookes.arnaudbos.luscinia.views.NursingFolderActivity;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * Events listener dedicated to the NursingFolderActivity
 * @author arnaudbos
 */
public class NursingFolderListener implements OnClickListener, android.content.DialogInterface.OnClickListener
{
	private NursingFolderActivity context;
	
	@Override
	public void onClick(View view)
	{
    	Log.d("NursingFolderListener.onClick");
    	context.showDialog(NursingFolderActivity.DIALOG_ADD_DOCUMENT);
	}

	@Override
	public void onClick(DialogInterface dialog, int id)
	{
    	Log.d("NursingFolderListener.DialogInterface.onClick");
		switch (id)
		{
			case NursingFolderActivity.ADD_TRANS_DOCUMENT:
				Log.d("ADD_TRANS_DOCUMENT pressed");
				context.addNewTransDocument();
				break;
			case NursingFolderActivity.ADD_MACROCIBLE_DOCUMENT:
				Log.d("ADD_MACROCIBLE_DOCUMENT pressed");
				context.addNewMacrocibleDocument();
				break;
		}
	}

	/**
	 * Set the listener's context
	 * @param context The context to set
	 */
	public void setContext(NursingFolderActivity context)
	{
    	Log.d("NursingFolderListener.setContext");
		this.context = context;
	}
}
