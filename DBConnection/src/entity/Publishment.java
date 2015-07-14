package entity;

import java.io.Serializable;

public class Publishment implements Serializable{

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
	private String pub_tag_level_2;
	/*
	 * in the pub_user_tag_link table
	 */
//	private int user_tag_id;

	/*
	 * in the pub_share table
	 */
//	private int num_vote;
//	private int num_comment;
//	private int num_share;
	public int getPub_id() {
		return pub_id;
	}
	public void setPub_id(int pub_id) {
		this.pub_id = pub_id;
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
	public String getPub_tag_level_2() {
		return pub_tag_level_2;
	}
	public void setPub_tag_level_2(String pub_tag_level_2) {
		this.pub_tag_level_2 = pub_tag_level_2;
	}
//	public int getPub_user_tag_id() {
//		return user_tag_id;
//	}
//	public void setPub_user_tag_id(int pub_user_tag_id) {
//		this.user_tag_id = pub_user_tag_id;
//	}
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
//	public int getNum_vote() {
//		return num_vote;
//	}
//	public void setNum_vote(int num_vote) {
//		this.num_vote = num_vote;
//	}
//	public int getNum_comment() {
//		return num_comment;
//	}
//	public void setNum_comment(int num_comment) {
//		this.num_comment = num_comment;
//	}
//	public int getNum_share() {
//		return num_share;
//	}
//	public void setNum_share(int num_share) {
//		this.num_share = num_share;
//	}
	
	
}
