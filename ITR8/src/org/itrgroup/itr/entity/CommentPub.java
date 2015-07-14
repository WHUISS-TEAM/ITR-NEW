package org.itrgroup.itr.entity;

import java.io.Serializable;
import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class CommentPub implements KvmSerializable,Serializable{

	private int com_id;//这条评论的ID
	private int com_pub_id;//消息发布者的ID
	private String com_location;
	private String com_time;
	private String com_content;
	private int com_user_id;//评论者ID
	private String com_user_name;//评论者名字
	private String com_user_avatar;//。。。头像
	private String com_user_avatar_encoding;
	
	
	public CommentPub() {
		super();
	}
	public CommentPub(int com_id, int com_pub_id, String com_location,
			String com_time, String com_content, int com_user_id,
			String com_user_name, String com_user_avatar) {
		super();
		this.com_id = com_id;
		this.com_pub_id = com_pub_id;
		this.com_location = com_location;
		this.com_time = com_time;
		this.com_content = com_content;
		this.com_user_id = com_user_id;
		this.com_user_name = com_user_name;
		this.com_user_avatar = com_user_avatar;
	}
	public int getCom_id() {
		return com_id;
	}
	public void setCom_id(int com_id) {
		this.com_id = com_id;
	}
	public int getCom_pub_id() {
		return com_pub_id;
	}
	public void setCom_pub_id(int com_pub_id) {
		this.com_pub_id = com_pub_id;
	}
	public String getCom_location() {
		return com_location;
	}
	public void setCom_location(String com_location) {
		this.com_location = com_location;
	}
	public String getCom_time() {
		return com_time;
	}
	public void setCom_time(String com_time) {
		this.com_time = com_time;
	}
	public String getCom_content() {
		return com_content;
	}
	public void setCom_content(String com_content) {
		this.com_content = com_content;
	}
	public int getCom_user_id() {
		return com_user_id;
	}
	public void setCom_user_id(int com_user_id) {
		this.com_user_id = com_user_id;
	}
	public String getCom_user_name() {
		return com_user_name;
	}
	public void setCom_user_name(String com_user_name) {
		this.com_user_name = com_user_name;
	}
	public String getCom_user_avatar() {
		return com_user_avatar;
	}
	public void setCom_user_avatar(String com_user_avatar) {
		this.com_user_avatar = com_user_avatar;
	}
	
	public String getCom_user_avatar_encoding() {
		return com_user_avatar_encoding;
	}
	public void setCom_user_avatar_encoding(String com_user_avatar_encoding) {
		this.com_user_avatar_encoding = com_user_avatar_encoding;
	}
	@Override
	public Object getProperty(int arg0) {
		// TODO Auto-generated method stub
		Object obj = null;
		switch(arg0){
		case 0:
			obj = this.com_id;
			break;
		case 1:
			obj = this.com_pub_id;
			break;
		case 2:
			obj = this.com_location;
			break;
		case 3:
			obj = this.com_time;
			break;
		case 4:
			obj = this.com_content;
			break;
		case 5:
			obj = this.com_user_id;
			break;
		case 6:
			obj = this.com_user_name;
			break;
		case 7:
			obj = this.com_user_avatar;
			break;
		case 8:
			obj = this.com_user_avatar_encoding;
		default:
			break;
		}
		return obj;
	}
	@Override
	public int getPropertyCount() {
		// TODO Auto-generated method stub
		return 9;
	}
	@Override
	public void getPropertyInfo(int arg0, Hashtable arg1, PropertyInfo arg2) {
		// TODO Auto-generated method stub
		switch (arg0) {
		case 0:
			arg2.name = "com_id";
			arg2.type = PropertyInfo.INTEGER_CLASS;
			break;
		case 1:
			arg2.name = "com_pub_id";
			arg2.type = PropertyInfo.INTEGER_CLASS;
			break;
		case 2:
			arg2.name = "com_location";
			arg2.type = PropertyInfo.STRING_CLASS;
			break;
		case 3:
			arg2.name = "com_time";
			arg2.type = PropertyInfo.STRING_CLASS;
			break;
		case 4:
			arg2.name = "com_content";
			arg2.type = PropertyInfo.STRING_CLASS;
			break;
		case 5:
			arg2.name = "com_user_id";
			arg2.type = PropertyInfo.INTEGER_CLASS;
			break;
		case 6:
			arg2.name = "com_user_name";
			arg2.type = PropertyInfo.STRING_CLASS;
			break;
		case 7:
			arg2.name = "com_user_avatar";
			arg2.type = PropertyInfo.STRING_CLASS;
			break;
		case 8:
			arg2.name = "com_user_avatar_encoding"; 
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
			this.com_id = Integer.parseInt(arg1.toString());
			break;
		case 1:
			this.com_pub_id = Integer.parseInt(arg1.toString());
			break;
		case 2:
			this.com_location = arg1.toString();
			break;
		case 3:
			this.com_time = arg1.toString();
			break;
		case 4:
			this.com_content = arg1.toString();
			break;
		case 5:
			this.com_user_id = Integer.parseInt(arg1.toString());
			break;
		case 6:
			this.com_user_name = arg1.toString();
			break;
		case 7:
			this.com_user_avatar = arg1.toString();
			break;
		case 8:
			this.com_user_avatar_encoding = arg1.toString();
			break;
		default:
			break;
		}
	}
}
