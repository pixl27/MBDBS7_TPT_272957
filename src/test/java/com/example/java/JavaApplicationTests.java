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
           
             ResultSet resultSet = statement.executeQuery("select IDTEAM from Team where nom like '%"+nom+"%'");
            int id = 0; 
            while (resultSet.next())
                id = resultSet.getInt(1);
            
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
            
            
             
          ArrayList<Match> val = new ArrayList<Match>();
          String url = "https://www.rivalry.com/api/v1/matches?game_id=3";
          String response = restTemplate.getForObject(url, String.class);  
          JSONObject json = new JSONObject(response);
          JSONArray array = json.getJSONArray("data");
          
          
          
          Date datenow = new java.sql.Date(Calendar.getInstance().getTime().getTime());
          for(int i=0;i<array.length();i++){
              int id = array.getJSONObject(i).getInt("id");
              
              JSONArray arrayTeam = array.getJSONObject(i).getJSONArray("competitors");
            
            
            String nomTeam = arrayTeam.getJSONObject(0).getString("name");
            int idTeam1 = findteambynom(nomTeam);
            
            
            
            nomTeam = arrayTeam.getJSONObject(1).getString("name");
            int idTeam2 = findteambynom(nomTeam);
            
            String[] arrOfStr = array.getJSONObject(i).getString("scheduled_at").split("T");
            
            Date datematch = Date.valueOf(arrOfStr[0]);
            
              System.out.println("id"+id);
              System.out.println("idTeam1"+idTeam1);
              System.out.println("idTeam2"+idTeam2);
              
              //si tous les equipes sont presentent dans notre BD
              if(idTeam1!=0 && idTeam2!=0){
                  System.out.println("team anaty bd");
                  System.out.println("Date match"+datematch);
                  if(datematch.compareTo(datenow)<=0){
                      Match temp = new Match(idTeam1,idTeam2,id,datematch);
                      val.add(temp);
                      System.out.println("tafiditra ao anaty liste");
                  }
                  else{
                      break;
                  }
              }
             
          }
          
            
            
            
	}

}
