package dbConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import daoimpl.PublishmentDaoImpl;
import entity.Publishment;
import utils.DBConnectionFactory;
import utils.DBConnection_Config;

@javax.jws.WebService(targetNamespace = "http://dbConnection/", serviceName = "WSPublishService", portName = "WSPublishPort", wsdlLocation = "WEB-INF/wsdl/WSPublishService.wsdl")
public class WSPublishDelegate {

	dbConnection.WSPublish wSPublish = new dbConnection.WSPublish();

	public int[] Publish(Publishment pub) {
		return wSPublish.Publish(pub);
	}

}