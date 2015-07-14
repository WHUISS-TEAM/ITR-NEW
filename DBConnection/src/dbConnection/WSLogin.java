package dbConnection;

import java.io.IOException;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.sun.xml.rpc.processor.modeler.j2ee.xml.string;

import daoimpl.ProfileDaoImpl;

import entity.Profile;

import utils.DBConnectionFactory;
import utils.DBConnection_Config;


public class WSLogin {

//	private Connection connection = null;
//	private Statement statement = null;
//	//连接数据库需要的信息，用户名和密码和端口要修改
//	private static final String PRE_DB_URL = DBConnection_Config.PRE_DB_URL;
//	private static final String POST_DB_URL = DBConnection_Config.POST_DB_URL;
//	private static final String USER = DBConnection_Config.USER;
//	private static final String PASSWORD = DBConnection_Config.PASSWORD;
//	private static final String TABLE_NAME = DBConnection_Config.PROFILE_TABLE_NAME;
//	private static final String DB_NAME = DBConnection_Config.DB_NAME;
//	private static final String USER_LINK_NAME = DBConnection_Config.USER_TAG_LINK_NAME;
	
	//此方法用来获得数据库连接
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
	
	//此方法用来判断用户是否输入正确的用户名和密码
	/**
	 * 登陆成功的话要返回用户信息
	 * @param email
	 * @param password
	 * @return
	 */
	public Profile verifyUser(Profile profile){
		System.out.println("in");
		DBConnectionFactory factory = DBConnectionFactory.getInstance();
		Connection conn = factory.makeConnection();
//		int result = 0;
//		int profile_id = 0;
//		int gender = 0;
//		int age = 0;
//		String avatar = "";
//		String username = "";
//		String usertag = "";
//		String introduction = null;
//		String school = null;
//		String academy = null;
//		String major = null;
//		
//		email = "'" + email + "'";
//		String sql = "SELECT * FROM " + TABLE_NAME 
//				+ " WHERE email = " + email;
		ProfileDaoImpl impl = new  ProfileDaoImpl();
		Profile temp_profile = null;
		try {
			temp_profile = impl.verifyUser(conn, profile);
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
		return temp_profile;
//		try {
//			ResultSet results = statement.executeQuery(sql);
//			if(results != null&&results.next()){
//				if(password.equals(results.getString("password"))){ result = 1; }
//				if(result ==  1){
//					profile_id = results.getInt("id");
//					username = results.getString("userName");
//					gender = results.getInt("gender");
//					age = results.getInt("age");
//					avatar = results.getString("userAvatar");
//					introduction = results.getString("introduction");
//					school = results.getString("school");
//					academy = results.getString("academy");
//					major = results.getString("major");
//				}
//			}
//			if(result == 1){
//				//这里的TABLE NEME以后也要改
//				sql =  "SELECT * FROM " + USER_LINK_NAME 
//						+ " WHERE  profile_id = " + profile_id;
//				ResultSet tag_results= statement.executeQuery(sql);
//				for(tag_results.first();!tag_results.isAfterLast();tag_results.next()){
//					usertag += tag_results.getInt("user_tag_id") + "#";
//				}
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e){
//			System.out.println("unknow exception2: " + e.getMessage());
//		}
//		
//		return new String[]{""+result, ""+profile_id, username, ""+gender, ""+age, avatar,  introduction, school,  academy, major,  usertag};
	}
	
//	//此方法用来关闭资源
//	public void closeAll(){
//		try {
//			if(!statement.isClosed()){
//				statement.close();
//			}
//			if(!connection.isClosed()){
//				connection.close();
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e){
//			System.out.println("unknow exception3: " + e.getMessage());
//		}
//	}
}

