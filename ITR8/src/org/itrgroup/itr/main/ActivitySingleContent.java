package org.itrgroup.itr.main;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;

import org.itrgroup.itr.R;
import org.itrgroup.itr.adapter.CommentAdapter;
import org.itrgroup.itr.entity.CommentPub;
import org.itrgroup.itr.utils.AppConfig;
import org.itrgroup.itr.utils.CircleImageView;
import org.itrgroup.itr.utils.IniDatabaseHelper;
import org.itrgroup.itr.utils.SingleExecutorService;
import org.itrgroup.itr.ws_thread.Thread_Comment;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ActivitySingleContent  extends Activity{
	
	private ActionBar actionBar = null;
	private List<CommentPub> commentList = null;  //评论列表
	private CommentAdapter mCommentAdapter = null;  //评论列表Adapter

	//控件
	private ListView commentListView = null;
	private RelativeLayout single_content = null;
	private LinearLayout comment_button = null;
	private CircleImageView user_avatar = null; 
	TextView user_name = null;		 //用户名
	TextView publish_time = null;  //发布时间
	ImageView fir_tag = null;  //一级标签
	TextView publish_content = null;	 //发布内容
	
	private EditText comEditText = null;
	
	private String username_string;
	private String publish_time_string;
	private String publish_content_string;
	private String pub_user_avatar;
	private int fir_tag_int;
	//如果是直接评论的话，这个user_id就用作replied_iserId
	private int user_id;
	//通过这个获得profile_id
	private int com_userId;
	//这个是当前用户的email
	private String com_useremail;
	private String com_userName;
	private String com_userAvatar;
	private int pub_inforId;
//	private int user_id;
	private SharedPreferences sp = null;
	
	private Handler activity_handler = null;
	private Thread_Comment thread_comment = null;
	private SQLiteDatabase database = null;
	private Thread_LoadComment thread_loadcomment = null;
	
	private ExecutorService mExecutorService = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_content);
		
		sp = getSharedPreferences("userInfo", MODE_WORLD_READABLE);
		com_userId = sp.getInt("PROFILE_ID", 0);
		com_userName = sp.getString("REGIST_USERNAME", "");
		com_useremail = sp.getString("LOGIN_EMAIL", "");
		
		//获得从主界面传来的数据
		Intent intent = this.getIntent();       
		Bundle bundle = intent.getExtras();
		pub_inforId = bundle.getInt("pub_inforId");
		username_string = bundle.getString("user_name");
		publish_time_string = bundle.getString("publish_time");
		publish_content_string = bundle.getString("publish_content");
		fir_tag_int = bundle.getInt("fir_tag");
		user_id = bundle.getInt("user_id");
		pub_user_avatar = bundle.getString("user_avatar");
		
		//获得控件
		comment_button = (LinearLayout)findViewById(R.id.comment);
		commentListView = (ListView)findViewById(R.id.CommentList);
		user_avatar = (CircleImageView)findViewById(R.id.user_avatar);  //用户头像
		user_name = (TextView)findViewById(R.id.user_name);		 //用户名
		publish_time = (TextView)findViewById(R.id.publish_time);  //发布时间
		fir_tag = (ImageView)findViewById(R.id.fir_tag);  //一级标签
		publish_content = (TextView)findViewById(R.id.publish_content);	 //发布内容
		
		//设置控件的值
		user_name.setText(username_string);
		publish_time.setText(publish_time_string);
		fir_tag.setBackgroundResource(fir_tag_int);
		publish_content.setText(publish_content_string);
		Bitmap bt = BitmapFactory.decodeFile(AppConfig.avatar_path + pub_user_avatar);
		System.out.println("HEY EVER2!  " + publish_content_string);
		if(bt!=null){
			Drawable drawable = new BitmapDrawable(this.getResources(), bt);
			user_avatar.setImageDrawable(drawable);
		}else{
			/*
			 * 如果本地没有该图片，则从云端下载
			 */
		}
		
		
		//设置ActionBar
		actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setLogo(R.drawable.back_icon);
		actionBar.setHomeButtonEnabled(true);
		View actionbarLayout = LayoutInflater.from(this).inflate(R.layout.pub_actionbar_view, null); 
		TextView textView = (TextView) actionbarLayout.findViewById(R.id.actionBar);
		textView.setText("详细内容");
		actionBar.setDisplayShowCustomEnabled(true); 
		actionBar.setCustomView(actionbarLayout);
		
		//An adapter to handle this messages
		commentList = new ArrayList<CommentPub>();
