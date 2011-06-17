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