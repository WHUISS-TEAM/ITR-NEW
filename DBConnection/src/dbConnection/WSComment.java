package dbConnection;

import java.sql.Connection;
import java.sql.SQLException;

import utils.DBConnectionFactory;
import daoimpl.CommentDaoImpl;
import entity.CommentPub;


public class WSComment {
//
//	private Connection connection = null;
//	private Statement statement = null;
//	//连接数据库需要的信息，用户名和密码和端口要修改
//	private static final String PRE_DB_URL = DBConnection_Config.PRE_DB_URL;
//	private static final String POST_DB_URL = DBConnection_Config.POST_DB_URL;
//	private static final String USER = DBConnection_Config.USER;
//	private static final String PASSWORD = DBConnection_Config.PASSWORD;
////	private static final String TABLE_NAME = DBConnection_Config.TABLE_NAME;
//	private static final String DB_NAME = DBConnection_Config.DB_NAME;
//	//private static final String PUBLISHMENT_TABLE_NAME = DBConnection_Config.PUBLISHMENT_TABLE_NAME;
//	private static final String COMMENT_TABLE_NAME = DBConnection_Config.COMMENT_TABLE_NAME;
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
	//com开头的都是评论者相关的信息
	//reply开头的都是被回复者的信息
	//0--失败
	//1--成功
	public int comment(CommentPub comment){
		DBConnectionFactory factory = DBConnectionFactory.getInstance();
		Connection conn = factory.makeConnection();
		CommentDaoImpl impl = new CommentDaoImpl();
		try {
			return impl.comment(conn, comment);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return 0;
//		getConnection();
//		com_loc = "'" + com_loc + "'";
//		com_time = "'" + com_time + "'";
//		com_userName = "'" + com_userName + "'";
//		com_userAvatar = "'" + com_userAvatar + "'"; 
//		com_content = "'" + com_content + "'";
//		replied_userName = "'" + replied_userName + "'";
//		String comment_sql = "INSERT " + COMMENT_TABLE_NAME + " ( com_userId,com_loc,com_time,com_userName" +
//				",com_userAvatar,pub_inforId,com_content,is_directed,replied_userId,replied_userName )" + " VALUES (" +
//						com_userId + "," + com_loc + "," + com_time + "," + com_userName + "," 
//						+ com_userAvatar + "," + pub_inforId + "," + com_content + ","
//						+ is_directed + "," + replied_userId + "," + replied_userName + ");";
//		try {
//			int re = statement.executeUpdate(comment_sql);
//			if(re!=0){
//				return 1;
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return 0;
	}
}
