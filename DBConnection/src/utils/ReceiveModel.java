package utils;

import java.io.Serializable;

public class ReceiveModel implements Serializable{

	public int pub_inforId;
	public String pub_userName;
	public String pub_userAvatar;
	public String pub_time;
	public String pub_loc;
	public String pub_content;
	public int pub_tag_level1;
	public String pub_tag_level2;
	public int vote_num;
	public int com_num;
	public int share_num;
	public int user_id;
	
	
	
	public ReceiveModel() {
		// TODO Auto-generated constructor stub
	}
	public ReceiveModel(int pub_inforId,String pub_userName, String pub_userHead,
			String pub_time, String pub_loc, String pub_content,
			int pub_tag_level1, String pub_tag_level2, int vote_num,
			int com_num, int share_num,int user_id) {
		this.pub_inforId = pub_inforId;
		this.pub_userName = pub_userName;
		this.pub_userAvatar = pub_userHead;
		this.pub_time = pub_time;
		this.pub_loc = pub_loc;
		this.pub_content = pub_content;
		this.pub_tag_level1 = pub_tag_level1;
		this.pub_tag_level2 = pub_tag_level2;
		this.vote_num = vote_num;
		this.com_num = com_num;
		this.share_num = share_num;
		this.user_id = user_id;
	}
	
	
}
