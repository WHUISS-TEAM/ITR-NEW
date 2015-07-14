package dbConnection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.sun.xml.rpc.wsdl.document.Output;

import daoimpl.ProfileDaoImpl;
import entity.Profile;

import sun.misc.BASE64Decoder;
import utils.DBConnectionFactory;
import utils.DBConnection_Config;

public class WSEditProfile {
	
//	private Connection connection = null;
//	private Statement statement = null;
//	private static final String SUF = "jpeg";

//	//连接数据库需要的信息，用户名和密码和端口要修改
//	private static final String PRE_DB_URL = DBConnection_Config.PRE_DB_URL;
//	private static final String POST_DB_URL = DBConnection_Config.POST_DB_URL;
//	private static final String USER = DBConnection_Config.USER;
//	private static final String PASSWORD = DBConnection_Config.PASSWORD;
//	private static final String DB_NAME = DBConnection_Config.DB_NAME;
//	private static final String TABLE_NAME = DBConnection_Config.PROFILE_TABLE_NAME;
//	private static final String USER_LINK_NAME = DBConnection_Config.USER_TAG_LINK_NAME;
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
//	}
	
	
	//0--插入失败
	//1--插入成功
	public int EditProfile(Profile profile){
		DBConnectionFactory factory = DBConnectionFactory.getInstance();
		Connection conn = factory.makeConnection();
		ProfileDaoImpl impl = new ProfileDaoImpl();
		try {
			return impl.edit(conn, profile);
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
//		if(statement==null||connection==null){
//			getConnection();
//		}
//		int result  = 0;
//		school = school.equals("") ? null : "'" + school+"'";
//		academy = academy.equals("") ? null : "'" + academy+"'";
//		major = major.equals("") ? null : "'" + major+"'";
//		introduction = introduction.equals("") ? null : "'" + introduction+"'";
//		
//		String insert_sql = "UPDATE " + TABLE_NAME + " SET " 
//				+ "userName = '" + userName + "', "
//				+ "school = " + school + ", "
//				+ "academy = " + academy + ", "
//				+ "major = " + major + ", "
//				+ "introduction = " + introduction + ", "
//				+ "gender = " + gender + ","
//				+ "age = " + age
//				+ " WHERE id = " + id;
//		try { 
//			result = statement.executeUpdate(insert_sql);
//			System.out.println("成功");
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return 0;
//		}
//		return result;
	}
	
	
	//之后再添加
	public void EditAvatar(){
		
	}
	/*
	 * 0--fail
	 * 1--succeed
	 */
	public int EditTag(Profile profile){
		DBConnectionFactory factory = DBConnectionFactory.getInstance();
		Connection conn = factory.makeConnection();
		ProfileDaoImpl impl = new ProfileDaoImpl();
		try {
			conn.setAutoCommit(false);
			int result = impl.editTag(conn, profile);
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
//		if(statement==null||connection==null){
//			getConnection();
//		}
//		int profile_id = tags[0];
//		String del_sql = "DELETE FROM " + USER_LINK_NAME + " WHERE profile_id = " + profile_id;
//		String pre_sql = "INSERT " + USER_LINK_NAME + "(profile_id,user_tag_id) VALUES ";
//		String post_sql = "";
//		try {
//			//删除之前储存的用户标签
//			int fir_check = statement.executeUpdate(del_sql);
//			System.out.println(fir_check+"什么鬼");
//			if(fir_check != 0){
//				System.out.println("进来了吗？");
//				for(int i=1;i<tags.length;i++){
//					post_sql = "(" + profile_id + "," + tags[i] + ");";
//					int check = statement.executeUpdate(pre_sql + post_sql);
//					if(check == 0) return 0;
//				}
//			}else {
//				return 0;
//			}
//		} catch (SQLException e) {
//			// TODO: handle exception
//			e.printStackTrace();
//			return 0;
//		}
//		return 1;
	}
	
	//传递base64编码的图片字符串
	//传递邮箱进来将email作为图片的名称
	//传递后缀进来
	//传递用户资料对应的id
	//0--失败
	//1--成功
	public int UploadAvatar(Profile profile){
	
		DBConnectionFactory factory = DBConnectionFactory.getInstance();
		Connection conn = factory.makeConnection();
		ProfileDaoImpl impl = new ProfileDaoImpl();
		try {
			return impl.uploadAvatar(conn, profile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
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
	}
//		String path = ".\\" + AVATAR_FOLDER;
//		File directory = new File(path);
//		if(!directory.exists()){
//			directory.mkdirs();
//		}
//		//去掉了.com
//		email = email.substring(0, email.length()-4) + "_avatar";
//		//文件的名称
//		String image_name = email + "." + SUF;
//		//创建了文件
//		File image_file = new File(directory, image_name);
//		
//		try {
//			FileOutputStream output = new FileOutputStream(image_file);
//			BASE64Decoder decoder = new BASE64Decoder();
//			byte[] image_byte = decoder.decodeBuffer(image);
//			output.write(image_byte);
//			output.close();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return 0;
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return 0;
//		}
//		//在完成了保存文件以后
//		//将数据库中的信息更新
//		StringBuilder relative_path = new StringBuilder(image_file.getPath());
//		relative_path = relative_path.insert(relative_path.indexOf(AVATAR_FOLDER), "\\");
//		relative_path = relative_path.insert(relative_path.indexOf(AVATAR_FOLDER)+AVATAR_FOLDER.length(),"\\");
//		int result = updateProfile(relative_path.toString(), profile_id);
//		if(result == 0){
//			try {
//				throw new Exception("update avatar fail");
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				return 0;
//			}
//		}
//		return 1;
//	}
//	
//	//传递相对地址进来
//	//传递id进来找到需要更新的记录
//	private int updateProfile(String relative_path,int profile_id){
//		if(statement==null||connection==null){
//			getConnection();
//		}
//		relative_path = "'" + relative_path + "'";
//		String profile_sql = "UPDATE " + TABLE_NAME
//				+ " SET userAvatar=" + relative_path + " WHERE id=" + profile_id + ";";
//		try {
//			int result = statement.executeUpdate(profile_sql);
//			if(result == 0){
//				return 0;
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return 0;
//		}
//		return 1;
//	}
}