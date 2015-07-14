package org.itrgroup.itr.login;

import org.itrgroup.itr.R;
import org.itrgroup.itr.main.MainActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class LaunchActivity  extends Activity {

	//���������Զ���½
	private SharedPreferences sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launch);
		
		sp = this.getSharedPreferences("userInfo", this.MODE_WORLD_READABLE);
		Handler handler = new Handler();
		
		//��LaunchActivity����ȴ�0.888��
		handler.postDelayed(new Runnable() {
		
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//����Ѿ���¼�����Զ���ת��MainActivity������ص�LoginActivity
				if(sp.getBoolean("IS_CHECKED", false)){
					startActivity(new Intent(LaunchActivity.this, MainActivity.class));
					//������finish������ת��ɺ󰴷��ؼ��޷����صȴ�����
					finish();
				}else{
					startActivity(new Intent(LaunchActivity.this, LoginActivity.class));
					finish();
				}
			}
		}, 888);

	}
	//�������ݿ������Ҫ�ܵ��û������ƣ����ֻ���ڵ�½��ע�����ʱ�������ݿ�ĳ�ʼ������
//	private class ini_Thread extends Thread{
//		@Override
//		public void run() {
//			 //TODO Auto-generated method stub
//			//��data/data/<package_name>�´���app_database�ļ���
//			getDir("database",MODE_PRIVATE);
//			//�������ݿ��ļ����ر����ݿ�
//			SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(AppConfig.DATABASE_PATH, null);
//			//����һ����ǩ ���ݱ�
//			String pub_tag_level1 = "CREATE TABLE pub_tag_level1 ("
//					  + "tag_id integer PRIMARY KEY AUTOINCREMENT NOT NULL,"
//					  + "tag_name VARCHAR(50) NOT NULL"
//					  + ")";
//			//�����û���ǩ ���ݱ�
//			String user_tag ="CREATE TABLE user_tag ("
//					+ "tag_id integer PRIMARY KEY AUTOINCREMENT NOT NULL,"
//					+ "tag_name VARCHAR(50) NOT NULL"
//					+ ")";
//			//�����û����� ���ݱ�
//			String profile = "CREATE TABLE profile ("
//					+ "profile_id integer PRIMARY KEY AUTOINCREMENT NOT NULL,"
//					+ "email VARCHAR(50) NOT NULL,"
//					+ "username VARCHAR(50) NOT NULL,"
//					+ "userhead VARCHAR(50) NOT NULL DEFAULT 'avatar_1.png',"  //Ĭ����avatar_1.png
//					+ "sex SMALLINT NOT NULL DEFAULT '2',"  // 1 for male, 0 for female, 2 for unknown
//					+ "age SMALLINT NOT NULL DEFAULT '0',"
//					+ "school VARCHAR(50),"
//					+ "major VARCHAR(50),"
//					+ "introduction VARCHAR(100),"
//					+ "experience VARCHAR(200)"
//					+ ")";
//			//�����û���ǩ��ϵ ���ݱ�
//			String user_tag_link = "CREATE TABLE user_tag_link ("
//					+ "link_id integer PRIMARY KEY AUTOINCREMENT NOT NULL,"
//					+ "profile_id integer,"
//					+ "tag_id SMALLINT,"
//					+ "FOREIGN KEY(profile_id) REFERENCES profile (profile_id)"
//					+ ")";
//			
//			try {
//				database.execSQL(pub_tag_level1);
//				database.execSQL(user_tag);
//				
//				for(String index : AppConfig.pub_tag_level1){
//					ContentValues values = new ContentValues();
//					values.put("tag_name", index);
//					database.insert("pub_tag_level1", null, values);
//				}
//				for (String index : AppConfig.user_tag_items) {
//					ContentValues values = new ContentValues();
//					values.put("tag_name", index);
//					database.insert("user_tag", null, values);
//				}
//				
//				database.execSQL(profile);
//				database.execSQL(user_tag_link);
//				
//			} catch (SQLException e) {
//				// TODO: handle exception
//				System.out.println("fail");
//			}finally{
//				database.close();
//			}
//		}
//	}
//
}
