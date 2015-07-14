package daointerface;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import entity.Profile;

public interface ProfileDao {

	public Profile verifyUser(Connection conn,Profile profile) throws SQLException;
	public int[] registerUser(Connection conn,Profile profile) throws SQLException,IOException;
	public int tagInsert(Connection conn,Profile profile) throws SQLException;
	public int edit(Connection conn,Profile profile) throws SQLException;
	public String downloadAvatar(Connection conn,Profile profile) throws SQLException,FileNotFoundException,IOException;
	public int uploadAvatar(Connection conn,Profile profile) throws SQLException,FileNotFoundException,IOException;
	public int editTag(Connection conn,Profile profile) throws SQLException;
}
