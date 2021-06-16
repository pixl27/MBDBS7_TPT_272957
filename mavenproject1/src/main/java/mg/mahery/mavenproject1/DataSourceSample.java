/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.mahery.mavenproject1;

/**
 *
 * @author tolot
 */
/* Copyright (c) 2015, Oracle and/or its affiliates. All rights reserved.*/
/*
   DESCRIPTION    
   The code sample shows how to use the DataSource API to establish a connection
   to the Database. You can specify properties with "setConnectionProperties".
   This is the recommended way to create connections to the Database.
   Note that an instance of oracle.jdbc.pool.OracleDataSource doesn't provide
   any connection pooling. It's just a connection factory. A connection pool,
   such as Universal Connection Pool (UCP), can be configured to use an
   instance of oracle.jdbc.pool.OracleDataSource to create connections and 
   then cache them.
    
    Step 1: Enter the Database details in this file. 
            DB_USER, DB_PASSWORD and DB_URL are required
    Step 2: Run the sample with "ant DataSourceSample"
  
   NOTES
    Use JDK 1.7 and above
   MODIFIED    (MM/DD/YY)
    nbsundar    02/17/15 - Creation 
 */


import com.google.gson.JsonObject;
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
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class DataSourceSample {
    // The recommended format of a connection URL is the long format with the
    // connection descriptor.
    final static String DB_URL= "jdbc:oracle:thin:@tcps://adb.eu-frankfurt-1.oraclecloud.com:1522/gf58dd1428e9980_tpt_tp.adb.oraclecloud.com?wallet_location=Wallet_tpt&oracle.net.ssl_server_cert_dn=\"CN=adwc.eucom-central-1.oraclecloud.com,OU=Oracle BMCS FRANKFURT,O=Oracle Corporation,L=Redwood City,ST=California,C=US\"";
    // For ATP and ADW - use the TNS Alias name along with the TNS_ADMIN when using 18.3 JDBC driver
    // final static String DB_URL="jdbc:oracle:thin:@wallet_dbname?TNS_ADMIN=/Users/test/wallet_dbname";
    // In case of windows, use the following URL
    // final static String DB_URL="jdbc:oracle:thin:@wallet_dbname?TNS_ADMIN=C:\\Users\\test\\wallet_dbname";
    final static String DB_USER = "ADMIN";
    final static String DB_PASSWORD = "WTFtennisman1";

    /*
     * The method gets a database connection using
     * oracle.jdbc.pool.OracleDataSource. It also sets some connection
     * level properties, such as,
     * OracleConnection.CONNECTION_PROPERTY_DEFAULT_ROW_PREFETCH,
     * OracleConnection.CONNECTION_PROPERTY_THIN_NET_CHECKSUM_TYPES, etc.,
     * There are many other connection related properties. Refer to
     * the OracleConnection interface to find more.
     */
    public static void main(String args[]) throws SQLException {
        Properties info = new Properties();
        info.put(OracleConnection.CONNECTION_PROPERTY_USER_NAME, DB_USER);
        info.put(OracleConnection.CONNECTION_PROPERTY_PASSWORD, DB_PASSWORD);
        info.put(OracleConnection.CONNECTION_PROPERTY_DEFAULT_ROW_PREFETCH, "20");


        OracleDataSource ods = new OracleDataSource();
        ods.setURL(DB_URL);
        ods.setConnectionProperties(info);

        // With AutoCloseable, the connection is closed automatically.
        try (OracleConnection connection = (OracleConnection) ods.getConnection()) {
            // Get the JDBC driver name and version
            DatabaseMetaData dbmd = connection.getMetaData();
            System.out.println("Driver Name: " + dbmd.getDriverName());
            System.out.println("Driver Version: " + dbmd.getDriverVersion());
            // Print some connection properties
            System.out.println("Default Row Prefetch Value is: " +
                    connection.getDefaultRowPrefetch());
            System.out.println("Database Username is: " + connection.getUserName());
            System.out.println();
            // Perform a database operation
            printEmployees(connection);
        }
        
        /*
        try (OracleConnection connection = (OracleConnection) ods.getConnection()){

            URL url = new URL("https://api.opendota.com/api/teams");//your url i.e fetch data from .
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP Error code : "
                        + conn.getResponseCode());
            }
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(in);
            
            
            
            StringBuilder sb = new StringBuilder();
            String output;
            System.out.println("Web service commence,Attention");
            
            while ((output = br.readLine()) != null) {
                
                    sb.append(output);
            }
            
            JSONArray jsonarray = new JSONArray(sb.toString());
            System.out.print(jsonarray.length());
            
            for(int i =0;i<jsonarray.length();i++){
                JSONObject obj = jsonarray.getJSONObject(i);
                int id = obj.getInt("team_id");
                String name = obj.getString("name");
                String tag = obj.getString("tag");
                
                if(!name.isEmpty()){
                    System.out.print("name"+name);
                    insertTeam(connection,id,name,tag);
                    System.out.println("inserer");
                }
                
                
            }
            
            
            conn.disconnect();

        } catch (Exception e) {
            System.out.println("Exception in NetClientGet:- " + e);
        }*/
    }
    /*
     * Displays first_name and last_name from the employees table.
     */
    public static void printEmployees(Connection connection) throws SQLException {
        // Statement and ResultSet are AutoCloseable and closed automatically.
        try (Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement
                    .executeQuery("select * from utilisateur")) {
                System.out.println("FIRST_NAME" + "  " + "LAST_NAME");
                System.out.println("---------------------");
                while (resultSet.next())
                    System.out.println(resultSet.getString(2) + " "
                            + " ");
            }
        }
    }
    
    public static void insertTeam(Connection connection,int id,String name,String tag) throws SQLException {
        
        // Statement and ResultSet are AutoCloseable and closed automatically.
        if(name.contains("'")){
            System.out.print("tafiditra ato");
            name = name.replace("'","''");
        }
        if(tag.contains("'")){
            System.out.print("tafiditra ato");
            tag = tag.replace("'","''");
        }
        try (Statement statement = connection.createStatement()) {
            statement.executeQuery("Insert into Team values("+id+",'"+name+"','"+tag+"')");
                

        }
    }
    
    
}