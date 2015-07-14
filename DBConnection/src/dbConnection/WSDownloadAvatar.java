package dbConnection;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.sun.xml.internal.ws.util.ByteArrayBuffer;

import daoimpl.ProfileDaoImpl;
import entity.Profile;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import utils.DBConnectionFactory;
import utils.DBConnection_Config;

public class WSDownloadAvatar {
//
//	private Connection connection = null;
//	private Statement statement = null;
//	//连接数据库需要的信息，用户名和密码和端口要修改
//	private static final String PRE_DB_URL = DBConnection_Config.PRE_DB_URL;
//	private static final String POST_DB_URL = DBConnection_Config.POST_DB_URL;
//	private static final String USER = DBConnection_Config.USER;
//	private static final String PASSWORD = DBConnection_Config.PASSWORD;
//	private static final String DB_NAME = DBConnection_Config.DB_NAME;
//	private static final String TABLE_NAME = DBConnection_Config.PROFILE_TABLE_NAME;
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
//	
	public String downloadAvatar(Profile profile){
		DBConnectionFactory factory = DBConnectionFactory.getInstance();
		Connection conn = factory.makeConnection();
		ProfileDaoImpl impl = new ProfileDaoImpl();
		try {
			return impl.downloadAvatar(conn, profile);
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
		return null;
//		getConnection();
//		String profile_sql = "SELECT userAvatar FROM " + TABLE_NAME + " WHERE id=" 
//		+ profile_id + ";";
//		try {
//			ResultSet profile_set = statement.executeQuery(profile_sql);
//			if(profile_set != null && profile_set.next()){
//				//得到avatar路径
//				String image_path = profile_set.getString("userAvatar");
//				BufferedInputStream buffered = new BufferedInputStream(new FileInputStream(image_path));
//				ByteArrayOutputStream output = new ByteArrayOutputStream();
//				byte[] temp = new byte[1024];
//				while(buffered.read(temp) >= 0){
//					output.write(temp);
//				}
//				BASE64Encoder encoder = new BASE64Encoder();
//				String image = encoder.encode(output.toByteArray());
//				output.close();
//				buffered.close();
//				return image;
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		}
	}
}
