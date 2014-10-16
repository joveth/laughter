package com.jov.laughter.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jov.laughter.bean.FavoriteBean;
import com.jov.laughter.bean.GoodBean;
import com.jov.laughter.bean.GoodContentBean;
import com.jov.laughter.bean.ImageBean;
import com.jov.laughter.bean.TextBean;

public class DBOpenHelper extends SQLiteOpenHelper {
	/**
	 * table
	 * */
	public static String TABLE_NAME_IMAGE = "tb_image";
	public static String TABLE_NAME_TEXT = "tb_text";
	public static String TABLE_NAME_GOOD = "tb_good";
	public static String TABLE_NAME_GOOD_CONTENT = "tb_good_content";
	public static String TABLE_NAME_FAVORITE = "tb_favorite";

	private static final String DB_NAME = "laugther.db";
	/**
	 * version
	 * */
	private static final int VERSION = 1;
	/**
	 * SQL for create table
	 * */
	private static final String CREATE_TABLE_IMAGE = "create table IF NOT EXISTS "
			+ TABLE_NAME_IMAGE
			+ "(id integer primary key autoincrement,imgurl varchar(1000))";
	private static final String CREATE_TABLE_TEXT = "create table IF NOT EXISTS "
			+ TABLE_NAME_TEXT
			+ "(tid integer primary key autoincrement,content varchar(6000),image varchar(500),coldate varchar(14))";
	private static final String CREATE_TABLE_GOOD = "create table IF NOT EXISTS "
			+ TABLE_NAME_GOOD
			+ "(gid integer primary key autoincrement,subject varchar(60),date varchar(14),tip varchar(100),sum integer)";
	private static final String CREATE_TABLE_GOOD_CONTENT = "create table IF NOT EXISTS "
			+ TABLE_NAME_GOOD_CONTENT
			+ "(cid integer primary key autoincrement,gdate varchar(14),content varchar(6000),image varchar(500))";
	private static final String CREATE_TABLE_FAVORITE = "create table IF NOT EXISTS "
			+ TABLE_NAME_FAVORITE
			+ "(fid integer primary key autoincrement,imageurl varchar(500),tid integer,cid integer)";

	/**
	 * SQL for drop table
	 * */
	private static final String DROP_TABLE_IMAGE = "DROP TABLE IF EXISTS "
			+ TABLE_NAME_IMAGE;
	private static final String DROP_TABLE_TEXT = "DROP TABLE IF EXISTS "
			+ TABLE_NAME_TEXT;
	private static final String DROP_TABLE_GOOD = "DROP TABLE IF EXISTS "
			+ TABLE_NAME_GOOD;
	private static final String DROP_TABLE_GOOD_CONTENT = "DROP TABLE IF EXISTS "
			+ TABLE_NAME_GOOD_CONTENT;
	private static final String DROP_TABLE_FAVORITE = "DROP TABLE IF EXISTS "
			+ TABLE_NAME_FAVORITE;
	private SQLiteDatabase db;

