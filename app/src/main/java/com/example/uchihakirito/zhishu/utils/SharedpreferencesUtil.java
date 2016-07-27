package com.example.uchihakirito.zhishu.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SharedpreferencesUtil {

	private Context context;

	public SharedpreferencesUtil(Context context) {
		super();
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public void delectImageTemp() {
		File imgTemp = new File(DemoUtils.getRealFilePath(context,getImageTempNameUri()));
		imgTemp.delete();
		setNewImageTempName();
	}

	public void setNewImageTempName() {
		SimpleDateFormat addtimeFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String addtime = addtimeFormat.format(new Date());
		final String IMAGE_FILE_LOCATION = "file://" + App.returnCacheDir()+ "/" + addtime + ".jpg";
//		final String IMAGE_FILE_LOCATION = "file:///sdcard/" + addtime + ".jpg";
		SharedPreferences mySharedPreferences = context.getSharedPreferences("ImageTempName", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = mySharedPreferences.edit();
		editor.putString("imageuri", IMAGE_FILE_LOCATION);
		editor.commit();
	}

	public Uri getImageTempNameUri() {
		SharedPreferences sharedPreferences = context.getSharedPreferences("ImageTempName", Activity.MODE_PRIVATE);
		String imageuri = sharedPreferences.getString("imageuri", "");
		return Uri.parse(imageuri);
	}
	public String getImageTempNameString() {
		SharedPreferences sharedPreferences = context.getSharedPreferences("ImageTempName", Activity.MODE_PRIVATE);
		String imageuri = sharedPreferences.getString("imageuri", "");
		return imageuri;
	}
	public String getImageTempNameString2() {
		SharedPreferences sharedPreferences = context.getSharedPreferences("ImageTempName", Activity.MODE_PRIVATE);
		String imageuri = sharedPreferences.getString("imageuri", "");
		return imageuri.replace("file://", "");
	}
}
