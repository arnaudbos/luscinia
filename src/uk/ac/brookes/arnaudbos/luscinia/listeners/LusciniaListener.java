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
import uk.ac.brookes.arnaudbos.luscinia.views.LusciniaActivity;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * Events listener dedicated to the LusciniaActivity
 * @author arnaudbos
 */
public class LusciniaListener implements OnClickListener
{
	private LusciniaActivity context;
	
	@Override
	public void onClick(View view)
	{
    	Log.d("LusciniaListener.onClick");
		context.connect();
	}

	/**
	 * Set the listener's context
	 * @param context The context to set
	 */
	public void setContext(LusciniaActivity context)
	{
    	Log.d("LusciniaListener.setContext");
		this.context = context;
	}
}
