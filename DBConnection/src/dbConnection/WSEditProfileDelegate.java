package dbConnection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.sun.xml.rpc.wsdl.document.Output;
import daoimpl.ProfileDaoImpl;
import entity.Profile;
import sun.misc.BASE64Decoder;
import utils.DBConnectionFactory;
import utils.DBConnection_Config;

@javax.jws.WebService(targetNamespace = "http://dbConnection/", serviceName = "WSEditProfileService", portName = "WSEditProfilePort", wsdlLocation = "WEB-INF/wsdl/WSEditProfileService.wsdl")
public class WSEditProfileDelegate {

	dbConnection.WSEditProfile wSEditProfile = new dbConnection.WSEditProfile();

	public int EditProfile(Profile profile) {
		return wSEditProfile.EditProfile(profile);
	}

	public void EditAvatar() {
		wSEditProfile.EditAvatar();
	}

	public int EditTag(Profile profile) {
		return wSEditProfile.EditTag(profile);
	}

	public int UploadAvatar(Profile profile) {
		return wSEditProfile.UploadAvatar(profile);
	}

}