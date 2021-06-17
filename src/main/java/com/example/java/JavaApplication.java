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
        
        
        int findteambynom(String nom) throws SQLException{
            OracleConnection connection = Connexion.getConnection();
           
            
            Statement statement = connection.createStatement();
           
            ResultSet resultSet = statement.executeQuery("select IDTEAM from Team where nom='"+nom+"'");
            int id = 0; 
            while (resultSet.next())
                id = resultSet.getInt(1);
            
            return id;
        }
        
        
        @GetMapping(path="/getallteam", produces = "application/json")
        @ResponseBody
        ArrayList<Team> getAllTeam() throws SQLException{
            
           OracleConnection connection = Connexion.getConnection();
           
            
            Statement statement = connection.createStatement();
           
           ResultSet resultSet = statement.executeQuery("select * from Team");
           ArrayList<Team> listeTeam = new ArrayList(); 
            while (resultSet.next()){
                Team temp = new Team(resultSet.getInt(1),resultSet.getString(2),resultSet.getString(3));
                listeTeam.add(temp);
            }
                
            
            return listeTeam;
        }
	public static void main(String[] args) {
		SpringApplication.run(JavaApplication.class, args);
	}
}

