package daointerface;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import entity.CommentPub;

public interface CommentDao {

	public int comment(Connection conn,CommentPub comm) throws SQLException;
	public List<CommentPub> loadComment(Connection conn, CommentPub comm) throws SQLException,FileNotFoundException,IOException;
	
}
