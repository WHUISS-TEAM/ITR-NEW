package org.itrgroup.itr.ws_thread;

import java.io.IOException;

import org.itrgroup.itr.entity.Profile;
import org.itrgroup.itr.utils.AppConfig;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class Thread_Login extends Thread{

	public Handler login_handler;
	//接受login类的handler，在处理完后用来向他发送消息
	private Handler activity_handler = null;
	//webservice的地址，只需要改ip和端口
	private static final String SERVICE_URL = AppConfig.WebService_IP + "DBConnection"
			+"/WSLoginPort";
	private static final String SERVICE_NS = "http://dbConnection/";
	private static final String ENTITY_NS = "http://entity/";
	
	private int final_result = 0;
	private int profile_id = 0;
	private String username = null;
	private String avatar = null; 
	private int gender = 0;
	private int age = 0;
	private String introduction = null;
	private String usertag = null;
	private String school = null;
	private String academy = null;
	private String major = null;
	
	private Profile response_profile = null;
	
	public Thread_Login() {
		// TODO Auto-generated constructor stub
	}
	
	public Thread_Login(Handler activity_handler){
		this.activity_handler = activity_handler;
	}
	
	public void run(){
		Looper.prepare();
		login_handler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if(msg.what == 0x100){
					Bundle message = msg.getData();
					String email = message.getString("email");
					String password = message.getString("password");
					//连接webservice的方法
					execute(email, password);
					
					Message result_msg = new Message();
					result_msg.what = 0x100;
					Bundle bundle = new Bundle();
					if(response_profile != null){
						bundle.putInt("result", 1);
						bundle.putSerializable("profile", response_profile);
					}else{
						bundle.putInt("result", 0);
					}
					
//					bundle.putInt("profile_id", profile_id);
//					bundle.putInt("gender", gender);
//					bundle.putInt("age", age);
//					bundle.putString("avatar", avatar);
//					bundle.putString("username", username);
//					bundle.putString("introduction", introduction);
//					bundle.putString("usertag", usertag);
//					bundle.putString("school", school);
//					bundle.putString("academy", academy);
//					bundle.putString("major", major);
					result_msg.setData(bundle);
					activity_handler.sendMessage(result_msg);
				}
			}
			
		};
		Looper.loop();
	}
	
	//0 表示失败
	//1 表示成功
	private void execute(String email,String password){
		int re =  0;
		//连接webservice的方法以及调用方法解析结果的方法
		final HttpTransportSE transport = new HttpTransportSE(SERVICE_URL);
		transport.debug = true;
		final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		SoapObject object = new SoapObject(SERVICE_NS, "verifyUser");
		Profile temp_profile = new Profile();
		temp_profile.setUser_email(email);
		temp_profile.setUser_password(password);
		object.addProperty("arg0", temp_profile);
		envelope.addMapping(ENTITY_NS, "Profile", Profile.class);
		envelope.bodyOut = object;
		try {
			transport.call(null, envelope);
			if(envelope.getResponse() != null){
				SoapObject result_object = (SoapObject)envelope.getResponse();
				System.out.println(result_object);
//				Profile temp = (Profile) result_object;
//				Profile profile = new Profile();
				response_profile = new Profile();
				response_profile.setUser_id(Integer.parseInt(result_object.getProperty("user_id").toString()));
				response_profile.setUser_name(result_object.getProperty("user_name").toString());
				response_profile.setUser_avatar(result_object.getProperty("user_avatar").toString());
				response_profile.setUser_age(Integer.parseInt(result_object.getProperty("user_age").toString()));
				response_profile.setUser_gender(Integer.parseInt(result_object.getProperty("user_gender").toString()));
				response_profile.setUser_school(result_object.getProperty("user_school").toString());
				response_profile.setUser_major(result_object.getProperty("user_major").toString());
				response_profile.setUser_introduction(result_object.getProperty("user_introduction").toString());
				response_profile.setUser_academy(result_object.getProperty("user_academy").toString());
				response_profile.setUser_tag(result_object.getProperty("user_tag").toString());
//				System.out.println(result_object.getProperty("return").toString());
//				if(re == 1){
//					profile_id = Integer.parseInt(result_object.getProperty(1).toString());
//					username = result_object.getProperty(2).toString();
//					gender = Integer.parseInt(result_object.getProperty(3).toString());
//					age = Integer.parseInt(result_object.getProperty(4).toString());
//					avatar = result_object.getProperty(5).toString();
//					if(result_object.getProperty(6)!= null){
//						introduction = result_object.getProperty(6).toString();
//					}
//					if(result_object.getProperty(7)!= null){
//						school = result_object.getProperty(7).toString();
//					}
//					if(result_object.getProperty(8)!= null){
//						academy = result_object.getProperty(8).toString();
//					}
//					if(result_object.getProperty(9)!= null){
//						major = result_object.getProperty(9).toString();
//					}
//					usertag = result_object.getProperty(10).toString();
//				}
			}
		} catch (HttpResponseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
}

