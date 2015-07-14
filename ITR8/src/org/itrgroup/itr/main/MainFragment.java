package org.itrgroup.itr.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import org.itrgroup.itr.R;
import org.itrgroup.itr.adapter.MainContentAdapter;
import org.itrgroup.itr.arcbutton.ArcMenu;
import org.itrgroup.itr.entity.CommentPub;
import org.itrgroup.itr.model.MainContentModel;
import org.itrgroup.itr.utils.AppConfig;
import org.itrgroup.itr.utils.IniDatabaseHelper;
import org.itrgroup.itr.utils.LocalDB;
import org.itrgroup.itr.utils.RefreshLayout;
import org.itrgroup.itr.utils.SingleExecutorService;
import org.itrgroup.itr.utils.RefreshLayout.OnLoadListener;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;



public class MainFragment extends Fragment{
	
	//Sub-button of the Circle button
	private static final int[] ITEM_DRAWABLES = { R.drawable.button_publish, R.drawable.button_contacts,
		R.drawable.button_database, R.drawable.button_setting };
	private List<MainContentModel> contentList = null;
	private ListView pubContentList = null;
	private MainContentAdapter mContentAdapter = null;
	private String email;
	private String db_name;
	private SharedPreferences sp;
	private String[] level1_tag = {"社团","创意","游玩","学习","招聘"};
	
	private RefreshLayout refresh_layout = null;
	
	//连接数据库的线程的
	private static final String SERVICE_URL = AppConfig.WebService_IP + "DBConnection"
			+"/WSRefreshReceivePort";
	private static final String SERVICE_NS = "http://dbConnection/";

	private ExecutorService mExecutorService = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		sp = getActivity().getSharedPreferences("userInfo", getActivity().MODE_WORLD_READABLE);
		email = sp.getString("LOGIN_EMAIL", "");
		db_name = email + ".db3";

		contentList = new ArrayList<MainContentModel>();
		//在此之后启动线程，因为这之后list才实例化
		Thread_GetReceive thread = new Thread_GetReceive();
		
		mExecutorService = SingleExecutorService.getInstance();
		mExecutorService.execute(thread);
		//thread.start();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container, false);

		//rootView.findViewById is avaliable on fragment
		//get arcMenu from layout file
		ArcMenu arcMenu = (ArcMenu)rootView.findViewById(R.id.arc_menu);
		//Initial arcMenu
		((MainActivity)getActivity()).initArcMenu(arcMenu, ITEM_DRAWABLES);
		
		//ListView contains all the published contents of main view
		refresh_layout = (RefreshLayout)rootView.findViewById(R.id.refresh_layout);
		pubContentList = (ListView)rootView.findViewById(R.id.pubContentList);
		//An adapter to handle this messages
		/*
		 * 往maincontentadapter里面传递一个commentpub对象，方便执行comment操作
		 */
		int com_user_id = sp.getInt("PROFILE_ID", 0);
		String com_user_name = sp.getString("REGIST_USERNAME", "");
		String user_email = sp.getString("LOGIN_EMAIL", "");
		CommentPub temp_comment = new CommentPub();
		temp_comment.setCom_user_id(com_user_id);
		temp_comment.setCom_user_name(com_user_name);
		mContentAdapter=new MainContentAdapter(getActivity(),contentList,temp_comment,user_email);
		pubContentList.setAdapter(mContentAdapter);
		
