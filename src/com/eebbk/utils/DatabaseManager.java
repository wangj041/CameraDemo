package com.eebbk.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class DatabaseManager {
	private static PicDatabase mDatabase;//得到一个数据库的实例
	private static SQLiteDatabase mDbWriter;
	private static Cursor mCursor;

	//创建一个可写数据库
	public static void getInstance(Context context){
		if(mDatabase ==null){
			mDatabase = new PicDatabase(context);
			mDbWriter = mDatabase.getWritableDatabase();
		}

	}

	//数据库的插入操作
	public static void insertDB(String table,String nullColumnHack,ContentValues cv){
		mDbWriter.insert(table, nullColumnHack, cv);

	}

	//数据库的查询操作
	public static Cursor queryDB(String table,String[] columns,String selection,String[] selectionArgs,
			String groupBy,String having,String orderBy){
		mCursor = mDbWriter.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
		return mCursor;

	}

	//数据库的更新操作
	public static void updateDB(String table,ContentValues values,String whereClause,String[] whereArgs){
		mDbWriter.update(table, values, whereClause, whereArgs);
	}

	//数据库的删除操作
	public static void deleteDB(String table,String whereClause,String[] whereArgs){
		mDbWriter.delete(table, whereClause, whereArgs);
		
	}
	
	//关闭数据库
	public static void closeDB(){
		mDbWriter.close();
	}

}
