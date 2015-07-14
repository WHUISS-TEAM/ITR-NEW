package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Profile{

	private int user_id;
	private String user_name;
	private String user_avatar;
	private String user_password;
	private String user_email;
	private int user_age;
	private int user_gender;
	private String user_school;
	private String user_major;
	private String user_introduction;
	private String user_academy;
	/*
	 * USER_TAG 的存储方式为#...#...
	 * 传入的第一个值永远是对应的user_id
	 * 因为在taginsert中会用到,也仅仅在此用到
	 * e.g. #13#1#2
	 * 标签在client端的表示方式是integer
	 * 
	 */
	private String user_tag;
	
	
	
	public int getUser_id() {
		return user_id;
	}



	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}



	public String getUser_name() {
		return user_name;
	}



	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}



	public String getUser_avatar() {
		return user_avatar;
	}



	public void setUser_avatar(String user_avatar) {
		this.user_avatar = user_avatar;
	}



	public String getUser_password() {
		return user_password;
	}



	public void setUser_password(String user_password) {
		this.user_password = user_password;
	}



	public String getUser_email() {
		return user_email;
	}



	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}



	public int getUser_age() {
		return user_age;
	}



	public void setUser_age(int user_age) {
		this.user_age = user_age;
	}



	public int getUser_gender() {
		return user_gender;
	}



	public void setUser_gender(int user_gender) {
		this.user_gender = user_gender;
	}



	public String getUser_school() {
		return user_school;
	}



	public void setUser_school(String user_school) {
		this.user_school = user_school;
	}



	public String getUser_major() {
		return user_major;
	}



	public void setUser_major(String user_major) {
		this.user_major = user_major;
	}



	public String getUser_introduction() {
		return user_introduction;
	}



	public void setUser_introduction(String user_introduction) {
		this.user_introduction = user_introduction;
	}



	public String getUser_academy() {
		return user_academy;
	}



	public void setUser_academy(String user_academy) {
		this.user_academy = user_academy;
	}



	public String getUser_tag() {
		return user_tag;
	}



	public void setUser_tag(String user_tag) {
		this.user_tag = user_tag;
	}

	
}
