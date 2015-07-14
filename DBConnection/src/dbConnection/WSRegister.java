package dbConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.sun.crypto.provider.RSACipher;

import daoimpl.ProfileDaoImpl;
import entity.Profile;

import utils.DBConnectionFactory;
import utils.DBConnection_Config;

public class WSRegister {

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
	
	
	
	//返回三个状态
	//0--邮箱已经被使用
	//1--用户名已经被使用
	//2--注册成功
	//3--注册失败
	//要获得插入记录的id值
	//为头像的插入提供默认值
	/**
	 * 更改之后返回值没有变
	 * @param profile
	 * @return
	 */
	public int[] registerUser(Profile profile){
		DBConnectionFactory factory = DBConnectionFactory.getInstance();
		Connection conn = factory.makeConnection();
		ProfileDaoImpl impl = new ProfileDaoImpl();
		
		try {
			conn.setAutoCommit(false);
			int[] response = impl.registerUser(conn, profile);
			conn.commit();
			return response;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally{
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
//		getConnection();
////		userAvatar = "'" + userAvatar + "'";
//		userName = "'" + userName + "'";
//		password = "'" + password + "'";
//		email = "'" + email + "'";
//		String insert_sql = "INSERT " + TABLE_NAME + "(userName,password,email)" 
//		+ "VALUES (" + userName + "," + password + "," + email + ");";
//		String query_sql_email = "SELECT " + "email " + "FROM " + TABLE_NAME 
//				+ " WHERE email = " + email;
//		String query_sql_name = "SELECT " + "username " + "FROM " + TABLE_NAME
//				+ " WHERE username = " + userName;
//		try { 
//			ResultSet result1 = statement.executeQuery(query_sql_email);
//			if(result1 != null&&result1.next()){
//				//邮箱已经被使用
//				return new int[]{0};
//			}
//			ResultSet result2 = statement.executeQuery(query_sql_name);
//			if(result2 != null&&result2.next()){
//				//用户名已被注册
//				return new int[]{1};
//			}
//			if(statement.executeUpdate(insert_sql,Statement.RETURN_GENERATED_KEYS)!=0){
//				ResultSet rs = statement.getGeneratedKeys();
//				rs.next();
//				return new int[]{2,rs.getInt(1)};
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return new int[]{3};
	}
	
	//0--插入出错
	//1--插入成功
	//传入的第一个值永远是对应的profile_id
	/**
	 * 更改之后返回值没有变
	 * @param profile
	 * @return
	 */
	public int tagInsert(Profile profile){
		DBConnectionFactory factory = DBConnectionFactory.getInstance();
		Connection conn = factory.makeConnection();
		ProfileDaoImpl impl = new ProfileDaoImpl();
		try {
			conn.setAutoCommit(false);
			int result = impl.tagInsert(conn, profile);
			conn.commit();
			return result;
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
		return 0;
	}
//		if(statement==null||connection==null){
//			getConnection();
//		}
//		String pre_sql = "INSERT " + USER_LINK_NAME + "(profile_id,user_tag_id) VALUES ";
//		int profile_id = tags[0];
//		String post_sql = "";
//		for(int i=1;i<tags.length;i++){
//			post_sql = "(" + profile_id + "," + tags[i] + ");";
//			try {
//				int check = statement.executeUpdate(pre_sql + post_sql);
//				if(check == 0) return 0;
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				return 0;
//			}
//		}
//		return 1;
	}
//	
//	
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
//}
