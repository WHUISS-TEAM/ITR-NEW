package org.itrgroup.itr.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;

import org.itrgroup.itr.R;
import org.itrgroup.itr.entity.CommentPub;
import org.itrgroup.itr.main.ActivitySingleContent;
import org.itrgroup.itr.model.MainContentModel;
import org.itrgroup.itr.utils.AppConfig;
import org.itrgroup.itr.utils.CircleImageView;
import org.itrgroup.itr.utils.IniDatabaseHelper;
import org.itrgroup.itr.utils.OnConvertViewClickListener;
import org.itrgroup.itr.utils.SingleExecutorService;
import org.itrgroup.itr.ws_thread.Thread_Comment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class MainContentAdapter extends BaseAdapter{
	
	private Context context;
	private List<MainContentModel> contentList;
	private CommentPub commentInfo;
	private ExecutorService mExecutorService = null;
	private CommentHandler commentHandler = null;
	private SQLiteDatabase database = null;
	//private int[] colors = new int[] { 0xFF1E90FF, 0xFFFF0000, 0xFF66CD00, 0xFFFFFF00, 0xFF8B008B };
	
	public MainContentAdapter(Context c,List<MainContentModel> l,CommentPub comment,String user_email){
		context=c; 
		contentList=l; 
		commentInfo = comment;
		mExecutorService = SingleExecutorService.getInstance();
		commentHandler = new CommentHandler();
		database = new IniDatabaseHelper(context, user_email + ".db3"
				, null, 1).getReadableDatabase();
	}
	
	public int getCount() {
		// TODO Auto-generated method stub
		return contentList.size();
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		//return 0
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View rootView = LayoutInflater.from(context).inflate(
                R.layout.item_content, null);
		
		
		RelativeLayout contentLayout = (RelativeLayout)rootView.findViewById(R.id.content_item);  //������Ϣ
		CircleImageView user_avatar = (CircleImageView)rootView.findViewById(R.id.user_avatar);  //�û�ͷ��
		TextView user_name = (TextView)rootView.findViewById(R.id.user_name);		 //�û���
		TextView publish_time = (TextView)rootView.findViewById(R.id.publish_time);  //����ʱ��
		ImageView fir_tag = (ImageView)rootView.findViewById(R.id.fir_tag);  //һ����ǩ
		TextView publish_content = (TextView)rootView.findViewById(R.id.publish_content);	 //��������
		Button share = (Button)rootView.findViewById(R.id.share);	//����ť
		Button comment = (Button)rootView.findViewById(R.id.comment);	//���۰�ť
		Button vote = (Button)rootView.findViewById(R.id.vote);	//ͶƱ��ť
		
		user_name.setText(contentList.get(position).userName);
		publish_time.setText(contentList.get(position).pubTime);
		fir_tag.setBackgroundResource(contentList.get(position).fir_tag_img);
		publish_content.setText(contentList.get(position).pubContent);
		//sec_tag.setText(contentList.get(position).sec_tag);
		
		Bitmap bt = BitmapFactory.decodeFile(AppConfig.avatar_path + contentList.get(position).avatar_Name);
		if(bt!=null){
			Drawable drawable = new BitmapDrawable(context.getResources(), bt);//ת����drawable
			user_avatar.setImageDrawable(drawable);
		}else{
			/*
			 * ����û�и�ͷ��
			 */
		}
		
		share.setText("����(" + contentList.get(position).share_num +")");
		comment.setText("����(" + contentList.get(position).comment_num + ")");
		vote.setText("��ͬ(" + contentList.get(position).vote_num + ")");

		
		contentLayout.setOnClickListener(new OnConvertViewClickListener(convertView, R.integer.ab__id_adapter_item_position) {
			
			@Override
			public void onClickCallBack(View registedView, int... positionIds) {
				// TODO Auto-generated method stub
				//������Ϣ�ľ������ݴ��ݸ���һ������
				System.out.println("-------------------" + positionIds[0]);
				Intent intent = new Intent();
				intent.setClass(context, ActivitySingleContent.class);
				Bundle bundle = new Bundle();
				bundle.putInt("user_id", contentList.get(positionIds[0]).user_id);
				bundle.putInt("pub_inforId", contentList.get(positionIds[0]).pub_inforId);
				bundle.putString("user_name", contentList.get(positionIds[0]).userName);
				bundle.putString("user_avatar", contentList.get(positionIds[0]).avatar_Name);
				bundle.putString("publish_time", contentList.get(positionIds[0]).pubTime);
				bundle.putInt("fir_tag", contentList.get(positionIds[0]).fir_tag_img);
				bundle.putString("publish_content", contentList.get(positionIds[0]).pubContent);
				//bundle.putString("sec_tag", contentList.get(positionIds[0]).sec_tag);
				System.out.println("HEY EVER!  " + contentList.get(positionIds[0]).pubContent);
				//bundle.putInt("user_id", value);
				intent.putExtras(bundle);
				context.startActivity(intent);
			}
		});

		comment.setOnClickListener(new OnConvertViewClickListener(convertView, R.integer.ab__id_adapter_item_position) {
			@Override
			public void onClickCallBack(View registedView, int... positionIds) {
				// TODO Auto-generated method stub
				giveComment(contentList.get(positionIds[0]).pub_inforId);
			}
		});
		
		if(convertView != null){
			convertView.setTag(R.integer.ab__id_adapter_item_position, position);
		}
		return rootView;
	}
	
	//�������ۿ�
	public void giveComment(int com_pub_id){
		final EditText comEditText = new EditText(context);
		final int pub_id = com_pub_id;
		comEditText.setHeight(400);
		comEditText.setBackgroundColor(Color.parseColor("#80ffffff"));
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("д���������" )    
		.setView(comEditText)
		.setPositiveButton("ȷ��" , new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				System.out.println();
				comment(comEditText.getText().toString(),pub_id);
			}
		})  
		.setNegativeButton("ȡ��" ,  null )  
		.show();	
	}
	
	private void comment(String com_content,int com_pub_id){
		if(com_content.equals("") || com_content == null){
			Toast.makeText(context, "˵��ɶ��", Toast.LENGTH_SHORT);
		}else{
			Thread_Comment thread_comment = new Thread_Comment(commentHandler,database);
			mExecutorService.execute(thread_comment);
			Date date = new Date();
			SimpleDateFormat date_format_db = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String date_str = date_format_db.format(date);
			//com_userAvatarҪ�������ؾݿ���ȡ��
//			int is_directed = 0;
			//com_loc�����Ǽٵ�
			String com_loc = "CS";
			//���ﲻ��Ҫreply_userId��reply_userName
//			int replied_userId = user_id;
//			String replied_userName = "";
			Bundle request = new Bundle();
			CommentPub temp_comment = new  CommentPub();
			temp_comment.setCom_user_id(commentInfo.getCom_user_id());
			temp_comment.setCom_location(com_loc);
			temp_comment.setCom_time(date_str);
			temp_comment.setCom_user_name(commentInfo.getCom_user_name());
			temp_comment.setCom_content(com_content);
			temp_comment.setCom_pub_id(com_pub_id);
			request.putSerializable("comment", temp_comment);
			Message msg = new Message();
			msg.what = 0x111;
			msg.setData(request);
			thread_comment.comment_handler.sendMessage(msg);
		}
		
	}
	
	private class CommentHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if(msg.what == 0x111){
				if(msg.arg1 == 0){
					Toast.makeText(context, "����ʧ��", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(context, "���۳ɹ�", Toast.LENGTH_SHORT).show();
				}
			}
		}
	}
	
	

	
}
