package com.example.java;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import oracle.jdbc.pool.OracleDataSource;
import oracle.jdbc.OracleConnection;
import java.sql.DatabaseMetaData;



@Controller
@SpringBootApplication
public class JavaApplication {
     // The recommended format of a connection URL is the long format with the
    // connection descriptor.
    final static String DB_URL= "jdbc:oracle:thin:@tcps://adb.eu-frankfurt-1.oraclecloud.com:1522/gf58dd1428e9980_tpt_tp.adb.oraclecloud.com?wallet_location=Wallet_tpt&oracle.net.ssl_server_cert_dn=\"CN=adwc.eucom-central-1.oraclecloud.com,OU=Oracle BMCS FRANKFURT,O=Oracle Corporation,L=Redwood City,ST=California,C=US\"";
    // For ATP and ADW - use the TNS Alias name along with the TNS_ADMIN when using 18.3 JDBC driver
    // final static String DB_URL="jdbc:oracle:thin:@wallet_dbname?TNS_ADMIN=/Users/test/wallet_dbname";
    // In case of windows, use the following URL
    // final static String DB_URL="jdbc:oracle:thin:@wallet_dbname?TNS_ADMIN=C:\\Users\\test\\wallet_dbname";
    final static String DB_USER = "ADMIN";
    final static String DB_PASSWORD = "WTFtennisman1";

        @RequestMapping("/")
        @ResponseBody
        String home(){
            return "x";
        }
        
        @RequestMapping("/hello")
        @ResponseBody
        String hello(){
            return "Heelo man";
        }
        
        @RequestMapping("/getteam")
        @ResponseBody
        String getTeam() throws SQLException{
             Properties info = new Properties();
        info.put(OracleConnection.CONNECTION_PROPERTY_USER_NAME, DB_USER);
        info.put(OracleConnection.CONNECTION_PROPERTY_PASSWORD, DB_PASSWORD);
        info.put(OracleConnection.CONNECTION_PROPERTY_DEFAULT_ROW_PREFETCH, "20");


        OracleDataSource ods = new OracleDataSource();
        ods.setURL(DB_URL);
        ods.setConnectionProperties(info);
        
           OracleConnection connection = (OracleConnection) ods.getConnection(); 
            // Get the JDBC driver name and version
            DatabaseMetaData dbmd = connection.getMetaData();
            System.out.println("Driver Name: " + dbmd.getDriverName());
            System.out.println("Driver Version: " + dbmd.getDriverVersion());
            // Print some connection properties
            System.out.println("Default Row Prefetch Value is: " +
                    connection.getDefaultRowPrefetch());
            System.out.println("Database Username is: " + connection.getUserName());
            System.out.println();
            
            Statement statement = connection.createStatement();
           
           ResultSet resultSet = statement.executeQuery("select * from utilisateur") ;
         String nom = "";
                while (resultSet.next())
                nom = resultSet.getString(2);

            
            return nom;
        }
	public static void main(String[] args) {
		SpringApplication.run(JavaApplication.class, args);
	}
}

