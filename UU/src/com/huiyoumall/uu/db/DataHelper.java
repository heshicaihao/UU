package com.huiyoumall.uu.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.huiyoumall.uu.entity.CityItem;
import com.huiyoumall.uu.entity.Merchant;

/**
 * SQLite�������ݿ��������ɾ�Ĳ飩 ������
 * 
 * @author ASUS
 * 
 */
public class DataHelper {

	// ���ݿ�����
	private static String DB_NAME = "hy.db";
	private static DataHelper mInstance;
	// ���ݿ�汾
	private static int DB_VERSION = 1;
	private SQLiteDatabase db;
	private DBHelper dbHelper;

	public DataHelper(Context context) {
		// ����һ��SQLite���ݿ�
		dbHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
		db = dbHelper.getWritableDatabase();
	}

	/**
	 * ��ʼ�����ݿ����DBUtil��
	 */
	public static DataHelper getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new DataHelper(context);
		}
		return mInstance;
	}

	public void Close() {
		db.close();
		dbHelper.close();
		// mInstance = null;
		// db = null;
		// dbHelper = null;
	}

	/**
	 * ���������� ����20����¼
	 * 
	 * @param data
	 */
	public void insertbrowse(Merchant data) {
		SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
		Cursor cursor = null;
		try {
			cursor = sqlite.rawQuery("select * from browse", null);
			if (cursor != null && cursor.moveToFirst()) {
				int count = cursor.getCount();
				if (count == 20) {
					// ��ɾ������һ���ٲ���
					sqlite.execSQL("delete from browse where last_date=(select min(last_date) from browse)");
					// �������
					insertBrowse(sqlite, cursor, data);
				} else {
					// ֱ��
					insertBrowse(sqlite, cursor, data);
				}
			} else {
				String sql = "insert into " + DBHelper.BROWSE_TABLE;
				sql += "(mid, m_name, grade, shop_price, market_price, near_station, last_date, lat, lon) values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
				sqlite.execSQL(
						sql,
						new String[] { data.getMid() + "", data.getM_name(),
								data.getGrade(), data.getShop_price(),
								data.getMarket_price(), data.getNear_station(),
								System.currentTimeMillis() + "",
								data.getLat() + "", data.getLon() + "" });
			}
		} catch (Exception e) {
		} finally {
			if (sqlite != null) {
				sqlite.close();
				sqlite = null;
			}
			if (null != cursor && !cursor.isClosed()) {
				cursor.close();
			}
		}
	}

	private void insertBrowse(SQLiteDatabase sqlite, Cursor cursor,
			Merchant data) {
		cursor = sqlite.rawQuery("select * from browse where mid=?",
				new String[] { data.getMid() });
		if (cursor != null && cursor.moveToFirst()) {
			// ���� ���޸�
			sqlite.rawQuery(
					"update browse set last_date=? where mid=?",
					new String[] { System.currentTimeMillis() + "",
							data.getMid() });
		} else {
			String sql = "insert into " + DBHelper.BROWSE_TABLE;
			sql += "(mid, m_name, grade, shop_price, market_price, near_station, last_date, lat, lon) values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
			sqlite.execSQL(
					sql,
					new String[] { data.getMid() + "", data.getM_name(),
							data.getGrade(), data.getShop_price(),
							data.getMarket_price(), data.getNear_station(),
							System.currentTimeMillis() + "",
							data.getLat() + "", data.getLon() + "" });
		}
	}

	/**
	 * ��
	 * 
	 * @param data
	 */
	public void insert(CityItem data) {
		String sql = "insert into " + DBHelper.CITY_TABLE;

		sql += "(_id, name, type, parent) values(?, ?, ?, ?)";

		SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
		sqlite.execSQL(sql, new String[] { data.id + "", data.name + "",
				data.type + "", data.parent + "" });
		sqlite.close();
		this.Close();
	}

	public void insert(List<CityItem> citys) {
		String sql = "insert into " + DBHelper.CITY_TABLE;
		sql += "(_id, name, type, parent) values(?, ?, ?, ?)";

		SQLiteDatabase sqlite = null;
		if (citys != null && citys.size() > 0) {
			sqlite = dbHelper.getWritableDatabase();
			for (int i = 0; i < citys.size(); i++) {
				String city = citys.get(i).name.replace("��", "");
				sqlite.execSQL(sql, new String[] { citys.get(i).id + "", city,
						citys.get(i).type + "", citys.get(i).parent + "" });
			}
			sqlite.close();
			this.Close();
		}
	}

	/**
	 * ���� ɾ
	 * 
	 * @param id
	 */
	public int deleteBrowse() {
		SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
		int id = sqlite.delete(DBHelper.BROWSE_TABLE, null, null);
		sqlite.close();
		this.Close();
		return id;
	}

	/**
	 * ɾ
	 * 
	 * @param id
	 */
	public void deleteCity(int id) {
		SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
		String sql = ("delete from " + DBHelper.CITY_TABLE + " where _id=?");
		sqlite.execSQL(sql, new Integer[] { id });
		sqlite.close();
		this.Close();
	}

	/**
	 * ��
	 * 
	 * @param data
	 */
	public void update(CityItem data) {
		SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
		String sql = ("update " + DBHelper.CITY_TABLE + " set _id=?, name=?, type=?, parent=? where _id=?");
		sqlite.execSQL(sql, new String[] { data.id + "", data.name + "",
				data.type + "", data.parent + "" });
		sqlite.close();
		this.Close();
	}

	/**
	 * ��
	 * 
	 * @param where
	 * @return
	 */
	public List<CityItem> query(String where) {
		SQLiteDatabase sqlite = dbHelper.getReadableDatabase();
		ArrayList<CityItem> lists = null;
		lists = new ArrayList<CityItem>();
		Cursor cursor = sqlite.rawQuery("select * from " + DBHelper.CITY_TABLE
				+ " where " + where, null);
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			CityItem data = new CityItem();
			data.id = cursor.getInt(0);
			data.name = cursor.getString(1);
			data.type = cursor.getInt(2);
			data.parent = cursor.getInt(3);
			lists.add(data);
		}
		if (!cursor.isClosed()) {
			cursor.close();
		}
		sqlite.close();
		this.Close();
		return lists;
	}

	/**
	 * public String _ID = "_id"; public static String MID = "mid"; public
	 * static String M_NAME = "m_name"; public static String GRADE = "grade";
	 * public static String SHOP_PRICE = "shop_price"; public static String
	 * MARKET_PRICE = "market_price"; public static String NEAR_STATION =
	 * "near_station"; public static String LAST_DATE = "last_date"; public
	 * static String LAT = "lat";// ���� lat public static String LON = "lon";//
	 * ����lon
	 * 
	 * @return
	 */
	public List<Merchant> querybrowse() {
		SQLiteDatabase sqlite = dbHelper.getReadableDatabase();
		ArrayList<Merchant> lists = new ArrayList<Merchant>();
		Cursor cursor = sqlite.rawQuery("select * from "
				+ DBHelper.BROWSE_TABLE + " order by last_date desc", null);
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			Merchant data = new Merchant();
			data.setMid(cursor.getString(cursor.getColumnIndex("mid")));
			data.setM_name(cursor.getString(cursor.getColumnIndex("m_name")));
			data.setGrade(cursor.getString(cursor.getColumnIndex("grade")));
			data.setShop_price(cursor.getString(cursor
					.getColumnIndex("shop_price")));
			data.setMarket_price(cursor.getString(cursor
					.getColumnIndex("market_price")));
			data.setNear_station(cursor.getString(cursor
					.getColumnIndex("near_station")));
			data.setLat(cursor.getDouble(cursor.getColumnIndex("lat")));
			data.setLon(cursor.getDouble(cursor.getColumnIndex("lon")));
			lists.add(data);
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		sqlite.close();
		this.Close();
		return lists;
	}

	/**
	 * ��
	 * 
	 * @param where
	 * @return
	 */
	public CityItem queryEntity(String where) {
		SQLiteDatabase sqlite = dbHelper.getReadableDatabase();
		ArrayList<CityItem> lists = null;
		lists = new ArrayList<CityItem>();
		Cursor cursor = sqlite.rawQuery("select * from " + DBHelper.CITY_TABLE
				+ " where " + where, null);
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			CityItem data = new CityItem();
			data.id = cursor.getInt(0);
			data.name = cursor.getString(1);
			data.type = cursor.getInt(2);
			data.parent = cursor.getInt(3);
			lists.add(data);
		}
		if (!cursor.isClosed()) {
			cursor.close();
		}
		sqlite.close();
		this.Close();
		if (lists.size() > 0) {
			return lists.get(0);
		} else {
			return null;
		}
	}
}
