package dbConnection;

import java.io.IOException;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.sun.xml.rpc.processor.modeler.j2ee.xml.string;
import daoimpl.ProfileDaoImpl;
import entity.Profile;
import utils.DBConnectionFactory;
import utils.DBConnection_Config;

@javax.jws.WebService(targetNamespace = "http://dbConnection/", serviceName = "WSLoginService", portName = "WSLoginPort", wsdlLocation = "WEB-INF/wsdl/WSLoginService.wsdl")
public class WSLoginDelegate {

	dbConnection.WSLogin wSLogin = new dbConnection.WSLogin();

	public Profile verifyUser(Profile profile) {
		return wSLogin.verifyUser(profile);
	}

}