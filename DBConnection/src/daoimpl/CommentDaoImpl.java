package daoimpl;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import sun.misc.BASE64Encoder;
import daointerface.CommentDao;
import entity.CommentPub;

public class CommentDaoImpl implements CommentDao{
	
	private static final String AVATAR_FOLDER = "avatar\\";

	/*
	 * (non-Javadoc)
	 * @see daointerface.CommentDao#comment(java.sql.Connection, entity.Comment)
	 * 0--fail
	 * 1--success
	 */
	/**
	 * @param conn the database connection object
	 * @param comm the comm object which contain the comment information
	 * @return return the dirty bit imply whether the comment operation is successful or not
	 */
	@Override
	public int comment(Connection conn, CommentPub comm) throws SQLException {
		// TODO Auto-generated method stub
		PreparedStatement prepared = conn.prepareStatement("INSERT INTO comment " +
				"(com_pub_id,com_location,com_time,com_content,com_user_id,com_user_name,com_user_avatar) " +
				"VALUES (?,?,?,?,?,?,?)");
		prepared.setInt(1, comm.getCom_pub_id());
		prepared.setString(2, comm.getCom_location());
		prepared.setString(3, comm.getCom_time());
		prepared.setString(4, comm.getCom_content());
		prepared.setInt(5, comm.getCom_user_id());
		prepared.setString(6, comm.getCom_user_name());
		prepared.setString(7, comm.getCom_user_avatar());
		prepared.execute();
		if(prepared.getUpdateCount() != -1){
			//which means the insertion is successful
			return 1;
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see daointerface.CommentDao#loadComment(java.sql.Connection, entity.Comment)
	 * 通过消息的id来获得该消息的所有评论
	 */
	/**
	 * @param conn the database connection object
	 * @param comm the comm object which contain the comment information
	 * @return return all the comment objects
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	@Override
	public List<CommentPub> loadComment(Connection conn, CommentPub comm)
			throws SQLException, FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		List<CommentPub> comments = new ArrayList<CommentPub>();
		PreparedStatement prepared = conn.prepareStatement("SELECT * FROM comment " +
				"WHERE com_pub_id=?");
		prepared.setInt(1,comm.getCom_pub_id());
		
		if(prepared.execute()){
			ResultSet result = prepared.getResultSet();
			while(result.next()){
				//在这里将com_user_avatar设置为base64编码的用户头像
				CommentPub comment = new CommentPub();
				comment.setCom_id(result.getInt("com_id"));
				comment.setCom_pub_id(result.getInt("com_pub_id"));
				comment.setCom_location(result.getString("com_location"));
				comment.setCom_time(result.getString("com_time"));
				comment.setCom_content(result.getString("com_content"));
				comment.setCom_user_id(result.getInt("com_user_id"));
				comment.setCom_user_name(result.getString("com_user_name"));
				comment.setCom_user_avatar(result.getString("com_user_avatar"));
				String avatar_path = ".\\" + AVATAR_FOLDER + result.getString("com_user_avatar");
				comment.setCom_user_avatar_encoding(encodingAvatar(avatar_path));
				comments.add(comment);
			}
			return comments;
		}
		return null;
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



}
