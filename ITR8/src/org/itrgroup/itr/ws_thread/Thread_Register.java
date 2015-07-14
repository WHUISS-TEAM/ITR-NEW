package org.itrgroup.itr.ws_thread;

import java.io.IOException;
import java.util.ArrayList;

import org.itrgroup.itr.entity.Profile;
import org.itrgroup.itr.login.RegisterActivity;
import org.itrgroup.itr.main.MainActivity;
import org.itrgroup.itr.utils.AppConfig;
import org.itrgroup.itr.utils.IniDatabaseHelper;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

public class Thread_Register extends Thread{

	public Handler register_handler = null;
	private int final_result = 3;
	private int tag_result = 0;
	private Handler activity_handler;
	private int profile_id = -1;
	
	//�����ϱ���д����
	private static final String SERVICE_URL = AppConfig.WebService_IP + "DBConnection"
			+"/WSRegisterPort";
	private static final String SERVICE_NS = "http://dbConnection/";
	
	private static final String PROFILE_NS = "http://entity/";
	
	//�������ܱ�
	private static final String SERVICE_URL_CT = AppConfig.WebService_IP + "DBConnection"
			+"/WSCreateTablePort";
	public Thread_Register() {
		// TODO Auto-generated constructor stub
	}
	
	public Thread_Register(Handler handler){
		activity_handler = handler;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Looper.prepare();
		register_handler = new Handler(){
			
			String username;
			String email;
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				//super.handleMessage(msg);
				//��һҳ��ע��
				if(msg.what == 0x103){
					Bundle bundle = msg.getData();
					username = bundle.getString("username");
					String password = bundle.getString("password");
					email = bundle.getString("email");
//					String user_head = bundle.getString("user_head");
					final_result = execute(username, password, email);
					Message message = new Message();
					message.arg1 = final_result;
					message.what = 0x103;
					activity_handler.sendMessage(message);
					//��ע��ɹ���ʱ���Զ�Ϊ�û�������
					//Ϊ�û���ʼ���������ݿ�
//					if(final_result == 2){
//						createTable(email);
//					}
				}
				
				//�����ǩ�������Ϣ
				if(msg.what == 0x104){
					Bundle bundle2 = msg.getData();
					ArrayList<Integer> seletedItems = new ArrayList<Integer>();
					seletedItems = bundle2.getIntegerArrayList("interests");
					tag_result = tagInsert(seletedItems);
					Message message2 = new Message();
					message2.arg1 = tag_result;
					message2.arg2 = profile_id;
					message2.what = 0x104;
					activity_handler.sendMessage(message2);
				}
			}
			
		};
		//super.run();
		Looper.loop();
	}
	
	private int execute(String username,String password,String email){
		final HttpTransportSE transport = new HttpTransportSE(SERVICE_URL);
		transport.debug = true;
		final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		SoapObject object = new SoapObject(SERVICE_NS, "registerUser");
//		object.addProperty("arg0", user_head);
		Profile temp_profile = new  Profile();
		temp_profile.setUser_name(username);
		temp_profile.setUser_password(password);
		temp_profile.setUser_email(email);
		object.addProperty("arg0", temp_profile);
		envelope.addMapping(PROFILE_NS, "Profile", Profile.class);	
		envelope.bodyOut = object;
		try {
			transport.call(null, envelope);
			if(envelope.getResponse() != null){
				SoapObject result_object = (SoapObject)envelope.bodyIn;
				int num = result_object.getPropertyCount();
				if(num != 1){
					profile_id = Integer.parseInt(result_object.getProperty(1).toString());
				}
				return Integer.parseInt(result_object.getProperty(0).toString());
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
		return 3;
	}

	//0--�������
	//1--����ɹ�
	private int tagInsert(ArrayList<Integer> selectedItems){
		final HttpTransportSE transport = new HttpTransportSE(SERVICE_URL);
		transport.debug = true;
		final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		SoapObject object = new SoapObject(SERVICE_NS,"tagInsert");
		//����ʹ�����飬��������Ҫ���л�
		Profile temp_profile = new Profile();
		String user_tag = "";
		temp_profile.setUser_id(profile_id);
		for(int i=0;i<selectedItems.size();i++){
			user_tag = user_tag + "#" + selectedItems.get(i);
		}
		System.out.println(user_tag);
		temp_profile.setUser_tag(user_tag);
		object.addProperty("arg0", temp_profile);
		envelope.addMapping(PROFILE_NS, "Profile", Profile.class);
		envelope.bodyOut = object;
		try {
			transport.call(null, envelope);
			if(envelope.getResponse()!=null){
				SoapObject reObject = (SoapObject)envelope.bodyIn;
				return Integer.parseInt(reObject.getProperty(0).toString());
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
		return 0;
	}
	
	//���ݵĲ���Ҫ���email
//	private void createTable(String email){
//		final HttpTransportSE transport = new HttpTransportSE(SERVICE_URL_CT);
//		final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
//		SoapObject object = new SoapObject(SERVICE_NS, "createTables");
//		object.addProperty("arg0", email);
//		envelope.bodyOut = object;
//		try {
//			transport.call(null, envelope);
//		} catch (HttpResponseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (XmlPullParserException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	
}
