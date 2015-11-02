package com.quban.qubanvip;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class BaseApplication extends Application {

	private static final String PACKAGE = "com.quban.qubanvip";

	private Context context;
	private static BaseApplication myApp = null;

	public RequestQueue mRequestQueue;// volley框架

	@Override
	public void onCreate() {
		super.onCreate();
		myApp = this;
		context = this;
		mRequestQueue = Volley.newRequestQueue(context);
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		// shutdownHttpClient();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		// shutdownHttpClient();
	}

	public int getVersionCode(Context context) {
		int versionCode = 0;
		try {
			versionCode = context.getPackageManager()
					.getPackageInfo(PACKAGE, 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}

	public String getVersionName(Context context) {
		String appclass = "";
		try {
			appclass = context.getPackageManager().getPackageInfo(PACKAGE, 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return appclass;
	}


}
