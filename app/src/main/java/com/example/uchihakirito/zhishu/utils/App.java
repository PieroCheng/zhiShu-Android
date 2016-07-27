package com.example.uchihakirito.zhishu.utils;

import android.os.Environment;

import java.io.File;

public class App {
	public static final boolean isFree=false;
	public static File CACHE_DIR_IMAGE;
	public static int RESULT_CODE_CAMERA=1;
	public static String CACHE_DIR;
	public static String CACHE_DIR_PRODUCTS;
	public static String CACHE_DIR_EXCEL;
	public static String CACHE_DIR_PDF;
	public static final String DirFileName="TestForPhotograph";
	public static void firstRun(){		
			if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
				CACHE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+DirFileName;
			} else {
				CACHE_DIR = Environment.getRootDirectory().getAbsolutePath() + "/"+DirFileName;
			}
			CACHE_DIR_PRODUCTS = CACHE_DIR + "/products";
//			CACHE_DIR_EXCEL=CACHE_DIR + "/excels";
//			CACHE_DIR_PDF=CACHE_DIR + "/pdfs";
			File cacheDir = new File(App.CACHE_DIR);
			if(!cacheDir.exists()){
				cacheDir.mkdirs();
			}
			File cacheDirProducts = new File(App.CACHE_DIR_PRODUCTS);
			if(!cacheDirProducts.exists()){
				cacheDirProducts.mkdirs();
			}
//			File cacheDirExcel = new File(App.CACHE_DIR_EXCEL);
//			if(!cacheDirExcel.exists()){
//				cacheDirExcel.mkdirs();
//			}
//			File cacheDirPdf = new File(App.CACHE_DIR_PDF);
//			if(!cacheDirPdf.exists()){
//				cacheDirPdf.mkdirs();
//			}
		}
	public static String returnCacheDir(){
		firstRun();
		return CACHE_DIR;
	}
}
