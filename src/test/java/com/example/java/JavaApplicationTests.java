package com.example.java;

import classe.Connexion;
import classe.Match;
import classe.MatchAPI;
import classe.Team;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.sql.Date;
import java.sql.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.*;



import oracle.jdbc.OracleConnection;
import org.json.JSONArray;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


@SpringBootTest
class JavaApplicationTests {
      @Autowired
                private RestTemplate restTemplate;
      
        @Bean
        public RestTemplate restTemplate() {
        return new RestTemplate();
        }

         int findteambynom(String nom) throws SQLException{
            OracleConnection connection = Connexion.getConnection();
           Statement statement = null;
           int id = 0; 
            try{
            
            statement = connection.createStatement();
           
             ResultSet resultSet = statement.executeQuery("select IDTEAM from Team where nom like '%"+nom+"%'");
            
            
            while (resultSet.next())
                id = resultSet.getInt(1);
            }
            finally{
                if(statement!=null){
                statement.close();
            }
                if(connection!=null){
                    connection.close();
                }
            }
            
            return id;
        }
         
            Match getMatch(int idMatchRivalry) throws SQLException, JSONException{
            String url = "https://www.rivalry.com/api/v1/matches/"+idMatchRivalry;
            String response = restTemplate.getForObject(url, String.class);  
            
            
            JSONObject json = new JSONObject(response);
            JSONArray array = json.getJSONObject("data").getJSONArray("competitors");
            
            
            String nomTeam = array.getJSONObject(0).getString("name");
            int idTeam1 = findteambynom(nomTeam);
            
            
            nomTeam = array.getJSONObject(1).getString("name");
            int idTeam2 = findteambynom(nomTeam);
            if(idTeam1==0 && idTeam2 == 0){
                return null;
            }
            
            String[] arrOfStr = json.getJSONObject("data").getString("scheduled_at").split("T");
            
            Date datematch = Date.valueOf(arrOfStr[0]);
            
            
            Match val = new Match(idTeam1,idTeam2,datematch);
            return val;
        }
         
	@Test
	void contextLoads() throws SQLException, JSONException{
            
         OracleConnection connection = Connexion.getConnection();
           Statement statement = null;
           Team val = new Team();
           
            try{
            
            statement = connection.createStatement();
           
            ResultSet resultSet = statement.executeQuery("select IDTEAM,LOGO from Team where nom like '%Natus Vincere%'");
            
            
            while (resultSet.next()){
                val.setIdTeam(resultSet.getInt(1));
                val.setLogo(resultSet.getString(2));}
                
            }
            finally{
                if(statement!=null){
                statement.close();
            }
                if(connection!=null){
                    connection.close();
                }
            }
            
            System.out.println("Team:"+val);
             
          }
          
            
            
            
	}


