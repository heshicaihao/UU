package com.huiyoumall.uu.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * SQLite创建 表 工具类
 * 
 * @author ASUS
 * 
 */
public class DBHelper extends SQLiteOpenHelper {

	private static final String TAG = "DBHelper";

	// 城市的表
	public static String CITY_TABLE = "city";

	public static String ID = "_id";
	public static String NAME = "name";
	public static String TYPE = "type";
	public static String PARENT = "parent";

	private String CREATE_TABLE_CITY = "CREATE TABLE " + CITY_TABLE + "(" + ID
			+ " integer primary key autoincrement," + NAME + " varchar(50),"
			+ TYPE + " integer ," + PARENT + " integer " + ")";

	// 浏览场馆的表
	public static String BROWSE_TABLE = "browse";

	public String _ID = "_id";
	public static String MID = "mid";
	public static String M_NAME = "m_name";
	public static String GRADE = "grade";
	public static String SHOP_PRICE = "shop_price";
	public static String MARKET_PRICE = "market_price";
	public static String NEAR_STATION = "near_station";
	public static String LAST_DATE = "last_date";
	public static String LAT = "lat";// 场馆 lat
	public static String LON = "lon";// 场馆lon

	private String CREATE_TABLE_BROWSE = "CREATE TABLE " + BROWSE_TABLE + "("
			+ _ID + " integer primary key autoincrement," + MID + " integer,"
			+ M_NAME + " varchar(50)," + GRADE + " varchar(50)," + SHOP_PRICE
			+ " varchar(50)," + MARKET_PRICE + " varchar(50)," + NEAR_STATION
			+ " varchar(50)," + LAST_DATE + " varchar(20)," + LAT
			+ " varchar(50)," + LON + " varchar(50) " + ")";

	// 创建数据库
	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	// 创建表等机构性文件
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_CITY);
		db.execSQL(CREATE_TABLE_BROWSE);
		Log.e(TAG, "onCreate");
	}

	// 若数据库版本有更新，则调用此方法
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS+" + CITY_TABLE);
		db.execSQL("DROP TABLE IF EXISTS+" + BROWSE_TABLE);
		onCreate(db);
		Log.e(TAG, "onUpgrade");
	}

	// 更新列
	public void updateColumn(SQLiteDatabase db, String tableName,
			String oldColumn, String newColumn, String typeColumn) {
		try {
			db.execSQL("ALTER TABLE " + tableName + " CHANGE " + oldColumn
					+ " " + newColumn + " " + typeColumn);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
