package com.wise.sql;

import com.wise.pubclas.Config;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBExcute {	
	/**
	 * 向数据库中插入记录
	 * @param values
	 */
	public void InsertDB(Context context, ContentValues values ,String table){
		DBHelper dbHelper = new DBHelper(context);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.insert(table, null, values);
		db.close();
		dbHelper.close();
	}
	/**
	 * 更新基础数据表
	 * @param context
	 * @param values
	 * @param Title
	 */
	public void UpdateDB(Context context,ContentValues values,String Title){
	    DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.update(Config.TB_Base, values, "Title=?", new String[] {Title});
        db.close();
        dbHelper.close();
	}
	/**
	 * 更新记录
	 * @param sql
	 */
	public void UpdateDB(Context context,String sql){
		DBHelper dbHelper = new DBHelper(context);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.execSQL(sql);
		db.close();
		dbHelper.close();
	}
	/**
	 * 删除记录
	 * @param id
	 */
	public void DeleteDB(Context context,String sql){
		DBHelper dbHelper = new DBHelper(context);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.execSQL(sql);
		db.close();
	}
}