		refresh_layout.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				new RefreshTask().execute(email);
			}
		});
		refresh_layout.setOnLoadListener(new OnLoadListener() {
			
			@Override
			public void onLoad() {
				// TODO Auto-generated method stub
				new LoadMoreTask().execute(email);
			}
		});
		
		refresh_layout.setColorSchemeResources(android.R.color.holo_blue_light,android.R.color.holo_green_light
				,android.R.color.holo_orange_light,android.R.color.holo_purple);
		
		return rootView;
	}
	
	//fragment不同于activity，在暂停和停止状态后再次可见都会回调onstart方法
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Bundle bundle = ((MainActivity)getActivity()).getBundle();
		if(bundle!=null){
			MainContentModel new_msg = (MainContentModel) bundle.getSerializable("new_msg");
			contentList.add(0, new_msg);
			((MainActivity)getActivity()).setBundle(null);
			//动态更新
			mContentAdapter.notifyDataSetChanged();
			//应该加到list里面就会自动更新显示，不需要重复设置adapter
			//如果在你手机上有问题在解除掉注释
//			mContentAdapter=new MainContentAdapter(getActivity(),contentList);
//			pubContentList.setAdapter(mContentAdapter);
		}
	}
	
	//不能在非UI线程中修改界面，所以setAdapter的方法不能放在这里
	//这个是在加载界面的时候从数据库中加载信息
	private class Thread_GetReceive extends Thread{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			String sql_query = "select * from Received ORDER BY " + LocalDB.INF_TIME_STRING
						+ " DESC";
			SQLiteDatabase db = 
					new IniDatabaseHelper(getActivity(), db_name, null, 1).getReadableDatabase();
			Cursor cursor = db.rawQuery(sql_query, null);
			for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
				int index = cursor.getColumnIndex(LocalDB.PUB_USERNAME_STRING);
				String userName = cursor.getString(index);
				index = cursor.getColumnIndex(LocalDB.PUB_INFORID_INT);
				int pub_inforId = cursor.getInt(index);
				index = cursor.getColumnIndex(LocalDB.PUB_USERID_INT);
				int user_id = cursor.getInt(index);
				index = cursor.getColumnIndex(LocalDB.INF_TIME_STRING);
				String pubTime = cursor.getString(index);
				int position = pubTime.lastIndexOf(":");
				pubTime = pubTime.substring(0, position);
				index = cursor.getColumnIndex(LocalDB.PUB_TAG_LEVEL1_INT);
				String fir_tag = level1_tag[cursor.getInt(index)-1];
				//index = cursor.getColumnIndex(LocalDB.PUB_TAG_LEVEL2_STRING);
				//String sec_tag = cursor.getString(index);
				index = cursor.getColumnIndex(LocalDB.INF_CONTENT_STRING);
				String pubContent = cursor.getString(index);
				//头像先假定一个，他是以string类型存储的
				index = cursor.getColumnIndex(LocalDB.PUB_USERHEAD_STRING);
				String avatar_Name = cursor.getString(index);
				index = cursor.getColumnIndex(LocalDB.SHARE_NUM_INT);
				int share_num = cursor.getInt(index);
				index = cursor.getColumnIndex(LocalDB.COM_NUM_INT);
				int comment_num = cursor.getInt(index);
				index = cursor.getColumnIndex(LocalDB.VOTE_NUM_INT);
				int vote_num = cursor.getInt(index);
				contentList.add(new MainContentModel(pub_inforId,userName, pubTime, fir_tag, pubContent, avatar_Name, share_num, comment_num, vote_num,user_id));
			}
				
			cursor.close();
			db.close();
				
		}
	}

	//这个是刷新的异步任务
	private class RefreshTask extends AsyncTask<String, Void, List<MainContentModel>>{
	
		@Override
		protected List<MainContentModel> doInBackground(String... params) {
			// TODO Auto-generated method stub
			//传递email进来
			//这里已经是新线程了，不需要启动新线程了
			//但是在这个方法里面不能试图改变界面，只能在onPost中改变界面布局
			List<MainContentModel> refresh_list = refresh(params[0],MODE_REFRESH);
	
			return refresh_list;
		}
		
		@Override
		protected void onPostExecute(List<MainContentModel> result) {
			// TODO Auto-generated method stub
			refresh_layout.setRefreshing(false);
			for(MainContentModel item:result){
				contentList.add(0, item);
				//看效果考虑是放在外面还是里面
				mContentAdapter.notifyDataSetChanged();
			}
			super.onPostExecute(result);
		}
	}

	//这个是loadmore的异步任务
	private class LoadMoreTask extends AsyncTask<String, Void, List<MainContentModel>>{
	
		@Override
		protected List<MainContentModel> doInBackground(String... params) {
			// TODO Auto-generated method stub
			List<MainContentModel> loadmore_list = refresh(params[0],MODE_LOADMORE);
			return loadmore_list;
		}
		
		@Override
		protected void onPostExecute(List<MainContentModel> result) {
			// TODO Auto-generated method stub
			refresh_layout.setLoading(false);
			for(MainContentModel item:result){
				contentList.add(contentList.size(), item);
				mContentAdapter.notifyDataSetChanged();
			}
			super.onPostExecute(result);
			
		}
	}
	
	
	//0--中途出错
	//1--操作成功
	private static final int MODE_REFRESH = 0;
	private static final int MODE_LOADMORE = 2;
	
	private List<MainContentModel> refresh(String email,int mode){
		SQLiteDatabase database = 
				new IniDatabaseHelper(getActivity(), db_name, null, 1).getReadableDatabase();
		List<MainContentModel> refresh_list = new ArrayList<MainContentModel>();
		final HttpTransportSE transport = new HttpTransportSE(SERVICE_URL);
		final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		
		SoapObject object = new SoapObject(SERVICE_NS, "Refresh");
		object.addProperty("arg0", email);
		object.addProperty("arg1", mode);
		envelope.bodyOut = object;
		
		try {
			transport.call(null, envelope);
			if(envelope.getResponse() != null){
				SoapObject response = (SoapObject) envelope.bodyIn;
				//开始解析结果,要将结果插入到数据库，同时要将结果加入到list中，使之在界面上显示
				for(int i=0;i<response.getPropertyCount();i++){
					SoapObject temp_obj = (SoapObject) response.getProperty(i);
					
					System.out.println(temp_obj);
					//所有的标签以服务端的receivemodel为准
					int pub_inforId = Integer.parseInt(temp_obj.getPropertyAsString("pub_inforId"));
					String pub_userName = temp_obj.getPropertyAsString("pub_userName");
					String pub_userAvatar = temp_obj.getPropertyAsString("pub_userAvatar");
					//数据库存的时间
					String pub_time = temp_obj.getPropertyAsString("pub_time");
					int position = pub_time.lastIndexOf(":");
					//消息显示的时间
					String model_pub_time = pub_time.substring(0, position);
					
					String pub_loc = temp_obj.getPropertyAsString("pub_loc");
					String pub_content = temp_obj.getPropertyAsString("pub_content");
					int pub_tag_level1 = Integer.parseInt(temp_obj.getPropertyAsString("pub_tag_level1"));
					//String pub_tag_level2 = temp_obj.getPropertyAsString("pub_tag_level2");
					String user_avatar = temp_obj.getPropertyAsString("user_avatar");
					int vote_num = Integer.parseInt(temp_obj.getPropertyAsString("vote_num"));
					int com_num = Integer.parseInt(temp_obj.getPropertyAsString("com_num"));
					int share_num = Integer.parseInt(temp_obj.getPropertyAsString("share_num"));
					int user_id = Integer.parseInt(temp_obj.getPropertyAsString("user_id"));
					//头像还是假定的
					//加入到界面中的过程就完成了
					refresh_list.add(0, new MainContentModel(pub_inforId,pub_userName, model_pub_time, level1_tag[pub_tag_level1-1], pub_content, user_avatar, share_num, com_num, vote_num,user_id));
					//然后是加入到数据库的过程
					ContentValues values = new ContentValues();
					values.put(LocalDB.PUB_INFORID_INT, pub_inforId);
					values.put(LocalDB.PUB_USERNAME_STRING, pub_userName);
					values.put(LocalDB.PUB_USERHEAD_STRING, pub_userAvatar);
					values.put(LocalDB.PUB_USERID_INT, user_id);
					values.put(LocalDB.INF_TIME_STRING, pub_time);
					values.put(LocalDB.INF_LOC_STRING, pub_loc);
					values.put(LocalDB.INF_CONTENT_STRING, pub_content);
					values.put(LocalDB.PUB_TAG_LEVEL1_INT, pub_tag_level1);
					values.put(LocalDB.VOTE_NUM_INT, vote_num);
					values.put(LocalDB.COM_NUM_INT, com_num);
					values.put(LocalDB.SHARE_NUM_INT, share_num);
					values.put(LocalDB.PUB_USERID_INT, user_id);
					if(database.insert("Received", null, values) == -1){
						return null;
					}
				}
				
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
		return refresh_list;
	}
		
	
}
