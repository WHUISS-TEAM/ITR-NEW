package org.itrgroup.itr.main;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

import org.itrgroup.itr.R;
import org.itrgroup.itr.utils.AppConfig;
import org.itrgroup.itr.utils.IniDatabaseHelper;
import org.itrgroup.itr.utils.SingleExecutorService;
import org.itrgroup.itr.ws_thread.Thread_EditProfile;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityEditProfile extends Activity{
	
	private ActionBar actionBar = null;
	private String email;
	private String username;
	private String introduction;
	private String school;
	private String academy;
	private String major;
	private String[] usertag;
	private int profile_id;
	private int age;
	private int gender;
	private Bitmap avatar_img;
	private final ArrayList<Integer> seletedItems=new ArrayList<Integer>();
	
	//传输数据相关
	private Thread_EditProfile edit_thread = null;
	private Handler edit_handler = null;
	private ExecutorService mExecutorService = null;
	
	//控件
	private ImageView avatarView;
	private EditText usernameText;
	private EditText introText;
	private EditText schoolText;
	private EditText academyText;
	private EditText majorText;
	private EditText ageText;
	private Button maleButton;
	private Button femaleButton;
	private Button tagButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_profile);
		
		//获得从资料界面传来的数据
		Intent intent = this.getIntent();       
		Bundle bundle = intent.getExtras(); 
		username = bundle.getString("username");
		introduction = bundle.getString("introduction");
		school = bundle.getString("school");
		academy = bundle.getString("academy");
		major = bundle.getString("major");
		age = bundle.getInt("age");
		gender = bundle.getInt("gender");
		usertag = bundle.getStringArray("tag_results");
		profile_id = bundle.getInt("profile_id");
		email = bundle.getString("email");
			
		//设置ActionBar
		actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setLogo(R.drawable.back_icon);
		actionBar.setHomeButtonEnabled(true);
		View actionbarLayout = LayoutInflater.from(this).inflate(R.layout.pub_actionbar_view, null); 
		TextView textView = (TextView) actionbarLayout.findViewById(R.id.actionBar);
		textView.setText("修改资料");
		actionBar.setDisplayShowCustomEnabled(true); 
		actionBar.setCustomView(actionbarLayout);
		
		//获得控件
		avatarView = (ImageView)findViewById(R.id.avatar);
		usernameText = (EditText)findViewById(R.id.username);
		introText = (EditText)findViewById(R.id.introduction);
		schoolText = (EditText)findViewById(R.id.school);
		academyText = (EditText)findViewById(R.id.academy);
		majorText = (EditText)findViewById(R.id.major);
		ageText = (EditText)findViewById(R.id.age);
		maleButton = (Button)findViewById(R.id.gender_male);
		femaleButton = (Button)findViewById(R.id.gender_female);
		tagButton = (Button)findViewById(R.id.tags_select);
		
		//传输数据
		edit_handler = new Edit_Handler();
		edit_thread = new Thread_EditProfile(edit_handler);
		edit_thread.setDB(new IniDatabaseHelper(ActivityEditProfile.this, email + ".db3", null, 1).getReadableDatabase());
		//edit_thread.start();
		mExecutorService = SingleExecutorService.getInstance();
		mExecutorService.execute(edit_thread);
		
		
		//若头像已保存在本地，则设置已有头像
		Bitmap bt = BitmapFactory.decodeFile(AppConfig.avatar_path + profile_id + "_avatar.jpeg");
		if(bt!=null){
			@SuppressWarnings("deprecation")
			Drawable drawable = new BitmapDrawable(this.getResources(), bt);//转换成drawable
			avatarView.setImageDrawable(drawable);
		}else{
			/**
			 *	如果SD里面没有则需要从服务器取头像，取回来的头像再保存在SD中
			 * 
			 */
		}
		
		
		//依据已有数据设定内容
		usernameText.setText(username);
		introText.setText(introduction);
		schoolText.setText(school);
		academyText.setText(academy);
		majorText.setText(major);
		ageText.setText(age+"");
		if(gender == 1){
			maleButton.setBackgroundColor(android.graphics.Color.parseColor("#550072E3"));
			femaleButton.setBackgroundColor(android.graphics.Color.parseColor("#EDEDED"));
		}else if(gender == 2){
			femaleButton.setBackgroundColor(android.graphics.Color.parseColor("#FFB6C1"));
			maleButton.setBackgroundColor(android.graphics.Color.parseColor("#EDEDED"));
		}else {
			maleButton.setBackgroundColor(android.graphics.Color.parseColor("#EDEDED"));
			femaleButton.setBackgroundColor(android.graphics.Color.parseColor("#EDEDED"));
		}
		
		//设置性别按钮监听器
		maleButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				maleButton.setBackgroundColor(android.graphics.Color.parseColor("#550072E3"));
				femaleButton.setBackgroundColor(android.graphics.Color.parseColor("#EDEDED"));
				gender = 1;
			}
			
		});
		
		femaleButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				femaleButton.setBackgroundColor(android.graphics.Color.parseColor("#FFB6C1"));
				maleButton.setBackgroundColor(android.graphics.Color.parseColor("#EDEDED"));
				gender = 2;
			}
		});
		
		//设置头像的监听器
		avatarView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ShowDialog();
			}
		});
		
		//设置修改用户标签按钮的监听器
		tagButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SelectInterests();
			}
		});
	}
	
	//设置ActionBar右侧按钮
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.pub_actionbar_view, menu);
        return true;
	}
	
	//设置ActionBar点击效果
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		 switch (item.getItemId()) {  
	        case android.R.id.home:  
	            Intent intent = new Intent(this, MainActivity.class);  
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
	            startActivity(intent);  
	            return true; 

	        case R.id.pub_send:
	        	execute_edit();
	        	return true;
	        default:  
	            return super.onOptionsItemSelected(item);  
	    }
	}
	
	//修改头像弹出框设定
	public void ShowDialog(){
		new AlertDialog.Builder(this).setTitle("头像来源").setItems(
				new String[] { "相册", "拍照" }, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						switch (which) {
						case 0:
							Intent intent1 = new Intent(Intent.ACTION_PICK, null);
							intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
							startActivityForResult(intent1, 1);
							break;
							
						case 1:
							Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
							intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
											"avatar.jpg")));
							startActivityForResult(intent2, 2);//采用ForResult打开
							break;
						default:
							break;
						}
					}
				}).setNegativeButton(
			     "取消", null).show();
	}
	
	//接收startActivityForResult返回的数据
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1:
			if (resultCode == RESULT_OK) {
				cropPhoto(data.getData());//裁剪图片
			}
			break;
		case 2:
			if (resultCode == RESULT_OK) {
				File temp = new File(Environment.getExternalStorageDirectory()
						+ "/avatar.jpg");
				cropPhoto(Uri.fromFile(temp));//裁剪图片
			}
			break;
		case 3:
			if (data != null) {
				Bundle extras = data.getExtras();
				avatar_img = extras.getParcelable("data");
				if(avatar_img!=null){
					/**
					 * 上传服务器代码
					 */
					ByteArrayOutputStream outputstream = new ByteArrayOutputStream();
					//quality是不是需要改变，100是不是精度有点高
					avatar_img.compress(CompressFormat.JPEG, 100, outputstream);
					//image就是图片的base64编码
					String image = Base64.encodeToString(outputstream.toByteArray(),Base64.DEFAULT);
					Bundle image_data = new Bundle();
					image_data.putString("image", image);
					image_data.putString("email", email);
					image_data.putInt("profile_id", profile_id);
					Message msg = new Message();
					msg.what = 0x105;
					msg.setData(image_data);
					edit_thread.edit_handler.sendMessage(msg);
					//上传图片完成
					setPicToView(avatar_img);//保存在SD卡中
					avatarView.setImageBitmap(avatar_img);//用ImageView显示出来
					File temp = new File(Environment.getExternalStorageDirectory()
							+ "/avatar.jpg");
					temp.delete();
					
				}
			}
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	};
	
	//系统提供的裁剪方法
	public void cropPhoto(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		 // aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 3);
	}
	
	//将图片保存在本地
	private void setPicToView(Bitmap mBitmap) {
		 String sdStatus = Environment.getExternalStorageState();  
       if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用  
              return;  
       }  
       FileOutputStream b = null;
       File file = new File(AppConfig.avatar_path);
       if(!file.exists()){
    	   file.mkdirs();// 创建文件夹
       }
       String fileName = AppConfig.avatar_path + profile_id + "_avatar.jpeg"; //图片名字
       try {
    	   b = new FileOutputStream(fileName);
    	   mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
    	   
       } catch (FileNotFoundException e) {
    	   e.printStackTrace();
       } finally {
    	   try {
    		   //关闭流
    		   b.flush();
    		   b.close();
    	   } catch (IOException e) {
    		   e.printStackTrace();
    	   }
       }
	}
	
	//选择兴趣的复选框
	private void SelectInterests(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("你对哪些内容感兴趣呢？我们将基于你的选择推送消息。(请至少选择3个标签 )");
        builder.setMultiChoiceItems(AppConfig.user_tag_items, null,
                new DialogInterface.OnMultiChoiceClickListener() {
         @Override
         public void onClick(DialogInterface dialog, int indexSelected,
                 boolean isChecked) {
             if (isChecked) {
            	 //任意一个选项被勾选时执行
            	 if(seletedItems.size()<=4){
            		 seletedItems.add(indexSelected + 1);
            	 }else{
            		 Toast.makeText(ActivityEditProfile.this, "最多只能选择5个标签 : )", Toast.LENGTH_SHORT).show();
                	 ((AlertDialog) dialog).getListView().setItemChecked(indexSelected, false);
            	 }
             } else if (seletedItems.contains(indexSelected + 1)) {
            	 //任意一个选项被取消勾选时执行
                 seletedItems.remove(Integer.valueOf(indexSelected + 1));
             }
         }
     }).setPositiveButton("完成", new DialogInterface.OnClickListener() {
         @Override
         public void onClick(DialogInterface dialog, int id) {
        	 if(seletedItems.size()<3){
        		 Toast.makeText(ActivityEditProfile.this, "请至少选择3个标签 : )", Toast.LENGTH_SHORT).show();
        	 }else{
        		execute_tag();
        	 }
         }
     })
     .setNegativeButton("取消", new DialogInterface.OnClickListener() {
         @Override
         public void onClick(DialogInterface dialog, int id) {
        	 seletedItems.clear();
         }
     });
        
        builder.create().show();
	}
	
	//修改基本资料
	private void execute_edit(){
		username = usernameText.getText().toString();
		school = schoolText.getText().toString();
		academy = academyText.getText().toString();
		major = majorText.getText().toString();
		introduction = introText.getText().toString();
		age = Integer.parseInt(ageText.getText().toString());
		Bundle data = new Bundle();
		data.putString("username", username);
		data.putString("school", school);
		data.putString("academy", academy);
		data.putString("major", major);
		data.putString("introduction", introduction);
		data.putInt("gender", gender);
		data.putInt("age", age);
		data.putInt("id", profile_id);
		if(username.equals("")){
			Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
		}else if(username.contains(" ")){
			Toast.makeText(this, "用户名不能包含空格", Toast.LENGTH_SHORT).show();
		}else {
			Message msg = new Message();
			msg.what = 0x106;
			msg.setData(data);
			edit_thread.edit_handler.sendMessage(msg);
		}
	}
	
	//修改用户标签
	private void execute_tag(){
		Message msg_interests = new Message();
		Bundle bundle_interests = new Bundle();
		bundle_interests.putIntegerArrayList("interests", seletedItems);
		bundle_interests.putInt("id", profile_id);
		msg_interests.setData(bundle_interests);
		msg_interests.what = 0x107;
		edit_thread.edit_handler.sendMessage(msg_interests);
	}
	
	private class Edit_Handler extends Handler{
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what == 0x106){
				if(msg.arg1 == 0 || msg.arg1 == 1){
					System.out.println(msg.arg1);
					Toast.makeText(ActivityEditProfile.this, "修改失败", Toast.LENGTH_SHORT).show();
				}
				if(msg.arg1 == 2){
					Toast.makeText(ActivityEditProfile.this, "修改成功", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent();
					intent.setClass(ActivityEditProfile.this, MainActivity.class);
					startActivity(intent);
					finish();
				}
			}
			if(msg.what == 0x107){
				if(msg.arg1 == 0 || msg.arg1 == 1){
					Toast.makeText(ActivityEditProfile.this, "用户标签修改失败，请重新选择", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(ActivityEditProfile.this, "用户标签修改成功，点击右上角蓝色按钮确认修改资料", Toast.LENGTH_SHORT).show();
				}
			}
			if(msg.what == 0x105){
				Bundle result_bundle = msg.getData();
				int result = result_bundle.getInt("upload_result");
				if(result == 1){
					Toast.makeText(ActivityEditProfile.this, "上传头像成功", Toast.LENGTH_SHORT).show();
				}
				else{
					Toast.makeText(ActivityEditProfile.this, "上传头像失败", Toast.LENGTH_SHORT).show();
				}
			}
		}
	}
}
