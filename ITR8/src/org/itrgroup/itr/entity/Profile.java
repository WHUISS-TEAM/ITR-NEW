package org.itrgroup.itr.entity;

import java.io.Serializable;
import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class Profile implements KvmSerializable,Serializable {

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
	 * 传入的第一个值永远是对应的profile_id
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

	@Override
	public Object getProperty(int arg0) {
		// TODO Auto-generated method stub
		Object obj = null;
		switch (arg0) {
		case 0:
			obj = this.user_id;
			break;
		case 1:
			obj = this.user_name;
			break;
		case 2:
			obj = this.user_avatar;
			break;
		case 3:
			obj = this.user_password;
			break;
		case 4:
			obj = this.user_email;
			break;
		case 5:
			obj = this.user_age;
			break;
		case 6:
			obj = this.user_gender;
			break;
		case 7:
			obj = this.user_school;
			break;
		case 8:
			obj = this.user_major;
			break;
		case 9:
			obj = this.user_introduction;
			break;
		case 10:
			obj = this.user_academy;
			break;
		case 11:
			obj = this.user_tag;
			break;
		default:
			break;
		}
		return obj;
	}

	@Override
	public int getPropertyCount() {
		// TODO Auto-generated method stub
		return 12;
	}

	@Override
	public void getPropertyInfo(int arg0, Hashtable arg1, PropertyInfo arg2) {
		// TODO Auto-generated method stub
		switch (arg0) {
		case 0:
			arg2.name = "user_id";
			arg2.type = PropertyInfo.INTEGER_CLASS;
			break;
		case 1:
			arg2.name = "user_name";
			arg2.type = PropertyInfo.STRING_CLASS;
			break;
		case 2:
			arg2.name = "user_avatar";
			arg2.type = PropertyInfo.STRING_CLASS;
			break;
		case 3:
			arg2.name = "user_password";
			arg2.type = PropertyInfo.STRING_CLASS;
			break;
		case 4:
			arg2.name = "user_email";
			arg2.type = PropertyInfo.STRING_CLASS;
			break;
		case 5:
			arg2.name = "user_age";
			arg2.type = PropertyInfo.INTEGER_CLASS;
			break;
		case 6:
			arg2.name = "user_gender";
			arg2.type = PropertyInfo.INTEGER_CLASS;
			break;
		case 7:
			arg2.name = "user_school";
			arg2.type = PropertyInfo.STRING_CLASS;
			break;
		case 8:
			arg2.name = "user_major";
			arg2.type = PropertyInfo.STRING_CLASS;
			break;
		case 9:
			arg2.name = "user_introduction";
			arg2.type = PropertyInfo.STRING_CLASS;
			break;
		case 10:
			arg2.name = "user_academy";
			arg2.type = PropertyInfo.STRING_CLASS;
			break;
		case 11:
			arg2.name = "user_tag";
			arg2.type = PropertyInfo.STRING_CLASS;
			break;
		default:
			break;
		}
	}

	@Override
	public void setProperty(int arg0, Object arg1) {
		// TODO Auto-generated method stub
		switch (arg0) {
		case 0:
			this.user_id = Integer.parseInt(arg1.toString());
			break;
		case 1:
			this.user_name = arg1.toString();
			break;
		case 2:
			this.user_avatar = arg1.toString();
			break;
		case 3:
			this.user_password = arg1.toString();
			break;
		case 4:
			this.user_email = arg1.toString();
			break;
		case 5:
			this.user_age = Integer.parseInt(arg1.toString());
			break;
		case 6:
			this.user_gender = Integer.parseInt(arg1.toString());
			break;
		case 7:
			this.user_school = arg1.toString();
			break;
		case 8:
			this.user_major = arg1.toString();
			break;
		case 9:
			this.user_introduction = arg1.toString();
			break;
		case 10:
			this.user_academy = arg1.toString();
			break;
		case 11:
			this.user_tag = arg1.toString();
			break;
		default:
			break;
		}
	}
}
