package dbConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import utils.DBConnection_Config;
import utils.PublishmentDB;
import utils.ReceiveModel;

public class WSRefreshReceive {

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
	//关于isreceived设置
	//0--从来没有被加载过，属于refresh操作的对象
	//1--当前被加载了，在用户端正在被显示
	//2--之前被加载过，但是目前没有显示给用户，也没有再本地数据库中，load加载的对象
	//mode就是这三个值
	public List<ReceiveModel> Refresh(String email,int mode){
		getConnection();
		
		//去掉.com
		email = email.substring(0, email.length() - 4);
		//去掉@
		email = email.replace("@", "");
		
		List<ReceiveModel> data = new ArrayList<ReceiveModel>();
		
		String receive_table_name = email + DBConnection_Config.RECEIVE_TABLE_NAME_SUF;
		
		String sql_received = "SELECT * FROM " + receive_table_name + " WHERE isReceived=" + mode +
				" ORDER BY infoReceived_id DESC";
		try {
			ResultSet received_set = statement.executeQuery(sql_received);
			List<Integer> new_received = new ArrayList<Integer>();
			//将所有的没有加载的msg的对应的id获得到
			if(received_set.first()){
				for(;!received_set.isAfterLast();received_set.next()){
					new_received.add(received_set.getInt(received_set.findColumn("infor_id")));
				}
				received_set.close();
				for(int item:new_received){
					String sql_publishment = "SELECT * FROM " + PUBLISHMENT_TABLE_NAME + 
							" WHERE pub_inforId=" + item;
					ResultSet temp = statement.executeQuery(sql_publishment);
					
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
					
					data.add(0, new ReceiveModel(pub_inforId,pub_userName, pub_userAvatar, pub_time, pub_loc, pub_content, pub_tag_level1, pub_tag_level2, vote_num, com_num, share_num,user_id));
					temp.close();
					String sql_update = "UPDATE " + receive_table_name + 
							" SET isReceived=1 WHERE infor_id=" + item;
					int update_result = statement.executeUpdate(sql_update);
					if(update_result == 0){
						throw new Exception("bad update");
					}
				}
				return data;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
}