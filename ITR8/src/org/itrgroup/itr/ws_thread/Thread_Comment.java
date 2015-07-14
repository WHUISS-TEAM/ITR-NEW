package org.itrgroup.itr.ws_thread;

import java.io.IOException;

import org.itrgroup.itr.entity.CommentPub;
import org.itrgroup.itr.entity.Profile;
import org.itrgroup.itr.entity.Publishment;
import org.itrgroup.itr.utils.AppConfig;
import org.itrgroup.itr.utils.LocalDB;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class Thread_Comment extends Thread{

	private Handler activity_handler = null;
	public Handler comment_handler = null;
	private SQLiteDatabase db = null;
	
	private static final String SERVICE_URL = AppConfig.WebService_IP + "DBConnection"
			+"/WSCommentPort";
	private static final String SERVICE_NS = "http://dbConnection/";
	private static final String ENTITY_NS = "http://entity/";

	public Thread_Comment(Handler activity_handler) {
		// TODO Auto-generated constructor stub
		this.activity_handler = activity_handler;
	}
	public Thread_Comment(Handler activity_handler,SQLiteDatabase db) {
		// TODO Auto-generated constructor stub
		this.activity_handler = activity_handler;
		this.db = db;
	}
	public void setDatabase(SQLiteDatabase db){
		this.db = db;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Looper.prepare();
		comment_handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if(msg.what == 0x108){
					super.handleMessage(msg);
					CommentPub temp_comment = preExecuteThread(msg);
					int result = comment(temp_comment);
					//将用户刚刚发布的comment返回，如果成功了就直接在客户端显示
					Bundle buffer_bundle = new Bundle();
					buffer_bundle.putSerializable("comment", temp_comment);
					Message message = new Message();
					message.what = 0x108;
					message.arg1 = result;
					message.setData(buffer_bundle);
					activity_handler.sendMessage(message);
				}
				if(msg.what == 0x111){
					super.handleMessage(msg);
					CommentPub temp_comment = preExecuteThread(msg);
					int result = comment(temp_comment);
					Message message = new Message();
					message.what = 0x109;
					message.arg1 = result;
					activity_handler.sendMessage(message);
				}
			}
		};
		super.run();
		Looper.loop();
	}
	//0--失败
	//1--成功
	private int comment(CommentPub comment){
		final HttpTransportSE transport = new HttpTransportSE(SERVICE_URL);
		final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		SoapObject object = new SoapObject(SERVICE_NS, "comment");
//		CommentPub temp_comm = new CommentPub();
//		temp_comm.setCom_pub_id(com_pub_id);
//		temp_comm.setCom_location(com_location);
//		temp_comm.setCom_time(com_time);
//		temp_comm.setCom_content(com_content);
//		temp_comm.setCom_user_id(com_user_id);
//		temp_comm.setCom_user_name(com_user_name);
//		comment.setCom_user_avatar(com_user_avatar);
		object.addProperty("arg0",comment);
		envelope.addMapping(ENTITY_NS, "CommentPub", CommentPub.class);
		envelope.bodyOut = object;
		try {
			transport.call(null, envelope);
			if(envelope.getResponse()!=null){
				SoapObject response = (SoapObject) envelope.bodyIn;
				return Integer.parseInt(response.getProperty(0).toString());
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
	
	private CommentPub preExecuteThread(Message msg){
		CommentPub comment = (CommentPub) msg.getData().getSerializable("comment");
		String profile_sql = "SELECT user_avatar FROM profile WHERE user_id =" + comment.getCom_user_id();
		Cursor cursor = db.rawQuery(profile_sql, null);
		cursor.moveToFirst();
		System.out.println(cursor.getString(cursor.getColumnIndex(LocalDB.PRO_AVATAR_STRING)));
		String com_userAvatar = cursor.getString(cursor.getColumnIndex(LocalDB.PRO_AVATAR_STRING));
		comment.setCom_user_avatar(com_userAvatar);
		return comment;
	}
	
	
}

