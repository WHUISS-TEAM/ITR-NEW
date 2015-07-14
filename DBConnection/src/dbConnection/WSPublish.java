package dbConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import daoimpl.PublishmentDaoImpl;
import entity.Publishment;

import utils.DBConnectionFactory;
import utils.DBConnection_Config;

public class WSPublish {

//	private Connection connection = null;
//	private Statement statement = null;
//	//连接数据库需要的信息，用户名和密码和端口要修改
//	private static final String PRE_DB_URL = DBConnection_Config.PRE_DB_URL;
//	private static final String POST_DB_URL = DBConnection_Config.POST_DB_URL;
//	private static final String USER = DBConnection_Config.USER;
//	private static final String PASSWORD = DBConnection_Config.PASSWORD;
////	private static final String TABLE_NAME = DBConnection_Config.TABLE_NAME;
//	private static final String DB_NAME = DBConnection_Config.DB_NAME;
//	private static final String PUBLISHMENT_TABLE_NAME = DBConnection_Config.PUBLISHMENT_TABLE_NAME;
//	
//	//此方法用来获得数据库连接
//	private void getConnection(){
//		try {
//			//这里用了MySQL的一个包里的访问数据库的驱动程序，要build path把那个包导入
//			//mysql-connection-java-....
//			Class.forName("com.mysql.jdbc.Driver");
//			connection = DriverManager.getConnection(PRE_DB_URL + DB_NAME + POST_DB_URL
//					, USER, PASSWORD);
//			//statement对象用来执行数据可的操作
//			statement = connection.createStatement();
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}catch(Exception e){
//			System.out.println("unknow exception: " + e.getMessage());
//		}
//
//	}
	
	//以下是必须要填写的项目
	//userName--消息发布人的id（其实就是表名）
	//Inf_time--发布时间
	//Inf_loc--发布的位置
	//Inf_content--发布的内容
	//Label_level1--一级标签，数字
	//Label_level2--二级标签
	//Vote_num--投票数,数字
	//Com_num--反对数，数字
	//Share_num--转发数，数字
	
	//0--插入失败
	//1--插入成功
	
	//要传递email进去来创建表
	public int[] Publish(Publishment pub){
//		getConnection();
		DBConnectionFactory factory = DBConnectionFactory.getInstance();
		Connection conn = factory.makeConnection();
		PublishmentDaoImpl imp = new PublishmentDaoImpl();
		try {
			conn.setAutoCommit(false);
			int[] response = imp.publish(conn, pub);
			conn.commit();
			return response;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}finally{
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
//		//去掉.com
//		email = email.substring(0, email.length() - 4);
//		//去掉@
//		email = email.replace("@", "");
//		
//		String received_table_name = email + DBConnection_Config.RECEIVE_TABLE_NAME_SUF;
//		String pubment_table_name = email + DBConnection_Config.PUBMENT_TABLE_NAME_SUF;
//		
//		String sql_receive_exist = "SHOW TABLES LIKE '" + received_table_name + "'";
//		String sql_pub_exist = "SHOW TABLES LIKE '" + pubment_table_name + "'";
//		
//		String sql_publishment = "INSERT " + PUBLISHMENT_TABLE_NAME + " (pub_userId,pub_userName,pub_userAvatar,pub_time,pub_loc,pub_content,pub_tag_level1,"
//				+ "pub_tag_level2,vote_num,com_num,share_num) " + "VALUES (" + userId + ",'" + userName + "',"+ "'" + user_head + "'," + "'"
//				+ Inf_time + "'," + "'" + Inf_loc + "'," + "'" + Inf_content + "'," + Label_lavel1 + ","
//				+ "'" + Label_level2 + "'," + vote + "," + com + "," + share + ");";
//
//		try {
//			ResultSet received_set = statement.executeQuery(sql_receive_exist);
//			//对应用户接受数据的数据表不存在，执行建表操作
//			if(received_set == null||!received_set.next()){
//				String received = "CREATE TABLE " + received_table_name + " (" 
//						+ "infoReceived_id int(20) unsigned NOT NULL AUTO_INCREMENT,"
//						+ "infor_id int(10) unsigned NOT NULL,"
//						+ "isReceived smallint(10) unsigned NOT NULL,"
//						+ "PRIMARY KEY (infoReceived_id),"
//						+ "KEY infor_id (infor_id),"
//						+ "CONSTRAINT " + received_table_name + " FOREIGN KEY (infor_id) REFERENCES publishment (pub_inforId) ON DELETE CASCADE ON UPDATE CASCADE"
//						+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
//				statement.executeUpdate(received);
//			}
//			received_set.close();
//			ResultSet pubment_set = statement.executeQuery(sql_pub_exist);
//			//对应的属于用户自己发布的表不存在，执行建表操作
//			if(pubment_set == null||!pubment_set.next()){
//				String pubment = "CREATE TABLE " + pubment_table_name + " ("
//						+ "infoPubment_id int(20) unsigned NOT NULL AUTO_INCREMENT,"
//						+ "infor_id int(10) unsigned NOT NULL,"
//						+ "PRIMARY KEY (infoPubment_id),"
//						+ "KEY infor_id (infor_id),"
//						+ "CONSTRAINT " + pubment_table_name + " FOREIGN KEY (infor_id) REFERENCES publishment (pub_inforId) ON DELETE CASCADE ON UPDATE CASCADE"
//						+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
//				statement.executeQuery(pubment);
//			}
//			pubment_set.close();
//			//确保了表属于用户的表已经存在，可以开始执行插入记录的操作
//			//插入的顺序应该是先插入到publishment中，在插入到received里面和pubment里面
//			//因为在执行这个插入的时候又会自动插入到本地的数据库并加载出来，所以这里received表中的isreceived要设为true
//			//插入到publishment后要获得pubinfoid来进行之后的插入操作
//			//要返回主键
//			int result = statement.executeUpdate(sql_publishment,Statement.RETURN_GENERATED_KEYS);
//			if(result != 0){
//				ResultSet r = statement.getGeneratedKeys();
//				r.next();
//				//publishment插入成功，返回主键
//				int pub_inforId = r.getInt(1);
//				String sql_received = "INSERT " + received_table_name + " (infor_id,isReceived) "
//						+ "VALUES (" + pub_inforId + ",1);";
//				
//				String sql_pubment = "INSERT " + pubment_table_name + " (infor_id) "
//						+ "VALUES (" + pub_inforId + ");";
//				r.close();
//				//从而可以执行received和pubment的插入操作
//				int receive_re = statement.executeUpdate(sql_received);
//				//插入成功,执行pubment的操作
//				if(receive_re != 0){
//					int pubment_re = statement.executeUpdate(sql_pubment);
//					//所有插入操作完成，返回1
//					if(pubment_re != 0){
//						return new int[]{1,pub_inforId};
//					}
//				}
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return new int[]{0};
//		}
		return new int[]{0};
	}
}
