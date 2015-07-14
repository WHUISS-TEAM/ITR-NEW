package dbConnection;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import utils.CommentDB;
import utils.CommentModel;
import utils.DBConnectionFactory;
import daoimpl.CommentDaoImpl;
import daointerface.CommentDao;
import entity.CommentPub;

public class WSLoadcomment {
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
//	private static final String PUBLISHMENT_TABLE_NAME = DBConnection_Config.PUBLISHMENT_TABLE_NAME;
//	private static final String COMMENT_TABLE_NAME = DBConnection_Config.COMMENT_TABLE_NAME;
	
//	private void getConnection(){
//		try {
//			Class.forName("com.mysql.jdbc.Driver");
//			connection = DriverManager.getConnection(PRE_DB_URL + DB_NAME + POST_DB_URL
//					, USER, PASSWORD);
//			statement = connection.createStatement();
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}
	
	//通过传递消息的id进来获得消息id对应的所有的评论
	//包括评论和回复
	public List<CommentPub> loadComment(CommentPub comm){
		System.out.println("in load");
		DBConnectionFactory factory = DBConnectionFactory.getInstance();
		Connection conn = factory.makeConnection();
		CommentDao impl = new CommentDaoImpl();
		try{
			return impl.loadComment(conn, comm);
		}catch (SQLException e){
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		String comment_sql = "SELECT * FROM " + COMMENT_TABLE_NAME 
//				+ " WHERE pub_inforId=" + pub_inforId + " ORDER BY com_time ASC;";
//		List<CommentModel> results = new ArrayList<CommentModel>();
//		try {
//			ResultSet result_set = statement.executeQuery(comment_sql);
//			if(result_set.first()){
//				for(;!result_set.isAfterLast();result_set.next()){
//					//从resultset中取得数据
//					int com_inforId = result_set.getInt(CommentDB.COM_INFORID_INT);
//					int com_userId = result_set.getInt(CommentDB.COM_USERID_INT);
//					String com_loc = result_set.getString(CommentDB.COM_LOC_STRING);
//					String com_time = result_set.getString(CommentDB.COM_TIME_STRING);
//					String com_content = result_set.getString(CommentDB.COM_CONTENT_STRING);
//					int is_directed = result_set.getInt(CommentDB.IS_DIRECTED_INT);
//					int replied_userId = result_set.getInt(CommentDB.REPLIED_USERID_INT);
//					String replied_userName = result_set.getString(CommentDB.REPLIED_USERNAME_STRING);
//					String com_userName = result_set.getString(CommentDB.COM_USERNAME_STRING);
//					String com_userAvatar = result_set.getString(CommentDB.COM_USERAVATAR_STRING);
//					results.add(new CommentModel(com_inforId,com_userId, com_loc, com_time, pub_inforId, com_content, is_directed, replied_userId, replied_userName, com_userName, com_userAvatar));
//				}
//				result_set.close();
//			}
//			return results;
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return null;
	}
}
