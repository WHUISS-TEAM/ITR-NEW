package utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnectionFactory {

	private static String driver;
	private static String url;
	private static String user;
	private static String password;
	
	private static final DBConnectionFactory factory = new DBConnectionFactory();
	private Connection conn;
	static {
		Properties properties = new Properties();
		InputStream in = DBConnectionFactory.class.getClassLoader().getResourceAsStream("utils/database.properties");
		try {
			properties.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		driver = properties.getProperty("driver");
		url = properties.getProperty("url");
		user = properties.getProperty("user");
		password = properties.getProperty("password");
	}
	
	private DBConnectionFactory(){
		
	}
	
	public static DBConnectionFactory getInstance(){
		return factory;
	}
	
	public Connection makeConnection(){
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}
}
