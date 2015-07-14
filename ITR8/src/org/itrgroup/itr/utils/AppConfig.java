package org.itrgroup.itr.utils;

public class AppConfig {

	/*
	 *各个msg.what的含义
	 *0x100 —— 发送登录 请求                        	(LoginActivity - Thread_Login) 
	 *0x101 —— 获取头像 请求                        	(LoginActivity - Thread_DownloadAvatar) 
	 *0x102 —— 加载Received请求 		(LoginActivity - Thread_LoadReceived) 
	 *0x103 —— 发送注册请求 			(RegisterActivity - Thread_Register) 
	 *0x104 —— 发送插入标签的请求 		(RegisterActivity - Thread_Register) 
  	 *0x105 —— 上传头像到服务器的请求 	(EditProfileActivity  - Thread_EditProfile) 
	 *0x106 —— 上传基本资料的请求 		(EditProfileActivity  - Thread_EditProfile) 
	 *0x107 —— 修改标签的请求 			(EditProfileActivity  - Thread_EditProfile) 
	 *0x108 —— 发送评论的请求 			(ActivitySingleContent - Thread_Comment) 
	 *0x109 —— 加载评论的请求 			(ActivitySingleContent - Thrad_LoadComment) 
	 *0x110 —— 发布信息的请求 			(ArcMenuPublish  - Thread_Pub) 
	 */
	
	//这里的常量是每一个要输入信息的TextView的标题
	//Constants for row two
	public static String SCHOOL_TITLE = "学校院系：";
	public static String MAJOR_TITLE = "专业：";
	public static String CLASS_TITLE = "班级：";
	public static String DORM_TITLE = "宿舍：";
	public static String YEAR_TITLE = "入学年份：";
	
	//Constants for row three
	public static String SEX_TITLE = "性别：";
	public static String AGE_TITLE = "年龄：";
	
	//Constants for WebService
	public static String WebService_IP = "http://192.168.81.129:8080/";
	public static String[] pub_tag_level1 = {"社团","创意","游玩","学习","招聘"};
	public static String[] user_tag_items = {"运动","音乐","旅行","摄影","游戏","阅读"};
	
	//Constants for local database
//	public static String DATABASE_PATH = "/data/data/org.itrgroup.itr/database";
	
	//Constants for external data
	public static String app_path = "/sdcard/itr/";
	public static String data_path = "/sdcard/itr/data/";
	public static String avatar_path = "/sdcard/itr/data/avatar/";  
	/*
	 * 头像的保存格式（地址是）AppConfig.avatar_path + profile_id + "_avatar.jpg"
	 *
	 */
}
