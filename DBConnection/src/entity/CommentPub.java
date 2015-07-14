package entity;

import java.io.Serializable;

public class CommentPub{

	private int com_id;
	private int com_pub_id;
	private String com_location;
	private String com_time;
	private String com_content;
	private int com_user_id;
	private String com_user_name;
	private String com_user_avatar;
	private String com_user_avatar_encoding;
	
	public String getCom_user_avatar_encoding() {
		return com_user_avatar_encoding;
	}
	public void setCom_user_avatar_encoding(String com_user_avatar_encoding) {
		this.com_user_avatar_encoding = com_user_avatar_encoding;
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
	
	
}
