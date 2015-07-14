package utils;

public class CommentModel {

	public int com_inforId;
	public int com_userId;
	public String com_loc;
	public String com_time;
	public int pub_inforId;
	public String com_content;
	public int is_directed;
	public int replied_userId;
	public String replied_userName;
	public String com_userName;
	public String com_userAvatar;
	
	public CommentModel(){
		
	}
	
	public CommentModel(int com_inforId,int com_userId, String com_loc, String com_time,
			int pub_inforId, String com_content, int is_directed,
			int replied_userId, String replied_userName, String com_userName,
			String com_userAvatar) {
		super();
		this.com_inforId = com_inforId;
		this.com_userId = com_userId;
		this.com_loc = com_loc;
		this.com_time = com_time;
		this.pub_inforId = pub_inforId;
		this.com_content = com_content;
		this.is_directed = is_directed;
		this.replied_userId = replied_userId;
		this.replied_userName = replied_userName;
		this.com_userName = com_userName;
		this.com_userAvatar = com_userAvatar;
	}
	
	
}
