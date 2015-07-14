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

public class Thread_DownloadAvatar extends Thread{

	private Handler activity_handler;
	public Handler down_handler;
	
	//webservice的地址，只需要改ip和端口
	private static final String SERVICE_URL = AppConfig.WebService_IP + "DBConnection"
			+"/WSDownloadAvatarPort";
	private static final String SERVICE_NS = "http://dbConnection/";
	private static final String ENTITY_NS = "http://entity/";
	
	public Thread_DownloadAvatar(Handler activity_handler) {
		// TODO Auto-generated constructor stub
		this.activity_handler = activity_handler;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Looper.prepare();
		down_handler = new Handler(){
			
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				if(msg.what == 0x101){
					Bundle bundle = msg.getData();
					int profile_id = bundle.getInt("profile_id");
					System.out.println("aaa" + profile_id);
					String image_result = downloadAvatar(profile_id);
					Message response_msg = new Message();
					response_msg.what = 0x101;
					Bundle response_bundle = new Bundle();
					response_bundle.putString("image", image_result);
					response_bundle.putInt("user_id", profile_id);
					response_msg.setData(response_bundle);
					activity_handler.sendMessage(response_msg);
				}
			}
		};
		Looper.loop();
	}
	
	private String downloadAvatar(int profile_id){
		final HttpTransportSE transport = new HttpTransportSE(SERVICE_URL);
		final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		SoapObject object = new SoapObject(SERVICE_NS, "downloadAvatar");
		Profile temp_profile = new Profile();
		temp_profile.setUser_id(profile_id);
//		object.addProperty("arg0", profile_id);
		object.addProperty("arg0",temp_profile);
		envelope.addMapping(ENTITY_NS, "Profile", Profile.class);
		envelope.bodyOut = object;
		try {
			transport.call(null, envelope);
			if(envelope.getResponse() != null){
				SoapObject response = (SoapObject) envelope.bodyIn;
				String result = response.getProperty(0).toString();
				return result;
			}
		} catch (HttpResponseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return null;
	}
}
