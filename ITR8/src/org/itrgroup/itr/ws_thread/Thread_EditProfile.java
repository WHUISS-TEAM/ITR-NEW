package org.itrgroup.itr.ws_thread;

import java.io.IOException;
import java.util.ArrayList;

import org.itrgroup.itr.entity.Profile;
import org.itrgroup.itr.utils.AppConfig;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.R.bool;
import android.R.integer;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.ContactsContract.CommonDataKinds.Email;

public class Thread_EditProfile extends Thread{
	
	//webservice的地址，只需要改ip和端口
	private static final String SERVICE_URL = AppConfig.WebService_IP + "DBConnection" + "/WSEditProfilePort";
	private static final String SERVICE_NS = "http://dbConnection/";
	private static final String ENTITY_NS = "http://entity/";
	
	//接受EditProfile类的handler，在处理完后用来向他发送消息
	private Handler activity_handler = null;
	public Handler edit_handler = null;
	
	//用于对本地数据库进行处理
	private SQLiteDatabase db = null;
	
	public Thread_EditProfile(Handler handler){
		activity_handler = handler;
	}
	
	public void setDB(SQLiteDatabase database){
		db = database;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Looper.prepare();
		edit_handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if(msg.what == 0x106){
					Bundle bundle = msg.getData();
					String username = bundle.getString("username");
					String school = bundle.getString("school");
					String academy = bundle.getString("academy");
					String major = bundle.getString("major");
					String introduction = bundle.getString("introduction");
					int gender = bundle.getInt("gender");
					int age = bundle.getInt("age");
					int id = bundle.getInt("id");
					Message response = new Message();
					int result = edit_profile(username, school, academy, major, introduction, gender, age, id);
					if(result == 1){
						if(edit_profile_local(username, school, academy, major, introduction, gender, age, id)){
							result = 2;
						}
					}
					response.arg1 = result;
					response.what = 0x106;
					activity_handler.sendMessage(response);
				}
				if(msg.what == 0x107){
					Bundle bundle2 = msg.getData();
					ArrayList<Integer> seletedItems = new ArrayList<Integer>();
					seletedItems = bundle2.getIntegerArrayList("interests");
					int id = bundle2.getInt("id");
					Message message2 = new Message();
					int result = edit_tag(seletedItems,id);
					if(result == 1){
						if(edit_tag_local(seletedItems, id)){
							result = 2;
						}
					}
					message2.arg1 = result;
					message2.what = 0x107;
					activity_handler.sendMessage(message2);
					seletedItems.clear();
				}
				//super.handleMessage(msg);
				if(msg.what == 0x105){
					System.out.println("in");
					Bundle image_data = msg.getData();
					String image = image_data.getString("image");
					String email = image_data.getString("email");
					int profile_id = image_data.getInt("profile_id");
					int result = upload_avatar(image, email, profile_id);
					if(result == 1){
						db.execSQL("UPDATE profile" 
								 	+ " SET user_Avatar = '" + profile_id + "_avatar.jpeg'"
									+ " WHERE user_id = " + profile_id);
					}
					Message result_msg = new Message();
					result_msg.what = 0x105;
					Bundle result_bundle = new Bundle();
					result_bundle.putInt("upload_result", result);
					result_msg.setData(result_bundle);
					activity_handler.sendMessage(result_msg);
				}
			}
		};
		super.run();
		Looper.loop();
	}
	
	//0--修改失败
	//1--修改成功
	private int edit_profile(String username, String school, String academy, String major, String introduction, int gender, int age, int id){
		final HttpTransportSE transport = new HttpTransportSE(SERVICE_URL);
		final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		SoapObject object = new SoapObject(SERVICE_NS, "EditProfile");
		Profile temp_profile = new Profile();
		temp_profile.setUser_id(id);
		temp_profile.setUser_name(username);
		temp_profile.setUser_school(school);
		temp_profile.setUser_academy(academy);
		temp_profile.setUser_major(major);
		temp_profile.setUser_introduction(introduction);
		temp_profile.setUser_gender(gender);
		temp_profile.setUser_age(age);
		object.addProperty("arg0",temp_profile);
		envelope.addMapping(ENTITY_NS, "Profile", Profile.class);
		envelope.bodyOut = object;
		try {
			transport.call(null, envelope);
			if(envelope.getResponse() != null){
				SoapObject result_object = (SoapObject)envelope.bodyIn;
				return Integer.parseInt(result_object.getProperty(0).toString());
			}
		} catch (HttpResponseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		return 0;
	}
	
	private boolean edit_profile_local(String username, String school, String academy, String major, String introduction, int gender, int age, int id){
		try{
			ContentValues values = new ContentValues();
			values.put("user_name", username);
			values.put("user_gender", gender);
			values.put("user_age", age);
			values.put("user_school", school);
			values.put("user_academy", academy);
			values.put("user_major", major);
			values.put("user_introduction",introduction);
			if(db.update("profile", values, "user_id = "+id, null) == -1){
				return false;
			}
		} catch (SQLException e){
			e.printStackTrace();
			return false;
		} finally {
			db.close();
		}
		return true;
	}
	
	//0--修改失败
	//1--修改成功
	private int edit_tag(ArrayList<Integer> selectedItems, int id){
		final HttpTransportSE transport = new HttpTransportSE(SERVICE_URL);
		transport.debug = true;
		final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		SoapObject object = new SoapObject(SERVICE_NS,"EditTag");
		Profile temp_profile = new Profile();
		temp_profile.setUser_id(id);
		String string_tag = "";
		for(int i=0;i<selectedItems.size();i++){
			string_tag = string_tag + "#" +selectedItems.get(i);
		}
		temp_profile.setUser_tag(string_tag);
		object.addProperty("arg0", temp_profile);
		envelope.addMapping(ENTITY_NS, "Profile", Profile.class);
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
	
	private boolean edit_tag_local(ArrayList<Integer> selectedItems, int id){
		try{
			db.delete("user_tag_link", "user_id = ?", new String[]{id+""});
			for (int index : selectedItems) {
				ContentValues tag_values = new ContentValues();
				tag_values.put("user_id", id);
				tag_values.put("user_tag_id", index);
				if(db.insert("user_tag_link", null, tag_values) == -1){
					return false;
				}
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
		return true;
	}
	
	//0--失败
	//1--成功
	private int upload_avatar(String user_avatar,String email,int user_id){
		final HttpTransportSE transport = new HttpTransportSE(SERVICE_URL);
		final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		SoapObject object = new SoapObject(SERVICE_NS, "UploadAvatar");
		Profile temp_profile = new Profile();
		temp_profile.setUser_id(user_id);
		temp_profile.setUser_avatar(user_avatar);
		temp_profile.setUser_email(email);
		object.addProperty("arg0", temp_profile);
		envelope.addMapping(ENTITY_NS, "Profile", Profile.class);
		envelope.bodyOut = object;
		try {
			transport.call(null, envelope);
			if(envelope.getResponse() != null){
				System.out.println("inini");
				SoapObject response = (SoapObject) envelope.bodyIn;
				int result = Integer.parseInt(response.getProperty(0).toString());
				return result;
			}
		} catch (HttpResponseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		return 0;
	}
}
