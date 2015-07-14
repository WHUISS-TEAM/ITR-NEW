package dbConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import utils.DBConnection_Config;
import utils.PublishmentDB;

@javax.jws.WebService(targetNamespace = "http://dbConnection/", serviceName = "WSCreateTableService", portName = "WSCreateTablePort", wsdlLocation = "WEB-INF/wsdl/WSCreateTableService.wsdl")
public class WSCreateTableDelegate {

	dbConnection.WSCreateTable wSCreateTable = new dbConnection.WSCreateTable();

	public void createTables(String email) {
		wSCreateTable.createTables(email);
	}

}