/**
 * File Name：HttpDbHelper.java
 * File Describe：SQLiteHelper File
 * Creation Date：2014-04-23 17:12:24
 * @author Jonze
 * @version 1.0.0
 */
package flytv.qaonline.http;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * 	Class Name：HttpDbHelper
 * 	Class Describe：SQLiteHelper
 *	@author Jonze
 *	@version 1.0.0
 */
public class HttpDbHelper extends SQLiteOpenHelper {
	private static final String DBNAME = "db_http"; // 数据库名字
	
	public static final String TABLE_HTTP = "TABLE_HTTP";
	public static final String COLUMN_KEY = "KEY"; // url md5摘要
	public static final String COLUMN_HEADER = "HEADER"; // 头信息
	public static final String COLUMN_CONTENT = "CONTENT"; // 请求内容
	public static final String COLUMN_TYPE = "TYPE"; // 请求方式
	public static final String COLUMN_DATA = "DATA"; // 返回结果数据
	public static final String COLUMN_TIME = "TIME"; // 请求时间
	public static final String COLUMN_RESULT = "RESULT"; // 请求结果
	public static final String COLUMN_EXTEND = "EXTEND"; // 扩展字段
	
	
	/**
	 * 构造方法
	 * @param context	上下文
	 * @param DBName	数据库名称
	 * @param version	数据库版本号
	 */
	public HttpDbHelper(Context context, int version) {
		super(context, DBNAME, null, version);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_HTTP
				 + "(MAIN_KEY INTEGER PRIMARY KEY AUTOINCREMENT, "
				 + COLUMN_KEY + " VARCHAR(50) NOT NULL, "
				 + COLUMN_TYPE + " VARCHAR(10), " 		
				 + COLUMN_TIME + " VARCHAR(30), "
				 + COLUMN_RESULT + " BOOLEAN, "
				 + COLUMN_HEADER + " TEXT, "
				 + COLUMN_CONTENT + " TEXT, " 
				 + COLUMN_DATA + " TEXT, " 
				 + COLUMN_EXTEND +" TEXT)");	
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
	 
}

// end of the file
