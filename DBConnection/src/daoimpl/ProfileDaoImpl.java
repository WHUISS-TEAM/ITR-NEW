package daoimpl;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import daointerface.ProfileDao;
import entity.Profile;

public class ProfileDaoImpl implements ProfileDao{
	private static final String AVATAR_FOLDER = "avatar\\";
	private static final String SUF = "jpeg";
	private static final String defaultAvatar = ".\\avatar\\default_avatar.jpeg";
	/**
	 * @param conn the database connection object
	 * @param profile the profile object which contains the login information
	 * @return return the profile which contains the user information if login successful
	 */
	@Override
	public Profile verifyUser(Connection conn, Profile profile)
			throws SQLException {
		// TODO Auto-generated method stub
		PreparedStatement prepared = conn.prepareStatement("SELECT * FROM " +
				"profile WHERE user_email=?");
		System.out.println(profile.getUser_email());
		System.out.println(profile.getUser_password());
		prepared.setString(1, profile.getUser_email());
		if(prepared.execute()){
			ResultSet result = prepared.getResultSet();
			if(result.next()){
				if(result.getString("user_password").equals(profile.getUser_password())){
					Profile user_profile = new Profile();
					user_profile.setUser_id(result.getInt("user_id"));
					user_profile.setUser_name(result.getString("user_name"));
					user_profile.setUser_avatar(result.getString("user_avatar"));
					user_profile.setUser_age(result.getInt("user_age"));
					user_profile.setUser_gender(result.getInt("user_gender"));
					user_profile.setUser_school(result.getString("user_school"));
					user_profile.setUser_major(result.getString("user_major"));
					user_profile.setUser_introduction(result.getString("user_introduction"));
					user_profile.setUser_academy(result.getString("user_academy"));
					result.close();
					prepared = conn.prepareStatement("SELECT * FROM " +
							" user_tag_link WHERE user_id=?");
					prepared.setInt(1,user_profile.getUser_id());
					if(prepared.execute()){
						ResultSet result_tag = prepared.getResultSet();
						String user_tag = "";
						while(result_tag.next()){
							user_tag += "#" + result_tag.getInt("user_tag_id");
						}
						user_profile.setUser_tag(user_tag);
						result_tag.close();
					}
					return user_profile;
				}else{
					return null;
				}
			}else{
				return null;
			}
		}else{
			return null;
		}
		
	}

	/*
	 * divide the register process into two parts
	 * the first is to register the user and second id to insert user tag
	 */
	
	/*
	 *返回三个状态
	 *0--邮箱已经被使用
	 *1--用户名已经被使用
	 *2--注册成功，并且返回主键
	 *3--注册失败
	 */
	/**
	 * @param conn the database connection object
	 * @param profile the profile object which contain the login information
	 * @return return the dirty bit imply whether the insertion operation is successful or not
	 * @throws IOException 
	 */
	@Override
	public int[] registerUser(Connection conn, Profile profile)
			throws SQLException, IOException {
		// TODO Auto-generated method stub
		PreparedStatement prepared = conn.prepareStatement("SELECT user_email FROM profile " +
				"WHERE user_email=?");
		prepared.setString(1, profile.getUser_email());
		prepared.execute();
		ResultSet result = prepared.getResultSet();
		if(result.next()){
			return new int[]{0};
		}else{
			prepared = conn.prepareStatement("SELECT user_name FROM profile " +
					" WHERE user_name=?");
			prepared.setString(1, profile.getUser_name());
			prepared.execute();
			ResultSet result_name = prepared.getResultSet();
			if(result_name.next()){
				return new int[]{1};
			}else{
				prepared = conn.prepareStatement("INSERT INTO profile (user_name,user_password,user_email) " +
						"VALUES (?,?,?)",Statement.RETURN_GENERATED_KEYS);
				prepared.setString(1, profile.getUser_name());
				prepared.setString(2, profile.getUser_password());
				prepared.setString(3, profile.getUser_email());
				prepared.execute();
				if(prepared.getUpdateCount() != -1){
					//which means the insertion is finished successfully
					ResultSet key = prepared.getGeneratedKeys();
					key.next();
					int user_id = key.getInt(1);
					String user_avatar = user_id + "_avatar" + "." + SUF;
					prepared = conn.prepareStatement("UPDATE profile SET user_avatar=? WHERE user_id=?");
					prepared.setString(1, user_avatar);
					prepared.setInt(2,user_id);
					prepared.execute();
					if(prepared.getUpdateCount() != -1){
						//which means the insertion of user_avatar succeed
						String userAvatarPath = ".\\" + AVATAR_FOLDER + user_avatar;
						if(initAvatar(defaultAvatar, userAvatarPath)){
							return new int[]{2,user_id};
						}else{
							return new int[]{3};
						}
					}else{
						return new int[]{3};
					}
				}else{
					return new int[]{3};
				}
			}
		}
	}
	
