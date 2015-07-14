package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class DBConnection_Config {
	public static final String PRE_DB_URL = "jdbc:mysql://127.0.0.1:3306/";
	public static final String POST_DB_URL = "?useUnicode=true&characterEncoding=utf-8";
	public static final String USER = "root";
	public static final String PASSWORD = "Albertlee";
	public static final String PROFILE_TABLE_NAME = "profile";
	public static final String PUBLISHMENT_TABLE_NAME = "publishment";
	public static final String RECEIVE_TABLE_NAME_SUF = "_received";
	public static final String PUBMENT_TABLE_NAME_SUF = "_pubment";
	public static final String COMMENT_TABLE_NAME = "comment";
	public static final String FAVORITISM_TABLE_NAME_SUF = "_favoritism";
	public static final String DB_NAME = "itr";
	public static final String USER_TAG_LINK_NAME = "user_tag_link";

}
