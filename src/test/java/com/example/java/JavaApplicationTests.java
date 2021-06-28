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
        
        
        private int getRowCount(ResultSet resultSet) {
        if (resultSet == null) {
            return 0;
        }

        try {
            resultSet.last();
            return resultSet.getRow();
        } catch (SQLException exp) {
        } finally {
            try {
                resultSet.beforeFirst();
            } catch (SQLException exp) {
            }
        }

        return 0;
    }
    
        int getVraiTeam(ArrayList<Team> list){
            int val = 0;
            int timeMax = 0;
            for(int i =0;i<list.size();i++){
                 int startTimeTemp = list.get(i).getIdTeam();
            }
            return 0;
        }
    
        Team findTeambynom(String nom) throws SQLException{
            OracleConnection connection = Connexion.getConnection();
           Statement statement = null;
           Team val = new Team();
           
            try{
            
                statement = connection.createStatement();

                ResultSet resultSet = statement.executeQuery("select IDTEAM,LOGO from Team where nom like '%"+nom+"%'");

                int nbrRow = getRowCount(resultSet);
                if(nbrRow==1){
                    while (resultSet.next()){
                        val.setIdTeam(resultSet.getInt(1));
                        val.setLogo(resultSet.getString(2));
                    }
                }
                if(nbrRow>1){
                    ArrayList<Team> arrayTeamTemp = new ArrayList();
                    while (resultSet.next()){
                        Team temp = new Team();
                        temp.setIdTeam(resultSet.getInt(1));
                        temp.setLogo(resultSet.getString(2));
                        arrayTeamTemp.add(temp);
                    }
                    
                }
            }
            finally{
                if(statement!=null){
                statement.close();
            }
                if(connection!=null){
                    connection.close();
                }
            }
            
            return val;
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
            
          JSONObject getJSONAPI(String url) throws JSONException{
         HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
            HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
            
          ResponseEntity response = restTemplate.exchange(url, HttpMethod.GET,entity ,String.class);  
          JSONObject json = new JSONObject(response.getBody().toString());
          return json;
    }
    
    JSONArray getJSONArrayAPI(String url) throws JSONException{
         HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
            HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
            
          ResponseEntity response = restTemplate.exchange(url, HttpMethod.GET,entity ,String.class);  
          
          JSONArray jsonarray = new JSONArray(response.getBody().toString());
          return jsonarray;
    }
         
     int getStartTime(int idTeam) throws JSONException{
          String url = "https://api.opendota.com/api/teams/"+idTeam+"/matches";
          JSONArray array = getJSONArrayAPI(url);
          int val = array.getJSONObject(0).getInt("start_time");
          return val;
    }
     
	@Test
	void contextLoads() throws SQLException, JSONException{
            /*
            
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

*/
            int startTime = getStartTime(7391077);
            System.out.println("Start:"+startTime);
             
          }
          
            
            
            
	}


