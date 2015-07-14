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
	
	//�����������
	private Thread_EditProfile edit_thread = null;
	private Handler edit_handler = null;
	private ExecutorService mExecutorService = null;
	
	//�ؼ�
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
		
		//��ô����Ͻ��洫��������
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
			
		//����ActionBar
		actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setLogo(R.drawable.back_icon);
		actionBar.setHomeButtonEnabled(true);
		View actionbarLayout = LayoutInflater.from(this).inflate(R.layout.pub_actionbar_view, null); 
		TextView textView = (TextView) actionbarLayout.findViewById(R.id.actionBar);
		textView.setText("�޸�����");
		actionBar.setDisplayShowCustomEnabled(true); 
		actionBar.setCustomView(actionbarLayout);
		
		//��ÿؼ�
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
		
		//��������
		edit_handler = new Edit_Handler();
		edit_thread = new Thread_EditProfile(edit_handler);
		edit_thread.setDB(new IniDatabaseHelper(ActivityEditProfile.this, email + ".db3", null, 1).getReadableDatabase());
		//edit_thread.start();
		mExecutorService = SingleExecutorService.getInstance();
		mExecutorService.execute(edit_thread);
		
		
		//��ͷ���ѱ����ڱ��أ�����������ͷ��
		Bitmap bt = BitmapFactory.decodeFile(AppConfig.avatar_path + profile_id + "_avatar.jpeg");
		if(bt!=null){
			@SuppressWarnings("deprecation")
			Drawable drawable = new BitmapDrawable(this.getResources(), bt);//ת����drawable
			avatarView.setImageDrawable(drawable);
		}else{
			/**
			 *	���SD����û������Ҫ�ӷ�����ȡͷ��ȡ������ͷ���ٱ�����SD��
			 * 
			 */
		}
		
		
		//�������������趨����
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
		
		//�����Ա�ť������
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
		
		//����ͷ��ļ�����
		avatarView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ShowDialog();
			}
		});
		
		//�����޸��û���ǩ��ť�ļ�����
		tagButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SelectInterests();
			}
		});
	}
	
	//����ActionBar�Ҳఴť
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.pub_actionbar_view, menu);
        return true;
	}
	
	//����ActionBar���Ч��
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
	
	//�޸�ͷ�񵯳����趨
	public void ShowDialog(){
		new AlertDialog.Builder(this).setTitle("ͷ����Դ").setItems(
				new String[] { "���", "����" }, new DialogInterface.OnClickListener() {
					
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
							startActivityForResult(intent2, 2);//����ForResult��
							break;
						default:
							break;
						}
					}
				}).setNegativeButton(
			     "ȡ��", null).show();
	}
	
	//����startActivityForResult���ص�����
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1:
			if (resultCode == RESULT_OK) {
				cropPhoto(data.getData());//�ü�ͼƬ
			}
			break;
		case 2:
			if (resultCode == RESULT_OK) {
				File temp = new File(Environment.getExternalStorageDirectory()
						+ "/avatar.jpg");
				cropPhoto(Uri.fromFile(temp));//�ü�ͼƬ
			}
			break;
		case 3:
			if (data != null) {
				Bundle extras = data.getExtras();
				avatar_img = extras.getParcelable("data");
				if(avatar_img!=null){
					/**
					 * �ϴ�����������
					 */
					ByteArrayOutputStream outputstream = new ByteArrayOutputStream();
					//quality�ǲ�����Ҫ�ı䣬100�ǲ��Ǿ����е��
					avatar_img.compress(CompressFormat.JPEG, 100, outputstream);
					//image����ͼƬ��base64����
					String image = Base64.encodeToString(outputstream.toByteArray(),Base64.DEFAULT);
					Bundle image_data = new Bundle();
					image_data.putString("image", image);
					image_data.putString("email", email);
					image_data.putInt("profile_id", profile_id);
					Message msg = new Message();
					msg.what = 0x105;
					msg.setData(image_data);
					edit_thread.edit_handler.sendMessage(msg);
					//�ϴ�ͼƬ���
					setPicToView(avatar_img);//������SD����
					avatarView.setImageBitmap(avatar_img);//��ImageView��ʾ����
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
	
	//ϵͳ�ṩ�Ĳü�����
	public void cropPhoto(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		 // aspectX aspectY �ǿ�ߵı���
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY �ǲü�ͼƬ���
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 3);
	}
	
	//��ͼƬ�����ڱ���
	private void setPicToView(Bitmap mBitmap) {
		 String sdStatus = Environment.getExternalStorageState();  
       if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // ���sd�Ƿ����  
              return;  
       }  
       FileOutputStream b = null;
       File file = new File(AppConfig.avatar_path);
       if(!file.exists()){
    	   file.mkdirs();// �����ļ���
       }
       String fileName = AppConfig.avatar_path + profile_id + "_avatar.jpeg"; //ͼƬ����
       try {
    	   b = new FileOutputStream(fileName);
    	   mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// ������д���ļ�
    	   
       } catch (FileNotFoundException e) {
    	   e.printStackTrace();
       } finally {
    	   try {
    		   //�ر���
    		   b.flush();
    		   b.close();
    	   } catch (IOException e) {
    		   e.printStackTrace();
    	   }
       }
	}
	
	//ѡ����Ȥ�ĸ�ѡ��
	private void SelectInterests(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("�����Щ���ݸ���Ȥ�أ����ǽ��������ѡ��������Ϣ��(������ѡ��3����ǩ )");
        builder.setMultiChoiceItems(AppConfig.user_tag_items, null,
                new DialogInterface.OnMultiChoiceClickListener() {
         @Override
         public void onClick(DialogInterface dialog, int indexSelected,
                 boolean isChecked) {
             if (isChecked) {
            	 //����һ��ѡ���ѡʱִ��
            	 if(seletedItems.size()<=4){
            		 seletedItems.add(indexSelected + 1);
            	 }else{
            		 Toast.makeText(ActivityEditProfile.this, "���ֻ��ѡ��5����ǩ : )", Toast.LENGTH_SHORT).show();
                	 ((AlertDialog) dialog).getListView().setItemChecked(indexSelected, false);
            	 }
             } else if (seletedItems.contains(indexSelected + 1)) {
            	 //����һ��ѡ�ȡ����ѡʱִ��
                 seletedItems.remove(Integer.valueOf(indexSelected + 1));
             }
         }
     }).setPositiveButton("���", new DialogInterface.OnClickListener() {
         @Override
         public void onClick(DialogInterface dialog, int id) {
        	 if(seletedItems.size()<3){
        		 Toast.makeText(ActivityEditProfile.this, "������ѡ��3����ǩ : )", Toast.LENGTH_SHORT).show();
        	 }else{
        		execute_tag();
        	 }
         }
     })
     .setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
         @Override
         public void onClick(DialogInterface dialog, int id) {
        	 seletedItems.clear();
         }
     });
        
        builder.create().show();
	}
	
	//�޸Ļ�������
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
			Toast.makeText(this, "�û�������Ϊ��", Toast.LENGTH_SHORT).show();
		}else if(username.contains(" ")){
			Toast.makeText(this, "�û������ܰ����ո�", Toast.LENGTH_SHORT).show();
		}else {
			Message msg = new Message();
			msg.what = 0x106;
			msg.setData(data);
			edit_thread.edit_handler.sendMessage(msg);
		}
	}
	
	//�޸��û���ǩ
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
					Toast.makeText(ActivityEditProfile.this, "�޸�ʧ��", Toast.LENGTH_SHORT).show();
				}
				if(msg.arg1 == 2){
					Toast.makeText(ActivityEditProfile.this, "�޸ĳɹ�", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent();
					intent.setClass(ActivityEditProfile.this, MainActivity.class);
					startActivity(intent);
					finish();
				}
			}
			if(msg.what == 0x107){
				if(msg.arg1 == 0 || msg.arg1 == 1){
					Toast.makeText(ActivityEditProfile.this, "�û���ǩ�޸�ʧ�ܣ�������ѡ��", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(ActivityEditProfile.this, "�û���ǩ�޸ĳɹ���������Ͻ���ɫ��ťȷ���޸�����", Toast.LENGTH_SHORT).show();
				}
			}
			if(msg.what == 0x105){
				Bundle result_bundle = msg.getData();
				int result = result_bundle.getInt("upload_result");
				if(result == 1){
					Toast.makeText(ActivityEditProfile.this, "�ϴ�ͷ��ɹ�", Toast.LENGTH_SHORT).show();
				}
				else{
					Toast.makeText(ActivityEditProfile.this, "�ϴ�ͷ��ʧ��", Toast.LENGTH_SHORT).show();
				}
			}
		}
	}
}
