package org.itrgroup.itr.ws_thread;

import java.io.IOException;

import org.itrgroup.itr.entity.Publishment;
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

public class Thread_Pub extends Thread{

	public Handler pub_handler = null;
	//接受pub类的handler，在处理完后用来向他发送消息
	private Handler activity_handler = null;
	private int pub_inforId;
	//webservice的地址，只需要改ip和端口
	private static final String SERVICE_URL = AppConfig.WebService_IP + "DBConnection"
			+"/WSPublishPort";
	private static final String SERVICE_NS = "http://dbConnection/";
	private static final String ENTITY_NS = "http://entity/";
	
	public Thread_Pub() {
		// TODO Auto-generated constructor stub
	}
	public Thread_Pub(Handler handler){
		activity_handler = handler;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Looper.prepare();
		pub_handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if(msg.what == 0x110){
					Bundle bundle = msg.getData();
//					String email = bundle.getString("email");
					String userName = bundle.getString("userName");
					String user_head = bundle.getString("user_head");
					String date = bundle.getString("Inf_time");
					String location = bundle.getString("Inf_loc");
					String str_publish_content = bundle.getString("Inf_content");
					int choose_tag = bundle.getInt("Pub_tag_level1");
//					String str_sec_tag = bundle.getString("Pub_tag_level2");
//					int vote = bundle.getInt("vote");
//					int com = bundle.getInt("com");
//					int share = bundle.getInt("share");
					int profile_id = bundle.getInt("profile_id");
					Message response = new Message();
					response.arg1 = 
					publish(userName,user_head, date, location, str_publish_content, choose_tag,profile_id);
					//pub_inforId存储在arg2里面
					response.arg2 = pub_inforId;
					response.what = 0x110;
					activity_handler.sendMessage(response);
					
				}
				super.handleMessage(msg);
			}
		};
		super.run();
		Looper.loop();
	}
	
	//0--发布失败
	//1--发布成功
	//增加了个email参数
	private int publish(String pub_user_name,String pub_user_avatar,String pub_time,String pub_location,String pub_content,
			int pub_tag_level_1,int pub_user_id){
		final HttpTransportSE transport = new HttpTransportSE(SERVICE_URL);
		final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		SoapObject object = new SoapObject(SERVICE_NS, "Publish");
		Publishment temp_pub = new Publishment();
		temp_pub.setPub_user_id(pub_user_id);
		temp_pub.setPub_user_name(pub_user_name);
		temp_pub.setPub_user_avatar(pub_user_avatar);
		temp_pub.setPub_time(pub_time);
		temp_pub.setPub_location(pub_location);
		temp_pub.setPub_content(pub_content);
		temp_pub.setPub_tag_level_1(pub_tag_level_1);
		object.addProperty("arg0",temp_pub);
		envelope.addMapping(ENTITY_NS, "Publishment", Publishment.class);
		envelope.bodyOut = object;
		try {
			transport.call(null, envelope);
			if(envelope.getResponse() != null){
				SoapObject result_object = (SoapObject)envelope.bodyIn;
				if(result_object.getPropertyCount() != 1){
					
					pub_inforId = Integer.parseInt(result_object.getProperty(1).toString());
				}
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
	
}
