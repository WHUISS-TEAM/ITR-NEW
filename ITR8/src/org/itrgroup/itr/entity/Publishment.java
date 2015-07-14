package org.itrgroup.itr.entity;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class Publishment implements KvmSerializable {
	/*
	 * in the publishment tabel
	 */
	private int pub_id;
	private int pub_user_id;
	private String pub_user_name;
	private String pub_user_avatar;
	private String pub_time;
	private String pub_location;
	private String pub_content;
	private int pub_tag_level_1;
	
	
	
	public int getPub_id() {
		return pub_id;
	}
	public void setPub_id(int pub_id) {
		this.pub_id = pub_id;
	}
	public int getPub_user_id() {
		return pub_user_id;
	}
	public void setPub_user_id(int pub_user_id) {
		this.pub_user_id = pub_user_id;
	}
	public String getPub_user_name() {
		return pub_user_name;
	}
	public void setPub_user_name(String pub_user_name) {
		this.pub_user_name = pub_user_name;
	}
	public String getPub_user_avatar() {
		return pub_user_avatar;
	}
	public void setPub_user_avatar(String pub_user_avatar) {
		this.pub_user_avatar = pub_user_avatar;
	}
	public String getPub_time() {
		return pub_time;
	}
	public void setPub_time(String pub_time) {
		this.pub_time = pub_time;
	}
	public String getPub_location() {
		return pub_location;
	}
	public void setPub_location(String pub_location) {
		this.pub_location = pub_location;
	}
	public String getPub_content() {
		return pub_content;
	}
	public void setPub_content(String pub_content) {
		this.pub_content = pub_content;
	}
	public int getPub_tag_level_1() {
		return pub_tag_level_1;
	}
	public void setPub_tag_level_1(int pub_tag_level_1) {
		this.pub_tag_level_1 = pub_tag_level_1;
	}

	@Override
	public Object getProperty(int arg0) {
		// TODO Auto-generated method stub
		Object obj = null;
		switch(arg0){
		case 0:
			obj = this.pub_id;
			break;
		case 1:
			obj = this.pub_user_id;
			break;
		case 2:
			obj = this.pub_user_name;
			break;
		case 3:
			obj = this.pub_user_avatar;
			break;
		case 4:
			obj = this.pub_time;
			break;
		case 5:
			obj = this.pub_location;
			break;
		case 6:
			obj = this.pub_content;
			break;
		case 7:
			obj = this.pub_tag_level_1;
			break;
		default:
			break;
		}
		return obj;
	}
	@Override
	public int getPropertyCount() {
		// TODO Auto-generated method stub
		return 8;
	}
	@Override
	public void getPropertyInfo(int arg0, Hashtable arg1, PropertyInfo arg2) {
		// TODO Auto-generated method stub
		switch (arg0) {
		case 0:
			arg2.name = "pub_id";
			arg2.type = PropertyInfo.INTEGER_CLASS;
			break;
		case 1:
			arg2.name = "pub_user_id";
			arg2.type = PropertyInfo.INTEGER_CLASS;
			break;
		case 2:
			arg2.name = "pub_user_name";
			arg2.type = PropertyInfo.STRING_CLASS;
			break;
		case 3:
			arg2.name = "pub_user_avatar";
			arg2.type = PropertyInfo.STRING_CLASS;
			break;
		case 4:
			arg2.name = "pub_time";
			arg2.type = PropertyInfo.STRING_CLASS;
			break;
		case 5:
			arg2.name = "pub_location";
			arg2.type = PropertyInfo.STRING_CLASS;
			break;
		case 6:
			arg2.name = "pub_content";
			arg2.type = PropertyInfo.STRING_CLASS;
			break;
		case 7:
			arg2.name = "pub_tag_level_1";
			arg2.type = PropertyInfo.INTEGER_CLASS;
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
			this.pub_id = Integer.parseInt(arg1.toString());
			break;
		case 1:
			this.pub_user_id = Integer.parseInt(arg1.toString());
			break;
		case 2:
			this.pub_user_name = arg1.toString();
			break;
		case 3:
			this.pub_user_avatar = arg1.toString();
			break;
		case 4:
			this.pub_time = arg1.toString();
			break;
		case 5:
			this.pub_location = arg1.toString();
			break;
		case 6:
			this.pub_content = arg1.toString();
			break;
		case 7:
			this.pub_tag_level_1 = Integer.parseInt(arg1.toString());
			break;
		default:
			break;
		}
	}
}
