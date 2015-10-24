/**
 * File Name：HttpDbOperater.java
 * File Describe：数据库管理操作文件
 * Creation Date：2014-04-23 17:13:41
 * @author Jonze
 * @version 1.0.0
 */
package flytv.qaonline.http;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 	Class Name：HttpDbOperater
 * 	Class Describe：数据库管理
 *	@author Jonze
 *	@version 1.0.0
 */
public class HttpDbOperater {
	private static HttpDbOperater dbOperater = null;
	private static HttpDbHelper mDbHelper;
	
	/**
	 * 获取数据操作对象 
	 */
	public static HttpDbOperater getDbOperater(Context context){
		if(dbOperater == null){
			dbOperater = new HttpDbOperater();
		}
		if(mDbHelper == null){
			mDbHelper = new HttpDbHelper(context, 1);
		}
		return dbOperater;
	}
	
	private HttpDbOperater(){}
	
	
	public void insertHttpData(String key,String data,boolean result){
		if(key != null && !"".equals(key)){
			insertHttpData(key,data,result,"");
		}
	}
	
	public void insertHttpData(String key,String data,boolean result,String header){
		if(key != null && !"".equals(key)){
			insertHttpData(key,data,result,header,"");
		}
	}
	
	public void insertHttpData(String key,String data,boolean result,String header,String content){
		if(key != null && !"".equals(key)){
			insertHttpData(key,data,result,header,content,HttpServer.HTTPREQUEST_GET);
		}
	}
	
	public void insertHttpData(String key,String data,boolean result,String header,String content,String type){
		if(key != null && !"".equals(key)){
			SQLiteDatabase db = mDbHelper.getWritableDatabase();
			if(db != null){
				try {
					String md5 = MD5.encryptMD5ToString(key);
					long time = System.currentTimeMillis();
					Cursor cur = db.query(HttpDbHelper.TABLE_HTTP, new String[]{},
							HttpDbHelper.COLUMN_KEY+"=?",new String[]{md5},null, null, null);
					boolean hasDataRecode = false;
					while(cur.moveToNext()){
						hasDataRecode = true;
					}
					ContentValues values = new ContentValues();
					values.put(HttpDbHelper.COLUMN_KEY,md5);  
					values.put(HttpDbHelper.COLUMN_HEADER,header);  
					values.put(HttpDbHelper.COLUMN_CONTENT,content);  
					values.put(HttpDbHelper.COLUMN_TYPE,type);  
					values.put(HttpDbHelper.COLUMN_TIME,time);  
					values.put(HttpDbHelper.COLUMN_DATA,data);  
					values.put(HttpDbHelper.COLUMN_RESULT,result);  
					values.put(HttpDbHelper.COLUMN_EXTEND,"");  
					if(hasDataRecode){
						db.update(HttpDbHelper.TABLE_HTTP, values,HttpDbHelper.COLUMN_KEY+"=?", new String[]{md5});
					}else{
						db.insert(HttpDbHelper.TABLE_HTTP, null, values);
					}
					if(cur != null)
						cur.close();
				} catch (Exception e) {
				} finally{
					if(db != null)
						db.close();
				}
			}
		}
	}

	public String getHttpData(String key){
		if(key != null && !"".equals(key)){
			SQLiteDatabase db = mDbHelper.getReadableDatabase();
			if(db != null){
				try {
					String md5 = MD5.encryptMD5ToString(key);
					Cursor cur = db.query(HttpDbHelper.TABLE_HTTP, new String[]{},
							HttpDbHelper.COLUMN_KEY+"=?",new String[]{md5},null, null, null);
					while(cur.moveToNext()){
						return cur.getString(cur.getColumnIndex(HttpDbHelper.COLUMN_DATA));
					}
					if(cur != null)
						cur.close();
				} catch (Exception e) {
				} finally{
					if(db != null)
						db.close();
				}
			}
		}
		return "";
	}
	
	public void cleanHttpData(){
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		if(db != null){
			db.delete(HttpDbHelper.TABLE_HTTP,null,null);
			db.close();
		}
	}
}

// end of the file
