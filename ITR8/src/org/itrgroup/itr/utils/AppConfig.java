package org.itrgroup.itr.utils;

public class AppConfig {

	/*
	 *����msg.what�ĺ���
	 *0x100 ���� ���͵�¼ ����                        	(LoginActivity - Thread_Login) 
	 *0x101 ���� ��ȡͷ�� ����                        	(LoginActivity - Thread_DownloadAvatar) 
	 *0x102 ���� ����Received���� 		(LoginActivity - Thread_LoadReceived) 
	 *0x103 ���� ����ע������ 			(RegisterActivity - Thread_Register) 
	 *0x104 ���� ���Ͳ����ǩ������ 		(RegisterActivity - Thread_Register) 
  	 *0x105 ���� �ϴ�ͷ�񵽷����������� 	(EditProfileActivity  - Thread_EditProfile) 
	 *0x106 ���� �ϴ��������ϵ����� 		(EditProfileActivity  - Thread_EditProfile) 
	 *0x107 ���� �޸ı�ǩ������ 			(EditProfileActivity  - Thread_EditProfile) 
	 *0x108 ���� �������۵����� 			(ActivitySingleContent - Thread_Comment) 
	 *0x109 ���� �������۵����� 			(ActivitySingleContent - Thrad_LoadComment) 
	 *0x110 ���� ������Ϣ������ 			(ArcMenuPublish  - Thread_Pub) 
	 */
	
	//����ĳ�����ÿһ��Ҫ������Ϣ��TextView�ı���
	//Constants for row two
	public static String SCHOOL_TITLE = "ѧУԺϵ��";
	public static String MAJOR_TITLE = "רҵ��";
	public static String CLASS_TITLE = "�༶��";
	public static String DORM_TITLE = "���᣺";
	public static String YEAR_TITLE = "��ѧ��ݣ�";
	
	//Constants for row three
	public static String SEX_TITLE = "�Ա�";
	public static String AGE_TITLE = "���䣺";
	
	//Constants for WebService
	public static String WebService_IP = "http://192.168.81.129:8080/";
	public static String[] pub_tag_level1 = {"����","����","����","ѧϰ","��Ƹ"};
	public static String[] user_tag_items = {"�˶�","����","����","��Ӱ","��Ϸ","�Ķ�"};
	
	//Constants for local database
//	public static String DATABASE_PATH = "/data/data/org.itrgroup.itr/database";
	
	//Constants for external data
	public static String app_path = "/sdcard/itr/";
	public static String data_path = "/sdcard/itr/data/";
	public static String avatar_path = "/sdcard/itr/data/avatar/";  
	/*
	 * ͷ��ı����ʽ����ַ�ǣ�AppConfig.avatar_path + profile_id + "_avatar.jpg"
	 *
	 */
}
