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
                 if(startTimeTemp>timeMax){
                     val = i;
                     timeMax = startTimeTemp;
                 }
            }
            return val;
        }
    
        Team findTeambynomV2(String nom) throws SQLException{
            OracleConnection connection = Connexion.getConnection();
           Statement statement = null;
           Team val = new Team();
           
            try{
            
                statement = connection.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);

                ResultSet resultSet = statement.executeQuery("select IDTEAM,LOGO from Team where nom like '%"+nom+"%'");

                int nbrRow = getRowCount(resultSet);
                System.out.println("nbrRow:"+nbrRow);
                if(nbrRow==1){
                    while (resultSet.next()){
                        val.setIdTeam(resultSet.getInt(1));
                        val.setLogo(resultSet.getString(2));
                    }
                }
                if(nbrRow>1){
                    System.out.println("Team de ce nom bobaka");
                    ArrayList<Team> arrayTeamTemp = new ArrayList();
                    while (resultSet.next()){
                        Team temp = new Team();
                        temp.setIdTeam(resultSet.getInt(1));
                        temp.setLogo(resultSet.getString(2));
                        arrayTeamTemp.add(temp);
                    }
                    int indiceVrai = getVraiTeam(arrayTeamTemp);
                    val = arrayTeamTemp.get(indiceVrai);
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
            ArrayList<MatchAPI> val = new ArrayList<>();
          String url = "https://www.rivalry.com/api/v1/matches?game_id=3";
          
          HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
            HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
            
          ResponseEntity response = restTemplate.exchange(url, HttpMethod.GET,entity ,String.class);  
          JSONObject json = new JSONObject(response.getBody().toString());
          
          
          JSONArray array = json.getJSONArray("data");
          
          
          
          Calendar c = Calendar.getInstance(); 
             c.add(Calendar.DATE, 14);
            Date datenow = new java.sql.Date(c.getTimeInMillis());
          for(int i=0;i<10;i++){
              int idRivalry = array.getJSONObject(i).getInt("id");
              String tournois = array.getJSONObject(i).getJSONObject("tournament").getString("name");
              
              JSONArray arrayTeam = array.getJSONObject(i).getJSONArray("competitors");
              
              JSONArray arrayOdds = array.getJSONObject(i).getJSONArray("markets").getJSONObject(0).getJSONArray("outcomes");
              
              int nbrMap = 1;
              while(nbrMap<=100){
                  String temp = "Map "+nbrMap+" - Winner";
                  int indice = nbrMap-1;
                  String name = array.getJSONObject(i).getJSONArray("markets").getJSONObject(indice).getString("name");
                  if(temp.equals(name)){
                      nbrMap++;
                  }
                  else{
                      
                      break;
                  }
              }
              nbrMap = nbrMap-1;
              
              float odds1 = 0;
              float odds2 = 0;
              if(arrayOdds.length()>0){
                  odds1 = (float) arrayOdds.getJSONObject(0).getDouble("odds");
                  odds2 = (float) arrayOdds.getJSONObject(1).getDouble("odds");
              }
              
              
            
            
            String nomTeam1 = arrayTeam.getJSONObject(0).getString("name");
            Team team1 = findTeambynomV2(nomTeam1);
            int idTeam1 = team1.getIdTeam();
            String logoTeam1 = team1.getLogo();
            
            
            
            String nomTeam2 = arrayTeam.getJSONObject(1).getString("name");
            Team team2 = findTeambynomV2(nomTeam2);
            int idTeam2 = team2.getIdTeam();
            String logoTeam2 = team2.getLogo();
            
            String time = array.getJSONObject(i).getString("scheduled_at");
            
            String[] arrOfStr = array.getJSONObject(i).getString("scheduled_at").split("T");
            
            Date datematch = Date.valueOf(arrOfStr[0]);
            
              
              
              //si tous les equipes sont presentent dans notre BD
              if(idTeam1!=0 || idTeam2!=0){
                 
                  
                     if(datematch.compareTo(datenow)<=0){
                      //int idTeam1, int idTeam2, int idMatchRivalry, Date datematch, String nomTeam1, String nomTeam2, float odds1, float odds2, String logo, String time, String tournois
                      MatchAPI temp = new MatchAPI(idTeam1,idTeam2,idRivalry,datematch,nomTeam1,nomTeam2,odds1,odds2,logoTeam1,logoTeam2,time,tournois,nbrMap);
                      
                      val.add(temp);
                     }
                     else{
                         break;
                     }
                  
              }
             
          }
          System.out.println(val);
          }
          
            
            
            
	}