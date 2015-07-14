package dbConnection;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utils.CommentDB;
import utils.CommentModel;
import utils.DBConnectionFactory;
import daoimpl.CommentDaoImpl;
import daointerface.CommentDao;
import entity.CommentPub;

@javax.jws.WebService(targetNamespace = "http://dbConnection/", serviceName = "WSLoadcommentService", portName = "WSLoadcommentPort", wsdlLocation = "WEB-INF/wsdl/WSLoadcommentService.wsdl")
public class WSLoadcommentDelegate {

	dbConnection.WSLoadcomment wSLoadcomment = new dbConnection.WSLoadcomment();

	public List<CommentPub> loadComment(CommentPub comm) {
		return wSLoadcomment.loadComment(comm);
	}

}