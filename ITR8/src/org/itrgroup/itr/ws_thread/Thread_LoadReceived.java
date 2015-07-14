package org.itrgroup.itr.ws_thread;

import java.io.IOException;

import org.itrgroup.itr.utils.AppConfig;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class Thread_LoadReceived extends Thread{

	public Handler handler = null;
	private Handler activity_handler = null;
	private SQLiteDatabase db = null;
	
	
	private static final String SERVICE_URL = AppConfig.WebService_IP + "DBConnection"
			+"/WSLoadReceivePort";
	private static final String SERVICE_NS = "http://dbConnection/";
	
	public Thread_LoadReceived(Handler activity_handler) {
		// TODO Auto-generated constructor stub
		this.activity_handler = activity_handler;
	}
	
	public Thread_LoadReceived(SQLiteDatabase database){
		db = database;
	}
	
	public void setDB(SQLiteDatabase database){
		db = database;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Looper.prepare();
		handler = new Handler(){
			
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				if(msg.what == 0x102){
					Bundle bundle = msg.getData();
					String email = bundle.getString("email");
					int state = getReceive(email);
					Message response = new Message();
					response.what = 0x102;
					response.arg1 = state;
					activity_handler.sendMessage(response);
				}
			}
		};
		Looper.loop();
	}
	
	//0--中途出错
	//1--操作完成
	private int getReceive(String email){
		final HttpTransportSE transport = new HttpTransportSE(SERVICE_URL);
		final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		SoapObject object = new SoapObject(SERVICE_NS, "Load");
		object.addProperty("arg0", email);
		envelope.bodyOut = object;
		try {
			transport.call(null, envelope);
			if(envelope.getResponse() != null){
				SoapObject results = (SoapObject)envelope.bodyIn;
				for(int i=0;i<results.getPropertyCount();i++){
					SoapObject temp_obj = (SoapObject) results.getProperty(i);
					ContentValues values = new ContentValues();
					
					//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!这一块要改之后！！！！！
					values.put("Pub_inforId", Integer.parseInt(temp_obj.getPropertyAsString("pub_inforId")));
					values.put("Pub_userName", temp_obj.getPropertyAsString("pub_userName"));
					values.put("Pub_userHead", temp_obj.getPropertyAsString("pub_userAvatar"));
					values.put("Pub_userId", Integer.parseInt(temp_obj.getPropertyAsString("user_id")));
					values.put("Inf_time", temp_obj.getPropertyAsString("pub_time"));
					values.put("Inf_loc", temp_obj.getPropertyAsString("pub_loc"));
					values.put("Inf_content", temp_obj.getPropertyAsString("pub_content"));
					values.put("Pub_tag_level1", Integer.parseInt(temp_obj.getPropertyAsString("pub_tag_level1")));
					values.put("Pub_tag_level2", temp_obj.getPropertyAsString("pub_tag_level2"));
					values.put("Vote_num", Integer.parseInt(temp_obj.getPropertyAsString("vote_num")));
					values.put("Com_num", Integer.parseInt(temp_obj.getPropertyAsString("com_num")));
					values.put("Share_num", Integer.parseInt(temp_obj.getPropertyAsString("share_num")));
					if(db.insert("Received", null, values) == -1){
						return 0;
					}
				}
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
		}finally{
			db.close();
		}
		return 1;
	}
}
