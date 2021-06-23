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
            
            
             
        ArrayList<Match> val = new ArrayList<>();
          String url = "https://www.rivalry.com/api/v1/matches?game_id=3";
          
          HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
            HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
            
          ResponseEntity response = restTemplate.exchange(url, HttpMethod.GET,entity ,String.class);  
          JSONObject json = new JSONObject(response.getBody().toString());
          
          
          JSONArray array = json.getJSONArray("data");
          
          
          
          Date datenow = new java.sql.Date(Calendar.getInstance().getTime().getTime());
          for(int i=0;i<array.length();i++){
              int idRivalry = array.getJSONObject(i).getInt("id");
              
              
              JSONArray arrayTeam = array.getJSONObject(i).getJSONArray("competitors");
            
            
            String nomTeam = arrayTeam.getJSONObject(1).getString("name");
            int idTeam1 = findteambynom(nomTeam);
            
            
            
            nomTeam = arrayTeam.getJSONObject(0).getString("name");
            System.out.println("nom team mbola mande"+nomTeam);
            int idTeam2 = findteambynom(nomTeam);
            
            String[] arrOfStr = array.getJSONObject(i).getString("scheduled_at").split("T");
            
            Date datematch = Date.valueOf(arrOfStr[0]);
            
              
              
              //si tous les equipes sont presentent dans notre BD
              if(idTeam1!=0 || idTeam2!=0){
                 
                  if(datematch.compareTo(datenow)<=0){
                      
                      Match temp = new Match(idTeam1,idTeam2,idRivalry,datematch);
                     
                      val.add(temp);
                     
                  }
                  else{
                      break;
                  }
              }
             
          }
             
          }
          
            
            
            
	}