	public DBOpenHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
	}

	public DBOpenHelper(Context context, String name, int version) {
		super(context, name, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_IMAGE);
		db.execSQL(CREATE_TABLE_TEXT);
		db.execSQL(CREATE_TABLE_GOOD);
		db.execSQL(CREATE_TABLE_GOOD_CONTENT);
		db.execSQL(CREATE_TABLE_FAVORITE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DROP_TABLE_IMAGE);
		db.execSQL(DROP_TABLE_TEXT);
		db.execSQL(DROP_TABLE_GOOD);
		db.execSQL(DROP_TABLE_GOOD_CONTENT);
		db.execSQL(DROP_TABLE_FAVORITE);
		onCreate(db);
	}

	public List<ImageBean> getImages() {
		if (db == null||!db.isOpen()) {
			db = this.getReadableDatabase();
		}
		Cursor cursor = null;
		List<ImageBean> list = new ArrayList<ImageBean>();
		cursor = db.rawQuery("select * from " + TABLE_NAME_IMAGE
				+ " order by id desc  ", null);
		while (cursor != null && cursor.moveToNext()) {
			ImageBean obj = new ImageBean();
			obj.setId(cursor.getInt(cursor.getColumnIndex("id")));
			obj.setImgUrl(cursor.getString(cursor.getColumnIndex("imgurl")));
			list.add(obj);
		}
		cursor.close();
		db.close();
		return list;
	}

	public List<String> getImageUrls() {
		if (db == null||!db.isOpen()) {
			db = this.getReadableDatabase();
		}
		Cursor cursor = null;
		List<String> list = new ArrayList<String>();
		cursor = db.rawQuery("select * from " + TABLE_NAME_IMAGE
				+ " order by id desc  ", null);
		while (cursor != null && cursor.moveToNext()) {
			list.add(cursor.getString(cursor.getColumnIndex("imgurl")));
		}
		cursor.close();
		db.close();
		return list;
	}

	public List<ImageBean> getImagesByKey(String key) {
		if (db == null||!db.isOpen()) {
			db = this.getReadableDatabase();
		}
		Cursor cursor = null;
		List<ImageBean> list = new ArrayList<ImageBean>();
		cursor = db.rawQuery("select * from " + TABLE_NAME_IMAGE
				+ " where imgurl=? order by id desc  ", new String[] { key });
		while (cursor != null && cursor.moveToNext()) {
			ImageBean obj = new ImageBean();
			obj.setId(cursor.getInt(cursor.getColumnIndex("id")));
			obj.setImgUrl(cursor.getString(cursor.getColumnIndex("imgurl")));
			list.add(obj);
		}
		cursor.close();
		db.close();
		return list;
	}

	public int getImageTotalCount() {
		if (db == null||!db.isOpen()) {
			db = this.getReadableDatabase();
		}
		String sql = "select count(*) from " + TABLE_NAME_IMAGE;
		Cursor rec = db.rawQuery(sql, null);
		rec.moveToLast();
		long recSize = rec.getLong(0);
		rec.close();
		db.close();
		return (int) recSize;
	}

	public void insertImage(String imgurl) {
		if (db == null||!db.isOpen()) {
			db = this.getReadableDatabase();
		}
		String sql = "insert into " + TABLE_NAME_IMAGE + "  values(null,?)";
		db.execSQL(sql, new String[] { imgurl });
		db.close();
	}

	public boolean hasTextDate(String date) {
		if (db == null||!db.isOpen()) {
			db = this.getReadableDatabase();
		}
		Cursor cursor = null;
		cursor = db.rawQuery("select * from " + TABLE_NAME_TEXT
				+ " where coldate=?  limit 1  ", new String[] { date });
		boolean has = false;
		if (cursor != null && cursor.moveToNext()) {
			has = true;
		}
		cursor.close();
		db.close();
		return has;
	}

	public void insertText(TextBean bean) {
		if (db == null||!db.isOpen()) {
			db = this.getReadableDatabase();
		}
		String sql = "insert into " + TABLE_NAME_TEXT + "  values(null,?,?,?)";
		db.execSQL(sql, new String[] { bean.getContent(), bean.getImagUrl(),
				bean.getColDate() });
		db.close();
	}

	public List<TextBean> getTexts(int offset) {
		if (db == null||!db.isOpen()) {
			db = this.getReadableDatabase();
		}
		Cursor cursor = null;
		List<TextBean> list = new ArrayList<TextBean>();
		cursor = db.rawQuery(
				"select * from " + TABLE_NAME_TEXT
						+ " order by tid desc  limit 10 offset "
						+ String.valueOf(offset), null);
		while (cursor != null && cursor.moveToNext()) {
			TextBean obj = new TextBean();
			obj.setTid(cursor.getInt(cursor.getColumnIndex("tid")));
			obj.setImagUrl(cursor.getString(cursor.getColumnIndex("image")));
			obj.setContent(cursor.getString(cursor.getColumnIndex("content")));
			obj.setColDate(cursor.getString(cursor.getColumnIndex("coldate")));
			list.add(obj);
		}
		cursor.close();
		db.close();
		return list;
	}

	public TextBean getText(int tid) {
		if (db == null||!db.isOpen()) {
			db = this.getReadableDatabase();
		}
		Cursor cursor = null;
		cursor = db.rawQuery("select * from " + TABLE_NAME_TEXT
				+ " where tid=?", new String[] { String.valueOf(tid) });
		TextBean obj = null;
		while (cursor != null && cursor.moveToNext()) {
			obj = new TextBean();
			obj.setTid(cursor.getInt(cursor.getColumnIndex("tid")));
			obj.setImagUrl(cursor.getString(cursor.getColumnIndex("image")));
			obj.setContent(cursor.getString(cursor.getColumnIndex("content")));
			obj.setColDate(cursor.getString(cursor.getColumnIndex("coldate")));
		}
		cursor.close();
		db.close();
		return obj;
	}

	public int getTextTotalCount() {
		if (db == null||!db.isOpen()) {
			db = this.getReadableDatabase();
		}
		String sql = "select count(*) from " + TABLE_NAME_TEXT;
		Cursor rec = db.rawQuery(sql, null);
		rec.moveToLast();
		long recSize = rec.getLong(0);
		rec.close();
		db.close();
		return (int) recSize;
	}

	public boolean hasGoodDate(String date) {
		if (db == null||!db.isOpen()) {
			db = this.getReadableDatabase();
		}
		Cursor cursor = null;
		cursor = db.rawQuery("select * from " + TABLE_NAME_GOOD
				+ " where date=?  limit 1  ", new String[] { date });
		boolean has = false;
		if (cursor != null && cursor.moveToNext()) {
			has = true;
		}
		cursor.close();
		db.close();
		return has;
	}

	public void insertGood(GoodBean bean) {
		if (db == null||!db.isOpen()) {
			db = this.getReadableDatabase();
		}
		String sql = "insert into " + TABLE_NAME_GOOD
				+ "  values(null,?,?,?,?)";
		db.execSQL(sql,
				new String[] { bean.getSubject(), bean.getDate(),
						bean.getTip(), String.valueOf(bean.getSum()) });
		db.close();
	}

	public List<GoodBean> getGoods(int offset) {
		if (db == null||!db.isOpen()) {
			db = this.getReadableDatabase();
		}
		Cursor cursor = null;
		List<GoodBean> list = new ArrayList<GoodBean>();
		cursor = db.rawQuery(
				"select * from " + TABLE_NAME_GOOD
						+ " order by gid desc  limit 20 offset "
						+ String.valueOf(offset), null);
		while (cursor != null && cursor.moveToNext()) {
			GoodBean obj = new GoodBean();
			obj.setGid(cursor.getInt(cursor.getColumnIndex("gid")));
			obj.setSubject(cursor.getString(cursor.getColumnIndex("subject")));
			obj.setDate(cursor.getString(cursor.getColumnIndex("date")));
			obj.setTip(cursor.getString(cursor.getColumnIndex("tip")));
			obj.setSum(cursor.getInt(cursor.getColumnIndex("sum")));
			list.add(obj);
		}
		cursor.close();
		db.close();
		return list;
	}

	public int getGoodTotalCount() {
		if (db == null||!db.isOpen()) {
			db = this.getReadableDatabase();
		}
		String sql = "select count(*) from " + TABLE_NAME_GOOD;
		Cursor rec = db.rawQuery(sql, null);
		rec.moveToLast();
		long recSize = rec.getLong(0);
		rec.close();
		db.close();
		return (int) recSize;
	}

	public void insertGoodContent(GoodContentBean bean) {
		if (db == null||!db.isOpen()) {
			db = this.getReadableDatabase();
		}
		String sql = "insert into " + TABLE_NAME_GOOD_CONTENT
				+ "  values(null,?,?,?) ";
		db.execSQL(
				sql,
				new String[] { bean.getGdate(), bean.getContent(),
						bean.getImage() });
		db.close();
	}

	public List<GoodContentBean> getGoodContents(int offset, String date) {
		if (db == null||!db.isOpen()) {
			db = this.getReadableDatabase();
		}
		Cursor cursor = null;
		List<GoodContentBean> list = new ArrayList<GoodContentBean>();
		cursor = db.rawQuery("select * from " + TABLE_NAME_GOOD_CONTENT
				+ " where gdate=? limit 10 offset  " + String.valueOf(offset),
				new String[] { date });
		while (cursor != null && cursor.moveToNext()) {
			GoodContentBean obj = new GoodContentBean();
			obj.setCid(cursor.getInt(cursor.getColumnIndex("cid")));
			obj.setImage(cursor.getString(cursor.getColumnIndex("image")));
			obj.setContent(cursor.getString(cursor.getColumnIndex("content")));
			list.add(obj);
		}
		cursor.close();
		db.close();
		return list;
	}

	public TextBean getGoodContent(int cid) {
		if (db == null||!db.isOpen()) {
			db = this.getReadableDatabase();
		}
		Cursor cursor = null;
		cursor = db.rawQuery("select * from " + TABLE_NAME_GOOD_CONTENT
				+ " where cid=?", new String[] { String.valueOf(cid) });
		TextBean obj = null;
		while (cursor != null && cursor.moveToNext()) {
			obj = new TextBean();
			obj.setImagUrl(cursor.getString(cursor.getColumnIndex("image")));
			obj.setContent(cursor.getString(cursor.getColumnIndex("content")));
		}
		cursor.close();
		db.close();
		return obj;
	}

	public int getGoodContentCount(String date) {
		if (db == null||!db.isOpen()) {
			db = this.getReadableDatabase();
		}
		String sql = "select count(*) from " + TABLE_NAME_GOOD_CONTENT
				+ " where gdate=?  ";
		Cursor rec = db.rawQuery(sql, new String[] { date });
		rec.moveToLast();
		long recSize = rec.getLong(0);
		rec.close();
		db.close();
		return (int) recSize;
	}

	public void insertFavoriteWithImage(String imageurl) {
		if (hasImageFav(imageurl)) {
			return;
		}
		if (db == null||!db.isOpen()) {
			db = this.getReadableDatabase();
		}
		String sql = "insert into " + TABLE_NAME_FAVORITE
				+ " (fid,imageurl)   values(null,?) ";
		db.execSQL(sql, new String[] { imageurl });
		db.close();
	}

	// text
	public void insertFavoriteWithTid(int tid) {
		if (hasTextFav(tid)) {
			return;
		}
		if (db == null||!db.isOpen()) {
			db = this.getReadableDatabase();
		}
		String sql = "insert into " + TABLE_NAME_FAVORITE
				+ " (fid,tid)   values(null,?) ";
		db.execSQL(sql, new String[] { String.valueOf(tid) });
		db.close();
	}

	// gcont
	public void insertFavoriteWithCid(int cid) {
		if (hasContentFav(cid)) {
			return;
		}
		if (db == null||!db.isOpen()) {
			db = this.getReadableDatabase();
		}
		String sql = "insert into " + TABLE_NAME_FAVORITE
				+ " (fid,cid)   values(null,?) ";
		db.execSQL(sql, new String[] { String.valueOf(cid) });
		db.close();
	}

	public List<FavoriteBean> getFavorites(int offset) {
		if (db == null||!db.isOpen()) {
			db = this.getReadableDatabase();
		}
		Cursor cursor = null;
		List<FavoriteBean> list = new ArrayList<FavoriteBean>();
		cursor = db.rawQuery(
				"select * from " + TABLE_NAME_FAVORITE
						+ " order by fid desc limit 20 offset  "
						+ String.valueOf(offset), null);
		while (cursor != null && cursor.moveToNext()) {
			FavoriteBean obj = new FavoriteBean();
			obj.setFid(cursor.getInt(cursor.getColumnIndex("fid")));
			int cid = cursor.getInt(cursor.getColumnIndex("cid"));
			int tid = cursor.getInt(cursor.getColumnIndex("tid"));
			if (cid != 0) {
				TextBean cont = getGoodContent(cid);
				obj.setTextBean(cont);
			} else if (tid != 0) {
				TextBean cont = getText(tid);
				obj.setTextBean(cont);
			} else {
				obj.setImgurl(cursor.getString(cursor
						.getColumnIndex("imageurl")));
			}
			list.add(obj);
		}
		cursor.close();
		db.close();
		return list;
	}

	public int getFavoriteCount() {
		if (db == null||!db.isOpen()) {
			db = this.getReadableDatabase();
		}
		String sql = "select count(*) from " + TABLE_NAME_FAVORITE;
		Cursor rec = db.rawQuery(sql, null);
		rec.moveToLast();
		long recSize = rec.getLong(0);
		rec.close();
		db.close();
		return (int) recSize;
	}

	public boolean hasImageFav(String imageurl) {
		if (db == null||!db.isOpen()) {
			db = this.getReadableDatabase();
		}
		Cursor cursor = null;
		cursor = db.rawQuery("select * from " + TABLE_NAME_FAVORITE
				+ " where imageurl=?  limit 1  ", new String[] { imageurl });
		boolean has = false;
		if (cursor != null && cursor.moveToNext()) {
			has = true;
		}
		cursor.close();
		db.close();
		return has;
	}

	public boolean hasTextFav(int tid) {
		if (tid == 0) {
			return true;
		}
		if (db == null||!db.isOpen()) {
			db = this.getReadableDatabase();
		}
		Cursor cursor = null;
		cursor = db.rawQuery("select * from " + TABLE_NAME_FAVORITE
				+ " where tid=?  limit 1  ",
				new String[] { String.valueOf(tid) });
		boolean has = false;
		if (cursor != null && cursor.moveToNext()) {
			has = true;
		}
		cursor.close();
		db.close();
		return has;
	}

	public boolean hasContentFav(int cid) {
		if (cid == 0) {
			return true;
		}
		if (db == null||!db.isOpen()) {
			db = this.getReadableDatabase();
		}
		Cursor cursor = null;
		cursor = db.rawQuery("select * from " + TABLE_NAME_FAVORITE
				+ " where cid=?  limit 1  ",
				new String[] { String.valueOf(cid) });
		boolean has = false;
		if (cursor != null && cursor.moveToNext()) {
			has = true;
		}
		cursor.close();
		db.close();
		return has;
	}
}
