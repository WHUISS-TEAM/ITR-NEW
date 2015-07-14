package dbConnection;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.sun.xml.internal.ws.util.ByteArrayBuffer;
import daoimpl.ProfileDaoImpl;
import entity.Profile;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import utils.DBConnectionFactory;
import utils.DBConnection_Config;

@javax.jws.WebService(targetNamespace = "http://dbConnection/", serviceName = "WSDownloadAvatarService", portName = "WSDownloadAvatarPort", wsdlLocation = "WEB-INF/wsdl/WSDownloadAvatarService.wsdl")
public class WSDownloadAvatarDelegate {

	dbConnection.WSDownloadAvatar wSDownloadAvatar = new dbConnection.WSDownloadAvatar();

	public String downloadAvatar(Profile profile) {
		return wSDownloadAvatar.downloadAvatar(profile);
	}

}