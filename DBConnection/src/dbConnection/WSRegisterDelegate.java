package dbConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.sun.crypto.provider.RSACipher;
import daoimpl.ProfileDaoImpl;
import entity.Profile;
import utils.DBConnectionFactory;
import utils.DBConnection_Config;

@javax.jws.WebService(targetNamespace = "http://dbConnection/", serviceName = "WSRegisterService", portName = "WSRegisterPort", wsdlLocation = "WEB-INF/wsdl/WSRegisterService.wsdl")
public class WSRegisterDelegate {

	dbConnection.WSRegister wSRegister = new dbConnection.WSRegister();

	public int[] registerUser(Profile profile) {
		return wSRegister.registerUser(profile);
	}

	public int tagInsert(Profile profile) {
		return wSRegister.tagInsert(profile);
	}

}