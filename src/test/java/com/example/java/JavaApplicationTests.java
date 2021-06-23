package com.example.java;

import classe.Connexion;
import classe.Match;
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
           
            
            Statement statement = connection.createStatement();
           
            ResultSet resultSet = statement.executeQuery("select IDTEAM from Team where nom='"+nom+"'");
            int id = 0; 
            while (resultSet.next())
                id = resultSet.getInt(1);
            
            return id;
        }
         
	@Test
	void contextLoads() throws SQLException, JSONException{
            
            
             
           String url = "https://www.rivalry.com/api/v1/matches/375189";
            String response = restTemplate.getForObject(url, String.class);  
            
            
            
            
            JSONObject json = new JSONObject(response);
            JSONArray array = json.getJSONObject("data").getJSONArray("competitors");
            
            
            String nomTeam = array.getJSONObject(0).getString("name");
            int idTeam1 = findteambynom(nomTeam);
            
            nomTeam = array.getJSONObject(1).getString("name");
            int idTeam2 = findteambynom(nomTeam);
            
            String[] arrOfStr = json.getJSONObject("data").getString("scheduled_at").split("T");
            System.out.println("date"+arrOfStr[0]);
            Date datematch = Date.valueOf(arrOfStr[0]);
            
            
            Match val = new Match(idTeam1,idTeam2,datematch);
            System.out.println("idTeam" + idTeam2);
            
            
            
	}

}
