/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classe;

import java.sql.SQLException;
import java.util.Properties;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.pool.OracleDataSource;

/**
 *
 * @author tolot
 */
public class Connexion {
     final static String DB_URL= "jdbc:oracle:thin:@tcps://adb.eu-frankfurt-1.oraclecloud.com:1522/gf58dd1428e9980_tpt_tp.adb.oraclecloud.com?wallet_location=Wallet_tpt&oracle.net.ssl_server_cert_dn=\"CN=adwc.eucom-central-1.oraclecloud.com,OU=Oracle BMCS FRANKFURT,O=Oracle Corporation,L=Redwood City,ST=California,C=US\"";
    // For ATP and ADW - use the TNS Alias name along with the TNS_ADMIN when using 18.3 JDBC driver
    // final static String DB_URL="jdbc:oracle:thin:@wallet_dbname?TNS_ADMIN=/Users/test/wallet_dbname";
    // In case of windows, use the following URL
    // final static String DB_URL="jdbc:oracle:thin:@wallet_dbname?TNS_ADMIN=C:\\Users\\test\\wallet_dbname";
    final static String DB_USER = "ADMIN";
    final static String DB_PASSWORD = "WTFtennisman1";
    
    public static OracleConnection getConnection() throws SQLException{
        Properties info = new Properties();
        info.put(OracleConnection.CONNECTION_PROPERTY_USER_NAME, DB_USER);
        info.put(OracleConnection.CONNECTION_PROPERTY_PASSWORD, DB_PASSWORD);
        info.put(OracleConnection.CONNECTION_PROPERTY_DEFAULT_ROW_PREFETCH, "20");


        OracleDataSource ods = new OracleDataSource();
        ods.setURL(DB_URL);
        ods.setConnectionProperties(info);
        
        OracleConnection connection = (OracleConnection) ods.getConnection(); 
        return connection;
    }
    
      
}
