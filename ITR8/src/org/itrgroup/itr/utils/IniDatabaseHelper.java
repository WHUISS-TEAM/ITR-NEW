package org.itrgroup.itr.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class IniDatabaseHelper extends SQLiteOpenHelper{

	public IniDatabaseHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		System.out.println("创建表！！！！！！！！！！！！！！！！！！！！！！！！");
		//建表语句
		//创建一级标签 数据表
		String pub_tag_level_1 = "CREATE TABLE pub_tag_level_1 ("
				  + "tag_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
				  + "tag_name VARCHAR(10) NOT NULL "
				  + ")";
		//创建用户标签 数据表
		String user_tag ="CREATE TABLE user_tag ("
				+ "tag_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
				+ "tag_name VARCHAR(10) NOT NULL"
				+ ")";
		//创建用户资料 数据表
		String profile = "CREATE TABLE profile ("
				+ "user_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
				+ "user_name VARCHAR(20) NOT NULL,"
				+ "user_avatar VARCHAR(255) NOT NULL DEFAULT 'default_avatar.jpeg',"
				+ "user_email VARCHAR(50) NOT NULL,"
				+ "user_age SMALLINT NOT NULL DEFAULT '0',"
				+ "user_gender TINYINT NOT NULL DEFAULT '2',"
				+ "user_school VARCHAR(30) DEFAULT NULL,"
				+ "user_academy VARCHAR(30) DEFAULT NULL,"
				+ "user_major VARCHAR(30) DEFAULT NULL,"
				+ "user_introduction VARCHAR(50) DEFAULT NULL"
				+ ")";
		//创建用户标签关系 数据表
		String user_tag_link = "CREATE TABLE user_tag_link ("
				+ "user_id INTEGER NOT NULL,"
				+ "user_tag_id INTEGER NOT NULL,"
				+ "PRIMARY KEY(user_id,user_tag_id),"
				+ "FOREIGN KEY(user_id) REFERENCES profile(user_id) ON DELETE CASCADE ON UPDATE CASCADE,"
				+ "FOREIGN KEY(user_tag_id) REFERENCES user_tag(tag_id) ON DELETE CASCADE ON UPDATE CASCADE"
				+ ")";
		//为用户创建接收到消息的数据表
		String create_sql = "CREATE TABLE Received ("
				+ "pub_id INT UNSIGNED NOT NULL PRIMARY KEY,"
				+ "pub_user_id INT UNSIGNED NOT NULL,"
				+ "pub_user_name VARCHAR(20) NOT NULL DEFAULT '',"
				+ "pub_user_avatar VARCHAR(255) NOT NULL,"
				+ "pub_time DATETIME,"
				+ "pub_location VARCHAR(100),"
				+ "pub_content VARCHAR(500) NOT NULL,"
				+ "pub_tag_level_1 SMALLINT UNSIGNED NOT NULL,"
				+ "pub_tag_level_2 VARCHAR(50),"
				+ "num_vote SMALLINT NOT NULL DEFAULT '0',"
				+ "num_comment SMALLINT NOT NULL DEFAULT '0',"
				+ "num_share SMALLINT NOT NULL DEFAULT '0',"
				+ "FOREIGN KEY (pub_tag_level_1) REFERENCES pub_tag_level_1(tag_id)"
				+ ")";
		//创建评论的表
//		String comment_sql = "CREATE TABLE Comment ("
//				+ "com_inforId integer PRIMARY KEY NOT NULL,"
//				+ "com_userId integer NOT NULL,"
//				+ "com_loc char(255) DEFAULT NULL,"
//				+ "com_time datetime NOT NULL,"
//				+ "pub_inforId integer NOT NULL,"
//				+ "com_content char(255) NOT NULL,"
//				+ "is_directed integer NOT NULL,"
//				+ "replied_userId integer NOT NULL,"
//				+ "replied_userName char(255) NOT NULL,"
//				+ "com_userName char(255) NOT NULL,"
//				+ "com_userAvatar char(255) NOT NULL,"
//				+ "FOREIGN KEY (com_userId) REFERENCES profile (profile_id)"
//				+ "FOREIGN KEY (pub_inforId) REFERENCES Received (Pub_inforId)"
//				+ "FOREIGN KEY (replied_userId) REFERENCES profile (profile_id)"
//				+ ")";
		try {
			db.execSQL(pub_tag_level_1);
			db.execSQL(create_sql);
			db.execSQL(user_tag);
			db.execSQL(profile);
			db.execSQL(user_tag_link);
//			db.execSQL(comment_sql);
			System.out.println("测试1-----------");
			for(String index : AppConfig.pub_tag_level1){
				ContentValues values = new ContentValues();
				values.put("tag_name", index);
				db.insert("pub_tag_level_1", null, values);
			}
			for (String index : AppConfig.user_tag_items) {
				ContentValues values = new ContentValues();
				values.put("tag_name", index);
				db.insert("user_tag", null, values);
			}
			System.out.println("测试2-------------");
		} catch (SQLException e) {
			// TODO: handle exception
			System.out.println("fail");
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
