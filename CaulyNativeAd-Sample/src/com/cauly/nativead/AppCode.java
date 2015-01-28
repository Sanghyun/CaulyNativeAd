package com.cauly.nativead;

import android.content.Context;

public class AppCode {

	public static final String APP_CODE = "ysYFIly2";
	
	public static String getAppCode(Context context) {
		return context.getString(R.string.cauly_app_code);
	}
	
}
