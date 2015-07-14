package org.itrgroup.itr.login;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;

import org.itrgroup.itr.R;
import org.itrgroup.itr.entity.Profile;
import org.itrgroup.itr.main.MainActivity;
import org.itrgroup.itr.model.PersonModel;
import org.itrgroup.itr.utils.AppConfig;
import org.itrgroup.itr.utils.IniDatabaseHelper;
import org.itrgroup.itr.utils.SingleExecutorService;
import org.itrgroup.itr.ws_thread.Thread_DownloadAvatar;
import org.itrgroup.itr.ws_thread.Thread_LoadReceived;
import org.itrgroup.itr.ws_thread.Thread_Login;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;


public class LoginActivity extends Activity {


	private EditText email_text = null;
	private EditText password_text = null;
	private Button login_button = null;
	private Button register_button = null;
	private String email = "";
	private String password = "";
	private Thread_Login login_Thread = null;
	private Thread_LoadReceived load_Received = null;
	private Thread_DownloadAvatar download = null;
	private Handler handler = null;
	
	//线程池
	private ExecutorService mExecutorService = null;
	
	//用于设置自动登陆
	private SharedPreferences sp;
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		final ScrollView myScroll = (ScrollView) findViewById(R.id.scroll);
		//Hide Scroll Bar
		myScroll.setOnTouchListener( new OnTouchListener(){ 
		    @Override
		    public boolean onTouch(View v, MotionEvent event) {
		        return true; 
		    }
		});
		//这个handler是这里的activity的handeler，用来接收线程处理完后的消息（这里是一个bool类型的值）
		handler = new LoginHandler();
		//这个是连接网络用的线程
		login_Thread = new Thread_Login(handler);
		load_Received = new Thread_LoadReceived(handler);
		download = new Thread_DownloadAvatar(handler);
		//login_Thread.start();
		//load_Received.start();
		//download.start();
		
		mExecutorService =  SingleExecutorService.getInstance();
		mExecutorService.execute(login_Thread);
		mExecutorService.execute(load_Received);
		mExecutorService.execute(download);
		
		email_text = (EditText)findViewById(R.id.email);
		password_text = (EditText)findViewById(R.id.password);
		login_button = (Button)findViewById(R.id.login_button);
		register_button = (Button)findViewById(R.id.regist_button);
		//如果登录过则自动保存密码
		sp = this.getSharedPreferences("userInfo", this.MODE_WORLD_READABLE);
		email_text.setText(sp.getString("LOGIN_EMAIL", ""));
		password_text.setText(sp.getString("LOGIN_PASSWORD", ""));
		
