package com.example.java;

import classe.Connexion;
import classe.Match;
import classe.Team;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import oracle.jdbc.OracleConnection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;





@Controller
@SpringBootApplication
@EnableAutoConfiguration(exclude = {org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration.class})
public class JavaApplication {
     // The recommended format of a connection URL is the long format with the
    // connection descriptor.
   @Autowired
private RestTemplate restTemplate;

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
           
            ResultSet resultSet = statement.executeQuery("select IDTEAM from Team where nom like '%"+nom+"%'");
            int id = 0; 
            while (resultSet.next())
                id = resultSet.getInt(1);
            
            return id;
        }
        
        int findteambynom(OracleConnection co,String nom) throws SQLException{
           
           
            
            Statement statement = co.createStatement();
           
            ResultSet resultSet = statement.executeQuery("select IDTEAM from Team where nom like '%"+nom+"%'");
            int id = 0; 
            while (resultSet.next())
                id = resultSet.getInt(1);
            
            return id;
        }
        
        int getSequence(OracleConnection co,String nom) throws SQLException{
            int val = 0;
            Statement statement = co.createStatement();
           
           ResultSet resultSet = statement.executeQuery("select "+nom+".currval from DUAL;");
           
            while (resultSet.next()){
                val = resultSet.getInt(1);
            }
            return val;
        }
        
        void insererMatch(OracleConnection connection,int idTeam1, int idTeam2, Date datematch) throws SQLException{
           
            Statement statement = null;
            try{
                statement = connection.createStatement();
           
                statement.executeQuery("insert into Match values(MATCH_SEQ.NEXTVAL,"+idTeam1+","+idTeam2+",TO_DATE('"+datematch+"','YYYY-MM-DD'))");
            }
            finally{
                if(statement!=null){
                    statement.close();
                }
            }
        }
        
        
        void insererParis(OracleConnection connection,int idUser,int idMatch,int idTeam,String type,float montant,float odds,Date dateparis,int statut) throws SQLException{
            
            Statement statement = null;
            try{
                statement = connection.createStatement();
           
                statement.executeQuery("insert into PARIS values(PARIS_SEQ.NEXTVAL,"+idUser+","+idMatch+","+idTeam+",'"+type+"',"+montant+","+odds+",TO_DATE('"+dateparis+"','YYYY-MM-DD'),"+statut+")");
            }
            finally{
                if(statement!=null){
                    statement.close();
                }
            }
            
        }
        
        
        
        Match getMatch(int idMatchRivalry) throws SQLException{
            String url = "https://www.rivalry.com/api/v1/matches/"+idMatchRivalry;
            String response = restTemplate.getForObject(url, String.class);  
            
            
            JSONObject json = new JSONObject(response);
            JSONArray array = json.getJSONObject("data").getJSONArray("competitors");
            
            
            String nomTeam = array.getJSONObject(0).getString("name");
            int idTeam1 = findteambynom(nomTeam);
            if(idTeam1 == 0){
                return null;
            }
            
            nomTeam = array.getJSONObject(1).getString("name");
            int idTeam2 = findteambynom(nomTeam);
            if(idTeam2 == 0){
                return null;
            }
            
            String[] arrOfStr = json.getJSONObject("data").getString("scheduled_at").split("T");
            System.out.println("date"+arrOfStr[0]);
            Date datematch = Date.valueOf(arrOfStr[0]);
            
            
            Match val = new Match(idTeam1,idTeam2,datematch);
            return val;
        }
        
        @Bean
        public RestTemplate restTemplate() {
        return new RestTemplate();
        }
        
        @PostMapping(value = "/parier", consumes = "application/json", produces = "application/json")
        @ResponseBody
        String parier(@RequestParam int idUser,@RequestParam int idMatch,@RequestParam String type,@RequestParam String nomTeam,@RequestParam float montant,@RequestParam float odds) throws SQLException{
            /*Match m = getMatch(idMatch);
            
            OracleConnection oc = Connexion.getConnection();
            try{
                insererMatch(oc,m.getIdTeam1(),m.getIdTeam2(),m.getDatematch());
                int idMatchTemp = getSequence(oc,"MATCH_SEQ");
                int idTeam = findteambynom(oc,nomTeam);
                Date datenow = new java.sql.Date(Calendar.getInstance().getTime().getTime());
                insererParis(oc,idUser,idMatchTemp,idTeam,type,montant,odds,datenow,0);
            }
            finally{
                 if(oc!=null){
                    oc.close();
                }
            }*/
            
            return "Match" +idUser+","+idMatch;
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
        
        @GetMapping(path="/getallmatch", produces = "application/json")
        @ResponseBody
        ArrayList<Match> getAllMatch() throws SQLException, JSONException{
            
           
             
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
            
            
            String nomTeam = arrayTeam.getJSONObject(0).getString("name");
            int idTeam1 = findteambynom(nomTeam);
            
            
            
            nomTeam = arrayTeam.getJSONObject(1).getString("name");
            int idTeam2 = findteambynom(nomTeam);
            
            String[] arrOfStr = array.getJSONObject(i).getString("scheduled_at").split("T");
            
            Date datematch = Date.valueOf(arrOfStr[0]);
            
              
              
              //si tous les equipes sont presentent dans notre BD
              if(idTeam1!=0 || idTeam2!=0){
                 
                  if(datematch.compareTo(datenow)<=0){
                      System.out.println("IdRivalry" +idRivalry);
                      Match temp = new Match(idTeam1,idTeam2,idRivalry,datematch);
                      val.add(temp);
                     
                  }
                  else{
                      break;
                  }
              }
             
          }
          return val;
          
        }
        
        
	public static void main(String[] args) {
		SpringApplication.run(JavaApplication.class, args);
	}
}

