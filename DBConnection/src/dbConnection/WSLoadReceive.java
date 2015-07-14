package dbConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import utils.DBConnection_Config;
import utils.ReceiveModel;
import utils.PublishmentDB;

public class WSLoadReceive {

	private Connection connection = null;
	private Statement statement = null;
	//连接数据库需要的信息，用户名和密码和端口要修改
	private static final String PRE_DB_URL = DBConnection_Config.PRE_DB_URL;
	private static final String POST_DB_URL = DBConnection_Config.POST_DB_URL;
	private static final String USER = DBConnection_Config.USER;
	private static final String PASSWORD = DBConnection_Config.PASSWORD;
//	private static final String TABLE_NAME = DBConnection_Config.TABLE_NAME;
	private static final String DB_NAME = DBConnection_Config.DB_NAME;
	private static final String PUBLISHMENT_TABLE_NAME = DBConnection_Config.PUBLISHMENT_TABLE_NAME;
	
	
	//现在先设定加载到本地的数据条数为10条
	private static final int LOAD_NUM = 10;
	
	//此方法用来获得数据库连接
	private void getConnection(){
		try {
			//这里用了MySQL的一个包里的访问数据库的驱动程序，要build path把那个包导入
			//mysql-connection-java-....
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(PRE_DB_URL + DB_NAME + POST_DB_URL
					, USER, PASSWORD);
			//statement对象用来执行数据可的操作
			statement = connection.createStatement();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(Exception e){
			System.out.println("unknow exception: " + e.getMessage());
		}

	}
	
	//先从received中获得infor_id
	//然后从publishment中通过id获得对应的记录
	public List<ReceiveModel> Load(String email){
		if(statement==null||connection==null){
			getConnection();
		}
		//去掉.com
		email = email.substring(0, email.length() - 4);
		//去掉@
		email = email.replace("@", "");
		
		List<ReceiveModel> loads = new ArrayList<ReceiveModel>();
		
		String received_table_name = email + DBConnection_Config.RECEIVE_TABLE_NAME_SUF;
		//查询最多10条记录,按照主键降序排列
		String sql_received = "SELECT * FROM " + received_table_name + " ORDER BY infoReceived_id DESC LIMIT " + LOAD_NUM;
		//现在先不管isreceived，统统视作false，因为从本地数据库删除数据的机制还没有
		try {
			ResultSet results = statement.executeQuery(sql_received);
			ArrayList<Integer> infor_id = new ArrayList<Integer>();
			//避免空集操作
			if(results.first()){
				for(;!results.isAfterLast();results.next()){
					infor_id.add(results.getInt("infor_id"));
				}
				results.close();
				//10条记录不多，所以就用这中方式
				for(int item:infor_id){
					//每一个item就是一个inforid，对应一个pubinforid
					String sql_publishment = "SELECT * FROM " + PUBLISHMENT_TABLE_NAME + " WHERE pub_inforId=" + item;
					ResultSet temp = statement.executeQuery(sql_publishment);
					//temp中应该只有一条记录
					temp.first();
					int pub_inforId = temp.getInt(PublishmentDB.PUB_INFORID_INT);
					String pub_userName = temp.getString(PublishmentDB.PUB_USERNAME_STRING);
					String pub_userAvatar = temp.getString(PublishmentDB.PUB_USERHEAD_STRING);
					String pub_time = temp.getString(PublishmentDB.INF_TIME_STRING);
					String pub_loc = temp.getString(PublishmentDB.INF_LOC_STRING);
					String pub_content = temp.getString(PublishmentDB.INF_CONTENT_STRING);
					int pub_tag_level1 = temp.getInt(PublishmentDB.PUB_TAG_LEVEL1_INT);
					String pub_tag_level2 = temp.getString(PublishmentDB.PUB_TAG_LEVEL2_STRING);
					int vote_num = temp.getInt(PublishmentDB.VOTE_NUM_INT);
					int com_num = temp.getInt(PublishmentDB.COM_NUM_INT);
					int share_num = temp.getInt(PublishmentDB.SHARE_NUM_INT);
					int user_id = temp.getInt(PublishmentDB.PUB_USERID_INT);
					
					loads.add(0, new ReceiveModel(pub_inforId,pub_userName, pub_userAvatar, pub_time, pub_loc, pub_content, pub_tag_level1, pub_tag_level2, vote_num, com_num, share_num,user_id));
					temp.close();
				}
				return loads;
			}


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
