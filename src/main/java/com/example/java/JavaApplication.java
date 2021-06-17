package com.example.java;

import classe.Connexion;
import classe.Team;
import com.google.gson.Gson;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import oracle.jdbc.OracleConnection;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;





@Controller
@SpringBootApplication
@EnableAutoConfiguration(exclude = {org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration.class})
public class JavaApplication {
     // The recommended format of a connection URL is the long format with the
    // connection descriptor.
   

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
        
        @RequestMapping("/getallteam")
        @ResponseBody
        String getAllTeam() throws SQLException{
            
           OracleConnection connection = Connexion.getConnection();
           
            
            Statement statement = connection.createStatement();
           
           ResultSet resultSet = statement.executeQuery("select * from Team order by IDTEAM FETCH FIRST 10 ROWS ONLY");
           ArrayList<Team> listeTeam = new ArrayList(); 
            while (resultSet.next()){
                Team temp = new Team(resultSet.getInt(1),resultSet.getString(2),resultSet.getString(3));
                listeTeam.add(temp);
            }
                
            String json = new Gson().toJson(listeTeam);
            
            return json;
        }
	public static void main(String[] args) {
		SpringApplication.run(JavaApplication.class, args);
	}
}

