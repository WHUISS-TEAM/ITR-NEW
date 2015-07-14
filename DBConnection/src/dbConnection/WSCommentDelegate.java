package dbConnection;

import java.sql.Connection;
import java.sql.SQLException;
import utils.DBConnectionFactory;
import daoimpl.CommentDaoImpl;
import entity.CommentPub;

@javax.jws.WebService(targetNamespace = "http://dbConnection/", serviceName = "WSCommentService", portName = "WSCommentPort", wsdlLocation = "WEB-INF/wsdl/WSCommentService.wsdl")
public class WSCommentDelegate {

	dbConnection.WSComment wSComment = new dbConnection.WSComment();

	public int comment(CommentPub comment) {
		return wSComment.comment(comment);
	}

}