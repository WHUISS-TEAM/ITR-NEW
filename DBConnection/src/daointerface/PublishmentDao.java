package daointerface;

import java.sql.Connection;
import java.sql.SQLException;

import entity.Publishment;

public interface PublishmentDao {

	public int[] publish(Connection conn,Publishment pub) throws SQLException;
}