	/**
	 * @param conn the database connection object
	 * @param profile the profile object which contain the login information
	 * @return return the dirty bit imply whether the insertion operation is successful or not
	 */
	/*
	 * (non-Javadoc)
	 * @see daointerface.ProfileDao#tagInsert(java.sql.Connection, entity.Profile)
	 * 0--fail
	 * 1--success
	 */
	@Override
	public int tagInsert(Connection conn, Profile profile) throws SQLException {
		// TODO Auto-generated method stub
		/*
		 * 传递过来的int值的组织形式是#..#..#..
		 * user_id就存储了user_id
		 * 需要进行解析
		 */
		PreparedStatement prepared = conn.prepareStatement("INSERT INTO user_tag_link (user_id,user_tag_id) " +
				"VALUES (?,?)");
		String[] string_tags = profile.getUser_tag().split("#");
		List<Integer> tag_int = new ArrayList<Integer>();
		for(int i=1;i<string_tags.length;i++){
			tag_int.add(Integer.parseInt(string_tags[i].toString()));
		}
		System.out.println(profile.getUser_id());
		System.out.println(profile.getUser_tag());
		prepared.setInt(1, profile.getUser_id());
		for(int i=0;i<tag_int.size();i++){
			prepared.setInt(2, tag_int.get(i));
			prepared.execute();
			if(prepared.getUpdateCount() == -1){
				return 0;
			}
		}
		return 1;
	}
	/**
	 * @param conn the database connection object
	 * @param profile the profile object which contain the login information
	 * @return return the profile which contain the user information if login successful
	 */
	/*
	 * (non-Javadoc)
	 * @see daointerface.ProfileDao#edit(java.sql.Connection, entity.Profile)
	 * 0--fail
	 * 1--success
	 */
	@Override
	public int edit(Connection conn, Profile profile) throws SQLException {
		// TODO Auto-generated method stub
		PreparedStatement prepared = conn.prepareStatement("UPDATE profile SET " +
				"user_name=?, user_school=?, user_academy=?, user_major=?, user_introduction=?, " +
				"user_gender=?, user_age=? WHERE user_id=?");
		prepared.setString(1, profile.getUser_name());
		prepared.setString(2, profile.getUser_school());
		prepared.setString(3, profile.getUser_academy());
		prepared.setString(4, profile.getUser_major());
		prepared.setString(5, profile.getUser_introduction());
		prepared.setInt(6, profile.getUser_gender());
		prepared.setInt(7, profile.getUser_age());
		prepared.setInt(8, profile.getUser_id());
		prepared.execute();
		if(prepared.getUpdateCount() != -1){
			//which means the operation is success
			return 1;
		}else{
			return 0;	
		}
	}

