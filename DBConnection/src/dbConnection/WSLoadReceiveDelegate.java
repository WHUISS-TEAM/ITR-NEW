package dbConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import utils.DBConnection_Config;
import utils.ReceiveModel;
import utils.PublishmentDB;

@javax.jws.WebService(targetNamespace = "http://dbConnection/", serviceName = "WSLoadReceiveService", portName = "WSLoadReceivePort", wsdlLocation = "WEB-INF/wsdl/WSLoadReceiveService.wsdl")
public class WSLoadReceiveDelegate {

	dbConnection.WSLoadReceive wSLoadReceive = new dbConnection.WSLoadReceive();

	public List<ReceiveModel> Load(String email) {
		return wSLoadReceive.Load(email);
	}

}