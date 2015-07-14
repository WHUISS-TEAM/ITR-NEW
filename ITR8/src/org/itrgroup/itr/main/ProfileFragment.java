package org.itrgroup.itr.main;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import org.itrgroup.itr.R;
import org.itrgroup.itr.utils.AppConfig;
import org.itrgroup.itr.utils.IniDatabaseHelper;
import org.itrgroup.itr.utils.LocalDB;
import org.itrgroup.itr.utils.SingleExecutorService;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class ProfileFragment extends Fragment {

	private SharedPreferences sp;
	private String email;
	private String username;
	private String introduction;
	private String school;
	private String academy;
	private String major;
	private String usertag = "";
	private String[] tag_results = null;

	private int profile_id;
	private int avatar;
	private int gender;
	private int age;
	private List<Integer> tagList = null;
	private List<TextView> tagTextViews = null;
	
	private Thread_LoadProfile thread;
	private ExecutorService mExecutorService = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		System.out.println("哈哈OC");
		//获得该登录用户的邮箱
		sp = getActivity().getSharedPreferences("userInfo", getActivity().MODE_WORLD_READABLE);
		email = sp.getString("LOGIN_EMAIL", "");
		
		tagList = new ArrayList<Integer>();
		thread = new Thread_LoadProfile();
		//thread.start();
		
		mExecutorService = SingleExecutorService.getInstance();
		mExecutorService.execute(thread);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
		tagTextViews = new ArrayList<TextView>();
		//获得控件
		ImageView user_avatar = (ImageView)rootView.findViewById(R.id.avatar);  //用户头像
		ImageView user_gender = (ImageView)rootView.findViewById(R.id.gender);  //用户姓名
		TextView user_name = (TextView)rootView.findViewById(R.id.username);		 //用户名
		TextView user_intro = (TextView)rootView.findViewById(R.id.introduction);		 //自我介绍
		TextView user_school = (TextView)rootView.findViewById(R.id.user_school);        //大学
		TextView user_academy = (TextView)rootView.findViewById(R.id.user_academy);        //院系
		TextView user_major = (TextView)rootView.findViewById(R.id.user_major);        //专业
		TextView user_age = (TextView)rootView.findViewById(R.id.user_age);        //年龄
		Button published_button = (Button)rootView.findViewById(R.id.my_published);
		Button edit_button = (Button)rootView.findViewById(R.id.edit_profile);
		tagTextViews.add((TextView)rootView.findViewById(R.id.user_tag_1));  //用户标签1
		tagTextViews.add((TextView)rootView.findViewById(R.id.user_tag_2));  //用户标签2
		tagTextViews.add((TextView)rootView.findViewById(R.id.user_tag_3));  //用户标签3
		tagTextViews.add((TextView)rootView.findViewById(R.id.user_tag_4));  //用户标签4
		tagTextViews.add((TextView)rootView.findViewById(R.id.user_tag_5));  //用户标签5
		tagTextViews.add((TextView)rootView.findViewById(R.id.user_tag_6));  //用户标签6
		tagTextViews.add((TextView)rootView.findViewById(R.id.user_tag_7));  //用户标签7
		tagTextViews.add((TextView)rootView.findViewById(R.id.user_tag_8));  //用户标签8
		
		//设置控件内容
		user_name.setText(username != null  ? username : "");
		user_intro.setText((introduction != null && !introduction.equals("")) ? introduction : "一句话介绍自己");
		user_school.setText((school != null && !school.equals("")) ? school : "未知");
		user_academy.setText((academy != null && !academy.equals("") )? academy : "未知");
		user_major.setText((major != null  && !major.equals(""))? major : "未知");
		user_age.setText(age + " 岁");
		tag_results = usertag.split("#");
		
		//用户标签
		int i = 0;
		for(String tag : tag_results){
			tagTextViews.get(i).setText("#" + tag);
			i++;
		}
		
		//性别
		if(gender == 1){
			user_gender.setBackgroundResource(R.drawable.profile_gender_male);
		}else{
			user_gender.setBackgroundResource(R.drawable.profile_gender_female);
		}
		
		//头像
		Bitmap bt = BitmapFactory.decodeFile(AppConfig.avatar_path + profile_id + "_avatar.jpeg");
		if(bt!=null){
			Drawable drawable = new BitmapDrawable(this.getResources(), bt);//转换成drawable
			user_avatar.setImageDrawable(drawable);
		}else{
			/**
			 *	如果SD里面没有则需要从服务器取头像，取回来的头像再保存在SD中
			 * 
			 */
		}
		
		//设置按钮监听器
		//修改资料
		edit_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), ActivityEditProfile.class);
				Bundle bundle = new Bundle();
				//将已获取的用户信息传递给修改资料界面
				bundle.putString("email", email);
				bundle.putString("username", username);
				bundle.putString("introduction", introduction);
				bundle.putString("school", school);
				bundle.putString("academy", academy);
				bundle.putString("major", major);
				bundle.putStringArray("usertag", tag_results);
				bundle.putInt("age", age);
				bundle.putInt("gender", gender);
				bundle.putInt("profile_id", profile_id);
				intent.putExtras(bundle);
				startActivity(intent);
				getActivity().finish();
				
			}
		});
		//我的发布
		published_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});
		
		return rootView;

	}
	
	//读取本地数据表profile的线程
	//在Fragment里用handler跟外部线程传输信息时会出现异常，且界面加载完可能数据还没读完，所以写为内部线程的形式
	public class Thread_LoadProfile extends Thread{
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			System.out.println("PF ThreadLoadFile");
			String db_name = email + ".db3";
			String sql_query = "SELECT * FROM profile WHERE user_email = '" + email + "'";
			
			SQLiteDatabase db = 
					new IniDatabaseHelper(getActivity(), db_name, null, 1).getReadableDatabase();
			Cursor cursor = db.rawQuery(sql_query, null);
			
			//查询用户基本资料
			for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
				int index = cursor.getColumnIndex(LocalDB.PRO_USERNAME_STRING);
				username = cursor.getString(index);
				index = cursor.getColumnIndex(LocalDB.PRO_ID_STRING);
				profile_id = cursor.getInt(index);
				index =  cursor.getColumnIndex(LocalDB.PRO_INTRODUCTION_STRING);
				introduction = cursor.getString(index);
				index =  cursor.getColumnIndex(LocalDB.PRO_SCHOOL_STRING);
				school = cursor.getString(index);
				index =  cursor.getColumnIndex(LocalDB.PRO_ACADEMY_STRING);
				academy = cursor.getString(index);
				index =  cursor.getColumnIndex(LocalDB.PRO_MAJOR_STRING);
				major = cursor.getString(index);
				index =  cursor.getColumnIndex(LocalDB.PRO_AGE_STRING);
				age = cursor.getInt(index);
				index =  cursor.getColumnIndex(LocalDB.PRO_GENDER_STRING);
				gender = cursor.getInt(index);
			}
			
			//查询用户标签
			sql_query = "SELECT * FROM user_tag_link WHERE user_id = '" + profile_id + "'";
			cursor = db.rawQuery(sql_query, null);
			for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
				int index = cursor.getColumnIndex(LocalDB.USER_TAG_ID);
				tagList.add(cursor.getInt(index));
			}
			
			for (int tag_id : tagList) {
				sql_query = "SELECT * FROM user_tag WHERE tag_id = '" + tag_id + "'";
				cursor = db.rawQuery(sql_query, null);
				for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
					int index = cursor.getColumnIndex(LocalDB.USER_TAG_NAME);
					usertag += cursor.getString(index) + "#";
				}
			}
			
			cursor.close();
			db.close();
		}
	}
}

