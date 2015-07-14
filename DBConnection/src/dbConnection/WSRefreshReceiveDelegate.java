package dbConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import utils.DBConnection_Config;
import utils.PublishmentDB;
import utils.ReceiveModel;

@javax.jws.WebService(targetNamespace = "http://dbConnection/", serviceName = "WSRefreshReceiveService", portName = "WSRefreshReceivePort", wsdlLocation = "WEB-INF/wsdl/WSRefreshReceiveService.wsdl")
public class WSRefreshReceiveDelegate {

	dbConnection.WSRefreshReceive wSRefreshReceive = new dbConnection.WSRefreshReceive();

	public List<ReceiveModel> Refresh(String email, int mode) {
		return wSRefreshReceive.Refresh(email, mode);
	}

}