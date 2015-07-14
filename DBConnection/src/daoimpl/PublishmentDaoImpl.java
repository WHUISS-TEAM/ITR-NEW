package daoimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import daointerface.PublishmentDao;
import entity.Publishment;

public class PublishmentDaoImpl implements PublishmentDao {

	/*
	 * (non-Javadoc)
	 * @see daointerface.PublishmentDao#publish(java.sql.Connection, entity.Publishment)
	 * 0--fail
	 * 1--success
	 */
	@Override
	public int[] publish(Connection conn, Publishment pub) throws SQLException {
		// TODO Auto-generated method stub
		//first insert the pub into the publishment table
		PreparedStatement prepared = conn.prepareStatement("INSERT INTO publishment " +
				"(pub_user_id,pub_user_name,pub_user_avatar,pub_time,pub_location,pub_content," +
				"pub_tag_level_1,pub_tag_level_2) VALUES (?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
		prepared.setInt(1,pub.getPub_user_id());
		prepared.setString(2,pub.getPub_user_name());
		prepared.setString(3, pub.getPub_user_avatar());
		prepared.setString(4,pub.getPub_time());
		prepared.setString(5, pub.getPub_location());
		prepared.setString(6, pub.getPub_content());
		prepared.setInt(7,pub.getPub_tag_level_1());
		prepared.setString(8, pub.getPub_tag_level_2());
		prepared.execute();
		if(prepared.getUpdateCount() != -1){
			//which means the insertion success
			//then insert the corresponding record into the share table
			ResultSet key = prepared.getGeneratedKeys();
			if(key.next()){
				int pub_id = key.getInt(1);
				return new int[]{1,pub_id};
			}else{
				return new int[]{0};
			}
		}else{
			return new int[]{0};
		}
	}

}