//		commentList.add(new CommentModel("", "", "", 1, 1, 1, 1, 1, "","",""));
//		commentList.add(new CommentModel("", "", "", 1, 1, 1, 1, 1, "","",""));
//		commentList.add(new CommentModel("", "", "", 1, 1, 1, 1, 1, "","",""));
		mCommentAdapter = new CommentAdapter(this,commentList);
		commentListView.setAdapter(mCommentAdapter);
		
		comment_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				giveComment();
			}
		});
		
		activity_handler = new Comment_Handler();
		database = new IniDatabaseHelper(this, com_useremail + ".db3"
				, null, 1).getReadableDatabase();
		thread_comment = new Thread_Comment(activity_handler, database);
		//thread_comment.start();
		
		thread_loadcomment = new Thread_LoadComment();
		//thread_loadcomment.start();
		
		mExecutorService = SingleExecutorService.getInstance();
		mExecutorService.execute(thread_comment);
		mExecutorService.execute(thread_loadcomment);
	}
	
	//设置ActionBar点击效果
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		 switch (item.getItemId()) {  
	        case android.R.id.home:  
	            finish();
	            return true; 

	        default:  
	            return super.onOptionsItemSelected(item);  
	    }
	}
	
	//设置评论框
	public void giveComment(){
		comEditText = new EditText(this);
		comEditText.setHeight(400);
		comEditText.setBackgroundColor(Color.parseColor("#80ffffff"));
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("写下你的评论" )    
		.setView(comEditText)
		.setPositiveButton("确定" , new OKOnClickListener())
		.setNegativeButton("取消" ,  null)  
		.show();  
		
	}
	
	private class OKOnClickListener implements android.content.DialogInterface.OnClickListener{

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			String com_content = comEditText.getText().toString();
			if(com_content.equals("") || com_content == null){
				Toast.makeText(ActivitySingleContent.this, "说点啥吧", Toast.LENGTH_SHORT).show();
			}
			else{
				Date date = new Date();
				SimpleDateFormat date_format_db = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				String date_str = date_format_db.format(date);
				//com_userAvatar要存数本地据库中取出
//				int is_directed = 0;
				//com_loc现在是假的
				String com_loc = "CS";
				//这里不需要reply_userId和reply_userName
//				int replied_userId = user_id;
//				String replied_userName = "";
				Bundle request = new Bundle();
				CommentPub temp_comment = new  CommentPub();
				temp_comment.setCom_user_id(com_userId);
				temp_comment.setCom_location(com_loc);
				temp_comment.setCom_time(date_str);
				temp_comment.setCom_user_name(com_userName);
				temp_comment.setCom_content(com_content);
				temp_comment.setCom_pub_id(pub_inforId);
//				request.putInt("com_userId", com_userId);
//				request.putString("com_loc", com_loc);
//				request.putString("com_time", date_str);
//				request.putString("com_userName", com_userName);
				//com_userAvatar到线程中从服务器中获取
//				request.putInt("pub_inforId", pub_inforId);
//				request.putString("com_content", com_content);
//				request.putInt("is_directed", is_directed);
//				request.putInt("replied_userId", replied_userId);
//				request.putString("replied_userName", replied_userName);
				request.putSerializable("comment", temp_comment);
				Message msg = new Message();
				msg.what = 0x108;
				msg.setData(request);
				thread_comment.comment_handler.sendMessage(msg);
				

			}
		}
	}
	
	private class Comment_Handler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what == 0x108){
				int result = msg.arg1;
				if(result == 0){
					Toast.makeText(ActivitySingleContent.this, "评论失败", Toast.LENGTH_SHORT).show();
				}
				if(result == 1){
					Toast.makeText(ActivitySingleContent.this, "评论成功", Toast.LENGTH_SHORT).show();
					//成功则直接将comment加上去
					/*
					 * 里面还没有设置decoding，所以还要添加avatar上去
					 * 从本地端直接获取头像
					 */
					CommentPub temp_comment = (CommentPub)msg.getData().getSerializable("comment");
					temp_comment.setCom_user_avatar_encoding(encodingAvatar(AppConfig.avatar_path + temp_comment.getCom_user_avatar()));
					commentList.add(temp_comment);
					mCommentAdapter.notifyDataSetChanged();
				}
			}
			
			if(msg.what == 0x109){
				Bundle response = msg.getData();
				ArrayList<CommentPub> response_list = (ArrayList<CommentPub>) response.getSerializable("response_list");
				if(response_list != null){
					for(int i=0;i<response_list.size();i++){
						commentList.add(response_list.get(i));
						mCommentAdapter.notifyDataSetChanged();
					}
				}
			}
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		database.close();
	}
	
	private class Thread_LoadComment extends Thread{
		private final String SERVICE_URL = AppConfig.WebService_IP + "DBConnection"
				+"/WSLoadcommentPort";
		private final String SERVICE_NS = "http://dbConnection/";
		private static final String ENTITY_NS = "http://entity/";

		@Override
		public void run() {
			// TODO Auto-generated method stub
			ArrayList<CommentPub> response_list = loadComment(pub_inforId);
			Bundle response = new Bundle();
			response.putSerializable("response_list", response_list);
			Message response_msg = new Message();
			response_msg.what = 0x109;
			response_msg.setData(response);
			activity_handler.sendMessage(response_msg);
			super.run();
		}
		private ArrayList<CommentPub> loadComment(int com_pub_id){
			final HttpTransportSE transport = new HttpTransportSE(SERVICE_URL);
			final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			SoapObject object = new SoapObject(SERVICE_NS, "loadComment");
			CommentPub temp_comment = new CommentPub();
			temp_comment.setCom_pub_id(com_pub_id);
			object.addProperty("arg0", temp_comment);
			envelope.addMapping(ENTITY_NS, "CommentPub", CommentPub.class);
			envelope.bodyOut = object;
			ArrayList<CommentPub> response_list = new ArrayList<CommentPub>();
			try {
				transport.call(null, envelope);
				if(envelope.getResponse()!=null){
					SoapObject response = (SoapObject) envelope.bodyIn;
					//返回一个List<CommentPub>
					for(int i=0;i<response.getPropertyCount();i++){
						SoapObject temp = (SoapObject) response.getProperty(i);
						CommentPub response_comment = new CommentPub();
						response_comment.setCom_id(Integer.parseInt(temp.getProperty("com_id").toString()));
						response_comment.setCom_pub_id(Integer.parseInt(temp.getProperty("com_pub_id").toString()));
						response_comment.setCom_location(temp.getProperty("com_location").toString());
						response_comment.setCom_time(temp.getProperty("com_time").toString());
						response_comment.setCom_content(temp.getProperty("com_content").toString());
						response_comment.setCom_user_id(Integer.parseInt(temp.getProperty("com_user_id").toString()));
						response_comment.setCom_user_name(temp.getProperty("com_user_name").toString());
						response_comment.setCom_user_avatar(temp.getProperty("com_user_avatar").toString());
						response_comment.setCom_user_avatar_encoding(temp.getProperty("com_user_avatar_encoding").toString());
//						int com_userId = Integer.parseInt(temp.getPropertyAsString("com_userId"));
//						String com_loc = temp.getPropertyAsString("com_loc");
//						String com_time = temp.getPropertyAsString("com_time");
						//int pub_inforId = Integer.parseInt(temp.getPropertyAsString("pub_inforId"));
//						String com_content = temp.getPropertyAsString("com_content");
//						int is_directed = Integer.parseInt(temp.getPropertyAsString("is_directed"));
//						int replied_userId = Integer.parseInt(temp.getPropertyAsString("replied_userId"));
//						String replied_userName = temp.getPropertyAsString("replied_userName");
//						String com_userName = temp.getPropertyAsString("com_userName");
//						String com_userAvatar = temp.getPropertyAsString("com_userName");
						response_list.add(response_comment);
					}
					return response_list;
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
			return null;
		}
	}
	
	private String encodingAvatar(String avatar_path){
		byte[] temp = new byte[1024];
		try{
			BufferedInputStream buffered = new BufferedInputStream(new FileInputStream(avatar_path));
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			while(buffered.read(temp) >= 0){
				output.write(temp);
			}
			String avatar = Base64.encodeToString(output.toByteArray(), Base64.DEFAULT);
			output.close();
			buffered.close();
			return avatar;
		}catch (IOException e){
			e.printStackTrace();
		}
		return null;
	}

}
