package org.itrgroup.itr.model;

import java.io.Serializable;

import org.itrgroup.itr.R;

import android.R.integer;



//使之可序列化
public class MainContentModel implements Serializable{
	public int pub_inforId;
	public String userName;
	public String pubTime;
	public String fir_tag;
	public int fir_tag_img;
	//public String sec_tag;
	public String pubContent;
	public String avatar_Name;
	public int share_num;
	public int comment_num;
	public int vote_num;
	public int user_id;   
	//tagColor_num是不同的一级标签对应的颜色号，社团对应的是1，创意对应的是2，游玩对应3，学习对应4，招聘对应5
	

	public MainContentModel(int pub_inforId,String userName, String pubTime, String fir_tag,
			String pubContent, String avatar_Name, int share_num, int comment_num, int vote_num,int user_id) {
		super();
		//this.user_id = id;
		this.pub_inforId = pub_inforId;
		this.userName = userName;
		this.pubTime = pubTime;
		this.fir_tag = fir_tag;
		//this.sec_tag = sec_tag;
		this.pubContent = pubContent;
		this.avatar_Name = avatar_Name;
		this.share_num = share_num;
		this.comment_num = comment_num;
		this.vote_num = vote_num;
		this.fir_tag_img = setFirtagimg(fir_tag);
		this.user_id = user_id;
	}
	
	public int setFirtagimg(String fir_tag){
		if(fir_tag=="社团"){
			return R.drawable.firsttag_1;
		}
		if(fir_tag=="创意"){
			return R.drawable.firsttag_2;
		}
		if(fir_tag=="游玩"){
			return R.drawable.firsttag_3;
		}
		if(fir_tag=="学习"){
			return R.drawable.firsttag_4;
		}
		if(fir_tag=="招聘"){
			return R.drawable.firsttag_5;
		}
		
		return R.drawable.firsttag_2;
	}
}
