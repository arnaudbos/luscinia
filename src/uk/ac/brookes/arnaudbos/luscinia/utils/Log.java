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

package uk.ac.brookes.arnaudbos.luscinia.utils;

import android.content.Context;

public class Log
{
	public static void v(String message)
	{
		android.util.Log.v("Luscinia", message);
	}

	public static void v(String message, Throwable e)
	{
		android.util.Log.v("Luscinia", message, e);
	}

	public static void v(Context context, String message)
	{
		android.util.Log.v("Luscinia", message);
	}

	public static void v(Context context, String message, Throwable e)
	{
		android.util.Log.v("Luscinia", message, e);
	}

	public static void e(Throwable e)
	{
		android.util.Log.e("Luscinia", "Error", e);
	}

	public static void e(String message)
	{
		android.util.Log.e("Luscinia", message);
	}

	public static void e(String message, Throwable e)
	{
		android.util.Log.e("Luscinia", message, e);
	}

	public static void e(Context context, String message)
	{
		android.util.Log.e("Luscinia", message);
	}

	public static void e(Context context, String message, Throwable e)
	{
		android.util.Log.e("Luscinia", message, e);
	}

	public static void w(Throwable e)
	{
		android.util.Log.w("Luscinia", "Warning", e);
	}

	public static void w(String message)
	{
		android.util.Log.w("Luscinia", message);
	}

	public static void w(String message, Throwable e)
	{
		android.util.Log.w("Luscinia", message, e);
	}

	public static void w(Context context, String message)
	{
		android.util.Log.w("Luscinia", message);
	}

	public static void w(Context context, String message, Throwable e)
	{
		android.util.Log.w("Luscinia", message, e);
	}

	public static void d(String message)
	{
		android.util.Log.d("Luscinia", message);
	}

	public static void d(String message, Throwable e)
	{
		android.util.Log.d("Luscinia", message, e);
	}

	public static void d(Context context, String message)
	{
		android.util.Log.d("Luscinia", message);
	}

	public static void d(Context context, String message, Throwable e)
	{
		android.util.Log.d("Luscinia", message, e);
	}

	public static void i(String message)
	{
		android.util.Log.i("Luscinia", message);
	}

	public static void i(String message, Throwable e)
	{
		android.util.Log.i("Luscinia", message, e);
	}

	public static void i(Context context, String message)
	{
		android.util.Log.i("Luscinia", message);
	}

	public static void i(Context context, String message, Throwable e)
	{
		android.util.Log.i("Luscinia", message, e);
	}
}