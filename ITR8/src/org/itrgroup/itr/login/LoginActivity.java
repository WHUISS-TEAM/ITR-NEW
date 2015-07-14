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
	
	//�̳߳�
	private ExecutorService mExecutorService = null;
	
	//���������Զ���½
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
		//���handler�������activity��handeler�����������̴߳���������Ϣ��������һ��bool���͵�ֵ��
		handler = new LoginHandler();
		//��������������õ��߳�
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
		//�����¼�����Զ���������
		sp = this.getSharedPreferences("userInfo", this.MODE_WORLD_READABLE);
		email_text.setText(sp.getString("LOGIN_EMAIL", ""));
		password_text.setText(sp.getString("LOGIN_PASSWORD", ""));
		
		login_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(email_text.getText().toString().equals("")){
					Toast.makeText(LoginActivity.this, "���䲻��Ϊ��", Toast.LENGTH_SHORT).show();
				}
				else{
					email = email_text.getText().toString();
					
					if(password_text.getText().toString().equals("")){
						Toast.makeText(LoginActivity.this, "���벻��Ϊ��", Toast.LENGTH_SHORT).show();
					}else{
						try {
							//��������MD5���ܵķ���
							MessageDigest digest = MessageDigest.getInstance("MD5");
							digest.update(password_text.getText().toString().getBytes());
							//ChangeByteToString��֮��ķ���
							//��Ϊת�����õ���byte���飬��������ת��Ϊһ��ʮ�����ƴ�����ַ�
							//����Ҫ��byte����ת��Ϊstring
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
						//�����߳��е�handle message
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
	
	
	//MD5��byte����ת��Ϊstring
		public static String changeByteToString(byte[] target){
			String results = "";
			String temp = "";
			for(int i=0;i<target.length;i++){
				//������ת��Ϊ16���ƴ����string
				temp = Integer.toHexString(target[i] & 0xff);
				if(temp.length()==1){
					//��0����
					temp = 0 + temp;
				}
				results += temp;
			}
			
			return results;
		} 
		
	//login���handle massage��������
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
					
					//���Ϊtrue�������˺����룬�����Զ���½��������ҳ��activity
					//setFlag��������������ģʽ
					//FLAG_ACTIVITY_CLEAR_TASK���������µ�activity֮ǰ�����еĶ���յ�
					//����ͺ���һ��ʹ��
					SharedPreferences users = getSharedPreferences("user_list", 0);
					//��������û��ǵ�һ���ڸ�����ϵ�½���򷵻�false
					boolean before = users.getBoolean(email, false);
					//���Ϊfalse����Ϊ���û�����һЩ��ʼ������
					//�����������ݿ����Ӧ�����ݱ�Ȼ���о��Ǽ��ظ��û��ƶ˵���Ϣ������
					//��Ӧ����ע����ɺ��Զ���ע���û�����Ϊtrue
					/*
					 * ��½�ɹ����ӷ���˼���ͷ�񵽱��أ������֮ǰ�Ƿ��½���޹�
					 * ��Ϊ�漰ͷ���Ƿ���¹�������
					 */
					Message image_msg = new Message();
					image_msg.what = 0x101;
					Bundle image_bundle = new Bundle();
					image_bundle.putInt("profile_id", user_id);
					image_msg.setData(image_bundle);
					download.down_handler.sendMessage(image_msg);
					System.out.println("����һ�£�~��~@��~~~");
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
					//˵�����û��Ѿ�����̨�����ϵ�½����
					//���´��ƶ˼������µ����ݵ��������ݿ�
					else{
						SQLiteDatabase database = 
								new IniDatabaseHelper(LoginActivity.this, email + ".db3", null, 1).getReadableDatabase();
						//������ݱ�
						database.execSQL("DELETE FROM Received");
						//��������1��ʼ
						ContentValues values = new ContentValues();
						values.put("seq", 0);
						System.out.println(database.update("sqlite_sequence" , values, "name=?", new String[]{"Received"}));
						//ͬ�ϵ��̲߳���
						load_Received.setDB(database);
						Message load = new Message();
						load.what = 0x102;
						Bundle data = new Bundle();
						data.putString("email", email);
						load.setData(data);
						/**
						 * ��load�йصı�û��д������ע�͵�
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
					Toast.makeText(LoginActivity.this, "�˺Ż��������", Toast.LENGTH_SHORT).show();
				}	
					
			}
			//0--�����м�����쳣
			//1--�������
			if(msg.what == 0x102){
				//������ɣ�����MainActivity
				if(msg.arg1 == 1){
					Intent intent = new Intent(LoginActivity.this, MainActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
					Toast.makeText(LoginActivity.this, "��½�ɹ�", Toast.LENGTH_SHORT).show();
				}
				//��������
				else{
					Toast.makeText(LoginActivity.this, "����", Toast.LENGTH_SHORT).show();
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
					Toast.makeText(LoginActivity.this, "������Դ�ɹ�", Toast.LENGTH_SHORT).show();
				}
				else{
					Toast.makeText(LoginActivity.this, "������Դ����", Toast.LENGTH_SHORT).show();
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