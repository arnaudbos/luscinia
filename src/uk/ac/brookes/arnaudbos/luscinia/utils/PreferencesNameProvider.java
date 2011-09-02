package uk.ac.brookes.arnaudbos.luscinia.utils;

import android.app.Application;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class PreferencesNameProvider implements Provider<String>
{ 
	private String packageName; 

	@Inject 
	public PreferencesNameProvider(Application application)
	{ 
		this.packageName = application.getPackageName(); 
	} 

	@Override 
	public String get()
	{ 
		return packageName + "_preferences"; 
	} 
}