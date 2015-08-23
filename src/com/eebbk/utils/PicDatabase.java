package com.eebbk.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class PicDatabase extends SQLiteOpenHelper{
	
	public static final String Table_Name_Pic = "picInfo";
	public static final String Pic_ID = "_id";
	public static final String Pic_Name = "picName";
	public static final String Pic_Path = "picPath";
	

	public PicDatabase(Context context) {
		super(context, "PictureDatabase.db", null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS "+Table_Name_Pic+"("
	         +Pic_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
	         +Pic_Name+" TEXT NOT NULL,"
	         +Pic_Path+" TEXT NOT NULL)");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
