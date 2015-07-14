package dbConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import utils.DBConnection_Config;
import utils.PublishmentDB;

public class WSCreateTable {

	private Connection connection = null;
	private Statement statement = null;
	//连接数据库需要的信息，用户名和密码和端口要修改
	private static final String PRE_DB_URL = DBConnection_Config.PRE_DB_URL;
	private static final String POST_DB_URL = DBConnection_Config.POST_DB_URL;
	private static final String USER = DBConnection_Config.USER;
	private static final String PASSWORD = DBConnection_Config.PASSWORD;
//	private static final String TABLE_NAME = DBConnection_Config.TABLE_NAME;
	private static final String DB_NAME = DBConnection_Config.DB_NAME;
	
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
		//由于所有发布的消息全部都要加到publishment中，所以在这里只需要为用户创建属于他的接受表和他的发布表
		//这两张表都是以索引的形式实现，所以这里将不需要创建复杂的用户的pub表
		//还要为每个用户创建favoritism的表和group的表
		//Pub_inforId--每一条消息的id
		//Pub_userName--消息发布人的id（其实就是表名）
		//Pub_userHead--发布人头像（我感觉直接到profile中获取比较好，要不然每一条记录存取的都是同样的东西，上同）
		//Inf_time--发布时间
		//Inf_loc--发布的位置
		//Inf_content--发布的内容
		//Label_level1--一级标签（数字表示）
		//Label_level2--二级标签
		//Vote_num--投票数
		//Com_num--反对数
		//Share_num--转发数
		public void createTables(String email){
			int result = 0;
			if(statement==null||connection==null){
				getConnection();
			}
			//去掉.com
			email = email.substring(0, email.length() - 4);
			//去掉@
			email = email.replace("@", "");
			//创建received表
			String received_table_name = email + DBConnection_Config.RECEIVE_TABLE_NAME_SUF;
			String receive = "CREATE TABLE " + received_table_name + " (" 
			+ "infoReceived_id int(20) unsigned NOT NULL AUTO_INCREMENT,"
			+ "infor_id int(10) unsigned NOT NULL,"
			+ "isReceived tinyint(4) unsigned NOT NULL,"
			+ "PRIMARY KEY (infoReceived_id),"
			+ "KEY infor_id (infor_id),"
			+ "CONSTRAINT " + received_table_name + " FOREIGN KEY (infor_id) REFERENCES publishment (pub_inforId) ON DELETE CASCADE ON UPDATE CASCADE"
			+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";

			//创建用户自己发布的表pubment
			//PUBMENT表同样使用外键，不过没有isreceived这个选项
			String pubment_table_name = email + DBConnection_Config.PUBMENT_TABLE_NAME_SUF;
			//String pre_sql = "DROP TABLE IF EXISTS "+ userName +";";
			String pubment = "CREATE TABLE " + pubment_table_name + " ("
					+ "infoPubment_id int(20) unsigned NOT NULL AUTO_INCREMENT,"
					+ "infor_id int(10) unsigned NOT NULL,"
					+ "PRIMARY KEY (infoPubment_id),"
					+ "KEY infor_id (infor_id),"
					+ "CONSTRAINT " + pubment_table_name + " FOREIGN KEY (infor_id) REFERENCES publishment (pub_inforId) ON DELETE CASCADE ON UPDATE CASCADE"
					+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
			
			//创建favoritism的表
			String favoritism_table_name = email + DBConnection_Config.FAVORITISM_TABLE_NAME_SUF;
			String favoritism = "CREATE TABLE " + favoritism_table_name + " ("
					+ "favor_id int(20) unsigned NOT NULL AUTO_INCREMENT,"
					+ "inforId int(10) unsigned NOT NULL,"
					+ "Isfavored smallint(10) NOT NULL,"
					+ "PRIMARY KEY (favor_id),"
					+ "KEY Pub_Info (inforId) USING BTREE,"
					+ "CONSTRAINT " + favoritism_table_name + " FOREIGN KEY (inforId) REFERENCES publishment (pub_inforId) ON DELETE CASCADE ON UPDATE CASCADE"
					+") ENGINE=InnoDB CHARSET=utf8 ROW_FORMAT=COMPACT;";
			
			//创建groups表
			/*
			 * 现阶段先不创建好友之类的表
			 */
			String groups_table_name = email + "_groups";
			String groups = "CREATE TABLE " + groups_table_name+ " ("
					+ "group_id smallint(20) unsigned NOT NULL AUTO_INCREMENT,"
					+ "group_name varchar(255) NOT NULL,"
					+ "PRIMARY KEY (group_id)"
					+ ")ENGINE=InnoDB DEFAULT CHARSET=utf8;";
					
			//使用result可以根据数字用来判断表是否建立成功，目前没有什么用处
			try {
				//statement.executeUpdate(pre_sql);
				statement.executeUpdate(receive);
				result++;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				statement.executeUpdate(pubment);
				result++;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				statement.executeUpdate(favoritism);
				result++;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
}