	/**
	 * @param conn the database connection object
	 * @param profile the profile object which contain the login information
	 * @return return the base64 encoding of the avatar image
	 */
	/*
	 * (non-Javadoc)
	 * @see daointerface.ProfileDao#downloadAvatar(java.sql.Connection, entity.Profile)
	 * return the base64 encoding of the avatar image
	 */
	@Override
	public String downloadAvatar(Connection conn, Profile profile)
			throws SQLException, FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		PreparedStatement prepared = conn.prepareStatement("SELECT user_avatar FROM profile " +
				"WHERE user_id=?");
		prepared.setInt(1, profile.getUser_id());
		if(prepared.execute()){
			ResultSet result = prepared.getResultSet();
			if(result.next()){
				String avatar_path = ".\\" + AVATAR_FOLDER + result.getString(1);
				String avatar = encodingAvatar(avatar_path);
				result.close();
				return avatar;
			}else{
				return null;
			}
		}
		return null;
	}

	@Override
	public int uploadAvatar(Connection conn, Profile profile)
			throws SQLException,FileNotFoundException,IOException {
		// TODO Auto-generated method stub
		String path = ".\\" + AVATAR_FOLDER;
		File directory = new File(path);
		if(!directory.exists()){
			directory.mkdirs();
		}
		//去掉了.com
		String user_id = profile.getUser_id()+"";
		//文件的名称
		String image_name = user_id+ "_avatar" + "." + SUF;
		//创建了文件
		File image_file = new File(directory, image_name);
		FileOutputStream output = new FileOutputStream(image_file);
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] image_byte = decoder.decodeBuffer(profile.getUser_avatar());
		output.write(image_byte);
		output.close();
		//finish the uploading of the avatar 
		//then do the update of the database
		StringBuilder relative_path = new StringBuilder(image_file.getPath());
		relative_path = relative_path.insert(relative_path.indexOf(AVATAR_FOLDER), "\\");
		relative_path = relative_path.insert(relative_path.indexOf(AVATAR_FOLDER)+AVATAR_FOLDER.length(),"\\");
		return updateProfile(conn,profile,image_name);
	}
	
	private int updateProfile(Connection conn,Profile profile,String image_path) throws SQLException{
		PreparedStatement preparedStatement = conn.prepareStatement("UPDATE profile " +
				"SET user_avatar=? WHERE user_id=?");
		preparedStatement.setString(1,image_path);
		preparedStatement.setInt(2, profile.getUser_id());
		preparedStatement.execute();
		if(preparedStatement.getUpdateCount() != -1){
			return 1;
		}else{
			return 0;
		}
	}

	@Override
	public int editTag(Connection conn, Profile profile) throws SQLException {
		// TODO Auto-generated method stub
		//profile user_id存储在user_id中
		PreparedStatement preparedStatement = conn.prepareStatement("DELETE FROM " +
				"user_tag_link WHERE user_id=?");
		preparedStatement.setInt(1, profile.getUser_id());
		preparedStatement.execute();
		if(preparedStatement.getUpdateCount() != -1){
			//delete succeed
			String[] string_tags = profile.getUser_tag().split("#");
			List<Integer> list_tags = new ArrayList<Integer>();
			for(int i=1;i<string_tags.length;i++){
				list_tags.add(Integer.parseInt(string_tags[i]));
			}
			preparedStatement = conn.prepareStatement("INSERT INTO user_tag_link " +
					"(user_id,user_tag_id) VALUES (?,?)");
			preparedStatement.setInt(1, profile.getUser_id());
			for(int i=0;i<list_tags.size();i++){
				preparedStatement.setInt(2, list_tags.get(i));
				preparedStatement.execute();
				if(preparedStatement.getUpdateCount() == -1){	
					return 0;
				}
			}
			return 1;
		}else{
			return 0;
		}
	}
	
	//解析avatar的方法
		private String encodingAvatar(String avatar_path)
				throws FileNotFoundException, IOException{
			BufferedInputStream buffered = new BufferedInputStream(new FileInputStream(avatar_path));
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			byte[] temp = new byte[1024];
			while(buffered.read(temp) >= 0){
				output.write(temp);
			}
			BASE64Encoder encoder = new BASE64Encoder();
			String avatar = encoder.encode(output.toByteArray());
			output.close();
			buffered.close();
			return avatar;
		}
		
		private boolean initAvatar(String defaultAvatar,String userAvatarPath) throws IOException{
			File inAvatar = new File(defaultAvatar);
			if(!inAvatar.exists()){
				System.out.println("file do not exist!");
			}else{
				FileChannel inChannel = new FileInputStream(inAvatar).getChannel();
				FileChannel outChannel = new FileOutputStream(new File(userAvatarPath)).getChannel();
				inChannel.transferTo(0,inChannel.size(),outChannel);
				inChannel.close();
				outChannel.close();
				return true;
			}
			return false;
		}

}
