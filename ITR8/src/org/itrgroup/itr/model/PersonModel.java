package org.itrgroup.itr.model;

import android.R.integer;

public class PersonModel {

	private int profile_id;
	private String avatar;
	private String email;
	private String username;
	private String introduction;
	private String usertag;
	private String school;
	private String academy;
	private String major;
	private int gender;
	private int age;
	
	//在设置界面的account需要的信息
	public String account;
	public String phone_num;
	
	public PersonModel() {
		super();
	}

	public int getProfile_id() {
		return profile_id;
	}

	public void setProfile_id(int profile_id) {
		this.profile_id = profile_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public PersonModel(String account, String phone_num, String e_mail) {
		super();
		this.account = account;
		this.phone_num = phone_num;
		this.email = e_mail;
	}
	
	public PersonModel(int profile_id, String avatar, String name, String email, String introduction,
			String usertag, String school, String academy, String major,
			int gender, int age) {
		super();
		this.profile_id = profile_id;
		this.avatar = avatar;
		this.username = name;
		this.email = email;
		this.introduction = introduction;
		this.usertag = usertag;
		this.school = school;
		this.academy = academy;
		this.major = major;
		this.gender = gender;
		this.age = age;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getUsertag() {
		return usertag;
	}

	public void setUsertag(String usertag) {
		this.usertag = usertag;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getAcademy() {
		return academy;
	}

	public void setAcademy(String academy) {
		this.academy = academy;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPhone_num() {
		return phone_num;
	}

	public void setPhone_num(String phone_num) {
		this.phone_num = phone_num;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String e_mail) {
		this.email = e_mail;
	}

	
}