		login_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(email_text.getText().toString().equals("")){
					Toast.makeText(LoginActivity.this, "邮箱不能为空", Toast.LENGTH_SHORT).show();
				}
				else{
					email = email_text.getText().toString();
					
					if(password_text.getText().toString().equals("")){
						Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
					}else{
						try {
							//将密码用MD5加密的方法
							MessageDigest digest = MessageDigest.getInstance("MD5");
							digest.update(password_text.getText().toString().getBytes());
							//ChangeByteToString是之后的方法
							//因为转化后获得的是byte数组，两个数字转化为一个十六进制代表的字符
							//所以要将byte数组转化为string
							password = changeByteToString(digest.digest());
						} catch (NoSuchAlgorithmException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Message msg = new Message();
						msg.what = 0x100;
						Bundle bundle = new Bundle();
						bundle.putString("email", email);
						bundle.putString("password",password);
						msg.setData(bundle);
						//启用线程中的handle message
						login_Thread.login_handler.sendMessage(msg);
					}
				}
			}
		});
		
		register_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
			}
		});
	}
	
	
	//MD5的byte数组转化为string
		public static String changeByteToString(byte[] target){
			String results = "";
			String temp = "";
			for(int i=0;i<target.length;i++){
				//将数字转化为16进制代表的string
				temp = Integer.toHexString(target[i] & 0xff);
				if(temp.length()==1){
					//补0操作
					temp = 0 + temp;
				}
				results += temp;
			}
			
			return results;
		} 
		
	//login类的handle massage操作定义
	public class LoginHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if(msg.what == 0x100){
				Bundle bundle = msg.getData();
				int result = 0;
				
				result = bundle.getInt("result");
				if(result == 1){
					Profile response_profile = (Profile) bundle.getSerializable("profile");
					int user_id = response_profile.getUser_id();
					int user_gender = response_profile.getUser_gender();
					int user_age = response_profile.getUser_age();
					String user_avatar = response_profile.getUser_avatar();
					String user_name = response_profile.getUser_name();
					String user_introduction = response_profile.getUser_introduction();
					String user_tag = response_profile.getUser_tag();
					String user_school = response_profile.getUser_school();
					String user_academy = response_profile.getUser_academy();
					String user_major = response_profile.getUser_major();
					
					//结果为true，保存账号密码，设置自动登陆并启动主页的activity
					//setFlag用来设置启动的模式
					//FLAG_ACTIVITY_CLEAR_TASK：在启动新的activity之前将已有的都清空掉
					//必须和后者一起使用
					SharedPreferences users = getSharedPreferences("user_list", 0);
					//如果该名用户是第一次在该软件上登陆，则返回false
					boolean before = users.getBoolean(email, false);
					//如果为false，则为该用户进行一些初始化工作
					//包括创建数据库和相应的数据表，然后还有就是加载该用户云端的消息到本地
					//相应的在注册完成后自动将注册用户设置为true
					/*
					 * 登陆成功，从服务端加载头像到本地，这个与之前是否登陆过无关
					 * 因为涉及头像是否更新过的问题
					 */
					Message image_msg = new Message();
					image_msg.what = 0x101;
					Bundle image_bundle = new Bundle();
					image_bundle.putInt("profile_id", user_id);
					image_msg.setData(image_bundle);
					download.down_handler.sendMessage(image_msg);
					System.out.println("测试一下！~！~@！~~~");
					if(!before){
						load_Received.setDB(new IniDatabaseHelper(LoginActivity.this, email + ".db3", null, 1).getReadableDatabase());
						Message load = new Message();
						load.what = 0x102;
						Bundle data = new Bundle();
						data.putString("email", email);
						load.setData(data);
						load_Received.handler.sendMessage(load);
						users.edit().putBoolean(email, true).commit();
						PersonModel personModel = new PersonModel(user_id, user_avatar, user_name, email,  user_introduction, user_tag, user_school, user_academy, user_major, user_gender, user_age);
						//new LoginLocalInsert(personModel,before).start();
						mExecutorService.execute(new LoginLocalInsert(personModel,before));
						
					}
					//说明该用户已经在这台机器上登陆过了
					//重新从云端加载最新的数据到本地数据库
					else{
						SQLiteDatabase database = 
								new IniDatabaseHelper(LoginActivity.this, email + ".db3", null, 1).getReadableDatabase();
						//清空数据表
						database.execSQL("DELETE FROM Received");
						//是自增从1开始
						ContentValues values = new ContentValues();
						values.put("seq", 0);
						System.out.println(database.update("sqlite_sequence" , values, "name=?", new String[]{"Received"}));
						//同上的线程操作
						load_Received.setDB(database);
						Message load = new Message();
						load.what = 0x102;
						Bundle data = new Bundle();
						data.putString("email", email);
						load.setData(data);
						/**
						 * 和load有关的表还没有写所以先注释掉
						 */
						load_Received.handler.sendMessage(load);
						
						PersonModel personModel = new PersonModel(user_id, user_avatar, user_name, email,  user_introduction, user_tag, user_school, user_academy, user_major, user_gender, user_age);
						//new LoginLocalInsert(personModel,before).start();	
						mExecutorService.execute(new LoginLocalInsert(personModel,before));
					}					
					Editor editor = sp.edit();
					editor.putInt("PROFILE_ID", user_id);
					editor.putString("LOGIN_EMAIL", email);
					editor.putString("LOGIN_PASSWORD",""+password_text.getText());
					editor.putString("REGIST_USERNAME", user_name);
					editor.putBoolean("IS_CHECKED", true);
					editor.commit();
				}else{
					Toast.makeText(LoginActivity.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
				}	
					
			}
			//0--操作中间出现异常
			//1--操作完成
			if(msg.what == 0x102){
				//操作完成，启动MainActivity
				if(msg.arg1 == 1){
					Intent intent = new Intent(LoginActivity.this, MainActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
					Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
				}
				//操作出错
				else{
					Toast.makeText(LoginActivity.this, "出错", Toast.LENGTH_SHORT).show();
				}
			}
			
			if(msg.what == 0x101){
				System.out.println("0x118");
				Bundle response = msg.getData();
				String image = response.getString("image");
				
				int user_id = response.getInt("user_id");
				if(image != null){
					System.out.println(image);
					System.out.println("inni");
					byte[] image_byte = Base64.decode(image, Base64.DEFAULT);
					File parent_path = new File(AppConfig.avatar_path);
					if(!parent_path.exists()){
						parent_path.mkdirs();
					}
					FileOutputStream avatar_output = null;
					File avatar = new File(parent_path, user_id + "_avatar.jpg");
					try {
						avatar_output = new FileOutputStream(avatar);
						avatar_output.write(image_byte);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return;
					} finally{
						if(avatar_output != null){
							try {
								avatar_output.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					Toast.makeText(LoginActivity.this, "下载资源成功", Toast.LENGTH_SHORT).show();
				}
				else{
					Toast.makeText(LoginActivity.this, "下载资源出错", Toast.LENGTH_SHORT).show();
				}
			}
		}
	}
	
	private class LoginLocalInsert extends Thread{
		
		private PersonModel personModel;
		private boolean before;
		
		public LoginLocalInsert(PersonModel personModel, boolean before) {
			// TODO Auto-generated constructor stub
			this.personModel = personModel;
			this.before = before;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			SQLiteDatabase database = 
			new IniDatabaseHelper(LoginActivity.this, email + ".db3", null, 1).getReadableDatabase();

			ContentValues values = new ContentValues();
			values.put("user_email", personModel.getEmail());
			values.put("user_name", personModel.getUsername());
			values.put("user_id", personModel.getProfile_id());
			values.put("user_avatar", personModel.getAvatar());
			values.put("user_gender", personModel.getGender());
			values.put("user_age", personModel.getAge());
			values.put("user_school", personModel.getSchool());
			values.put("user_academy", personModel.getAcademy());
			values.put("user_major", personModel.getMajor());
			values.put("user_introduction", personModel.getIntroduction());
			
			if(before){
				database.update("profile", values, "user_email = ?", new String[]{personModel.getEmail()});
				database.delete("user_tag_link", "user_id = ?", new String[]{personModel.getProfile_id()+""});
				
				String[] tag_results = personModel.getUsertag().split("#");
				for(int i=1;i<tag_results.length;i++){
					String tag = tag_results[i];
					ContentValues tag_values = new ContentValues();
					tag_values.put("user_id", personModel.getProfile_id());
					tag_values.put("user_tag_id", Integer.parseInt(tag));
					database.insert("user_tag_link", null, tag_values);
				}
			}else{
				database.insert("profile", null, values);
				
				String[] tag_results = personModel.getUsertag().split("#");
				for(int i=1;i<tag_results.length;i++){
					String tag = tag_results[i];
					ContentValues tag_values = new ContentValues();
					tag_values.put("user_id", personModel.getProfile_id());
					tag_values.put("user_tag_id", Integer.parseInt(tag));
					database.insert("user_tag_link", null, tag_values);
				}
			}
			
			database.close();
		}
	}
	
}